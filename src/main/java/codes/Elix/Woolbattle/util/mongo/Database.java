package codes.Elix.Woolbattle.util.mongo;



import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.ChatColor;

public class Database {
    public static final String PREFIX = ChatColor.BLUE + "MongoDB: " + ChatColor.RESET, ERROR = ChatColor.BLUE + "MongoDB " + ChatColor.RED + "ERROR: " + ChatColor.RESET;
    //connect to the server/database/collection
    private static MongoClient mongoClient;
    public Database() {
        connect();
    }

    public void connect() {
        //connect to the server/database/collection
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("database");
        MongoCollection<Document> col = database.getCollection("collection");
    }

    public void disconnect() {
    }

    public static boolean hasConnection() {
        return mongoClient != null;
    }

    public static MongoClient getConnection() {
        return mongoClient;
    }
    public static MongoDatabase getCollection(String name) {
        return mongoClient.getDatabase(name);
    }
}
