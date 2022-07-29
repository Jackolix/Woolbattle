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
            FileConfiguration config = Woolbattle.getPlugin().getCustomConfig();
            switch (event.getCurrentItem().getType()) {
                case TRIPWIRE_HOOK -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "booster");
                    System.out.println("booster safed");
                    Woolbattle.getPlugin().saveConfig();
                }
                case FISHING_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "Enterhaken");
                    System.out.println("Enterhaken safed");
                    Woolbattle.getPlugin().saveConfig();
                }
                case PACKED_ICE -> {
                    config.set(event.getWhoClicked().getName() + ".1Perk", "Freezer");
                    System.out.println("Freezer safed");
                    Woolbattle.getPlugin().saveConfig();
                }

            }
        }
        if (event.getClickedInventory().getTitle().equals("§3Zweites Perk:")) {
            event.setCancelled(true);
            FileConfiguration config = Woolbattle.getPlugin().getCustomConfig();
            switch (event.getCurrentItem().getType()) {
                case TRIPWIRE_HOOK -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "booster");
                    System.out.println("booster as 2Perk safed");
                    Woolbattle.getPlugin().saveConfig();
                }
                case FISHING_ROD -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "Enterhaken");
                    System.out.println("Enterhaken as 2Perk safed");
                    Woolbattle.getPlugin().saveConfig();
                }
                case PACKED_ICE -> {
                    config.set(event.getWhoClicked().getName() + ".2Perk", "Freezer");
                    System.out.println("Freezer as 2Perk safed");
                    Woolbattle.getPlugin().saveConfig();
                }

            }
        }
    }

    public void checkSeclectet(Player player, Inventory inventory) {
        FileConfiguration config = Woolbattle.getPlugin().getCustomConfig();
        Object o = config.get(player.getName() + ".1Perk");
        if ("booster".equals(o)) {
            Items.create(player.getInventory(), Material.TRIPWIRE_HOOK, Enchantment.KNOCKBACK, 5, "§3Booster", 0);
            player.openInventory(inventory);
        }

    }
}