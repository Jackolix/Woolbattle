package codes.Elix.Woolbattle.game.perks;

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
import java.util.List;

public class woolbomb implements Listener {

    int cost = 8;
    int cooldown = 13;

    @EventHandler
    public void onBlockHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;
        if (!(event.getEntity().hasMetadata("Woolbomb"))) return;
        Location location;
        if (event.getHitBlock() == null) {
            location = event.getHitEntity().getLocation();
        } else
            location = event.getHitBlock().getLocation();

        TNTPrimed tnt = (TNTPrimed) Bukkit.getWorlds().get(0).spawnEntity(location, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(10); // How long until it explodes

        System.out.println(location.getNearbyEntities(5,5,5));

        @NotNull Collection<Entity> list = location.getNearbyEntities(5,5,5);
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

        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player player : players) {
                    int xPos = location.getBlockX() - player.getLocation().getBlockX();
                    int zPos = location.getBlockZ() - player.getLocation().getBlockZ();
                    double redux = 0.25; // You don't want the player flying thousands of blocks
                    Vector v = new Vector(-(xPos / redux), 0.5, -(zPos / redux));
                    //player.setVelocity(new Vector(-(xPos / redux), 0.5, -(zPos / redux)));
                    player.setVelocity(v.normalize().multiply(5));
                }
            }
        }, 10);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!(event.getItem().getType() == Material.TNT)) return;
        Player player = event.getPlayer();

        if (Woolbattle.debug) {
            event.setCancelled(true);
            Projectile snowball = player.launchProjectile(Snowball.class);
            snowball.setMetadata("Woolbomb", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
            return;
        }

        if (!Items.cost(player, cost)) {
            event.setCancelled(true);
            return;
        }

        Projectile snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("Woolbomb", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

        event.setCancelled(true);
        int slot = player.getInventory().getHeldItemSlot();
        Items.visualCooldown(player, cooldown, Material.TNT, slot, "§3Woolbomb");
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new woolbomb(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
