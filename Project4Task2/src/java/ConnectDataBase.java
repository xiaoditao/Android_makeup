/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author xiaoditao
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.MongoCursor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectDataBase {
    MongoDatabase database = null;
    MongoCollection<Document> collection = null;
    static int count = 0;
    static int sum = 0;
    static int avg = 0;
    static Map<String, Integer> map = null;
    static String maxKey = "";
    static int maxValue;
    public String getTopInput() {
        int size = map.size();
        String[][] array = new String[size][2];
        int i =0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            String valueStr = String.valueOf(value);
            array[i][0] = key;
            array[i][1] = valueStr;
            i++;
        }
        for(int j =0; j < array.length; j++) {
            int val = Integer.valueOf(array[j][1]);
            if(maxValue < val) {
                maxValue = val;
                maxKey = array[j][0];
            }
        }
        return maxKey;
    }
    public ConnectDataBase() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb://xiaodit:Wangliyun1970@cluster0-shard-00-00-3wcku.mongodb.net:27017,cluster0-shard-00-01-3wcku.mongodb.net:27017,cluster0-shard-00-02-3wcku.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
        MongoClient mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("test10");
        collection = database.getCollection("test10");
        map = new HashMap<>();
    }
    
    public int getAvg(){
        if(ConnectDataBase.count == 0) {
            return 0;
        }
        ConnectDataBase.avg = ConnectDataBase.sum/ (int) this.collection.count();
        return ConnectDataBase.avg;
    }
    
    public int getCount(){
        ConnectDataBase.count = (int) this.collection.count();
        return (int) this.collection.count();
    }
    public String getFirstView() throws JSONException {
        MongoCursor<Document> cursor2 = collection.find().iterator();
        JSONObject date1 = null;
        JSONObject date2 = null;
        String res = "";
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        
        try {
            while (cursor2.hasNext()) {
                JSONObject json = new JSONObject(cursor2.next().toJson());
                date1 = (JSONObject)json.get("Time Request From Mobile");
                long start = (long)date1.get("$date");
                Date resultStart = new Date(start);
                res += "Time Request From Mobile: " + simple.format(resultStart);
                res = res + "<br>"+ "<br>";
                res += "Search Term From Mobile: " + json.get("Search Term From Mobile");
                res = res + "<br>"+ "<br>";
                res += "Request To API: " + json.get("Request To API");
                res =  res + "<br>" + "<br>";
                res += "Reply From API: " + json.get("Reply From API");
                res = res + "<br>" + "<br>";
                res += "Reply To Android:" + json.get("Reply To Android");
                res = res + "<br>" + "<br>";
                res += "Device Type:" + json.get("Device Type");
                res = res + "<br>"+ "<br>";
                date2 = (JSONObject)json.get("Time Reply To Android");
                long end = (long)date2.get("$date");
                Date resultEnd = new Date(end);
                res += "Time Reply To Mobile: " + simple.format(resultEnd);
                res = res + "<br>";
                res += "--------------------------------------------------------------------seperate line-----------------------------------------------------------------------------------------------";
                res = res + "<br>" + "<br>";
            }
        } finally {
            cursor2.close();
        }
        return res;
    }
    public String connectMango(Timestamp timeRequestFromAndroid, String searchTerm, String requestAPI, String replyFromAPI, String replyToAndroid, Timestamp timeReplyToAndroid, String deviceType) throws JSONException {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        String res = "";
        if(deviceType.contains("Android")){
            Document doc = new Document("Time Request From Mobile", timeRequestFromAndroid)
                    .append("Search Term From Mobile", searchTerm)
                    .append("Request To API", requestAPI)
                    .append("Reply From API", replyFromAPI)
                    .append("Reply To Android", replyToAndroid)
                    .append("Time Reply To Android", timeReplyToAndroid)
                    .append("Device Type", deviceType);
            collection.insertOne(doc);
            MongoCursor<Document> cursor = collection.find().iterator();
            JSONObject date1 = null;
            JSONObject date2 = null;
            try {
                ConnectDataBase.sum = 0;
                map = new HashMap<>();
                while (cursor.hasNext()) {
                    JSONObject json = new JSONObject(cursor.next().toJson());
                    date1 = (JSONObject)json.get("Time Request From Mobile");
                    long start = (long)date1.get("$date");
                    Date resultStart = new Date(start);
                    res += "Time Request From Mobile: " + simple.format(resultStart);
                    res = res + "<br>"+ "<br>";
                    res += "Search Term From Mobile: " + json.get("Search Term From Mobile");
                    if(map.containsKey((String)json.get("Search Term From Mobile"))) {
                        int tempt = map.get((String)json.get("Search Term From Mobile"));
                        tempt++;
                        map.put((String)json.get("Search Term From Mobile"), tempt);
                    }else {
                        map.put((String)json.get("Search Term From Mobile"), 1);
                    }
                    res = res + "<br>"+ "<br>";
                    res += "Request To API: " + json.get("Request To API");
                    res =  res + "<br>" + "<br>";
                    res += "Reply From API: " + json.get("Reply From API");
                    res = res + "<br>" + "<br>";
                    res += "Reply To Android:" + json.get("Reply To Android");
                    res = res + "<br>" + "<br>";
                    res += "Device Type:" + json.get("Device Type");
                    res = res + "<br>"+ "<br>";
                    date2 = (JSONObject)json.get("Time Reply To Android");
                    long end = (long)date2.get("$date");
                    Date resultEnd = new Date(end);
                    res += "Time Reply To Mobile: " + simple.format(resultEnd);
                    ConnectDataBase.sum += end - start;
                    res = res + "<br>";
                    res += "--------------------------------------------------------------------seperate line-----------------------------------------------------------------------------------------------";
                    res = res + "<br>" + "<br>";
                }
            } finally {
                cursor.close();
            }
        }
        return res;
    }
    
}
