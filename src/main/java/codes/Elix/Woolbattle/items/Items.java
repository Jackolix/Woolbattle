// This class was created by Elix on 26.07.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.listeners.GameProtectionListener;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import net.kyori.adventure.text.Component;
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
    public static HashMap<Player, Perk> perks = new HashMap<>();

    public static void standartitems(Player player) {
        create(player.getInventory(), Material.ENDER_PEARL,"EnderPearl", 2);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowmeta = bow.getItemMeta();
        bowmeta.displayName(Component.text("§bWool Blaster"));
        bowmeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, false);
        bowmeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        bowmeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        bowmeta.setUnbreakable(true);
        bow.setItemMeta(bowmeta);

        ItemStack shears = new ItemStack(Material.SHEARS);
        ItemMeta shearsmeta = shears.getItemMeta();
        shearsmeta.displayName(Component.text("§aMega Schere"));
        shearsmeta.addEnchant(Enchantment.DIG_SPEED, 5, false);
        shearsmeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        shearsmeta.setUnbreakable(true);
        shears.setItemMeta(shearsmeta);

        player.getInventory().setItem(0, bow);
        player.getInventory().setItem(1, shears);
    }

    //Normal ItemConstructor
    public static void create(Inventory inventory, Material material, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(name));
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

    //Normal ItemConstructor with enchants
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

    //Normal ItemConstructor with more than one enchant
    public static void create(Inventory inventory, Material material, HashMap<Enchantment, Integer> enchant, Integer enchantnumber, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
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
        itemMeta.setDisplayName(name);
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
        itemMeta.setDisplayName(name);;
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
        itemMeta.setDisplayName(name);

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
        itemMeta.setDisplayName(name);
        if (enchantEffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GRAY + description));
        lore.add(Component.text(ChatColor.GOLD + "Preis: " + cost + " Wolle"));
        lore.add(Component.text(ChatColor.GOLD + "Cooldown: " + cooldown));
        itemMeta.lore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }


    //PassivePerk Constructor with hiddenEnchants
    public static void createPassivePerk(Inventory inventory, Material material, boolean enchantEffect, String name, String description, String cost, String cooldown, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchantEffect) {
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GRAY + description));
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
        itemMeta.setDisplayName(name);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);

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
        itemMeta.setDisplayName(name);

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
        return true;
    }

    public static void visualCooldown(Player player, int cooldown, Material perk, int slot, String perkname) {
        interact.add(player);
        ItemStack item = new ItemStack(perk);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(perkname));
        itemMeta.setUnbreakable(true);
        item.setItemMeta(itemMeta);

        int taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Woolbattle.getPlugin(), new Runnable() {
            int count = cooldown;

            @Override
            public void run() {
                createCooldown(player.getInventory(), Material.GRAY_DYE, count, "Cooldown", slot);
                count--;
                if (count == 0) {
                    // create(player.getInventory(), item, perkname, slot);
                    player.getInventory().setItem(slot, item);
                    interact.remove(player);
                    cancel(tasks.get(player).get(perkname));
                }
            }
        }, 0, 20);
        tasks.computeIfAbsent(player, k -> new HashMap<>()).put(perkname, taskID);
        Console.send("Task added to Database");

    }

    public static Material getWoolColor(Player player) {
        return switch (CustomPlayer.getCustomPlayer(player).getTeam().getName()) {
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
