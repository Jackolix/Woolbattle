// This class was created by Elix on 22.09.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BowShoot implements Listener {

    public ArrayList<Projectile> arrows = new ArrayList<>();


    @EventHandler
    public void onShootBow(EntityShootBowEvent e){
        if (!(e.getEntity() instanceof Player player)) return;

        if (Items.amount(player, Items.getWoolColor(player)) == 0) {
            Console.send("Player has less than 1 wool");
            ItemStack arrow = new ItemStack(Material.ARROW);
            player.getInventory().removeItem(arrow);
            return;
        }

        ItemStack item = new ItemStack(Items.getWoolColor(player));
        item.setAmount(1);
        player.getInventory().removeItem(item);
        check(player);

        arrows.add((Projectile) e.getProjectile());
        addParticleEffect((Projectile)e.getProjectile());
    }
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Arrow){
            arrows.remove(e.getEntity());
        }
    }

    //moved the Bukkit runnable to a single method and not in the onEnable
    @SuppressWarnings("deprecation")
    public void addParticleEffect(final Projectile entity){
        new BukkitRunnable(){

            @Override
            public void run() {
                if(arrows.contains(entity)){//if the arrow still is in the air
                    Map<Player, Location> locationCache = new HashMap<>();
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        locationCache.put(online, online.getLocation());
                        online.spawnParticle(Particle.SONIC_BOOM, entity.getLocation(), 2);
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



}