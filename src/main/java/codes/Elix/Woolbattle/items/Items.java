// This class was created by Elix on 26.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.listeners.GameProtectionListener;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Items {
    //TODO: Enchantments zum Array machen

    @Deprecated
    public static ArrayList<Player> interact = new ArrayList<>(); // Deprecated: Use perkCooldowns instead
    public static Map<Player, Set<String>> perkCooldowns = new HashMap<>(); // Tracks which perks are on cooldown per player
    public static Map<Player, Map<String, Integer>> tasks = new HashMap<>();
    public static HashMap<Player, Perk> perks = new HashMap<>();

    public static void standartitems(Player player) {
        create(player.getInventory(), Material.ENDER_PEARL,"EnderPearl", 2);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowmeta = bow.getItemMeta();
        bowmeta.displayName(Component.text("Wool Blaster", NamedTextColor.AQUA));
        bowmeta.addEnchant(Enchantment.PUNCH, 2, false);
        bowmeta.addEnchant(Enchantment.INFINITY, 1, false);
        bowmeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        bowmeta.setUnbreakable(true);
        bow.setItemMeta(bowmeta);

        ItemStack shears = new ItemStack(Material.SHEARS);
        ItemMeta shearsmeta = shears.getItemMeta();
        shearsmeta.displayName(Component.text("Mega Schere", NamedTextColor.GREEN));
        shearsmeta.addEnchant(Enchantment.EFFICIENCY, 100, true);
        shearsmeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        shearsmeta.setUnbreakable(true);
        shears.setItemMeta(shearsmeta);

        player.getInventory().setItem(0, bow);
        player.getInventory().setItem(1, shears);
    }

    // Helper method to convert legacy colors to proper Adventure components
    public static Component convertLegacyText(String text) {
        if (text.contains("§")) {
            // Use Adventure's built-in legacy serializer to properly convert all legacy formatting codes
            return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                    .legacySection()
                    .deserialize(text);
        } else {
            return Component.text(text);
        }
    }

    //Normal ItemConstructor
    public static void create(Inventory inventory, Material material, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    //Normal ItemConstructor with enchants
    public static void create(Inventory inventory, Material material, Enchantment enchant, Integer enchantnumber, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        if (enchant != null) {
            itemMeta.addEnchant(enchant, enchantnumber, true);
        }

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    //Normal ItemConstructor with more than one enchant
    public static void create(Inventory inventory, Material material, HashMap<Enchantment, Integer> enchant, Integer enchantnumber, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        if (enchant != null) {
            //enchant.forEach(); // TODO this should be the right way
            //itemMeta.addEnchant(enchant, enchantnumber, true);
        }

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //Normal ItemConstructor with hiddenEnchants
    public static void create(Inventory inventory, Material material, boolean enchantEffect, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        if (enchantEffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //Normal ItemConstructor with lore and hiddenEnchants
    public static void create(Inventory inventory, Material material, boolean enchantEffect, String name, String text, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        if (enchantEffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<Component> lore = new ArrayList<>(); // To list with component
        lore.add(Component.text(text));
        itemMeta.lore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void create(Inventory inventory, Material material, int amount, String name, String text, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(text));
        itemMeta.lore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //Perks
    //ItemConstructor for Perks with hiddenEnchants
    public static void create(Inventory inventory, Material material, boolean enchantEffect, String name, String description, int cost, int cooldown, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        if (enchantEffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(description, NamedTextColor.GRAY));
        lore.add(Component.text("Preis: " + cost + " Wolle", NamedTextColor.GOLD));
        lore.add(Component.text("Cooldown: " + cooldown, NamedTextColor.GOLD));
        itemMeta.lore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //PassivePerk Constructor with hiddenEnchants
    public static void createPassivePerk(Inventory inventory, Material material, boolean enchantEffect, String name, String description, String cost, String cooldown, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        if (enchantEffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(description, NamedTextColor.GRAY));
        if (cost != null)
            lore.add(Component.text(cost));
        if (cooldown != null)
            lore.add(Component.text(cooldown));
        itemMeta.lore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void createPassivePerk(Inventory inventory, Material material, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    //TeamItemsConstructor
    public static void createTeam(Inventory inventory, Material material, String name, ArrayList<String> lore, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void createMap(Inventory inventory, Material material, int amount, String name, String text, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(" "));
        lore.add(Component.text(text));
        lore.add(Component.text(" "));
        itemMeta.lore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void createMapEffect(Inventory inventory, Material material, int amount, String name, String text, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(" "));
        lore.add(Component.text(text));
        lore.add(Component.text(" "));
        itemMeta.lore(lore);
        itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void createCooldown(Inventory inventory, Material material, int amount, String name, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(name));

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static ItemStack boots(Color color) {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.displayName(Component.text("Boots", NamedTextColor.BLUE));
        meta.setColor(color);
        meta.setUnbreakable(true);

        item.setItemMeta(meta);
        return item;
    }


    public static int amount(Player player, Material item) {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack is : inv.all(item).values()) {
            if (is != null && is.getType() == item)
                count = count + is.getAmount();
        }
        return count;
    }

    public static int woolAmount(Player player) {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (Material material : GameProtectionListener.toDestroy) {
            for (ItemStack is : inv.all(material).values()) {
                if (is != null && is.getType() == material)
                    count = count + is.getAmount();
            }
        }
        return count;
    }

    public static boolean cost(Player player, int cost) {
        if (!(Items.woolAmount(player) >= cost))
            return false;

        ItemStack item = new ItemStack(Items.getWoolColor(player));
        item.setAmount(cost);
        player.getInventory().removeItem(item);

        // Update flight ability after consuming wool
        codes.Elix.Woolbattle.game.DoubleJump.updateFlightBasedOnWool(player);

        return true;
    }

    /**
     * Check if a specific perk is on cooldown for a player
     * @param player The player to check
     * @param perkName The name of the perk to check
     * @return true if the perk is on cooldown, false otherwise
     */
    public static boolean isPerkOnCooldown(Player player, String perkName) {
        return perkCooldowns.containsKey(player) && perkCooldowns.get(player).contains(perkName);
    }

    /**
     * Add a perk to the cooldown list for a player
     * @param player The player
     * @param perkName The name of the perk
     */
    public static void addPerkCooldown(Player player, String perkName) {
        perkCooldowns.computeIfAbsent(player, k -> new HashSet<>()).add(perkName);
    }

    /**
     * Remove a perk from the cooldown list for a player
     * @param player The player
     * @param perkName The name of the perk
     */
    public static void removePerkCooldown(Player player, String perkName) {
        if (perkCooldowns.containsKey(player)) {
            perkCooldowns.get(player).remove(perkName);
            if (perkCooldowns.get(player).isEmpty()) {
                perkCooldowns.remove(player);
            }
        }
    }

    public static void visualCooldown(Player player, int cooldown, Material perk, int slot, String perkname) {
        interact.add(player);  // Keep for backward compatibility
        addPerkCooldown(player, perkname);  // Add perk-specific cooldown
        ItemStack item = new ItemStack(perk);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(convertLegacyText(perkname));
        itemMeta.setUnbreakable(true);
        item.setItemMeta(itemMeta);

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Woolbattle.getPlugin(), new Runnable() {
            int count = cooldown;

            @Override
            public void run() {
                count--;
                if (count > 0) {
                    createCooldown(player.getInventory(), Material.GRAY_DYE, count, "Cooldown", slot);
                } else {
                    // create(player.getInventory(), item, perkname, slot);
                    player.getInventory().setItem(slot, item);
                    interact.remove(player);  // Keep for backward compatibility
                    removePerkCooldown(player, perkname);  // Remove perk-specific cooldown
                    cancel(tasks.get(player).get(perkname));
                }
            }
        }, 0, 20);
        tasks.computeIfAbsent(player, k -> new HashMap<>()).put(perkname, taskID);
    }

    public static Material getWoolColor(Player player) {
        codes.Elix.Woolbattle.game.HelpClasses.Team team = CustomPlayer.getCustomPlayer(player).getTeam();
        if (team == null) return null;

        return switch (team.getName()) {
            case "red" -> Material.RED_WOOL;
            case "blue" -> Material.BLUE_WOOL;
            case "green" -> Material.GREEN_WOOL;
            case "yellow" -> Material.YELLOW_WOOL;
            default -> null;
        };
    }
    public static void cancel(Integer taskID) {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static NamedTextColor getColor(String team) {
        return switch (team) {
            case "red" -> NamedTextColor.RED;
            case "blue" -> NamedTextColor.BLUE;
            case "green" -> NamedTextColor.GREEN;
            case "yellow" -> NamedTextColor.YELLOW;
            default -> null;
        };
    }
}
