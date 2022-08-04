// This class was created by Elix on 27.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class LobbyItems implements Listener {

    public static int VotedLives;
    public static int Teams;

    public static void Lobby(Player player) {
        Items.create(player, Material.BOW, null, 5, "§3Perks", 0);
        Items.create(player, Material.BOOK, null, 5, "§5Team wählen", 1);
        particelitem(player);
        Items.create(player, Material.CHEST, null, 5, "§6Inventarsortierung", 5);
        Items.create(player, Material.NAME_TAG, null, 5, "§aLebensanzahl ändern", 7);
        Items.create(player, Material.PAPER, null, 5, "§6Maps", 8);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState)) return;
        switch (event.getItem().getType()) {
            case BOW -> PerkvorInventory(event.getPlayer());
            case BOOK -> TeamsInventory(event.getPlayer());
            case BLAZE_ROD -> PartikelAction(event.getPlayer());
            case CHEST -> Inventarsortierung();
            case NAME_TAG -> Lifes();
            case PAPER -> Maps();
        }
    }

    private void PerkvorInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§3Wähle deine Perks!");
        Items.create(inventory, Material.CHEST, null, 5, "§6Aktiv-Perk #1", 3);
        Items.create(inventory, Material.TRAPPED_CHEST, null, 5, "§6Aktiv-Perk #2", 4);
        Items.create(inventory, Material.ENDER_CHEST, null, 5, "§3Passiv-Perk", 5);
        player.openInventory(inventory);
    }

    private void PerkInventory(Player player, String title) {
        Inventory inventory = Bukkit.createInventory(null, 9*4, title);
        Items.createPerk(inventory, Material.TRIPWIRE_HOOK, false, "§3Booster", "Boostet dich durch die Luft",12, 25, 0);
        Items.createPerk(inventory, Material.FISHING_ROD, false, "§3Enterhaken", "Ziehe dich mit einem Enterhaken über die Map!", 5, 16, 1);
        Items.createPerk(inventory, Material.PACKED_ICE, false, "§3Freezer", "Friere deine Gegner ein!", 6, 6, 2);
        Items.createPerk(inventory, Material.WATCH, false, "§3Großvaters Uhr", "Teleportiert dich nach kurzer Zeit zu einem beliebigen Punk!",8, 14, 3);
        Items.createPerk(inventory, Material.STICK, false, "§3Linebuilder", "Baut eine Linie aus Wolle", 8, 10, 4);
        Items.createPerk(inventory, Material.DIAMOND_BARDING, false, "§3Minigun", "Schießt viele Pfeile in kurzer Zeit!", 12, 12, 5);
        Items.createPerk(inventory, Material.FIREBALL, false, "§3Pfeilbombe", "Schießt am Explosionsort Pfeile in alle Richtungen", 7, 9, 6);
        Items.createPerk(inventory, Material.OBSIDIAN, false, "§3Portal", "Teleportier dich mithilfe des Portals zwischen zwei Punkten hin und her!", 35, 25, 7);
        Items.createPerk(inventory, Material.STAINED_GLASS, 14,  false, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
        Items.createPerk(inventory, Material.BLAZE_ROD, false, "§3Rettungsplattform", "Erschaffe eine Wollplattform unter dir!", 12, 28, 9);
        Items.createPerk(inventory, Material.VINE, false, "§3Rope", "Baut eine vertikale Linie aus Wolle", 12, 25, 10);
        Items.createPerk(inventory, Material.EMERALD, false, "§3Schuztschild", "Beschützt dich vor allen Projektilen! Hält 4 Sekunden an!", 5, 10, 11);
        Items.createPerk(inventory, Material.SLIME_BALL,false, "§3SlimePlattform", "Kataplutiert dich hoch in die Luft!", 15, 25, 12);
        Items.createPerk(inventory, Material.STONE_PLATE, false, "§3Sprengsatz", "Setze eine Mine als Tretmine oder nutze den Fernzünder!", 8, 6, 13);
        Items.createPerk(inventory, Material.SNOW_BALL, false, "§3Tauscher", "Tausche mit deinem Gegner den Platz", 8, 6, 14);
        Items.createPerk(inventory, Material.GLASS_BOTTLE, false, "§3Klospülung", "Spült dich und deine Gegner weg!", 10, 16, 15);
        Items.createPerk(inventory, Material.STAINED_GLASS_PANE, 14, false, "§3Wandgenerator", "Baut eine Wand aus Wolle vor dir", 8, 10, 16);
        Items.createPerk(inventory, Material.TNT, false, "§3WoolBombe", "Booste deine Gegner mit einem werfbaren TNT weg!", 8, 13, 17);
        Items.createPerk(inventory, Material.FISHING_ROD, false, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
        Items.create(inventory, Material.DARK_OAK_DOOR_ITEM, false, "§3Back", 35);
        checkSeclectet(player, inventory);
    }

    private void PassivePerkInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*4, "§3Verfügbare Perks:");
        Items.createPassivePerk(inventory, Material.LADDER, false, "§3Aufzug", "Teleportiere dich mit der Enderperle auf den getroffenen Block!", null, "§6Cooldown: 3 Enderperlen", 0);
        Items.createPassivePerk(inventory, Material.TNT, false, "§3Explodierender Pfeil", "Ein besonderer Pfeil der alles in die Luft sprengt!", "§6Preis: 8 Wolle", "Nach Pfeilen: 8", 1);
        Items.createPassivePerk(inventory, Material.FISHING_ROD, false, "§3IDK", "IDK", "IDK", "IDK", 2);
        Items.createPassivePerk(inventory, Material.DISPENSER, false, "§3Pfeilregen", "Lässt einen Pfeilregen herunter prasseln", "§6Preis: 4 Wolle", "§cCooldown: 6", 3);
        Items.createPassivePerk(inventory, Material.IRON_CHESTPLATE, false, "§3Recharger", "Verringert den Cooldown deiner Perks um 20%", "§6Preis: 20 Wolle", null, 4);
        Items.createPassivePerk(inventory, Material.CACTUS, false, "§3Reflector", "Gibt das dir zugefügte Knockback zu 30% an deinen Gegner zurück!", "§6Preis: 15 Wolle", "§6Cooldown: 13", 5);
        Items.createPassivePerk(inventory, Material.RABBIT_FOOT, false, "§3Rocket Jump", "Lässt dich einen höheren Doppelsprung machen!", "§6Preis: 14 Wolle", null, 6);
        Items.createPassivePerk(inventory, Material.GOLD_INGOT, false, "§3IDK", "", "§6Preis: 8 Wolle", "§6Cooldown: 2", 7);
        Items.createPassivePerk(inventory, Material.BLAZE_POWDER, false, "§3Schock Pfeil", "Blockiere die Perks deines Gegners!", "§6Preis: 10 Wolle", "§6Nach Pfeilen: 8", 8);
        Items.createPassivePerk(inventory, Material.ARROW, false, "§3SlowArrow", "Verlangsame den Gegner!", "§6Preis: 2 Wolle", "§6Nach Pfeilen: 3", 9);
        Items.createPassivePerk(inventory, Material.MONSTER_EGGS, 52, false, "§3Spinne", "Klettere wie eine Spinne an einer Wand hoch!", "§6Preis: 2 Wolle je Sekunde klettern", null, 10); //TODO Falscher Byte Code
        Items.createPassivePerk(inventory, Material.DIAMOND_BOOTS, false, "§3Stomper", "Katapultiert alle Gegner in deiner Nähe nach einem Doppelsprung weg!", "§6Preis: 10 Wolle", "§6Nach Sprüngen: 2", 11);
        Items.create(inventory, Material.GOLD_HOE, null, 5, "§3SlimePlattform", 12);
        Items.create(inventory, Material.DARK_OAK_DOOR_ITEM, false, "§3Back", 35);
        checkSeclectet(player, inventory);
    }

    private void TeamsInventory(Player player) {
        //TODO: Material stimmt nicht
        Inventory inventory = Bukkit.createInventory(null, 9, "§bWähle den Team!");
        Items.create(inventory, Material.REDSTONE, null, 5, "§cTeam Rot", 1);
        Items.create(inventory, Material.LAPIS_ORE, null, 5, "§bTeam Blau", 3);
        Items.create(inventory, Material.YELLOW_FLOWER, null, 5, "§eTeam Gelb", 5);
        Items.create(inventory, Material.GREEN_RECORD, null, 5, "§aTeam Grün", 7);
        player.openInventory(inventory);
    }

    private void PartikelAction(Player player) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        if (config.get(player.getName() + ".particels") == null) {
            config.set(player.getName()+ ".particels", "true");
        } else if (config.get(player.getName() + ".particels") == "true") {
            config.set(player.getName()+ ".particels", "false");
        } else config.set(player.getName()+ ".particels", "true");
        Woolbattle.getPlugin().saveConfig();
        Lobby(player);
    }

    private void Inventarsortierung() {}

    private void Lifes() {
        //TODO: Voting System
        //Festlegen der Lebensanzahl
        VotedLives = 6;
        Teams = 3; //Könnte auch durch Config festgelegt werden
    }

    private void Maps() {}

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        if (event.getClickedInventory().getTitle().equals("§3Wähle deine Perks!")) {
            event.setCancelled(true); //Player kann das Item nicht aus dem Inventar ziehen
            switch (event.getCurrentItem().getType()) {
                case CHEST -> PerkInventory(player, "§3Erstes Perk:");
                case TRAPPED_CHEST -> PerkInventory(player,"§3Zweites Perk:");
                case ENDER_CHEST -> PassivePerkInventory(player);
            }
        }

        if (event.getClickedInventory().getTitle().equals("§3Erstes Perk:")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case TRIPWIRE_HOOK -> config.set(event.getWhoClicked().getName() + ".1Perk", "booster");
                case FISHING_ROD -> config.set(event.getWhoClicked().getName() + ".1Perk", "enterhaken");
                case PACKED_ICE -> config.set(event.getWhoClicked().getName() + ".1Perk", "freezer");
                case WATCH -> config.set(event.getWhoClicked().getName() + ".1Perk", "großvatersuhr");
                case STICK -> config.set(event.getWhoClicked().getName() + ".1Perk", "linebuilder");
                case DIAMOND_BARDING -> config.set(event.getWhoClicked().getName() + ".1Perk", "minigun");
                case FIREBALL -> config.set(event.getWhoClicked().getName() + ".1Perk", "pfeilbombe");
                case OBSIDIAN -> config.set(event.getWhoClicked().getName() + ".1Perk", "portal");
                case STAINED_GLASS -> config.set(event.getWhoClicked().getName() + ".1Perk", "rettungskapsel");
                case BLAZE_ROD -> config.set(event.getWhoClicked().getName() + ".1Perk", "rettungsplattform");
                case VINE -> config.set(event.getWhoClicked().getName() + ".1Perk", "rope");
                case EMERALD -> config.set(event.getWhoClicked().getName() + ".1Perk", "schutzschild");
                case SLIME_BALL -> config.set(event.getWhoClicked().getName() + ".1Perk", "slimeplattform");
                case STONE_PLATE -> config.set(event.getWhoClicked().getName() + ".1Perk", "sprengsatz");
                case SNOW_BALL -> config.set(event.getWhoClicked().getName() + ".1Perk", "tauscher");
                case GLASS_BOTTLE -> config.set(event.getWhoClicked().getName() + ".1Perk", "klospülung");
                case STAINED_GLASS_PANE -> config.set(event.getWhoClicked().getName() + ".1Perk", "wandgenerator");
                case TNT -> config.set(event.getWhoClicked().getName() + ".1Perk", "woolbombe");
                // THE GRABBER
                case DARK_OAK_DOOR_ITEM -> {
                    PerkvorInventory(player);
                    return;
                }
            }
            Woolbattle.getPlugin().saveConfig();
            PerkInventory(player, "§3Erstes Perk:");
        }

        if (event.getClickedInventory().getTitle().equals("§3Zweites Perk:")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case TRIPWIRE_HOOK -> config.set(event.getWhoClicked().getName() + ".2Perk", "booster");
                case FISHING_ROD -> config.set(event.getWhoClicked().getName() + ".2Perk", "enterhaken");
                case PACKED_ICE -> config.set(event.getWhoClicked().getName() + ".2Perk", "freezer");
                case WATCH -> config.set(event.getWhoClicked().getName() + ".2Perk", "großvatersuhr");
                case STICK -> config.set(event.getWhoClicked().getName() + ".2Perk", "linebuilder");
                case DIAMOND_BARDING -> config.set(event.getWhoClicked().getName() + ".2Perk", "minigun");
                case FIREBALL -> config.set(event.getWhoClicked().getName() + ".2Perk", "pfeilbombe");
                case OBSIDIAN -> config.set(event.getWhoClicked().getName() + ".2Perk", "portal");
                case STAINED_GLASS -> config.set(event.getWhoClicked().getName() + ".2Perk", "rettungskapsel");
                case BLAZE_ROD -> config.set(event.getWhoClicked().getName() + ".2Perk", "rettungsplattform");
                case VINE -> config.set(event.getWhoClicked().getName() + ".2Perk", "rope");
                case EMERALD -> config.set(event.getWhoClicked().getName() + ".2Perk", "schutzschild");
                case SLIME_BALL -> config.set(event.getWhoClicked().getName() + ".2Perk", "slimeplattform");
                case STONE_PLATE -> config.set(event.getWhoClicked().getName() + ".2Perk", "sprengsatz");
                case SNOW_BALL -> config.set(event.getWhoClicked().getName() + ".2Perk", "tauscher");
                case GLASS_BOTTLE -> config.set(event.getWhoClicked().getName() + ".2Perk", "klospülung");
                case STAINED_GLASS_PANE -> config.set(event.getWhoClicked().getName() + ".2Perk", "wandgenerator");
                case TNT -> config.set(event.getWhoClicked().getName() + ".2Perk", "woolbombe");
                // THE GRABBER
                case DARK_OAK_DOOR_ITEM -> {
                    PerkvorInventory(player);
                    return;
                }
            }
            Woolbattle.getPlugin().saveConfig();
            PerkInventory(player,"§3Zweites Perk:");
        }

        if (event.getClickedInventory().getTitle().equals("§3Verfügbare Perks:")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case LADDER -> config.set(event.getWhoClicked().getName() + ".passive", "aufzug");
                case TNT -> config.set(event.getWhoClicked().getName() + ".passive", "explodierender_pfeil");
                case FISHING_ROD -> config.set(event.getWhoClicked().getName() + ".passive", "IDK");
                case DISPENSER -> config.set(event.getWhoClicked().getName() + ".passive", "pfeilregen");
                case IRON_CHESTPLATE -> config.set(event.getWhoClicked().getName() + ".passive", "recharger");
                case CACTUS -> config.set(event.getWhoClicked().getName() + ".passive", "reflector");
                case RABBIT_FOOT -> config.set(event.getWhoClicked().getName() + ".passive", "rocket_jump");
                case GOLD_INGOT -> config.set(event.getWhoClicked().getName() + ".passive", "portal");
                case BLAZE_ROD -> config.set(event.getWhoClicked().getName() + ".passive", "schock_pfeil");
                case ARROW -> config.set(event.getWhoClicked().getName() + ".passive", "slowarrow");
                case MONSTER_EGG -> config.set(event.getWhoClicked().getName() + ".passive", "spinne");
                case DIAMOND_BOOTS -> config.set(event.getWhoClicked().getName() + ".passive", "stomper");
                case GOLD_HOE -> config.set(event.getWhoClicked().getName() + ".passive", "slimeplattform");
                case DARK_OAK_DOOR_ITEM -> {
                    PerkvorInventory(player);
                    return;
                }
            }
            Woolbattle.getPlugin().saveConfig();
            PassivePerkInventory(player);
        }
        if (event.getClickedInventory().getTitle().equals("§bWähle den Team!")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case REDSTONE -> addToTeam(LiveSystem.TeamRed, player);
                case LAPIS_ORE -> addToTeam(LiveSystem.TeamBlue, player);
                case YELLOW_FLOWER -> addToTeam(LiveSystem.TeamYellow, player);
                case GREEN_RECORD -> addToTeam(LiveSystem.TeamGreen, player);
            }
        }
    }

    public void checkSeclectet(Player player, Inventory inventory) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");
        Object Pperk = config.get(player.getName() + ".passive");
        if (inventory.getTitle().equals("§3Erstes Perk:")) {
            if ("booster".equals(Fperk)) {
                Items.createPerk(inventory, Material.TRIPWIRE_HOOK, true, "§3Booster", "Boostet dich durch die Luft",12, 25, 0);
            } else if ("enterhaken".equals(Fperk)) {
                Items.createPerk(inventory, Material.FISHING_ROD, true, "§3Enterhaken", "Ziehe dich mit einem Enterhaken über die Map!", 5, 16, 1);
            } else if ("freezer".equals(Fperk)) {
                Items.createPerk(inventory, Material.PACKED_ICE, true, "§3Freezer", "Friere deine Gegner ein!", 6, 6, 2);
            } else if ("großvatersuhr".equals(Fperk)) {
                Items.createPerk(inventory, Material.WATCH, true, "§3Großvaters Uhr", "Teleportiert dich nach kurzer Zeit zu einem beliebigen Punk!",8, 14, 3);
            } else if ("linebuilder".equals(Fperk)) {
                Items.createPerk(inventory, Material.STICK, true, "§3Linebuilder", "Baut eine Linie aus Wolle", 8, 10, 4);
            } else if ("minigun".equals(Fperk)) {
                Items.createPerk(inventory, Material.DIAMOND_BARDING, true, "§3Minigun", "Schießt viele Pfeile in kurzer Zeit!", 12, 12, 5);
            } else if ("pfeilbombe".equals(Fperk)) {
                Items.createPerk(inventory, Material.FIREBALL, true, "§3Pfeilbombe", "Schießt am Explosionsort Pfeile in alle Richtungen", 7, 9, 6);
            } else if ("portal".equals(Fperk)) {
                Items.createPerk(inventory, Material.OBSIDIAN, true, "§3Portal", "Teleportier dich mithilfe des Portals zwischen zwei Punkten hin und her!", 35, 25, 7);
            } else if ("rettungskapsel".equals(Fperk)) {
                Items.createPerk(inventory, Material.STAINED_GLASS, 14,  true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
            } else if ("rettungsplattform".equals(Fperk)) {
                Items.createPerk(inventory, Material.BLAZE_ROD, true, "§3Rettungsplattform", "Erschaffe eine Wollplattform unter dir!", 12, 28, 9);
            } else if ("rope".equals(Fperk)) {
                Items.createPerk(inventory, Material.VINE, true, "§3Rope", "Baut eine vertikale Linie aus Wolle", 12, 25, 10);
            } else if ("schutzschild".equals(Fperk)) {
                Items.createPerk(inventory, Material.EMERALD, true, "§3Schuztschild", "Beschützt dich vor allen Projektilen! Hält 4 Sekunden an!", 5, 10, 11);
            } else if ("slimeplattform".equals(Fperk)) {
                Items.createPerk(inventory, Material.SLIME_BALL,true, "§3SlimePlattform", "Kataplutiert dich hoch in die Luft!", 15, 25, 12);
            } else if ("sprengsatz".equals(Fperk)) {
                Items.createPerk(inventory, Material.STONE_PLATE, true, "§3Sprengsatz", "Setze eine Mine als Tretmine oder nutze den Fernzünder!", 8, 6, 13);
            } else if ("tauscher".equals(Fperk)) {
                Items.createPerk(inventory, Material.SNOW_BALL, true, "§3Tauscher", "Tausche mit deinem Gegner den Platz", 8, 6, 14);
            } else if ("klospülung".equals(Fperk)) {
                Items.createPerk(inventory, Material.GLASS_BOTTLE, true, "§3Klospülung", "Spült dich und deine Gegner weg!", 10, 16, 15);
            } else if ("wandgenerator".equals(Fperk)) {
                Items.createPerk(inventory, Material.STAINED_GLASS_PANE, 14, true, "§3Wandgenerator", "Baut eine Wand aus Wolle vor dir", 8, 10, 16);player.openInventory(inventory);
            } else if ("woolbombe".equals(Fperk)) {
                Items.createPerk(inventory, Material.TNT, true, "§3WoolBombe", "Booste deine Gegner mit einem werfbaren TNT weg!", 8, 13, 17);
            } else if ("thegrabber".equals(Fperk)) {
                Items.createPerk(inventory, Material.FISHING_ROD, true, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
            } else
                System.out.println("[NO_PERK]: " + player.getName());

        } else if (inventory.getTitle().equals("§3Zweites Perk:")) {
            if ("booster".equals(Sperk)) {
                Items.createPerk(inventory, Material.TRIPWIRE_HOOK, true, "§3Booster", "Boostet dich durch die Luft",12, 25, 0);
            } else if ("enterhaken".equals(Sperk)) {
                Items.createPerk(inventory, Material.FISHING_ROD, true, "§3Enterhaken", "Ziehe dich mit einem Enterhaken über die Map!", 5, 16, 1);
            } else if ("freezer".equals(Sperk)) {
                Items.createPerk(inventory, Material.PACKED_ICE, true, "§3Freezer", "Friere deine Gegner ein!", 6, 6, 2);
            } else if ("großvatersuhr".equals(Sperk)) {
                Items.createPerk(inventory, Material.WATCH, true, "§3Großvaters Uhr", "Teleportiert dich nach kurzer Zeit zu einem beliebigen Punk!",8, 14, 3);
            } else if ("linebuilder".equals(Sperk)) {
                Items.createPerk(inventory, Material.STICK, true, "§3Linebuilder", "Baut eine Linie aus Wolle", 8, 10, 4);
            } else if ("minigun".equals(Sperk)) {
                Items.createPerk(inventory, Material.DIAMOND_BARDING, true, "§3Minigun", "Schießt viele Pfeile in kurzer Zeit!", 12, 12, 5);
            } else if ("pfeilbombe".equals(Sperk)) {
                Items.createPerk(inventory, Material.FIREBALL, true, "§3Pfeilbombe", "Schießt am Explosionsort Pfeile in alle Richtungen", 7, 9, 6);
            } else if ("portal".equals(Sperk)) {
                Items.createPerk(inventory, Material.OBSIDIAN, true, "§3Portal", "Teleportier dich mithilfe des Portals zwischen zwei Punkten hin und her!", 35, 25, 7);
            } else if ("rettungskapsel".equals(Sperk)) {
                Items.createPerk(inventory, Material.STAINED_GLASS, 14,  true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
            } else if ("rettungsplattform".equals(Sperk)) {
                Items.createPerk(inventory, Material.BLAZE_ROD, true, "§3Rettungsplattform", "Erschaffe eine Wollplattform unter dir!", 12, 28, 9);
            } else if ("rope".equals(Sperk)) {
                Items.createPerk(inventory, Material.VINE, true, "§3Rope", "Baut eine vertikale Linie aus Wolle", 12, 25, 10);
            } else if ("schutzschild".equals(Sperk)) {
                Items.createPerk(inventory, Material.EMERALD, true, "§3Schuztschild", "Beschützt dich vor allen Projektilen! Hält 4 Sekunden an!", 5, 10, 11);
            } else if ("slimeplattform".equals(Sperk)) {
                Items.createPerk(inventory, Material.SLIME_BALL,true, "§3SlimePlattform", "Kataplutiert dich hoch in die Luft!", 15, 25, 12);
            } else if ("sprengsatz".equals(Sperk)) {
                Items.createPerk(inventory, Material.STONE_PLATE, true, "§3Sprengsatz", "Setze eine Mine als Tretmine oder nutze den Fernzünder!", 8, 6, 13);
            } else if ("tauscher".equals(Sperk)) {
                Items.createPerk(inventory, Material.SNOW_BALL, true, "§3Tauscher", "Tausche mit deinem Gegner den Platz", 8, 6, 14);
            } else if ("klospülung".equals(Sperk)) {
                Items.createPerk(inventory, Material.GLASS_BOTTLE, true, "§3Klospülung", "Spült dich und deine Gegner weg!", 10, 16, 15);
            } else if ("wandgenerator".equals(Sperk)) {
                Items.createPerk(inventory, Material.STAINED_GLASS_PANE, 14, true, "§3Wandgenerator", "Baut eine Wand aus Wolle vor dir", 8, 10, 16);
            } else if ("woolbombe".equals(Sperk)) {
                Items.createPerk(inventory, Material.TNT, true, "§3WoolBombe", "Booste deine Gegner mit einem werfbaren TNT weg!", 8, 13, 17);
            } else if ("thegrabber".equals(Sperk)) {
                Items.createPerk(inventory, Material.FISHING_ROD, true, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
            } else
                System.out.println("[NO_PERK2]: " + player.getName());

        } else if (inventory.getTitle().equals("§3Verfügbare Perks:")) {
            if ("aufzug".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.LADDER, true, "§3Aufzug", "Teleportiere dich mit der Enderperle auf den getroffenen Block!", null, "§6Cooldown: 3 Enderperlen", 0);
            } else if ("explodierender_pfeil".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.TNT, true, "§3Explodierender Pfeil", "Ein besonderer Pfeil der alles in die Luft sprengt!", "§6Preis: 8 Wolle", "Nach Pfeilen: 8", 1);
            } else if ("IDK".equals(Pperk)) { //TODO: Wrong name
                Items.createPassivePerk(inventory, Material.FISHING_ROD, true, "§3IDK", "IDK", "IDK", "IDK", 2);
            } else if ("pfeilregen".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.DISPENSER, true, "§3Pfeilregen", "Lässt einen Pfeilegen herunter prasseln", "§6Preis: 4 Wolle", "§cCooldown: 6", 3);
            } else if ("recharger".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.IRON_CHESTPLATE, true, "§3Recharger", "Verringert den Cooldown deiner Perks um 20%", "§6Preis: 20 Wolle", null, 4);
            } else if ("reflector".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.CACTUS, true, "§3Reflector", "Gibt das dir zugefügte Knockback zu 30% an deinen Gegner zurück!", "§6Preis: 15 Wolle", "§6Cooldown: 13", 5);
            } else if ("rocket_jump".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.RABBIT_FOOT, true, "§3Rocket Jump", "Lässt dich einen höheren Doppelsprung machen!", "§6Preis: 14 Wolle", null, 6);
            } else if ("portal".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.GOLD_INGOT, true, "§3IDK", "", "§6Preis: 8 Wolle", "§6Cooldown: 2", 7);
            } else if ("schock_pfeil".equals(Pperk)) {
                Items.createPerk(inventory, Material.STAINED_GLASS, 14,  true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
            } else if ("slowarrow".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.ARROW, true, "§3SlowArrow", "Verlangsame den Gegener!", "§6Preis: 2 Wolle", "§6Nach Pfeilen: 3", 9);
            } else if ("spinne".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.MONSTER_EGGS, 52, true, "§3Spinne", "Klettere wie eine Spinne an einer Wand hoch!", "§6Preis: 2 Wolle je Sekunde klettern", null, 10);
            } else if ("stomper".equals(Pperk)) {
                Items.createPassivePerk(inventory, Material.DIAMOND_BOOTS, true, "§3Stomper", "Katapultiert alle Gegner in deiner Nähe nach einem Doppelsprung weg!", "§6Preis: 10 Wolle", "§6Nach Sprüngen: 2", 11);
            } else if ("slimeplattform".equals(Pperk)) { //TODO: Wrong name
                Items.create(inventory, Material.GOLD_HOE, true, "§3SlimePlattform", 12);
            } else
                System.out.println("[ERROR] NO_PERK_FOUND");
        } else
            System.out.println("[ERROR] NO_INVENTORY_FOUND");
        player.openInventory(inventory);
    }

    private void addToTeam(ArrayList<Player> arrayList, Player player) {
        if (arrayList.size() < LiveSystem.TeamSize) {
            arrayList.add(player);
        } else System.out.println("Team is full");

    }

    public static void particelitem(Player player) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        if (config.get(player.getName() + ".particels") == null) {
            config.set(player.getName() + ".particels", "true");
            Items.create(player, Material.BLAZE_ROD, null, 5, "§ePartikel sind §aAN", 3);
        } else if (config.get(player.getName() + ".particels") == "true") {
            Items.create(player, Material.BLAZE_ROD, null, 5, "§ePartikel sind §aAN", 3);
        } else
            Items.create(player, Material.BLAZE_ROD, null, 5, "§ePartikel sind §cAUS", 3);
        Woolbattle.getPlugin().saveConfig();
    }
}