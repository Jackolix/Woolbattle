// This class was created by Elix on 26.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.List;
import java.util.Map;

public class Items {
    //TODO: Enchantments zum Array machen

    public static ArrayList<Player> interact = new ArrayList<>();
    public static Map<Player, Map<String, Integer>> tasks = new HashMap<>();

    public static void standartitems(Player player) {
        create(player.getInventory(), Material.ENDER_PEARL, null, null, "EnderPearl", 2);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowmeta = bow.getItemMeta();
        bowmeta.setDisplayName("§bWool Blaster");
        bowmeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, false);
        bowmeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        bowmeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        bowmeta.setUnbreakable(true);
        bow.setItemMeta(bowmeta);

        ItemStack shears = new ItemStack(Material.SHEARS);
        ItemMeta shearsmeta = shears.getItemMeta();
        shearsmeta.setDisplayName("§aMega Schere");
        shearsmeta.addEnchant(Enchantment.DIG_SPEED, 5, false);
        shearsmeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        shearsmeta.setUnbreakable(true);
        shears.setItemMeta(shearsmeta);

        player.getInventory().setItem(0, bow);
        player.getInventory().setItem(1, shears);
    }

    //Normal Itemconstructor
    public static void create(Inventory inventory, Material material, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    //Normal Itemconstructor with enchants
    public static void create(Inventory inventory, Material material, Enchantment enchant, Integer enchantnumber, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchant != null) {
            itemMeta.addEnchant(enchant, enchantnumber, true);
        }

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //Normal ItemConstructor with hiddenenchants
    public static void create(Inventory inventory, Material material, boolean enchanteffect, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchanteffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //Normal Itemconstructor with lore and hiddenenchants
    public static void create(Inventory inventory, Material material, boolean enchanteffect, String name, String text, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchanteffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        ArrayList<String> lore = new ArrayList<>();
        lore.add(text);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void create(Inventory inventory, Material material, int amount, String name, String text, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(text);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //Perks
    //Itemcostructor for Perks with hidenenchants
    public static void create(Inventory inventory, Material material, boolean enchanteffect, String name, String description, int cost, int cooldown, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchanteffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + description);
        lore.add(ChatColor.GOLD + "Preis: " + cost + " Wolle");
        lore.add(ChatColor.GOLD + "Cooldown: " + cooldown);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //PassivePerk Constructor with hiddenenchants
    public static void createPassivePerk(Inventory inventory, Material material, boolean enchanteffect, String name, String description, String cost, String cooldown, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchanteffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + description);
        if (cost != null)
            lore.add(cost);
        if (cooldown != null)
            lore.add(cooldown);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    //TeamItemsConstructor
    public static void createTeam(Inventory inventory, Material material, String name, ArrayList<String> lore, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void createMap(Inventory inventory, Material material, int amount, String name, String text, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(text);
        lore.add(" ");
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static void createcooldown(Inventory inventory, Material material, int amount, String name, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    public static ItemStack boots(Color color) {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "Boots");
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

    public static boolean cost(Player player, int cost) {
        if (!(Items.amount(player, Items.getWoolColor(player)) >= cost))
            return false;

        ItemStack item = new ItemStack(Items.getWoolColor(player));
        item.setAmount(cost);
        player.getInventory().removeItem(item);
        return true;
    }

    public static void visualCooldown(Player player, int cooldown, Material perk, int slot, String perkname) {
        Items.interact.add(player);

        int taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Woolbattle.getPlugin(), new Runnable() {
            int count = cooldown;

            @Override
            public void run() {
                Items.createcooldown(player.getInventory(), Material.GRAY_DYE, count, "Cooldown", slot);
                count--;
                if (count == 0) {
                    Items.create(player.getInventory(), perk, perkname, slot);
                    Items.interact.remove(player);
                    cancel(tasks.get(player).get(perkname));
                }
            }
        }, 0, 20);
        tasks.computeIfAbsent(player, k -> new HashMap<>()).put(perkname, taskID);
        Console.send("Task added to Database");

    }

    public static Material getWoolColor(Player player) {
        return switch (LiveSystem.Team.get(player)) {
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

    public static ChatColor getColor(String team) {
        return switch (team) {
            case "red" -> ChatColor.RED;
            case "blue" -> ChatColor.BLUE;
            case "green" -> ChatColor.GREEN;
            case "yellow" -> ChatColor.YELLOW;
            default -> null;
        };
    }
}
