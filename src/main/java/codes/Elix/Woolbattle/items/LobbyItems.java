// This class was created by Elix on 27.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.Perk;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Objects;

public class LobbyItems implements Listener {

    public static int VotedLives = 6;
    private String Title;

    private int life6;
    private int life12;
    private int life18;
    private int life23;

    public static void Lobby(Player player) {
        Items.create(player.getInventory(), Material.BOW, null, 5, "§3Perks", 0);
        Items.create(player.getInventory(), Material.BOOK, null, 5, "§5Team wählen", 1);
        particleitem(player);
        Items.create(player.getInventory(), Material.CHEST, null, 5, "§6Inventarsortierung", 5);
        Items.create(player.getInventory(), Material.NAME_TAG, null, 5, "§aLebensanzahl ändern", 7);
        Items.create(player.getInventory(), Material.PAPER, null, 5, "§6Maps", 8);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState)) return;
        switch (event.getItem().getType()) {
            case BOW -> PerkVorInventory(event.getPlayer());
            case BOOK -> TeamsInventory(event.getPlayer());
            case BLAZE_ROD -> PartikelAction(event.getPlayer());
            case CHEST -> Inventarsortierung();
            case NAME_TAG -> Lifes(event.getPlayer());
            case PAPER -> Maps();
        }
    }

    private void PerkVorInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§3Wähle deine Perks!");
        Items.create(inventory, Material.CHEST, null, 5, "§6Aktiv-Perk #1", 3);
        Items.create(inventory, Material.TRAPPED_CHEST, null, 5, "§6Aktiv-Perk #2", 4);
        Items.create(inventory, Material.ENDER_CHEST, null, 5, "§3Passiv-Perk", 5);
        player.openInventory(inventory);
    }

    private void PerkInventory(Player player, String title) {
        Title = title;
        Inventory inventory = Bukkit.createInventory(null, 9*4, title);
        Items.create(inventory, Material.TRIPWIRE_HOOK, false, "§3Booster", "Boostet dich durch die Luft",12, 25, 0);
        Items.create(inventory, Material.FISHING_ROD, false, "§3Enterhaken", "Ziehe dich mit einem Enterhaken über die Map!", 5, 16, 1);
        Items.create(inventory, Material.PACKED_ICE, false, "§3Freezer", "Friere deine Gegner ein!", 6, 6, 2);
        Items.create(inventory, Material.CLOCK, false, "§3Großvaters Uhr", "Teleportiert dich nach kurzer Zeit zu einem beliebigen Punk!",8, 14, 3);
        Items.create(inventory, Material.STICK, false, "§3Linebuilder", "Baut eine Linie aus Wolle", 8, 10, 4);
        Items.create(inventory, Material.DIAMOND_HORSE_ARMOR, false, "§3Minigun", "Schießt viele Pfeile in kurzer Zeit!", 12, 12, 5);
        Items.create(inventory, Material.FIRE_CHARGE, false, "§3Pfeilbombe", "Schießt am Explosionsort Pfeile in alle Richtungen", 7, 9, 6);
        Items.create(inventory, Material.OBSIDIAN, false, "§3Portal", "Teleportier dich mithilfe des Portals zwischen zwei Punkten hin und her!", 35, 25, 7);
        Items.create(inventory, Material.RED_STAINED_GLASS,false, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
        Items.create(inventory, Material.BLAZE_ROD, false, "§3Rettungsplattform", "Erschaffe eine Wollplattform unter dir!", 12, 28, 9);
        Items.create(inventory, Material.VINE, false, "§3Rope", "Baut eine vertikale Linie aus Wolle", 12, 25, 10);
        Items.create(inventory, Material.EMERALD, false, "§3Schuztschild", "Beschützt dich vor allen Projektilen! Hält 4 Sekunden an!", 5, 10, 11);
        Items.create(inventory, Material.SLIME_BALL,false, "§3SlimePlattform", "Kataplutiert dich hoch in die Luft!", 15, 25, 12);
        Items.create(inventory, Material.STONE_PRESSURE_PLATE, false, "§3Sprengsatz", "Setze eine Mine als Tretmine oder nutze den Fernzünder!", 8, 6, 13);
        Items.create(inventory, Material.SNOWBALL, false, "§3Tauscher", "Tausche mit deinem Gegner den Platz", 8, 6, 14);
        Items.create(inventory, Material.GLASS_BOTTLE, false, "§3Klospülung", "Spült dich und deine Gegner weg!", 10, 16, 15);
        Items.create(inventory, Material.RED_STAINED_GLASS_PANE, false, "§3Wandgenerator", "Baut eine Wand aus Wolle vor dir", 8, 10, 16);
        Items.create(inventory, Material.TNT, false, "§3WoolBombe", "Booste deine Gegner mit einem werfbaren TNT weg!", 8, 13, 17);
        Items.create(inventory, Material.FISHING_ROD, false, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
        Items.create(inventory, Material.DARK_OAK_DOOR, false, "§3Back", 35);
        checkSelectet(player, inventory);
    }

    private void PassivePerkInventory(Player player) {
        Title = "§3Verfügbare Perks:";
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
        Items.createPassivePerk(inventory, Material.SPIDER_SPAWN_EGG,false, "§3Spinne", "Klettere wie eine Spinne an einer Wand hoch!", "§6Preis: 2 Wolle je Sekunde klettern", null, 10); //TODO Falscher Byte Code
        Items.createPassivePerk(inventory, Material.DIAMOND_BOOTS, false, "§3Stomper", "Katapultiert alle Gegner in deiner Nähe nach einem Doppelsprung weg!", "§6Preis: 10 Wolle", "§6Nach Sprüngen: 2", 11);
        Items.create(inventory, Material.GOLDEN_HOE, null, 5, "§3SlimePlattform", 12);
        Items.create(inventory, Material.DARK_OAK_DOOR, false, "§3Back", 35);
        checkSelectet(player, inventory);
    }

    private void TeamsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§bWähle dein Team!");
        if (LiveSystem.Teams == 2) {
            checkTeam(LiveSystem.TeamRed, inventory, Material.RED_DYE, "§cTeam Rot", 3, ChatColor.RED);
            checkTeam(LiveSystem.TeamBlue, inventory, Material.BLUE_DYE, "§bTeam Blau", 5, ChatColor.AQUA);
        } else {
            checkTeam(LiveSystem.TeamRed, inventory, Material.RED_DYE, "§cTeam Rot", 1, ChatColor.RED);
            checkTeam(LiveSystem.TeamBlue, inventory, Material.BLUE_DYE, "§bTeam Blau", 3, ChatColor.AQUA);
            checkTeam(LiveSystem.TeamYellow, inventory, Material.YELLOW_DYE, "§eTeam Gelb", 5, ChatColor.YELLOW);
            checkTeam(LiveSystem.TeamGreen, inventory, Material.GREEN_DYE, "§aTeam Grün", 7, ChatColor.GREEN);
        }
        player.openInventory(inventory);
    }


    private void Inventarsortierung() {}

    private void Lifes(Player player) {
        //TODO: Voting System

        Inventory inventory = Bukkit.createInventory(null, 3*9, "§aLebensanzahl");
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 0);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 1);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 2);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 3);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 4);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 5);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 6);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 7);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 8);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 9);
        Items.createMap(inventory, Material.MAGENTA_DYE, 6, "§e6 Leben", "§8» §7Votes: §5" + life6, 10);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 11);
        Items.createMap(inventory, Material.MAGENTA_DYE, 12, "§e12 Leben", "§8» §7Votes: §5" + life12, 12);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 13);
        Items.create(inventory, Material.MAGENTA_DYE, 18, "§e18 Leben", "§8» §7Votes: §5" + life18, 14);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 15);
        Items.createMap(inventory, Material.MAGENTA_DYE, 23, "§e23 Leben", "§8» §7Votes: §5" + life23, 16);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 17);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 18);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 19);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 20);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 21);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 22);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 23);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 24);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 25);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 26);
        player.openInventory(inventory);
    }

    private void Maps() {}

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;

        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        Material clicked = event.getCurrentItem().getType();

        String FirstPerk = (String) config.get(player.getName() + ".1Perk");
        String SecondPerk = (String) config.get(player.getName() + ".2Perk");
        String PassivePerk = (String) config.get(player.getName() + ".passive");
        // Perk playerperk = new Perk(player, FirstPerk, SecondPerk, PassivePerk);
        // if (playerperk.getfirstPerk().equals(playerperk.getpassivePerk())) something like that
        // if (playerperk.getfirstPerk() || playerperk.getsecondtPerk())

        if (event.getView().getTitle().equals("§3Wähle deine Perks!")) {
            event.setCancelled(true); //Player kann das Item nicht aus dem Inventar ziehen
            switch (clicked) {
                case CHEST -> PerkInventory(player, "§3Erstes Perk:");
                case TRAPPED_CHEST -> PerkInventory(player,"§3Zweites Perk:");
                case ENDER_CHEST -> PassivePerkInventory(player);
            }
        }

        if (event.getView().getTitle().equals("§3Erstes Perk:")) {
            event.setCancelled(true);
            switch (clicked) {
                case TRIPWIRE_HOOK -> PerkItems.select(player, "booster", ".1Perk");
                case FISHING_ROD -> PerkItems.select(player, "enterhaken", ".1Perk");
                case PACKED_ICE -> PerkItems.select(player, "freezer", ".1Perk");
                case CLOCK -> PerkItems.select(player, "großvatersuhr", ".1Perk");
                case STICK -> PerkItems.select(player, "linebuilder", ".1Perk");
                case DIAMOND_HORSE_ARMOR -> PerkItems.select(player, "minigun", ".1Perk");
                case FIRE_CHARGE -> PerkItems.select(player, "pfeilbombe", ".1Perk");
                case OBSIDIAN -> PerkItems.select(player, "portal", ".1Perk");
                case RED_STAINED_GLASS -> PerkItems.select(player, "rettungskapsel", ".1Perk");
                case BLAZE_ROD -> PerkItems.select(player, "rettungsplattform", ".1Perk");
                case VINE -> PerkItems.select(player, "rope", ".1Perk");
                case EMERALD -> PerkItems.select(player, "schutzschild", ".1Perk");
                case SLIME_BALL -> PerkItems.select(player, "slimeplattform", ".1Perk");
                case STONE_PRESSURE_PLATE -> PerkItems.select(player, "sprengsatz", ".1Perk");
                case SNOWBALL -> PerkItems.select(player, "tauscher", ".1Perk");
                case GLASS_BOTTLE -> PerkItems.select(player, "klospülung", ".1Perk");
                case RED_STAINED_GLASS_PANE -> PerkItems.select(player, "wandgenerator", ".1Perk");
                case TNT -> PerkItems.select(player, "woolbombe", ".1Perk");
                // THE GRABBER
                case DARK_OAK_DOOR -> {
                    PerkVorInventory(player);
                    return;
                }
            }
            Woolbattle.getPlugin().saveConfig();
            PerkInventory(player, "§3Erstes Perk:");
        }

        if (event.getView().getTitle().equals("§3Zweites Perk:")) {
            event.setCancelled(true);
            switch (clicked) {
                case TRIPWIRE_HOOK -> PerkItems.select(player, "booster", ".2Perk");
                case FISHING_ROD -> PerkItems.select(player, "enterhaken", ".2Perk");
                case PACKED_ICE -> PerkItems.select(player, "freezer", ".2Perk");
                case CLOCK -> PerkItems.select(player, "großvatersuhr", ".2Perk");
                case STICK -> PerkItems.select(player, "linebuilder", ".2Perk");
                case DIAMOND_HORSE_ARMOR -> PerkItems.select(player, "minigun", ".2Perk");
                case FIRE_CHARGE -> PerkItems.select(player, "pfeilbombe", ".2Perk");
                case OBSIDIAN -> PerkItems.select(player, "portal", ".2Perk");
                case RED_STAINED_GLASS -> PerkItems.select(player, "rettungskapsel", ".2Perk");
                case BLAZE_ROD -> PerkItems.select(player, "rettungsplattform", ".2Perk");
                case VINE -> PerkItems.select(player, "rope", ".2Perk");
                case EMERALD -> PerkItems.select(player, "schutzschild", ".2Perk");
                case SLIME_BALL -> PerkItems.select(player, "slimeplattform", ".2Perk");
                case STONE_PRESSURE_PLATE -> PerkItems.select(player, "sprengsatz", ".2Perk");
                case SNOWBALL -> PerkItems.select(player, "tauscher", ".2Perk");
                case GLASS_BOTTLE -> PerkItems.select(player, "klospülung", ".2Perk");
                case RED_STAINED_GLASS_PANE -> PerkItems.select(player, "wandgenerator", ".2Perk");
                case TNT -> PerkItems.select(player, "woolbombe", ".2Perk");
                // THE GRABBER
                case DARK_OAK_DOOR -> {
                    PerkVorInventory(player);
                    return;
                }
            }
            Woolbattle.getPlugin().saveConfig();
            PerkInventory(player,"§3Zweites Perk:");
        }

        if (event.getView().getTitle().equals("§3Verfügbare Perks:")) {
            event.setCancelled(true);
            String name = event.getWhoClicked().getName();
            switch (clicked) {
                case LADDER -> config.set(name + ".passive", "aufzug");
                case TNT -> config.set(name + ".passive", "explodierender_pfeil");
                case FISHING_ROD -> config.set(name + ".passive", "IDK");
                case DISPENSER -> config.set(name + ".passive", "pfeilregen");
                case IRON_CHESTPLATE -> config.set(name + ".passive", "recharger");
                case CACTUS -> config.set(name + ".passive", "reflector");
                case RABBIT_FOOT -> config.set(name + ".passive", "rocket_jump");
                case GOLD_INGOT -> config.set(name + ".passive", "portal");
                case BLAZE_ROD -> config.set(name + ".passive", "schock_pfeil");
                case ARROW -> config.set(name + ".passive", "slowarrow");
                case SPIDER_SPAWN_EGG -> config.set(name + ".passive", "spinne");
                case DIAMOND_BOOTS -> config.set(name + ".passive", "stomper");
                case GOLDEN_HOE -> config.set(name + ".passive", "slimeplattform");
                case DARK_OAK_DOOR -> {
                    PerkVorInventory(player);
                    return;
                }
            }
            Woolbattle.getPlugin().saveConfig();
            PassivePerkInventory(player);
        }

        if (event.getView().getTitle().equals("§bWähle dein Team!")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§cTeam Rot" -> addToTeam(LiveSystem.TeamRed, player);
                case "§bTeam Blau" -> addToTeam(LiveSystem.TeamBlue, player);
                case "§eTeam Gelb" -> addToTeam(LiveSystem.TeamYellow, player);
                case "§aTeam Grün" -> addToTeam(LiveSystem.TeamGreen, player);
            }
            TeamsInventory(player);
        }

        if (event.getView().getTitle().equals("§aLebensanzahl")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§e6 Leben" -> clickedvotes(player, life6);
                case "§e12 Leben" -> clickedvotes(player, life12);
                case "§e18 Leben" -> clickedvotes(player, life18);
                case "§e23 Leben" -> clickedvotes(player, life23);
            }
            Lifes(player);
        }
        if (GameStateManager.getCurrentGameState() instanceof LobbyState)
            event.setCancelled(true);
        if (Items.interact.contains(player))
            event.setCancelled(true);
        if (event.getCurrentItem().getType() ==  Material.LEATHER_BOOTS)
            event.setCancelled(true);
    }

    public void checkSelectet(Player player, Inventory inventory) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");
        Object Pperk = config.get(player.getName() + ".passive");

        switch (Title) {
            case "§3Erstes Perk:" -> {
                if (Fperk == null) {
                    config.set(player.getName() + ".1Perk", "booster");
                    Fperk = "booster";
                }
                switch (Fperk.toString()) {
                    case "booster" -> Items.create(inventory, Material.TRIPWIRE_HOOK, true, "§3Booster", "Boostet dich durch die Luft", 12, 25, 0);
                    case "enterhaken" -> Items.create(inventory, Material.FISHING_ROD, true, "§3Enterhaken", "Ziehe dich mit einem Enterhaken über die Map!", 5, 16, 1);
                    case "freezer" -> Items.create(inventory, Material.PACKED_ICE, true, "§3Freezer", "Friere deine Gegner ein!", 6, 6, 2);
                    case "großvatersuhr" -> Items.create(inventory, Material.CLOCK, true, "§3Großvaters Uhr", "Teleportiert dich nach kurzer Zeit zu einem beliebigen Punk!", 8, 14, 3);
                    case "linebuilder" -> Items.create(inventory, Material.STICK, true, "§3Linebuilder", "Baut eine Linie aus Wolle", 8, 10, 4);
                    case "minigun" -> Items.create(inventory, Material.DIAMOND_HORSE_ARMOR, true, "§3Minigun", "Schießt viele Pfeile in kurzer Zeit!", 12, 12, 5);
                    case "pfeilbombe" -> Items.create(inventory, Material.FIRE_CHARGE, true, "§3Pfeilbombe", "Schießt am Explosionsort Pfeile in alle Richtungen", 7, 9, 6);
                    case "portal" -> Items.create(inventory, Material.OBSIDIAN, true, "§3Portal", "Teleportier dich mithilfe des Portals zwischen zwei Punkten hin und her!", 35, 25, 7);
                    case "rettungskapsel" -> Items.create(inventory, Material.RED_STAINED_GLASS, true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
                    case "rettungsplattform" -> Items.create(inventory, Material.BLAZE_ROD, true, "§3Rettungsplattform", "Erschaffe eine Wollplattform unter dir!", 12, 28, 9);
                    case "rope" -> Items.create(inventory, Material.VINE, true, "§3Rope", "Baut eine vertikale Linie aus Wolle", 12, 25, 10);
                    case "schutzschild" -> Items.create(inventory, Material.EMERALD, true, "§3Schuztschild", "Beschützt dich vor allen Projektilen! Hält 4 Sekunden an!", 5, 10, 11);
                    case "slimeplattform" -> Items.create(inventory, Material.SLIME_BALL, true, "§3SlimePlattform", "Kataplutiert dich hoch in die Luft!", 15, 25, 12);
                    case "sprengsatz" -> Items.create(inventory, Material.STONE_PRESSURE_PLATE, true, "§3Sprengsatz", "Setze eine Mine als Tretmine oder nutze den Fernzünder!", 8, 6, 13);
                    case "tauscher" -> Items.create(inventory, Material.SNOWBALL, true, "§3Tauscher", "Tausche mit deinem Gegner den Platz", 8, 6, 14);
                    case "klospülung" -> Items.create(inventory, Material.GLASS_BOTTLE, true, "§3Klospülung", "Spült dich und deine Gegner weg!", 10, 16, 15);
                    case "wandgenerator" -> Items.create(inventory, Material.RED_STAINED_GLASS_PANE, true, "§3Wandgenerator", "Baut eine Wand aus Wolle vor dir", 8, 10, 16);
                    case "woolbombe" -> Items.create(inventory, Material.TNT, true, "§3WoolBombe", "Booste deine Gegner mit einem werfbaren TNT weg!", 8, 13, 17);
                    case "thegrabber" -> Items.create(inventory, Material.FISHING_ROD, true, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
                    default -> System.out.println("[NO_FIRST_PERK]: " + player.getName());
                }
            }
            case "§3Zweites Perk:" -> {
                if (Sperk == null) {
                    config.set(player.getName() + ".2Perk", "enterhaken");
                    Sperk = "enterhaken";
                }
                switch (Objects.requireNonNull(Sperk).toString()) {
                    case "booster" -> Items.create(inventory, Material.TRIPWIRE_HOOK, true, "§3Booster", "Boostet dich durch die Luft", 12, 25, 0);
                    case "enterhaken" -> Items.create(inventory, Material.FISHING_ROD, true, "§3Enterhaken", "Ziehe dich mit einem Enterhaken über die Map!", 5, 16, 1);
                    case "freezer" -> Items.create(inventory, Material.PACKED_ICE, true, "§3Freezer", "Friere deine Gegner ein!", 6, 6, 2);
                    case "großvatersuhr" -> Items.create(inventory, Material.CLOCK, true, "§3Großvaters Uhr", "Teleportiert dich nach kurzer Zeit zu einem beliebigen Punk!", 8, 14, 3);
                    case "linebuilder" -> Items.create(inventory, Material.STICK, true, "§3Linebuilder", "Baut eine Linie aus Wolle", 8, 10, 4);
                    case "minigun" -> Items.create(inventory, Material.DIAMOND_HORSE_ARMOR, true, "§3Minigun", "Schießt viele Pfeile in kurzer Zeit!", 12, 12, 5);
                    case "pfeilbombe" -> Items.create(inventory, Material.FIRE_CHARGE, true, "§3Pfeilbombe", "Schießt am Explosionsort Pfeile in alle Richtungen", 7, 9, 6);
                    case "portal" -> Items.create(inventory, Material.OBSIDIAN, true, "§3Portal", "Teleportier dich mithilfe des Portals zwischen zwei Punkten hin und her!", 35, 25, 7);
                    case "rettungskapsel" -> Items.create(inventory, Material.RED_STAINED_GLASS, true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
                    case "rettungsplattform" -> Items.create(inventory, Material.BLAZE_ROD, true, "§3Rettungsplattform", "Erschaffe eine Wollplattform unter dir!", 12, 28, 9);
                    case "rope" -> Items.create(inventory, Material.VINE, true, "§3Rope", "Baut eine vertikale Linie aus Wolle", 12, 25, 10);
                    case "schutzschild" -> Items.create(inventory, Material.EMERALD, true, "§3Schuztschild", "Beschützt dich vor allen Projektilen! Hält 4 Sekunden an!", 5, 10, 11);
                    case "slimeplattform" -> Items.create(inventory, Material.SLIME_BALL, true, "§3SlimePlattform", "Kataplutiert dich hoch in die Luft!", 15, 25, 12);
                    case "sprengsatz" -> Items.create(inventory, Material.STONE_PRESSURE_PLATE, true, "§3Sprengsatz", "Setze eine Mine als Tretmine oder nutze den Fernzünder!", 8, 6, 13);
                    case "tauscher" -> Items.create(inventory, Material.SNOWBALL, true, "§3Tauscher", "Tausche mit deinem Gegner den Platz", 8, 6, 14);
                    case "klospülung" -> Items.create(inventory, Material.GLASS_BOTTLE, true, "§3Klospülung", "Spült dich und deine Gegner weg!", 10, 16, 15);
                    case "wandgenerator" -> Items.create(inventory, Material.RED_STAINED_GLASS_PANE, true, "§3Wandgenerator", "Baut eine Wand aus Wolle vor dir", 8, 10, 16);
                    case "woolbombe" -> Items.create(inventory, Material.TNT, true, "§3WoolBombe", "Booste deine Gegner mit einem werfbaren TNT weg!", 8, 13, 17);
                    case "thegrabber" -> Items.create(inventory, Material.FISHING_ROD, true, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
                    default -> System.out.println("[NO_SECOND_PERK]: " + player.getName());
                }
            }
            case "§3Verfügbare Perks:" -> {
                if (Pperk == null) {
                    config.set(player.getName() + ".passive", "aufzug");
                    Pperk = "aufzug";
                }
                switch (Objects.requireNonNull(Pperk).toString()) {
                    case "aufzug" -> Items.createPassivePerk(inventory, Material.LADDER, true, "§3Aufzug", "Teleportiere dich mit der Enderperle auf den getroffenen Block!", null, "§6Cooldown: 3 Enderperlen", 0);
                    case "explodierender_pfeil" -> Items.createPassivePerk(inventory, Material.TNT, true, "§3Explodierender Pfeil", "Ein besonderer Pfeil der alles in die Luft sprengt!", "§6Preis: 8 Wolle", "Nach Pfeilen: 8", 1);
                    case "IDK" -> Items.createPassivePerk(inventory, Material.FISHING_ROD, true, "§3IDK", "IDK", "IDK", "IDK", 2);
                    case "pfeilregen" -> Items.createPassivePerk(inventory, Material.DISPENSER, true, "§3Pfeilregen", "Lässt einen Pfeilegen herunter prasseln", "§6Preis: 4 Wolle", "§cCooldown: 6", 3);
                    case "recharger" -> Items.createPassivePerk(inventory, Material.IRON_CHESTPLATE, true, "§3Recharger", "Verringert den Cooldown deiner Perks um 20%", "§6Preis: 20 Wolle", null, 4);
                    case "reflector" -> Items.createPassivePerk(inventory, Material.CACTUS, true, "§3Reflector", "Gibt das dir zugefügte Knockback zu 30% an deinen Gegner zurück!", "§6Preis: 15 Wolle", "§6Cooldown: 13", 5);
                    case "rocket_jump" -> Items.createPassivePerk(inventory, Material.RABBIT_FOOT, true, "§3Rocket Jump", "Lässt dich einen höheren Doppelsprung machen!", "§6Preis: 14 Wolle", null, 6);
                    case "portal" -> Items.createPassivePerk(inventory, Material.GOLD_INGOT, true, "§3IDK", "", "§6Preis: 8 Wolle", "§6Cooldown: 2", 7);
                    case "schock_pfeil" -> Items.create(inventory, Material.RED_STAINED_GLASS, true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
                    case "slowarrow" -> Items.createPassivePerk(inventory, Material.ARROW, true, "§3SlowArrow", "Verlangsame den Gegener!", "§6Preis: 2 Wolle", "§6Nach Pfeilen: 3", 9);
                    case "spinne" -> Items.createPassivePerk(inventory, Material.SPIDER_SPAWN_EGG, true, "§3Spinne", "Klettere wie eine Spinne an einer Wand hoch!", "§6Preis: 2 Wolle je Sekunde klettern", null, 10);
                    case "stomper" -> Items.createPassivePerk(inventory, Material.DIAMOND_BOOTS, true, "§3Stomper", "Katapultiert alle Gegner in deiner Nähe nach einem Doppelsprung weg!", "§6Preis: 10 Wolle", "§6Nach Sprüngen: 2", 11);
                    case "slimeplattform" -> Items.create(inventory, Material.GOLDEN_HOE, true, "§3SlimePlattform", 12);
                    default -> System.out.println("[NO_PASSIVE_PERK]: " + player.getName());
                }
            }
            default -> System.out.println("[ERROR] NO_INVENTORY_FOUND");
        }
        player.openInventory(inventory);
    }


    public static void particleitem(Player player) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        Object particels = config.get(player.getName() + ".particels");
        if (particels == null) {
            config.set(player.getName() + ".particels", "true");
            Items.create(player.getInventory(), Material.BLAZE_ROD, null, 5, "§ePartikel sind §aAN", 3);
            Woolbattle.getPlugin().saveConfig();
            return;
        }
        switch (particels.toString()) {
            case "true" -> Items.create(player.getInventory(), Material.BLAZE_ROD, null, 5, "§ePartikel sind §aAN", 3);
            case "false" -> Items.create(player.getInventory(), Material.BLAZE_ROD, null, 5, "§ePartikel sind §cAUS", 3);
            default -> {
                config.set(player.getName() + ".particels", "true");
                Items.create(player.getInventory(), Material.BLAZE_ROD, null, 5, "§ePartikel sind §aAN", 3);
            }
        }
        Woolbattle.getPlugin().saveConfig();
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

    private void addToTeam(ArrayList<Player> arrayList, Player player) {
        if (arrayList.contains(player)) return;
        ArrayList<Player> team = LiveSystem.VotedPlayers.get(player); //get the arrayList (team) where the player was before
        if (team != null)
            team.remove(player); //remove the player from the team

        if (arrayList.size() < LiveSystem.TeamSize) {
            arrayList.add(player);
            LiveSystem.VotedPlayers.put(player, arrayList);
            IngameState.teamupdate();
        } else Console.send(ChatColor.RED + "Team is full");
    }

    private void checkTeam(ArrayList<Player> arrayList, Inventory inventory, Material material, String Team, int slot, ChatColor color) {
        ArrayList<String> lore = new ArrayList<>();
        for (Player team : arrayList)
            lore.add(color + team.getName());
        Items.createTeam(inventory, material, Team, lore, slot);
    }

    private void clickedvotes(Player player, int lifes) {
        //TODO: Funktioniert nicht
        life6 = 1;
        Lifes(player);
    }
}