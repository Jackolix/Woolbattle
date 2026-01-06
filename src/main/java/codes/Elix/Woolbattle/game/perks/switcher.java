// This class was created by Elix on 31.07.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Objects;


public class switcher implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("switcher");
    }

    @EventHandler
    public void onSwitch(EntityDamageByEntityEvent event) {

        if ((event.getDamager() instanceof Player)) return;
        if (!(event.getDamager() instanceof Snowball)) return;

        Projectile projectile = (Projectile) event.getDamager();
        if (!projectile.hasMetadata("Switcher")) return;

        Player shooter = (Player) projectile.getShooter();
        Player hitted = (Player) event.getEntity();

        CustomPlayer customShooter = CustomPlayer.getCustomPlayer(shooter);
        CustomPlayer customHitted= CustomPlayer.getCustomPlayer(hitted);

        if (customHitted.getTeam().equals(customShooter.getTeam())) return;

        Location location1 = shooter.getLocation();
        Location location2 = hitted.getLocation();

        shooter.teleport(location2);
        hitted.teleport(location1);

        shooter.playSound(shooter.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);
        hitted.playSound(hitted.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (!(event.getItem().getType() == Material.SNOWBALL)) return;

        // Only handle right-click actions (normal way to throw items)
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_AIR &&
            event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        event.setCancelled(true);

        // Check if player is already on cooldown
        if (Items.interact.contains(player)) {
            return;
        }
        
        PerkConfig.PerkSettings settings = getSettings();
        
        if (!Woolbattle.debug)
            if (!Items.cost(player, settings.getCost())) {
                return;
            }
        
        Vector direction = player.getLocation().getDirection();
        Vector velocity = direction.multiply(settings.getProjectileVelocity());
        
        Projectile snowball = player.launchProjectile(Snowball.class, velocity);
        snowball.setMetadata("Switcher", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

        int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
            ? settings.getCooldownRecharger()
            : settings.getCooldown();
            
        int slot = player.getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.SNOWBALL, slot, "§3Tauscher");
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new switcher(), Woolbattle.getPlugin());
    }
    public static void disable() {}

}

