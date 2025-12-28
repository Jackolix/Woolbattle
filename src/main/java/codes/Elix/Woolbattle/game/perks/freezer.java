package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class freezer implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("freezer");
    }


    @EventHandler
    public void onFreezerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.PACKED_ICE) {
            Player player = event.getPlayer();
            PerkConfig.PerkSettings settings = getSettings();
            
            if (!Woolbattle.debug)
                if (!Items.cost(player, settings.getCost())) return;

            Vector direction = player.getLocation().getDirection();
            Vector velocity = direction.multiply(settings.getProjectileVelocity());
            Projectile snowball = player.launchProjectile(Snowball.class, velocity);
            snowball.setMetadata("Freezer", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger") 
                ? settings.getCooldownRecharger() 
                : settings.getCooldown();
            
            System.out.println("[FREEZER] Using cooldown: " + cooldown + " (normal: " + settings.getCooldown() + ", recharger: " + settings.getCooldownRecharger() + ", cost: " + settings.getCost() + ")");
            
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
        
        PerkConfig.PerkSettings settings = getSettings();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 
            settings.getFreezeTimeSeconds() * 20, settings.getSlownessStrength()));
    }
    public static void enable() { Bukkit.getPluginManager().registerEvents(new freezer(), Woolbattle.getPlugin()); }
    public static void disable() {}
}