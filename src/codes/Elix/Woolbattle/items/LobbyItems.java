// This class was created by Elix on 27.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public class LobbyItems implements Listener {


    public static void Lobby(Player player) {
        Items.create(player, Material.BOW, null, 5, "§3Perks", 0);
        Items.create(player, Material.BOOK, null, 5, "§5Team wählen", 1);
        Items.create(player, Material.BLAZE_ROD, null, 5, "§ePartikel sind §aAN", 3);
        Items.create(player, Material.CHEST, null, 5, "§6Inventarsortierung", 5);
        Items.create(player, Material.NAME_TAG, null, 5, "§aLebensanzahl ändern", 7);
        Items.create(player, Material.PAPER, null, 5, "§6Maps", 8);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        switch (event.getItem().getType()) {
            case BOW -> PerkvorInventory(event.getPlayer());
            case BOOK -> TeamsInventory(event.getPlayer());
            case BLAZE_ROD -> PartikelAction();
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
        Items.create(inventory, Material.TRIPWIRE_HOOK, null, 5, "§3Booster", 0);
        Items.create(inventory, Material.FISHING_ROD, null, 5, "§3Enterhaken", 1);
        Items.create(inventory, Material.PACKED_ICE, null, 5, "§3Freezer", 2);
        Items.create(inventory, Material.WATCH, null, 5, "§3Großvaters Uhr", 3);
        Items.create(inventory, Material.STICK, null, 5, "§3Linebuilder", 4);
        Items.create(inventory, Material.DIAMOND_BARDING, null, 5, "§3Minigun", 5);
        Items.create(inventory, Material.FIREBALL, null, 5, "§3Pfeilbombe", 6);
        Items.create(inventory, Material.OBSIDIAN, null, 5, "§3Portal", 7);
        Items.create(inventory, Material.STAINED_GLASS, 14, null, 5, "§3Rettungskapsel",8);
        Items.create(inventory, Material.BLAZE_ROD, null, 5, "§3Rettungsplattform", 9);
        Items.create(inventory, Material.VINE, null, 5, "§3Rope", 10);
        Items.create(inventory, Material.EMERALD, null, 5, "§3Schuztschild", 11);
        Items.create(inventory, Material.SLIME_BALL, null, 5, "§3SlimePlattform", 12);
        Items.create(inventory, Material.STONE_PLATE, null, 5, "§3Sprengsatz", 13);
        Items.create(inventory, Material.SNOW_BALL, null, 5, "§3Tauscher", 14);
        Items.create(inventory, Material.GLASS_BOTTLE, null, 5, "§3Klospülung", 15);
        Items.create(inventory, Material.STAINED_GLASS_PANE, 14, null, 5, "§3Wandgenerator", 16);
        Items.create(inventory, Material.TNT, null, 5, "§3WoolBombe", 17);
        Items.create(inventory, Material.FISHING_ROD, null, 5, "§3The Grabber", 18);
        Items.create(inventory, Material.SPRUCE_DOOR, null, 5, "§3Back", 34);
        checkSeclectet(player, inventory);
        //player.openInventory(inventory);
    }

    private void PassivePerkInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*4, "§3Verfügbare Perks:");
        Items.create(inventory, Material.LADDER, null, 5, "§3Aufzug", 0);
        Items.create(inventory, Material.TNT, null, 5, "§3Explodierender Pfeil", 1);
        Items.create(inventory, Material.FISHING_ROD, null, 5, "§3IDK", 2);
        Items.create(inventory, Material.DISPENSER, null, 5, "§3Pfeilregen", 3);
        Items.create(inventory, Material.IRON_CHESTPLATE, null, 5, "§3Recharger", 4);
        Items.create(inventory, Material.CACTUS, null, 5, "§3Reflector", 5);
        Items.create(inventory, Material.RABBIT_FOOT, null, 5, "§3Rocket Jump", 6);
        Items.create(inventory, Material.GOLD_INGOT, null, 5, "§3Portal", 7);
        Items.create(inventory, Material.BLAZE_POWDER, null, 5, "§3Schock Pfeil", 8);
        Items.create(inventory, Material.ARROW, null, 5, "§3SlowArrow", 9);
        Items.create(inventory, Material.MONSTER_EGGS, 52, null, 5, "§3Spinne", 10); //TODO Falscher Byte Code
        Items.create(inventory, Material.DIAMOND_BOOTS, null, 5, "§3Stomper", 11);
        Items.create(inventory, Material.GOLD_HOE, null, 5, "§3SlimePlattform", 12);
        Items.create(inventory, Material.SPRUCE_DOOR, null, 5, "§3Back", 34);
        player.openInventory(inventory);
    }

    private void TeamsInventory(Player player) {

    }

    private void PartikelAction() {

    }

    private void Inventarsortierung() {

    }

    private void Lifes() {

    }

    private void Maps() {

    }
    //TODO Fehler wenn ausherhalb des InvGUI´s geklickt wird
    @EventHandler
    public void onBOwGUIClick(InventoryClickEvent event) throws IOException {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
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
            FileConfiguration config = Woolbattle.getPlugin().getConfig();
            switch (event.getCurrentItem().getType()) {
                case TRIPWIRE_HOOK -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "booster");
                    System.out.println("booster safed");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case FISHING_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "enterhaken");
                    System.out.println("Enterhaken safed");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case PACKED_ICE -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "freezer");
                    System.out.println("Freezer safed");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case WATCH -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "großvatersuhr");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case STICK -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "linebuilder");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case DIAMOND_BARDING -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "minigun");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case FIREBALL -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "pfeilbombe");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case OBSIDIAN -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "portal");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case STAINED_GLASS -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "rettungskapsel");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case BLAZE_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "rettungsplattform");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case VINE -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "rope");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case EMERALD -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "schutzschild");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case SLIME_BALL -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "slimeplattform");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case STONE_PLATE -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "sprengsatz");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case SNOW_BALL -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "tauscher");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case GLASS_BOTTLE -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "klospülung");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case STAINED_GLASS_PANE -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "wandgenerator");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                case TNT -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "woolbombe");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player, "§3Erstes Perk:");
                }
                // THE GRABBER

            }

        }
        if (event.getClickedInventory().getTitle().equals("§3Zweites Perk:")) {
            event.setCancelled(true);
            FileConfiguration config = Woolbattle.getPlugin().getConfig();
            switch (event.getCurrentItem().getType()) {
                case TRIPWIRE_HOOK -> {
                    config.set(event.getWhoClicked().getName() + ".2erk", "booster");
                    System.out.println("booster safed");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case FISHING_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "enterhaken");
                    System.out.println("Enterhaken safed");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case PACKED_ICE -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "freezer");
                    System.out.println("Freezer safed");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case WATCH -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "großvatersuhr");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case STICK -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "linebuilder");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case DIAMOND_BARDING -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "minigun");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case FIREBALL -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "pfeilbombe");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case OBSIDIAN -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "portal");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case STAINED_GLASS -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "rettungskapsel");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case BLAZE_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "rettungsplattform");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case VINE -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "rope");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case EMERALD -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "schutzschild");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case SLIME_BALL -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "slimeplattform");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case STONE_PLATE -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "sprengsatz");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case SNOW_BALL -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "tauscher");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case GLASS_BOTTLE -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "klospülung");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case STAINED_GLASS_PANE -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "wandgenerator");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                case TNT -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "woolbombe");
                    Woolbattle.getPlugin().saveConfig();
                    PerkInventory(player,"§3Zweites Perk:");
                }
                // THE GRABBER

            }
        }

        if (event.getClickedInventory().getTitle().equals("§3Verfügbare Perks:")) {
            event.setCancelled(true);
            FileConfiguration config = Woolbattle.getPlugin().getConfig();
            switch (event.getCurrentItem().getType()) {
                case LADDER -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "aufzug");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case TNT -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "explodierender_pfeil");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case FISHING_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "IDK");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case DISPENSER -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "pfeilregen");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case IRON_CHESTPLATE -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "recharger");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case CACTUS -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "reflector");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case RABBIT_FOOT -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "rocket_jump");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case GOLD_INGOT -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "portal");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case BLAZE_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "schock:_pfeil");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case ARROW -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "slowarrow");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case MONSTER_EGG -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "spinne");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case DIAMOND_BOOTS -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "stomper");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }
                case GOLD_HOE -> {
                    config.set(event.getWhoClicked().getName() + ".passive", "slimeplattform");
                    Woolbattle.getPlugin().saveConfig();
                    PassivePerkInventory(player);
                }

            }
        }
    }

    public void checkSeclectet(Player player, Inventory inventory) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");
        if (inventory.getTitle().equals("§3Erstes Perk:")) {
            if ("booster".equals(Fperk)) {
                System.out.println("Booster selectet");
                Items.create(inventory, Material.TRIPWIRE_HOOK, Enchantment.KNOCKBACK, 5, "§3Booster", 0);
                player.openInventory(inventory);
            } else if ("enterhaken".equals(Fperk)) {
                Items.create(inventory, Material.FISHING_ROD, Enchantment.KNOCKBACK, 5, "§3Enterhaken", 1);
                player.openInventory(inventory);
            } else if ("freezer".equals(Fperk)) {
                Items.create(inventory, Material.PACKED_ICE, Enchantment.KNOCKBACK, 5, "§3Freezer", 2);
                player.openInventory(inventory);
            } else if ("großvatersuhr".equals(Fperk)) {
                Items.create(inventory, Material.WATCH, Enchantment.KNOCKBACK, 5, "§3Großvaters Uhr", 3);
                player.openInventory(inventory);
            } else if ("linebuilder".equals(Fperk)) {
                Items.create(inventory, Material.STICK, Enchantment.KNOCKBACK, 5, "§3Linebuilder", 4);
                player.openInventory(inventory);
            } else if ("minigun".equals(Fperk)) {
                Items.create(inventory, Material.DIAMOND_BARDING, Enchantment.KNOCKBACK, 5, "§3Minigun", 5);
                player.openInventory(inventory);
            } else if ("pfeilbombe".equals(Fperk)) {
                Items.create(inventory, Material.FIREBALL, Enchantment.KNOCKBACK, 5, "§3Pfeilbombe", 6);
                player.openInventory(inventory);
            } else if ("portal".equals(Fperk)) {
                Items.create(inventory, Material.OBSIDIAN, Enchantment.KNOCKBACK, 5, "§3Portal", 7);
                player.openInventory(inventory);
            } else if ("rettungskapsel".equals(Fperk)) {
                Items.create(inventory, Material.STAINED_GLASS, 14, Enchantment.KNOCKBACK, 5, "§3Rettungskapsel", 8);
                player.openInventory(inventory);
            } else if ("rettungsplattform".equals(Fperk)) {
                Items.create(inventory, Material.BLAZE_ROD, Enchantment.KNOCKBACK, 5, "§3Rettungsplattform", 9);
                player.openInventory(inventory);
            } else if ("rope".equals(Fperk)) {
                Items.create(inventory, Material.VINE, Enchantment.KNOCKBACK, 5, "§3Rope", 10);
                player.openInventory(inventory);
            } else if ("schutzschild".equals(Fperk)) {
                Items.create(inventory, Material.EMERALD, Enchantment.KNOCKBACK, 5, "§3Schuztschild", 11);
                player.openInventory(inventory);
            } else if ("slimeplattform".equals(Fperk)) {
                Items.create(inventory, Material.SLIME_BALL, Enchantment.KNOCKBACK, 5, "§3SlimePlattform", 12);
                player.openInventory(inventory);
            } else if ("sprengsatz".equals(Fperk)) {
                Items.create(inventory, Material.STONE_PLATE, Enchantment.KNOCKBACK, 5, "§3Sprengsatz", 13);
                player.openInventory(inventory);
            } else if ("tauscher".equals(Fperk)) {
                Items.create(inventory, Material.SNOW_BALL, Enchantment.KNOCKBACK, 5, "§3Tauscher", 14);
                player.openInventory(inventory);
            } else if ("klospülung".equals(Fperk)) {
                Items.create(inventory, Material.GLASS_BOTTLE, Enchantment.KNOCKBACK, 5, "§3Klospülung", 15);
                player.openInventory(inventory);
            } else if ("wandgenerator".equals(Fperk)) {
                Items.create(inventory, Material.STAINED_GLASS_PANE, 14, Enchantment.KNOCKBACK, 5, "§3Wandgenerator", 16);
                player.openInventory(inventory);
            } else if ("woolbombe".equals(Fperk)) {
                Items.create(inventory, Material.TNT, Enchantment.KNOCKBACK, 5, "§3WoolBombe", 17);
                player.openInventory(inventory);
            } else if ("thegrabber".equals(Fperk)) {
                Items.create(inventory, Material.FISHING_ROD, Enchantment.KNOCKBACK, 5, "§3The Grabber", 18);
                player.openInventory(inventory);
            } else {
                player.openInventory(inventory);
                System.out.println("[NO_PERK]: " + player.getName());
            }



        } else if (inventory.getTitle().equals("§3Zweites Perk:")) {
            if ("booster".equals(Sperk)) {
                System.out.println("Booster 2perk selectet");
                Items.create(inventory, Material.TRIPWIRE_HOOK, Enchantment.KNOCKBACK, 5, "§3Booster", 0);
                player.openInventory(inventory);
            } else if ("enterhaken".equals(Sperk)) {
                Items.create(inventory, Material.FISHING_ROD, Enchantment.KNOCKBACK, 5, "§3Enterhaken", 1);
                player.openInventory(inventory);
            } else if ("freezer".equals(Sperk)) {
                Items.create(inventory, Material.PACKED_ICE, Enchantment.KNOCKBACK, 5, "§3Freezer", 2);
                player.openInventory(inventory);
            } else if ("großvatersuhr".equals(Sperk)) {
                Items.create(inventory, Material.WATCH, Enchantment.KNOCKBACK, 5, "§3Großvaters Uhr", 3);
                player.openInventory(inventory);
            } else if ("linebuilder".equals(Sperk)) {
                Items.create(inventory, Material.STICK, Enchantment.KNOCKBACK, 5, "§3Linebuilder", 4);
                player.openInventory(inventory);
            } else if ("minigun".equals(Sperk)) {
                Items.create(inventory, Material.DIAMOND_BARDING, Enchantment.KNOCKBACK, 5, "§3Minigun", 5);
                player.openInventory(inventory);
            } else if ("pfeilbombe".equals(Sperk)) {
                Items.create(inventory, Material.FIREBALL, Enchantment.KNOCKBACK, 5, "§3Pfeilbombe", 6);
                player.openInventory(inventory);
            } else if ("portal".equals(Sperk)) {
                Items.create(inventory, Material.OBSIDIAN, Enchantment.KNOCKBACK, 5, "§3Portal", 7);
                player.openInventory(inventory);
            } else if ("rettungskapsel".equals(Sperk)) {
                Items.create(inventory, Material.STAINED_GLASS, 14, Enchantment.KNOCKBACK, 5, "§3Rettungskapsel", 8);
                player.openInventory(inventory);
            } else if ("rettungsplattform".equals(Sperk)) {
                Items.create(inventory, Material.BLAZE_ROD, Enchantment.KNOCKBACK, 5, "§3Rettungsplattform", 9);
                player.openInventory(inventory);
            } else if ("rope".equals(Sperk)) {
                Items.create(inventory, Material.VINE, Enchantment.KNOCKBACK, 5, "§3Rope", 10);
                player.openInventory(inventory);
            } else if ("schutzschild".equals(Sperk)) {
                Items.create(inventory, Material.EMERALD, Enchantment.KNOCKBACK, 5, "§3Schuztschild", 11);
                player.openInventory(inventory);
            } else if ("slimeplattform".equals(Sperk)) {
                Items.create(inventory, Material.SLIME_BALL, Enchantment.KNOCKBACK, 5, "§3SlimePlattform", 12);
                player.openInventory(inventory);
            } else if ("sprengsatz".equals(Sperk)) {
                Items.create(inventory, Material.STONE_PLATE, Enchantment.KNOCKBACK, 5, "§3Sprengsatz", 13);
                player.openInventory(inventory);
            } else if ("tauscher".equals(Sperk)) {
                Items.create(inventory, Material.SNOW_BALL, Enchantment.KNOCKBACK, 5, "§3Tauscher", 14);
                player.openInventory(inventory);
            } else if ("klospülung".equals(Sperk)) {
                Items.create(inventory, Material.GLASS_BOTTLE, Enchantment.KNOCKBACK, 5, "§3Klospülung", 15);
                player.openInventory(inventory);
            } else if ("wandgenerator".equals(Sperk)) {
                Items.create(inventory, Material.STAINED_GLASS_PANE, 14, Enchantment.KNOCKBACK, 5, "§3Wandgenerator", 16);
                player.openInventory(inventory);
            } else if ("woolbombe".equals(Sperk)) {
                Items.create(inventory, Material.TNT, Enchantment.KNOCKBACK, 5, "§3WoolBombe", 17);
                player.openInventory(inventory);
            } else if ("thegrabber".equals(Sperk)) {
                Items.create(inventory, Material.FISHING_ROD, Enchantment.KNOCKBACK, 5, "§3The Grabber", 18);
                player.openInventory(inventory);
            } else {
                player.openInventory(inventory);
                System.out.println("[NO_PERK]: " + player.getName());
            }
        } else
            System.out.println("NO_INVENTORY FOUND");
    }
}