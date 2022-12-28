package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class freezer implements Listener {

    int cooldown = 5;
    int freeze_time_sec = 3;
    int slowness_strength = 5;
    int cost = 5;


    @EventHandler
    public void onFreezerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.PACKED_ICE) {
            Player player = event.getPlayer();
            if (!Woolbattle.debug)
                if (!Items.cost(player, cost)) return;

            Projectile snowball = player.launchProjectile(Snowball.class);
            snowball.setMetadata("Freezer", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

            if (Objects.equals(PerkHelper.passive(player), "recharger"))
                cooldown = 3;
            int slot = player.getInventory().getHeldItemSlot();
            if (!Woolbattle.debug)
                Items.visualCooldown(player, cooldown, Material.PACKED_ICE, slot, "§3Freezer");
        }
    }

    @EventHandler
    public void onFreezerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Snowball)) return;
        if (!(e.getEntity() instanceof Player player)) return;

        Projectile snowball = (Projectile) e.getDamager();

        if (!(snowball.hasMetadata("Freezer"))) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, freeze_time_sec * 20, slowness_strength));
    }
    public static void enable() { Bukkit.getPluginManager().registerEvents(new freezer(), Woolbattle.getPlugin()); }
    public static void disable() {}
}