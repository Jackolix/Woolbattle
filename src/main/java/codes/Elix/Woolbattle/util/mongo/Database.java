package codes.Elix.Woolbattle.util.mongo;



import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;

public class Database {
    //connects to the server/database/collection
    public static final Component PREFIX = Component.text("MongoDB: " , NamedTextColor.BLUE), ERROR = Component.text("MongoDB ", NamedTextColor.BLUE).append(Component.text("ERROR: ", NamedTextColor.RED));
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    public Database() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        MongoCollection<Document> col = database.getCollection("players");
        this.database = database;
        this.collection = col;
    }

    public static boolean hasConnection() {
        return mongoClient != null;
    }

    public static MongoClient getConnection() {
        return mongoClient;
    }
    public static MongoDatabase getDatabase() {
        return database;
    }
    public static MongoCollection<Document> getCollection() {
        return collection;
    }
}
