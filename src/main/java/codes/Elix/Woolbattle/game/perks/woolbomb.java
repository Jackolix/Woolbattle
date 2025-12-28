package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class woolbomb implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("woolbomb");
    }

    @EventHandler
    public void onBlockHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;
        if (!(event.getEntity().hasMetadata("Woolbomb"))) return;
        
        PerkConfig.PerkSettings settings = getSettings();
        Location location;
        if (event.getHitBlock() == null) {
            location = event.getHitEntity().getLocation();
        } else
            location = event.getHitBlock().getLocation();

        TNTPrimed tnt = (TNTPrimed) Bukkit.getWorlds().get(0).spawnEntity(location, EntityType.TNT);
        tnt.setFuseTicks(settings.getFuseTicks());

        @NotNull Collection<Entity> list = location.getNearbyEntities(settings.getExplosionRadius(), settings.getExplosionRadius(), settings.getExplosionRadius());
        ArrayList<Player> players = new ArrayList<>();
        for (Entity entity : list) {
            if (entity instanceof Player)
                players.add((Player) entity);
        }

        //Player player = (Player) event.getEntity().getShooter();
        //int xPos = location.getBlockX() -  player.getLocation().getBlockX();
        //int zPos = location.getBlockZ() -  player.getLocation().getBlockX();
        //int redux = 5; // You don't want the player flying thousands of blocks
        // player.setVelocity(new Vector(xPos/redux, 0.5, zPos/redux));

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player player : players) {
                    int xPos = location.getBlockX() - player.getLocation().getBlockX();
                    int zPos = location.getBlockZ() - player.getLocation().getBlockZ();
                    double redux = 0.25; // You don't want the player flying thousands of blocks
                    Vector v = new Vector(-(xPos / redux), settings.getKnockbackYVelocity(), -(zPos / redux));
                    player.setVelocity(v.normalize().multiply(settings.getKnockbackMultiplier()));
                }
            }
        }, settings.getExplosionDelayTicks());

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!(event.getItem().getType() == Material.TNT)) return;
        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();

        Vector direction = player.getLocation().getDirection();
        Vector velocity = direction.multiply(settings.getProjectileVelocity());
        
        if (Woolbattle.debug) {
            event.setCancelled(true);
            Projectile snowball = player.launchProjectile(Snowball.class, velocity);
            snowball.setMetadata("Woolbomb", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
            return;
        }

        if (!Items.cost(player, settings.getCost())) {
            event.setCancelled(true);
            return;
        }

        Projectile snowball = player.launchProjectile(Snowball.class, velocity);
        snowball.setMetadata("Woolbomb", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

        event.setCancelled(true);
        int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
            ? settings.getCooldownRecharger()
            : settings.getCooldown();
        int slot = player.getInventory().getHeldItemSlot();
        Items.visualCooldown(player, cooldown, Material.TNT, slot, "§3Woolbomb");
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new woolbomb(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
