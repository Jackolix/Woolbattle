// This class was created by Elix on 26.07.22


package codes.Elix.Woolbattle.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

    public static void standartitems(Player player) {
        create(player, Material.BOW, Enchantment.KNOCKBACK, 5, "§5Woolbow", 0);
        create(player, Material.SHEARS, Enchantment.KNOCKBACK, 5, "§5Woolshear", 1);
        create(player, Material.ENDER_PEARL, null, null, "EnderPearl", 2);
    }

    public static void create(Player Inventory, Material material, Enchantment enchant, Integer enchantnumber, String name, int slot) {
            Inventory inventory = Inventory.getInventory();
            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(name);
            if (enchant != null) {
                itemMeta.addEnchant(enchant, enchantnumber, true);
            }
            item.setItemMeta(itemMeta);
            inventory.setItem(slot, item);
    }

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

    public static void create(Player Inventory, Material material, int Itembyte, Enchantment enchant, Integer enchantnumber, String name, int slot) {
        Inventory inventory = Inventory.getInventory();
        ItemStack item = new ItemStack(material, 1, (short) 0, (byte) Itembyte);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (enchant != null) {
            itemMeta.addEnchant(enchant, enchantnumber, true);
        }
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);

    }
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

}
