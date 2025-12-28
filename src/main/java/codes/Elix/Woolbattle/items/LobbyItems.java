// This class was created by Elix on 27.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.gui.PerkConfigGUI;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.SchematicManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LobbyItems implements Listener {
    private HashMap<Player, String> Title = new HashMap<>();
    private static Set<Player> playersViewingMapVoting = new HashSet<>();

    public static void Lobby(Player player) {
        Items.create(player.getInventory(), Material.BOW, "§3Perks", 0);
        Items.create(player.getInventory(), Material.BOOK, "§5Team wählen", 1);
        particleitem(player);
        Items.create(player.getInventory(), Material.CHEST, "§6Inventarsortierung", 5);
        // Add perk config item for OP players
        if (player.hasPermission("woolbattle.admin.config") || player.isOp()) {
            Items.create(player.getInventory(), Material.COMMAND_BLOCK, "§dPerk Configuration", 6);
        }
        Items.create(player.getInventory(), Material.NAME_TAG, "§aLebensanzahl ändern", 7);
        Items.create(player.getInventory(), Material.PAPER,"§6Maps", 8);
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
            case COMMAND_BLOCK -> {
                if (event.getPlayer().hasPermission("woolbattle.admin.config") || event.getPlayer().isOp()) {
                    PerkConfigGUI.openMainConfigGUI(event.getPlayer());
                }
            }
            case NAME_TAG -> Lifes(event.getPlayer());
            case PAPER -> Maps(event.getPlayer());
        }
    }

    private void PerkVorInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, Component.text("Wähle deine Perks!", NamedTextColor.DARK_AQUA));
        Items.create(inventory, Material.CHEST,"§6Aktiv-Perk #1", 3);
        Items.create(inventory, Material.TRAPPED_CHEST,"§6Aktiv-Perk #2", 4);
        Items.create(inventory, Material.ENDER_CHEST,"§3Passiv-Perk", 5);
        player.openInventory(inventory);
    }

    private void PerkInventory(Player player, String title) {
        Title.put(player, title);
        Inventory inventory = Bukkit.createInventory(null, 9*4, Items.convertLegacyText(title));
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
        Items.create(inventory, Material.CARROT_ON_A_STICK, false, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
        Items.create(inventory, Material.DARK_OAK_DOOR, false, "§3Back", 35);
        checkSelectet(player, inventory);
    }

    private void PassivePerkInventory(Player player) {
        Title.put(player,"§3Verfügbare Perks:");
        Inventory inventory = Bukkit.createInventory(null, 9*4, Component.text("Verfügbare Perks:", NamedTextColor.DARK_AQUA));
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
        Items.create(inventory, Material.GOLDEN_HOE,"§3SlimePlattform", 12);
        Items.create(inventory, Material.DARK_OAK_DOOR, false, "§3Back", 35);
        checkSelectet(player, inventory);
    }

    private void TeamsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, Component.text("Wähle dein Team!", NamedTextColor.AQUA));
        if (LiveSystem.Teams == 2) {
            checkTeam(LiveSystem.Team.get("red"), inventory, Material.RED_DYE, "§cTeam Rot", 3, "§c");
            checkTeam(LiveSystem.Team.get("blue"), inventory, Material.BLUE_DYE, "§bTeam Blau", 5, "§b");
        } else {
            checkTeam(LiveSystem.Team.get("red"), inventory, Material.RED_DYE, "§cTeam Rot", 1, "§c");
            checkTeam(LiveSystem.Team.get("blue"), inventory, Material.BLUE_DYE, "§bTeam Blau", 3, "§b");
            checkTeam(LiveSystem.Team.get("yellow"), inventory, Material.YELLOW_DYE, "§eTeam Gelb", 5, "§e");
            checkTeam(LiveSystem.Team.get("green"), inventory, Material.GREEN_DYE, "§aTeam Grün", 7, "§a");
        }
        player.openInventory(inventory);
    }


    private void Inventarsortierung() {}

    private void Lifes(Player player) {
        Title.put(player,"§aLebensanzahl");
        Inventory inventory = Bukkit.createInventory(null, 3*9, Component.text("Lebensanzahl", NamedTextColor.GREEN));
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
        Items.createMap(inventory, Material.MAGENTA_DYE, 6, "§e6 Leben", "§8» §7Votes: §5" + Voting.six.size(), 10);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 11);
        Items.createMap(inventory, Material.MAGENTA_DYE, 12, "§e12 Leben", "§8» §7Votes: §5" + Voting.twelve.size(), 12);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 13);
        Items.create(inventory, Material.MAGENTA_DYE, 18, "§e18 Leben", "§8» §7Votes: §5" + Voting.eighteen.size(), 14);
        Items.create(inventory, Material.GRAY_STAINED_GLASS_PANE, " ", 15);
        Items.createMap(inventory, Material.MAGENTA_DYE, 23, "§e23 Leben", "§8» §7Votes: §5" + Voting.twentythree.size(), 16);
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
        checkSelectet(player, inventory);
    }

    private void Maps(Player player) {
        // Add player to tracking set
        playersViewingMapVoting.add(player);

        List<SchematicManager.SchematicInfo> schematics = SchematicManager.getGameSchematics();
        Console.send("Loading maps menu for " + player.getName() + " - found " + schematics.size() + " game schematics (lobby maps excluded)");

        int inventorySize = Math.max(9, ((schematics.size() + 8) / 9) * 9);
        if (inventorySize > 54) inventorySize = 54;

        Inventory inventory = Bukkit.createInventory(null, inventorySize, Component.text("Game Maps (" + schematics.size() + " available)", NamedTextColor.GREEN));
        
        String currentWinningMap = MapVoting.getCurrentWinningMap();
        String playerVote = MapVoting.getPlayerVote(player);
        
        int slot = 0;
        for (SchematicManager.SchematicInfo schematic : schematics) {
            if (slot >= inventorySize) break;
            
            Console.send("Adding schematic to slot " + slot + ": " + schematic.getDisplayName());
            
            // Add vote count to display name using Adventure components
            int voteCount = MapVoting.getVotesForMap(schematic.getFileName());
            Component displayName = Component.text(schematic.getDisplayName(), NamedTextColor.GOLD);
            if (voteCount > 0) {
                displayName = displayName.append(Component.text(" (", NamedTextColor.DARK_GRAY))
                    .append(Component.text(voteCount + " votes", NamedTextColor.YELLOW))
                    .append(Component.text(")", NamedTextColor.DARK_GRAY));
            }
            
            // Determine material and display name color based on state
            Material itemMaterial = schematic.getMaterial();
            NamedTextColor nameColor = NamedTextColor.GOLD;
            
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text("File: ", NamedTextColor.GRAY).append(Component.text(schematic.getFileName(), NamedTextColor.YELLOW)));
            lore.add(Component.text("Votes: ", NamedTextColor.GRAY).append(Component.text(voteCount, NamedTextColor.YELLOW)));
            
            if (schematic.getFileName().equals(currentWinningMap)) {
                // Winning map - use gold block and bright color
                itemMaterial = Material.GOLD_BLOCK;
                nameColor = NamedTextColor.YELLOW;
                lore.add(Component.text("⭐ CURRENTLY WINNING ⭐", NamedTextColor.YELLOW));
            }
            
            if (schematic.getFileName().equals(playerVote)) {
                // Player's vote - use emerald block and green color
                itemMaterial = Material.EMERALD_BLOCK;
                nameColor = NamedTextColor.GREEN;
                lore.add(Component.text("✓ YOUR VOTE", NamedTextColor.GREEN));
            }
            
            // If it's both winning and player's vote, prioritize player's vote appearance
            if (schematic.getFileName().equals(playerVote) && schematic.getFileName().equals(currentWinningMap)) {
                itemMaterial = Material.EMERALD_BLOCK;
                nameColor = NamedTextColor.GREEN;
                lore.clear();
                lore.add(Component.text("File: ", NamedTextColor.GRAY).append(Component.text(schematic.getFileName(), NamedTextColor.YELLOW)));
                lore.add(Component.text("Votes: ", NamedTextColor.GRAY).append(Component.text(voteCount, NamedTextColor.YELLOW)));
                lore.add(Component.text("✓ YOUR VOTE & WINNING! ⭐", NamedTextColor.GOLD));
            }
            
            lore.add(Component.text("Click to vote for this map!", NamedTextColor.GRAY));
            
            // Update display name color
            displayName = Component.text(schematic.getDisplayName(), nameColor);
            if (voteCount > 0) {
                displayName = displayName.append(Component.text(" (", NamedTextColor.DARK_GRAY))
                    .append(Component.text(voteCount + " votes", NamedTextColor.YELLOW))
                    .append(Component.text(")", NamedTextColor.DARK_GRAY));
            }
            
            // Create item
            ItemStack item = new ItemStack(itemMaterial);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.displayName(displayName);
            itemMeta.lore(lore);
            item.setItemMeta(itemMeta);
            inventory.setItem(slot, item);
            slot++;
        }
        
        if (schematics.isEmpty()) {
            Console.send("No schematics found - adding barrier item");
            Items.create(inventory, Material.BARRIER, "§cNo maps available", 4);
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            // Remove player from map voting tracking when they close any inventory
            playersViewingMapVoting.remove(player);
        }
    }

    @EventHandler()
    public void onGUIClick(InventoryClickEvent event) {
        System.out.println("[WOOLBATTLE DEBUG] InventoryClickEvent triggered!");
        if (!(event.getWhoClicked() instanceof Player player)) {
            System.out.println("[WOOLBATTLE DEBUG] Not a player click");
            return;
        }
        if (event.getCurrentItem() == null) {
            System.out.println("[WOOLBATTLE DEBUG] No item clicked");
            return;
        }
        System.out.println("[WOOLBATTLE DEBUG] Player: " + player.getName() + " clicked item: " + event.getCurrentItem().getType());


        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        Material clicked = event.getCurrentItem().getType();

        /*
        String FirstPerk = (String) config.get(player.getName() + ".1Perk");
        String SecondPerk = (String) config.get(player.getName() + ".2Perk");
        String PassivePerk = (String) config.get(player.getName() + ".passive");
         */
        Perk perk = PerkHelper.getPerks(player);
        String FirstPerk = perk.getfirstPerk();
        String SecondPerk = perk.getsecondPerk();
        String PassivePerk = perk.getpassivePerk();


        if (event.getView().title().equals(Component.text("Wähle deine Perks!", NamedTextColor.DARK_AQUA))) {
            event.setCancelled(true); //Player kann das Item nicht aus dem Inventar ziehen
            switch (clicked) {
                case CHEST -> PerkInventory(player, "§3Erstes Perk:");
                case TRAPPED_CHEST -> PerkInventory(player, "§3Zweites Perk:");
                case ENDER_CHEST -> PassivePerkInventory(player);
            }
        }

        if (event.getView().title().equals(Items.convertLegacyText("§3Erstes Perk:"))) {
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
                case CARROT_ON_A_STICK -> PerkItems.select(player, "grabber", ".1Perk");
                case DARK_OAK_DOOR -> {
                    PerkVorInventory(player);
                    return;
                }
            }
            // Woolbattle.getPlugin().saveConfig();
            PerkInventory(player, "§3Erstes Perk:");
        }

        if (event.getView().title().equals(Items.convertLegacyText("§3Zweites Perk:"))) {
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
                case CARROT_ON_A_STICK -> PerkItems.select(player, "grabber", ".2Perk");
                case DARK_OAK_DOOR -> {
                    PerkVorInventory(player);
                    return;
                }
            }
            // Woolbattle.getPlugin().saveConfig();
            PerkInventory(player, "§3Zweites Perk:");
        }

        if (event.getView().title().equals(Component.text("Verfügbare Perks:", NamedTextColor.DARK_AQUA))) {
            event.setCancelled(true);
            String name = event.getWhoClicked().getName();
            /*
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
             */
            switch (clicked) {
                case LADDER -> PerkItems.selectPassive(player, "aufzug");
                case TNT -> PerkItems.selectPassive(player, "exploding_arrow");
                case FISHING_ROD -> PerkItems.selectPassive(player, "IDK");
                case DISPENSER -> PerkItems.selectPassive(player, "arrowrain");
                case IRON_CHESTPLATE -> PerkItems.selectPassive(player, "recharger");
                case CACTUS -> PerkItems.selectPassive(player, "reflector");
                case RABBIT_FOOT -> PerkItems.selectPassive(player, "rocket_jump");
                case GOLD_INGOT -> PerkItems.selectPassive(player, "portal");
                case BLAZE_ROD -> PerkItems.selectPassive(player, "shockarrow");
                case ARROW -> PerkItems.selectPassive(player, "slowarrow");
                case SPIDER_SPAWN_EGG -> PerkItems.selectPassive(player, "spider");
                case DIAMOND_BOOTS -> PerkItems.selectPassive(player, "stomper");
                case GOLDEN_HOE -> PerkItems.selectPassive(player, "slimeplattform");
                case DARK_OAK_DOOR -> {
                    PerkVorInventory(player);
                    return;
                }
            }
            PassivePerkInventory(player);
        }

        if (event.getView().title().equals(Component.text("Wähle dein Team!", NamedTextColor.AQUA))) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§cTeam Rot" -> addToTeam(LiveSystem.Team.get("red"), player);
                case "§bTeam Blau" -> addToTeam(LiveSystem.Team.get("blue"), player);
                case "§eTeam Gelb" -> addToTeam(LiveSystem.Team.get("yellow"), player);
                case "§aTeam Grün" -> addToTeam(LiveSystem.Team.get("green"), player);
            }
            TeamsInventory(player);
        }

        if (event.getView().title().equals(Component.text("Lebensanzahl", NamedTextColor.GREEN))) {
            event.setCancelled(true);
            System.out.println(event.getCurrentItem().getItemMeta().displayName());
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§e6 Leben" -> Voting.vote(player, 6);
                case "§e12 Leben" -> Voting.vote(player, 12);
                case "§e18 Leben" -> Voting.vote(player, 18);
                case "§e23 Leben" -> Voting.vote(player,23);
            }
            Lifes(player);
        }

        // Use modern Paper Adventure API for title detection
        Component titleComponent = event.getView().title();
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(titleComponent);
        
        // Handle Maps voting - check if title contains Maps
        if (title.contains("Maps")) {
            event.setCancelled(true);
            System.out.println("[DEBUG] Maps inventory detected!");
            Console.send("Maps inventory clicked by " + player.getName());
            if (event.getCurrentItem().getType() != Material.AIR) {
                System.out.println("[DEBUG] Item type: " + event.getCurrentItem().getType());
                Console.send("Item clicked: " + event.getCurrentItem().getType());
                handleMapSelection(player, event.getCurrentItem());
            }
            return; // Exit early for maps
        }

        // Handle Perk Configuration GUIs - don't cancel these events
        if (title.contains("Perk Configuration") || title.contains("Configure ")) {
            // Let PerkConfigGUI handle these events
            System.out.println("[LOBBY ITEMS] Perk Config GUI detected, letting PerkConfigGUI handle: " + title);
            return;
        }

        if (GameStateManager.getCurrentGameState() instanceof LobbyState)
            event.setCancelled(true);
        if (Items.interact.contains(player))
            event.setCancelled(true);
        if (event.getCurrentItem().getType() == Material.LEATHER_BOOTS)
            event.setCancelled(true);
        if (event.getCurrentItem().hasItemFlag(ItemFlag.HIDE_DESTROYS))
            event.setCancelled(true);
    }

    public void checkSelectet(Player player, Inventory inventory) {
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        /*
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");
        Object Pperk = config.get(player.getName() + ".passive");
         */

        Integer number = Voting.voted.get(player);
        Perk perk = PerkHelper.getPerks(player);

        String Fperk = perk.getfirstPerk();
        String Sperk = perk.getsecondPerk();
        String Pperk = perk.getpassivePerk();


        switch (Title.get(player)) {
            case "§3Erstes Perk:" -> {
                if (Fperk == null) {
                    perk.setFirstPerk("booster");
                    // config.set(player.getName() + ".1Perk", "booster");
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
                    case "grabber" -> Items.create(inventory, Material.CARROT_ON_A_STICK, true, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
                    default -> Console.send("[NO_FIRST_PERK]: " + player.getName());
                }
            }
            case "§3Zweites Perk:" -> {
                if (Sperk == null) {
                    perk.setSecondPerk("enterhaken");
                    // config.set(player.getName() + ".2Perk", "enterhaken");
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
                    case "grabber" -> Items.create(inventory, Material.CARROT_ON_A_STICK, true, "§3The Grabber", "Ziehe Gegner zu dir!", 8, 5, 18);
                    default -> Console.send("[NO_SECOND_PERK]: " + player.getName());
                }
            }
            case "§3Verfügbare Perks:" -> {
                if (Pperk == null) {
                    if (!Woolbattle.useDB) {
                        config.set(player.getName() + ".passive", "aufzug");
                    } else
                        perk.setPassivePerk("aufzug");
                    Pperk = "aufzug";
                }
                switch (Objects.requireNonNull(Pperk).toString()) {
                    case "aufzug" -> Items.createPassivePerk(inventory, Material.LADDER, true, "§3Aufzug", "Teleportiere dich mit der Enderperle auf den getroffenen Block!", null, "§6Cooldown: 3 Enderperlen", 0);
                    case "exploding_arrow" -> Items.createPassivePerk(inventory, Material.TNT, true, "§3Explodierender Pfeil", "Ein besonderer Pfeil der alles in die Luft sprengt!", "§6Preis: 8 Wolle", "Nach Pfeilen: 8", 1);
                    case "IDK" -> Items.createPassivePerk(inventory, Material.FISHING_ROD, true, "§3IDK", "IDK", "IDK", "IDK", 2);
                    case "arrowrain" -> Items.createPassivePerk(inventory, Material.DISPENSER, true, "§3Pfeilregen", "Lässt einen Pfeilegen herunter prasseln", "§6Preis: 4 Wolle", "§cCooldown: 6", 3);
                    case "recharger" -> Items.createPassivePerk(inventory, Material.IRON_CHESTPLATE, true, "§3Recharger", "Verringert den Cooldown deiner Perks um 20%", "§6Preis: 20 Wolle", null, 4);
                    case "reflector" -> Items.createPassivePerk(inventory, Material.CACTUS, true, "§3Reflector", "Gibt das dir zugefügte Knockback zu 30% an deinen Gegner zurück!", "§6Preis: 15 Wolle", "§6Cooldown: 13", 5);
                    case "rocket_jump" -> Items.createPassivePerk(inventory, Material.RABBIT_FOOT, true, "§3Rocket Jump", "Lässt dich einen höheren Doppelsprung machen!", "§6Preis: 14 Wolle", null, 6);
                    case "portal" -> Items.createPassivePerk(inventory, Material.GOLD_INGOT, true, "§3IDK", "", "§6Preis: 8 Wolle", "§6Cooldown: 2", 7);
                    case "shockarrow" -> Items.create(inventory, Material.RED_STAINED_GLASS, true, "§3Rettungskapsel", "Umhüllt dich mit Blöcken", 15, 30, 8);
                    case "slowarrow" -> Items.createPassivePerk(inventory, Material.ARROW, true, "§3SlowArrow", "Verlangsame den Gegener!", "§6Preis: 2 Wolle", "§6Nach Pfeilen: 3", 9);
                    case "spider" -> Items.createPassivePerk(inventory, Material.SPIDER_SPAWN_EGG, true, "§3Spinne", "Klettere wie eine Spinne an einer Wand hoch!", "§6Preis: 2 Wolle je Sekunde klettern", null, 10);
                    case "stomper" -> Items.createPassivePerk(inventory, Material.DIAMOND_BOOTS, true, "§3Stomper", "Katapultiert alle Gegner in deiner Nähe nach einem Doppelsprung weg!", "§6Preis: 10 Wolle", "§6Nach Sprüngen: 2", 11);
                    case "slimeplattform" -> Items.create(inventory, Material.GOLDEN_HOE, true, "§3SlimePlattform", 12);
                    default -> Console.send("[NO_PASSIVE_PERK]: " + player.getName());
                }
            }
            case "§aLebensanzahl" -> {
                if (number == null)
                    break;
                switch (number) {
                    case 6 -> Items.createMapEffect(inventory, Material.PURPLE_DYE, 6, "§e6 Leben", "§8» §7Votes: §5" + Voting.six.size(), 10);
                    case 12 -> Items.createMapEffect(inventory, Material.PURPLE_DYE, 12, "§e12 Leben", "§8» §7Votes: §5" + Voting.twelve.size(), 12);
                    case 18 -> Items.createMapEffect(inventory, Material.PURPLE_DYE, 18, "§e18 Leben", "§8» §7Votes: §5" + Voting.eighteen.size(), 14);
                    case 23 -> Items.createMapEffect(inventory, Material.PURPLE_DYE, 23, "§e23 Leben", "§8» §7Votes: §5" + Voting.twentythree.size(), 16);
                    default -> Console.send("[NO_LIVE_SELECTED]: " + player.getName());
                }
            }
            default -> Console.send("[ERROR] NO_INVENTORY_FOUND");
        }
        player.openInventory(inventory);
        if (!Woolbattle.useDB)
            Woolbattle.getPlugin().saveConfig();
    }


    public static void particleitem(Player player) {
        if (!Woolbattle.useDB) {
            FileConfiguration config = Woolbattle.getPlugin().getConfig();
            Object particels = config.get(player.getName() + ".particels");

            if (particels == null) {
                config.set(player.getName() + ".particels", "true");
                Items.create(player.getInventory(), Material.BLAZE_ROD, "§ePartikel sind §aAN", 3);
                Woolbattle.getPlugin().saveConfig();
                return;
                }

            switch (particels.toString()) {
                case "true" -> Items.create(player.getInventory(), Material.BLAZE_ROD, "§ePartikel sind §aAN", 3);
                case "false" -> Items.create(player.getInventory(), Material.BLAZE_ROD,"§ePartikel sind §cAUS", 3);
                default -> {
                    config.set(player.getName() + ".particels", "true");
                    Items.create(player.getInventory(), Material.BLAZE_ROD,"§ePartikel sind §aAN", 3);
                }
            }
            Woolbattle.getPlugin().saveConfig();
            return;
        }

        Perk perk = PerkHelper.getPerks(player);
        boolean particels = perk.hasParticles();
        if (particels) {
            Items.create(player.getInventory(), Material.BLAZE_ROD,"§ePartikel sind §aAN", 3);
        } else
            Items.create(player.getInventory(), Material.BLAZE_ROD,"§ePartikel sind §cAUS", 3);
    }

    private void PartikelAction(Player player) {
        if (!Woolbattle.useDB) {
            FileConfiguration config = Woolbattle.getPlugin().getConfig();
            if (config.get(player.getName() + ".particels") == null) {
                config.set(player.getName()+ ".particels", "true");
            } else if (config.get(player.getName() + ".particels") == "true") {
                config.set(player.getName()+ ".particels", "false");
            } else config.set(player.getName()+ ".particels", "true");
            Woolbattle.getPlugin().saveConfig();
            return;
        }

        Perk perk = PerkHelper.getPerks(player);
        Perk oldPerk = new Perk(perk.getfirstPerk(), perk.getsecondPerk(), perk.getpassivePerk(), perk.hasParticles(), perk.getfirstPerkSlot(), perk.getsecondPerkSlot());
        boolean particles = perk.hasParticles();
        if (particles) {
            perk.setParticles(false);
        } else perk.setParticles(true);
        PerkHelper.updatePerks(player, oldPerk, perk);
        Lobby(player);
    }

    private void addToTeam(Team team, Player player) {
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        if (team.getMembers().isEmpty()) {
            team.addMember(player);
            return;
        }
        if (team.getMembers().contains(player)) return;
        // Abragen ob der player nicht in einem anderen team ist
        if (team.getMembers().size() < LiveSystem.TeamSize) {
            team.addMember(player);
            Console.send("Added Player to " + team.getName());
        } else Console.send("§cTeam is full");

        /*
        if (arrayList.contains(player)) return;
        ArrayList<Player> team = LiveSystem.VotedPlayers.get(player); //get the arrayList (team) where the player was before
        if (team != null)
            team.remove(player); //remove the player from the team

        if (arrayList.size() < LiveSystem.TeamSize) {
            arrayList.add(player);
            LiveSystem.VotedPlayers.put(player, arrayList);
            IngameState.teamUpdate();
        } else Console.send("§cTeam is full");
         */
    }

    private void checkTeam(Team team, Inventory inventory, Material material, String Team, int slot, String colorCode) {
        ArrayList<String> lore = new ArrayList<>();
        for (Player player : team.getMembers())
            lore.add(colorCode + player.getName());
        Items.createTeam(inventory, material, Team, lore, slot);
    }

    private static final Set<String> passivePerks = new HashSet<>();
    static {
        passivePerks.add("§3Aufzug");
        passivePerks.add("§3Explodierender Pfeil");
        passivePerks.add("§3IDK");
        passivePerks.add("§3Pfeilregen");
        passivePerks.add("§3Recharger");
        passivePerks.add("§3Reflector");
        passivePerks.add("§3Rocket Jump");
        passivePerks.add("§3IDK1");
        passivePerks.add("§3Rettungskapsel");
        passivePerks.add("§3SlowArrow");
        passivePerks.add("§3Spinne");
        passivePerks.add("§3Stomper");
        passivePerks.add("§3SlimePlattform");
    }

    /**
     * Refreshes the map voting UI for all players who currently have it open
     */
    public static void refreshMapVotingForAllViewers() {
        // Create a copy to avoid concurrent modification
        Set<Player> viewers = new HashSet<>(playersViewingMapVoting);
        for (Player viewer : viewers) {
            if (viewer.isOnline()) {
                // Check if they still have a Maps inventory open
                Component titleComponent = viewer.getOpenInventory().title();
                String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(titleComponent);
                if (title.contains("Maps")) {
                    // Refresh their inventory
                    refreshMapVotingUI(viewer);
                } else {
                    // They closed it or switched inventories, remove from tracking
                    playersViewingMapVoting.remove(viewer);
                }
            } else {
                playersViewingMapVoting.remove(viewer);
            }
        }
    }

    /**
     * Refreshes the map voting UI for a specific player
     */
    private static void refreshMapVotingUI(Player player) {
        List<SchematicManager.SchematicInfo> schematics = SchematicManager.getGameSchematics();

        int inventorySize = Math.max(9, ((schematics.size() + 8) / 9) * 9);
        if (inventorySize > 54) inventorySize = 54;

        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.clear();

        String currentWinningMap = MapVoting.getCurrentWinningMap();
        String playerVote = MapVoting.getPlayerVote(player);

        int slot = 0;
        for (SchematicManager.SchematicInfo schematic : schematics) {
            if (slot >= inventorySize) break;

            // Add vote count to display name using Adventure components
            int voteCount = MapVoting.getVotesForMap(schematic.getFileName());
            Component displayName = Component.text(schematic.getDisplayName(), NamedTextColor.GOLD);
            if (voteCount > 0) {
                displayName = displayName.append(Component.text(" (", NamedTextColor.DARK_GRAY))
                    .append(Component.text(voteCount + " votes", NamedTextColor.YELLOW))
                    .append(Component.text(")", NamedTextColor.DARK_GRAY));
            }

            // Determine material and display name color based on state
            Material itemMaterial = schematic.getMaterial();
            NamedTextColor nameColor = NamedTextColor.GOLD;

            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text("File: ", NamedTextColor.GRAY).append(Component.text(schematic.getFileName(), NamedTextColor.YELLOW)));
            lore.add(Component.text("Votes: ", NamedTextColor.GRAY).append(Component.text(voteCount, NamedTextColor.YELLOW)));

            if (schematic.getFileName().equals(currentWinningMap)) {
                // Winning map - use gold block and bright color
                itemMaterial = Material.GOLD_BLOCK;
                nameColor = NamedTextColor.YELLOW;
                lore.add(Component.text("⭐ CURRENTLY WINNING ⭐", NamedTextColor.YELLOW));
            }

            if (schematic.getFileName().equals(playerVote)) {
                // Player's vote - use emerald block and green color
                itemMaterial = Material.EMERALD_BLOCK;
                nameColor = NamedTextColor.GREEN;
                lore.add(Component.text("✓ YOUR VOTE", NamedTextColor.GREEN));
            }

            // If it's both winning and player's vote, prioritize player's vote appearance
            if (schematic.getFileName().equals(playerVote) && schematic.getFileName().equals(currentWinningMap)) {
                itemMaterial = Material.EMERALD_BLOCK;
                nameColor = NamedTextColor.GREEN;
                lore.clear();
                lore.add(Component.text("File: ", NamedTextColor.GRAY).append(Component.text(schematic.getFileName(), NamedTextColor.YELLOW)));
                lore.add(Component.text("Votes: ", NamedTextColor.GRAY).append(Component.text(voteCount, NamedTextColor.YELLOW)));
                lore.add(Component.text("✓ YOUR VOTE & WINNING! ⭐", NamedTextColor.GOLD));
            }

            lore.add(Component.text("Click to vote for this map!", NamedTextColor.GRAY));

            // Update display name color
            displayName = Component.text(schematic.getDisplayName(), nameColor);
            if (voteCount > 0) {
                displayName = displayName.append(Component.text(" (", NamedTextColor.DARK_GRAY))
                    .append(Component.text(voteCount + " votes", NamedTextColor.YELLOW))
                    .append(Component.text(")", NamedTextColor.DARK_GRAY));
            }

            // Create item
            ItemStack item = new ItemStack(itemMaterial);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.displayName(displayName);
            itemMeta.lore(lore);
            item.setItemMeta(itemMeta);
            inventory.setItem(slot, item);
            slot++;
        }

        if (schematics.isEmpty()) {
            Items.create(inventory, Material.BARRIER, "§cNo maps available", 4);
        }
    }

    private void handleMapSelection(Player player, ItemStack clickedItem) {
        if (clickedItem.getItemMeta() == null || clickedItem.getType() == Material.BARRIER) return;
        
        List<Component> lore = clickedItem.getItemMeta().lore();
        if (lore == null || lore.isEmpty()) return;
        
        String fileName = null;
        net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer plainSerializer = 
            net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText();
        
        for (Component line : lore) {
            String loreText = plainSerializer.serialize(line);
            
            // Look for the file line and extract filename
            if (loreText.startsWith("File: ")) {
                fileName = loreText.substring(6); // Remove "File: " prefix
                break;
            }
        }
        
        if (fileName != null) {
            List<SchematicManager.SchematicInfo> schematics = SchematicManager.getGameSchematics();
            for (SchematicManager.SchematicInfo schematic : schematics) {
                if (schematic.getFileName().equals(fileName)) {
                    MapVoting.voteForMap(player, fileName);
                    
                    String previousVote = MapVoting.getPlayerVote(player);
                    Component displayName = Component.text(schematic.getDisplayName(), NamedTextColor.GOLD);
                    if (previousVote != null && !previousVote.equals(fileName)) {
                        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Changed vote to: ", NamedTextColor.YELLOW).append(displayName)));
                    } else {
                        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Voted for: ", NamedTextColor.GREEN).append(displayName)));
                    }
                    
                    Console.send(player.getName() + " voted for map: " + schematic.getFileName());

                    // UI refresh happens automatically for all viewers via MapVoting.voteForMap()
                    return;
                }
            }
        }
    }
}