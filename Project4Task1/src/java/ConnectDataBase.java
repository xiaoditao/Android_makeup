/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author xiaoditao
 */
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.ServerAddress;
//
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoCollection;
//
//import org.bson.Document;
//import java.util.Arrays;
//import com.mongodb.Block;
//
//import com.mongodb.client.MongoCursor;
//import static com.mongodb.client.model.Filters.*;
//import com.mongodb.client.result.DeleteResult;
//import static com.mongodb.client.model.Updates.*;
//import com.mongodb.client.result.UpdateResult;
//import java.util.ArrayList;
//import java.util.List;
//public class ConnectDataBase {
//    public static void main(String[] args) {
//        ConnectDataBase cdb = new ConnectDataBase();
//        cdb.connectMango();
//    }
//    public void connectMango() {
//        MongoClientURI uri = new MongoClientURI(
//        "mongodb://xiaodit:Wangliyun1970@cluster0-shard-00-00-3wcku.mongodb.net:27017,cluster0-shard-00-01-3wcku.mongodb.net:27017,cluster0-shard-00-02-3wcku.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("test");
//        
//        MongoCollection<Document> collection = database.getCollection("test");
//        
//        Document doc = new Document("name", "MongoDB")
//                .append("type", "database")
//                .append("count", 1)
//                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//                .append("info", new Document("x", 203).append("y", 102));
//        collection.insertOne(doc);
//        List<Document> documents = new ArrayList<Document>();
//            for (int i = 0; i < 100; i++) {
//                documents.add(new Document("i", i));
//                }
//        collection.insertMany(documents);
//        System.out.println(collection.count());
//        Document myDoc = collection.find().first();
//        System.out.println(myDoc.toJson());
//    }
//    //MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//    
    
//}
