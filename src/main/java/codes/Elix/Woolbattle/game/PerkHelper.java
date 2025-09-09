package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.mongo.Database;
import codes.Elix.Woolbattle.util.mongo.UpdateObjekt;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Bukkit;
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
    private static final Perk defaultPerk = new Perk("booster", "enterhaken", "rocket_jump", true, 6, 7);
    public static HashMap<Player, Perk> updatedPerks = new HashMap<>();

    public static String passive(Player player) {
        Perk perk = getPerks(player);
        return perk.getpassivePerk();
    }

    interface Callback {
        void onSuccess(boolean value);
    }

    @NotNull
    public static Perk getPerks(Player player) {
        if (Items.perks.containsKey(player))
            return Items.perks.get(player);
        return defaultPerk;
    }

    public static void setPerk(Player player, Perk perk) {
        if (!Woolbattle.useDB) {
            Items.perks.put(player, perk);
            return;
        }
        
        if (!Database.hasConnection()) return;
        
        Console.debug(Database.PREFIX.append(Component.text("trying to inject", NamedTextColor.WHITE)));
        Document user = new Document("UUID", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("perks", new Document("firstperk", perk.getfirstPerk())
                        .append("secondperk", perk.getsecondPerk())
                        .append("passiveperk", perk.getpassivePerk())
                        .append("firstperkslot", perk.getfirstPerkSlot())
                        .append("secondperkslot", perk.getsecondPerkSlot()))
                .append("particles", perk.hasParticles());

        insert(Database.getCollection(), user);
    }

    public static void onJoin(Player player) {
        if (!Woolbattle.useDB) {
            Items.perks.put(player, defaultPerk);
            return;
        }
        
        if (!Database.hasConnection()) {
            Items.perks.put(player, defaultPerk);
            return;
        }
        
        exists(player.getUniqueId().toString(), new Callback() {
            @Override
            public void onSuccess(boolean value) {
                if (!value)
                    setPerk(player, defaultPerk);
                find(Database.getCollection(), player.getUniqueId());
            }
        });
    }

    public static void updatePerks(Player player, Perk oldPerk, Perk newPerk) {
        Items.perks.put(player, newPerk);
        
        if (!Woolbattle.useDB || !Database.hasConnection()) {
            return;
        }

        //TODO update when going to ingamestate, not everytime the user clicks
        // Is it better to just update the entire perk or just the part which changed?
        // updatedPerks.put(player, newPerk);
        if (oldPerk.getfirstPerk() != newPerk.getfirstPerk())
            new UpdateObjekt(player, "perks.firstperk", newPerk.getfirstPerk());
        if (oldPerk.getsecondPerk() != newPerk.getsecondPerk())
            new UpdateObjekt(player, "perks.secondperk", newPerk.getsecondPerk());
        if (oldPerk.getpassivePerk() != newPerk.getpassivePerk())
            new UpdateObjekt(player, "perks.passiveperk", newPerk.getpassivePerk());
        if (oldPerk.getsecondPerkSlot() != newPerk.getfirstPerkSlot())
            new UpdateObjekt(player, "perks.firstperkslot", String.valueOf(newPerk.getfirstPerkSlot()));
        if (oldPerk.getfirstPerkSlot() != newPerk.getsecondPerkSlot())
            new UpdateObjekt(player, "perks.secondperkslot", String.valueOf(newPerk.getsecondPerkSlot()));
        if (oldPerk.hasParticles() != newPerk.hasParticles())
            new UpdateObjekt(player, "particles", String.valueOf(newPerk.hasParticles()));
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
                Console.debug(Database.ERROR.append(Component.text("Insert: " + t, NamedTextColor.WHITE)));
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
                Console.debug(Database.PREFIX.append(Component.text("Found Player Perks.", NamedTextColor.WHITE)));
            }
            @Override
            public void onError(Throwable t) {
                Console.send(Database.ERROR.append(Component.text("Find: " + t, NamedTextColor.WHITE)));
                Items.perks.put(Bukkit.getPlayer(uuid), defaultPerk);
            }
            @Override
            public void onComplete() {
                Console.debug(Database.PREFIX.append(Component.text("Find action complete.", NamedTextColor.WHITE)));
            }
        });
    }

    public static void update(MongoCollection<Document> collection, UUID uuid, String param1, String param2) {

        if (Objects.equals(param1, "perks.firstperk") || Objects.equals(param1, "perks.secondperk") || Objects.equals(param1, "perks.passiveperk")) {
            collection.updateOne(eq("UUID", uuid.toString()), set(param1, param2)).subscribe(new Subscriber<UpdateResult>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Integer.MAX_VALUE);
                }
                @Override
                public void onNext(UpdateResult updateResult) {
                    Console.debug(Database.PREFIX.append(Component.text("Updateresult: " + updateResult.toString(), NamedTextColor.WHITE)));
                }
                @Override
                public void onError(Throwable t) {
                    Console.send(Database.ERROR.append(Component.text("Update Error: " + t.toString(), NamedTextColor.WHITE)));
                }
                @Override
                public void onComplete() {
                    Console.debug(Database.PREFIX.append(Component.text("Update action complete.", NamedTextColor.WHITE)));
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
                    Console.debug(Database.PREFIX.append(Component.text("Updateresult: " + updateResult.toString(), NamedTextColor.WHITE)));
                }

                @Override
                public void onError(Throwable t) {
                    Console.send(Database.ERROR.append(Component.text("Update Error: " + t.toString(), NamedTextColor.WHITE)));
                }

                @Override
                public void onComplete() {
                    Console.debug(Database.PREFIX.append(Component.text("Update action complete.", NamedTextColor.WHITE)));
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
                    Console.debug(Database.PREFIX.append(Component.text("Updateresult: " + updateResult.toString(), NamedTextColor.WHITE)));
                }
                @Override
                public void onError(Throwable t) {
                    Console.send(Database.ERROR.append(Component.text("Update Error: " + t.toString(), NamedTextColor.WHITE)));
                }
                @Override
                public void onComplete() {
                    Console.debug(Database.PREFIX.append(Component.text("Update action complete.", NamedTextColor.WHITE)));
                }
            });
        }
    }

    public static void exists(String id, final Callback callback) {
        if (!Woolbattle.useDB || !Database.hasConnection()) {
            callback.onSuccess(false);
            return;
        }
        
        Query query = new Query(Criteria.where("UUID").is(id));
        ReactiveMongoTemplate template = new ReactiveMongoTemplate(Database.getConnection(), "minecraft");
        Mono<Boolean> exists = template.exists(query, "players");
        exists.subscribe(
                value -> callback.onSuccess(value),
                error -> Console.send(Database.ERROR.append(Component.text(error.toString(), NamedTextColor.WHITE)))
        );
    }
}

