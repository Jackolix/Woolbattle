package codes.Elix.Woolbattle.util.mongo;

import codes.Elix.Woolbattle.game.PerkHelper;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UpdateObjekt {
    private static List<UpdateObjekt> updatePerks = new ArrayList<>();

    private final Player player;
    private final String perk;
    private final String perkName;

    public UpdateObjekt(Player player, String perk, String perkName) {
        this.player = player;
        this.perk = perk;
        this.perkName = perkName;
        updatePerks.add(this);
    }

    public static void update() {
        if (!Database.hasConnection()) return;
        MongoClient mongoClient = Database.getConnection();
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        MongoCollection<Document> collection = database.getCollection("players");

        for (UpdateObjekt updateObjekt : updatePerks)
            PerkHelper.update(collection, updateObjekt.player.getUniqueId(), updateObjekt.perk, updateObjekt.perkName);
    }
}
