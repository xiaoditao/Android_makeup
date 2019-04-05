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
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat; 
import java.text.SimpleDateFormat; 
import java.util.Date; 
import javax.json.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectDataBase {
    MongoDatabase database = null;
    MongoCollection<Document> collection = null;
    static int count = 0;
    static int sum = 0;
    static int avg = 0;
    public ConnectDataBase() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb://xiaodit:Wangliyun1970@cluster0-shard-00-00-3wcku.mongodb.net:27017,cluster0-shard-00-01-3wcku.mongodb.net:27017,cluster0-shard-00-02-3wcku.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
        MongoClient mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("test4");
        collection = database.getCollection("test4");
    }
//    public static void main(String[] args) {
//        ConnectDataBase cdb = new ConnectDataBase();
//        //cdb.connectMango();
//    }
    public int getAvg(){ 
        ConnectDataBase.avg = ConnectDataBase.sum/ ConnectDataBase.count;
        return ConnectDataBase.avg;
    }
    
    public int getCount(){ 
        ConnectDataBase.count = (int) this.collection.count();
        return (int) this.collection.count();
    }
    public String connectMango(Timestamp timeRequestFromAndroid, String searchTerm, String requestAPI, String replyFromAPI, String replyToAndroid, Timestamp timeReplyToAndroid, String requestIP) throws JSONException {
        //BigInteger miliSec = new BigInteger("1554407336696");
        long miliSec = 1554407336696L;
  
        // Creating date format 
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z"); 
  
        // Creating date from milliseconds 
        // using Date() constructor 
        Date result = new Date(miliSec); 
  
        // Formatting Date according to the 
        // given format 
        System.out.println("time transferd!!!!!!" + simple.format(result)); 
        //////////////
        //////
//        Timestamp d = new Timestamp(System.currentTimeMillis());
//        System.out.println(d);
        
        String res = "";
        Document doc = new Document("Time Request From Mobile", timeRequestFromAndroid)
        .append("Search Term From Mobile", searchTerm)
        .append("Request To API", requestAPI)
        .append("Reply From API", replyFromAPI)
        .append("Reply To Android", replyToAndroid)
        .append("Time Reply To Android", timeReplyToAndroid)
        .append("Request IP", requestIP);
        collection.insertOne(doc);
//        List<Document> documents = new ArrayList<Document>();
//            for (int i = 0; i < 100; i++) {
//                documents.add(new Document("i", i));
//                }
//        collection.insertMany(documents);
//        System.out.println(collection.count());
        MongoCursor<Document> cursor = collection.find().iterator();
        JSONObject date1 = null;
        JSONObject date2 = null;
        try {
            while (cursor.hasNext()) {
            JSONObject json = new JSONObject(cursor.next().toJson());
            date1 = (JSONObject)json.get("Time Request From Mobile");
            res += "Time Request From Mobile: " + date1.get("$date");
            long start = (long)date1.get("$date");
            res = res + "<br>"+ "<br>";
            res += "Search Term From Mobile: " + json.get("Search Term From Mobile");
            res = res + "<br>"+ "<br>";
            res += "Request To API: " + json.get("Request To API");
            res =  res + "<br>" + "<br>";
            res += "Reply From API: " + json.get("Reply From API");
            res = res + "<br>" + "<br>";
            res += "Reply To Android:" + json.get("Reply To Android");
            res = res + "<br>" + "<br>";
            res += "Request IP:" + json.get("Request IP");
            res = res + "<br>"+ "<br>";
            date2 = (JSONObject)json.get("Time Reply To Android");
            res += "Time Reply To Android: " + date2.get("$date");
            long end = (long)date2.get("$date");
            ConnectDataBase.sum += end - start;
            res = res + "<br>";
            res += "--------------------------------------------------------------------seperate line-----------------------------------------------------------------------------------------------";
            res = res + "<br>" + "<br>";
            }
        } finally {
            cursor.close();
        }
//        String testdate = "";
//        //while(res.contains("$date")) {
//	int leftDate1 = res.indexOf(("$date"));
//        res = res.substring(leftDate1);
//	int rightDate = res.indexOf(("}"));
//        System.out.println(rightDate);
//        testdate = res.substring(leftDate1,rightDate);
       // }

        //res///res//res//res//res//
        
        //System.out.println(res);
        //res += testdate;
        return res;
    }
    //MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//    public String test(String searchTerm) {
//        String res = "searchTerm from android:" + searchTerm;
//        return res;
//    }
    
}
