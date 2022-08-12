// This class was created by Elix on 26.07.22


package codes.Elix.Woolbattle.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Items {
    //TODO: Enchantments zum Array machen

    public static void standartitems(Player player) {
        create(player.getInventory(), Material.BOW, Enchantment.KNOCKBACK, 5, "§5Woolbow", 0);
        create(player.getInventory(), Material.SHEARS, Enchantment.KNOCKBACK, 5, "§5Woolshear", 1);
        create(player.getInventory(), Material.ENDER_PEARL, null, null, "EnderPearl", 2);
    }



    //Normal Itemconstructor
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


    //Normal Itemconstructor with bytes
    public static void create(Inventory inventory, Material material, int Itembyte, Enchantment enchant, Integer enchantnumber, String name, int slot) {
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
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


    //Normal ItemConstructor with hiddenenchants and bytes
    public static void create(Inventory inventory, Material material, int Itembyte, boolean enchanteffect, String name, int slot) {
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
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


    //Normal ItemConstructor with lore and hiddenenchants and bytes
    public static void create(Inventory inventory, Material material, int Itembyte, boolean enchanteffect, String name, String text, int slot) {
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
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


    //Perks
    //Itemcostructor for Perks with hidenenchants
    public static void createPerk(Inventory inventory, Material material, boolean enchanteffect, String name, String description, int cost, int cooldown, int slot) {
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

    //Itemcostructor for Perks with hidenenchants and bytes
    public static void createPerk(Inventory inventory, Material material, int Itembyte, boolean enchanteffect, String name, String description, int cost, int cooldown, int slot) {
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
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


    //PassivePerk Constructor with hiddenenchants and bytes
    public static void createPassivePerk(Inventory inventory, Material material, int Itembyte, boolean enchanteffect, String name, String description, String cost, String cooldown, int slot) {
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
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
    public static void createTeam(Inventory inventory, Material material, int Itembyte, String name, ArrayList<String> lore, int slot) {
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

}
