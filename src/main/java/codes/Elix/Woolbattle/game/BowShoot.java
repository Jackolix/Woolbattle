// This class was created by Elix on 22.09.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BowShoot implements Listener {

    public ArrayList<Projectile> arrows = new ArrayList<>();
    public static HashMap<Player, Integer> bomb = new HashMap<>();


    @EventHandler
    public void onShootBow(EntityShootBowEvent e){
        if (!(e.getEntity() instanceof Player player)) return;

        if (Items.woolAmount(player) == 0) {
            Console.send("Player has less than 1 wool");
            ItemStack arrow = new ItemStack(Material.ARROW);
            player.getInventory().removeItem(arrow);
            return;
        }

        ItemStack item = new ItemStack(Items.getWoolColor(player));
        item.setAmount(1);
        player.getInventory().removeItem(item);
        check(player);

        if (bomb.containsKey(player)) {
            int amount = bomb.get(player);
            amount++;
            if (amount >= 8) {
                e.getProjectile().setMetadata("bomb", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
                bomb.put(player, 0);
            } else
                bomb.put(player, amount);
        }
        arrows.add((Projectile) e.getProjectile());
        addParticleEffect((Projectile)e.getProjectile());
    }
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Arrow){
            arrows.remove(e.getEntity());
        }
        if (!(e.getEntity().getShooter() instanceof Player player1)) return;
        if (!Objects.equals(Items.perks.get(player1).getpassivePerk(), "exploding_arrow")) return;
        if ((!e.getEntity().hasMetadata("bomb"))) return;
        Location location;
        if (e.getHitBlock() == null) return;
        location = e.getHitBlock().getLocation();
        Location spawnLocation = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());

        TNTPrimed tnt = (TNTPrimed) Bukkit.getWorlds().get(0).spawnEntity(spawnLocation, EntityType.TNT);
        tnt.setFuseTicks(10); // How long until it explodes
        tnt.setMetadata("exploding_arrow", new FixedMetadataValue(Woolbattle.getPlugin(), "okay"));

        @NotNull Collection<Entity> list = location.getNearbyEntities(5,5,5);
        ArrayList<Player> players = new ArrayList<>();
        for (Entity entity : list) {
            if (entity instanceof Player)
                players.add((Player) entity);
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(Woolbattle.getPlugin(), () -> {
            for (Player player : players) {
                int xPos = location.getBlockX() - player.getLocation().getBlockX();
                int zPos = location.getBlockZ() - player.getLocation().getBlockZ();
                double redux = 0.25; // You don't want the player flying thousands of blocks
                Vector v = new Vector(-(xPos / redux), 0.5, -(zPos / redux));
                player.setVelocity(v.normalize().multiply(5));
            }
        }, 10);
    }

    public void addParticleEffect(final Projectile entity){
        new BukkitRunnable(){

            @Override
            public void run() {
                if(arrows.contains(entity)){//if the arrow still is in the air
                    Map<Player, Location> locationCache = new HashMap<>();
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        locationCache.put(online, online.getLocation());
                        online.spawnParticle(Particle.GLOW, entity.getLocation(), 2); //Particle.SONIC_BOOM
                    }

                    Location loc = entity.getLocation();
                    for (Map.Entry<Player, Location> entry : locationCache.entrySet()) {
                        if(entry.getKey().isOnline()){

                            //If the player has left when the arrow was going. we should remove him from the map to avoid NPE's
                            if (entry.getValue().getWorld() == loc.getWorld()) {
                                entry.getValue().distanceSquared(loc);
                            }
                        }
                    }
                }else{//we cancel the event when the arrow isn't in the air anymore.
                    this.cancel();
                    return;
                }
            }
        }.runTaskTimer(Woolbattle.getPlugin(), 0, 1);
    }

    @EventHandler
    public void onEntityExplode(ExplosionPrimeEvent event){
        if(event.getEntity().getType() == EntityType.TNT){
            if (!event.getEntity().hasMetadata("exploding_arrow")) return;
            // event.setCancelled(true);
            event.setRadius(1.0F);
        }
    }

    public void check(Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (Items.amount(player, Items.getWoolColor(player)) == 0) {
                    Console.send("Player has less than 1 wool");
                    ItemStack arrow = new ItemStack(Material.ARROW);
                    player.getInventory().removeItem(arrow);
                }
            }
        }, 20);
    }

    public static void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            codes.Elix.Woolbattle.game.HelpClasses.Team team = CustomPlayer.getCustomPlayer(player).getTeam();
            if (team == null || Objects.equals(team.getName(), "spectator")) continue;
            if (Objects.equals(Items.perks.get(player).getpassivePerk(), "exploding_arrow"))
                bomb.put(player, 0);
        }

    }



}