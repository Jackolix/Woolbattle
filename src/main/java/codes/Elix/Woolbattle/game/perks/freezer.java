package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class freezer implements Listener {

    private int cooldown = 4;
    private int freeze_time_sec = 5;
    private int slowness_strength = 5;


    private ArrayList<Player> available = new ArrayList<>();

    private JavaPlugin plugin;

    @EventHandler
    public void onFreezerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (available.contains(event.getPlayer()))  return;
        if (event.getItem().getType() == Material.PACKED_ICE) {
            available.add(event.getPlayer());
            for(Enchantment ench: event.getItem().getEnchantments().keySet()){
                event.getItem().removeEnchantment(ench);
            }

            Projectile snowball = event.getPlayer().launchProjectile(Snowball.class);
            snowball.setMetadata("Freezer", new FixedMetadataValue(plugin, "keineAhnungWiesoIchDasBrauch"));

            //TODO: Cooldown for freezer (visual representation)
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> activate(event.getPlayer(), event.getItem()), 20 * cooldown);
        }
    }


private void activate(Player player, ItemStack item) {
        available.remove(player);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemmeta);
}

    @EventHandler
    public void onFreezerDamage(EntityDamageByEntityEvent e) {
        Entity snowball = e.getDamager();
        Player player   = (Player) e.getEntity();
        if(!(snowball instanceof Snowball))
            return;
        if (snowball.getCustomName().equals("freezer")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, freeze_time_sec * 20, slowness_strength));
        }

    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new freezer(), Woolbattle.getPlugin());
    }

    public static void disable() {}
}