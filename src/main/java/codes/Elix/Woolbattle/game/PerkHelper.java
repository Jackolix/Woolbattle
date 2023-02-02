package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.mongo.Database;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class PerkHelper {
    private static FileConfiguration config = Woolbattle.getPlugin().getConfig();
    private static final Perk nullperk = new Perk("booster", "enterhaken", "rocket_jump", true, 6, 7);

    public static String passive(Player player) {
        Object Pperk = config.get(player.getName() + ".passive");
        assert Pperk != null;
        return Pperk.toString();
    }

    interface Callback {
        void onSuccess(boolean value);
    }

    @NotNull
    public static Perk getPerks(Player player) {
        if (Items.perks.containsKey(player))
            return Items.perks.get(player);
        return nullperk;
    }

    public static void setPerk(Player player, Perk perk) {
        if (!Database.hasConnection()) return;
        MongoClient mongoClient = Database.getConnection();
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        MongoCollection<Document> collection = database.getCollection("players");
        Console.send(Database.PREFIX + "trying to inject");

        Document user = new Document("UUID", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("perks", new Document("firstperk", perk.getfirstPerk())
                        .append("secondperk", perk.getsecondPerk())
                        .append("passiveperk", perk.getpassivePerk())
                        .append("firstperkslot", perk.getfirstPerkSlot())
                        .append("secondperkslot", perk.getsecondPerkSlot()))
                .append("particles", perk.hasParticles());

        insert(collection, user);
    }

    public static void onJoin(Player player) {
        //connect to the server/database/collection
        if (!Database.hasConnection()) return;
        MongoClient mongoClient = Database.getConnection();
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        MongoCollection<Document> collection = database.getCollection("players");

        find(collection, player.getUniqueId());
        exists(player.getUniqueId().toString(), new Callback() {
            @Override
            public void onSuccess(boolean value) {
                if (!value)
                    setPerk(player, nullperk);
            }
        });
    }

    public static void updatePerks(Player player, Perk oldPerk, Perk newPerk) {
        if (!Database.hasConnection()) return;
        MongoClient mongoClient = Database.getConnection();
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        MongoCollection<Document> collection = database.getCollection("players");

        if (oldPerk.getfirstPerk() != newPerk.getfirstPerk())
            update(collection, player.getUniqueId(), "perks.firstperk", newPerk.getfirstPerk());
        if (oldPerk.getsecondPerk() != newPerk.getsecondPerk())
            update(collection, player.getUniqueId(), "perks.secondperk", newPerk.getsecondPerk());
        if (oldPerk.getpassivePerk() != newPerk.getpassivePerk())
            update(collection, player.getUniqueId(), "perks.passiveperk", newPerk.getpassivePerk());
        if (oldPerk.hasParticles() != newPerk.hasParticles())
            update(collection, player.getUniqueId(), "perks.firstperkslot", String.valueOf(newPerk.getfirstPerkSlot()));
        if (oldPerk.getfirstPerkSlot() != newPerk.getsecondPerkSlot())
            update(collection, player.getUniqueId(), "perks.secondperkslot", String.valueOf(newPerk.getsecondPerkSlot()));
        if (oldPerk.hasParticles() != newPerk.hasParticles())
            update(collection, player.getUniqueId(), "particles", String.valueOf(newPerk.hasParticles())); //TODO update when going to ingamestate, not everytime the user clicks
        Items.perks.put(player, newPerk);
    }

    private static void insert(MongoCollection<Document> collection, Document doc) {
        collection.insertOne(doc).subscribe(new Subscriber<InsertOneResult>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }
            @Override
            public void onNext(InsertOneResult insertOneResult) { }
            @Override
            public void onError(Throwable t) {
                Console.send(Database.ERROR + "Insert: " + t);
            }
            @Override
            public void onComplete() { }
        });
    }

    private static void find(MongoCollection<Document> collection, UUID uuid) {
        collection.find(eq("UUID", uuid.toString())).first().subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }
            @Override
            public void onNext(Document doc) {
                Document perks = (Document) doc.get("perks");
                Perk perk = new Perk(perks.getString("firstperk"), perks.getString("secondperk"), perks.getString("passiveperk"), doc.getBoolean("particles"), perks.getInteger("firstperkslot"), perks.getInteger("secondperkslot"));
                Items.perks.put(Bukkit.getPlayer(uuid), perk);
            }
            @Override
            public void onError(Throwable t) {
                Console.send(Database.ERROR + "Find: " + t);
                Items.perks.put(Bukkit.getPlayer(uuid), nullperk);
            }
            @Override
            public void onComplete() {
                Console.send(Database.PREFIX + "Find action complete.");
            }
        });
    }

    private static void update(MongoCollection<Document> collection, UUID uuid, String param1, String param2) {

        if (Objects.equals(param1, "perks.firstperk") || Objects.equals(param1, "perks.secondperk") || Objects.equals(param1, "perks.passiveperk")) {
            collection.updateOne(eq("UUID", uuid.toString()), set(param1, param2)).subscribe(new Subscriber<UpdateResult>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Integer.MAX_VALUE);
                }
                @Override
                public void onNext(UpdateResult updateResult) {
                    Console.send(Database.PREFIX + "Updateresult: " + updateResult.toString());
                }
                @Override
                public void onError(Throwable t) {
                    Console.send(Database.ERROR + "Update Error: " + t.toString());
                }
                @Override
                public void onComplete() {
                    Console.send(Database.PREFIX + "Update action complete.");
                }
            });
            return;
        }

        if (Objects.equals(param1, "perks.firstperkslot") || Objects.equals(param1, "secondperkslot")) {
            Integer values = Integer.valueOf(param2);
            collection.updateOne(eq("UUID", uuid.toString()), set(param1, values)).subscribe(new Subscriber<UpdateResult>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Integer.MAX_VALUE);
                }

                @Override
                public void onNext(UpdateResult updateResult) {
                    Console.send(Database.PREFIX + "Updateresult: " + updateResult.toString());
                }

                @Override
                public void onError(Throwable t) {
                    Console.send(Database.ERROR + "Update Error: " + t.toString());
                }

                @Override
                public void onComplete() {
                    Console.send(Database.PREFIX + "Update action complete.");
                }
            });
            return;
        }

        if (Objects.equals(param1, "particles")) {
            boolean values = Boolean.parseBoolean(param2);
            collection.updateOne(eq("UUID", uuid.toString()), set(param1, values)).subscribe(new Subscriber<UpdateResult>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Integer.MAX_VALUE);
                }
                @Override
                public void onNext(UpdateResult updateResult) {
                    Console.send(Database.PREFIX + "Updateresult: " + updateResult.toString());
                }
                @Override
                public void onError(Throwable t) {
                    Console.send(Database.ERROR + "Update Error: " + t.toString());
                }
                @Override
                public void onComplete() {
                    Console.send(Database.PREFIX + "Update action complete.");
                }
            });
        }
    }

    public static void exists(String id, final Callback callback) {
        Query query = new Query(Criteria.where("UUID").is(id));
        ReactiveMongoTemplate template = new ReactiveMongoTemplate(Database.getConnection(), "minecraft");
        Mono<Boolean> exists = template.exists(query, "players");
        exists.subscribe(
                value -> callback.onSuccess(value),
                error -> Console.send(Database.ERROR + error)
        );
    }
}

