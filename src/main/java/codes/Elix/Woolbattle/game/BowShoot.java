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

import java.util.*;

public class BowShoot implements Listener {

    public static ArrayList<Projectile> arrows = new ArrayList<>();
    public static HashMap<Player, Integer> bomb = new HashMap<>();
    public static HashMap<Player, Integer> ghostArrow = new HashMap<>();
    private static BukkitRunnable globalParticleTask = null;


    @EventHandler
    public void onShootBow(EntityShootBowEvent e){
        if (!(e.getEntity() instanceof Player player)) return;

        // Force use of normal arrows - replace spectral arrows with regular arrows
        if (e.getProjectile() instanceof SpectralArrow) {
            e.setCancelled(true);
            
            // Spawn a regular arrow instead
            Location loc = e.getProjectile().getLocation();
            Vector velocity = e.getProjectile().getVelocity();
            Arrow newArrow = player.getWorld().spawnArrow(loc, velocity, (float) velocity.length(), 0.0f);
            newArrow.setShooter(player);
            
            // Copy relevant properties
            if (e.getProjectile() instanceof AbstractArrow oldArrow) {
                newArrow.setPickupStatus(oldArrow.getPickupStatus());
                newArrow.setCritical(oldArrow.isCritical());
                newArrow.setKnockbackStrength(oldArrow.getKnockbackStrength());
            }
            
            // Remove the spectral arrow entity
            e.getProjectile().remove();
            
            // Trigger the rest of the logic with the new arrow
            handleArrowShoot(player, newArrow);
            return;
        }

        if (Items.woolAmount(player) == 0) {
            ItemStack arrow = new ItemStack(Material.ARROW);
            player.getInventory().removeItem(arrow);
            return;
        }

        ItemStack item = new ItemStack(Items.getWoolColor(player));
        item.setAmount(1);
        player.getInventory().removeItem(item);
        check(player);
        
        handleArrowShoot(player, (AbstractArrow) e.getProjectile());
    }
    
    private void handleArrowShoot(Player player, AbstractArrow projectile) {
        if (bomb.containsKey(player)) {
            int amount = bomb.get(player);
            amount++;
            if (amount >= 8) {
                projectile.setMetadata("bomb", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
                bomb.put(player, 0);
            } else
                bomb.put(player, amount);
        }
        
        // Ghost Arrow passive perk - activates every 7 arrows
        if (ghostArrow.containsKey(player)) {
            int amount = ghostArrow.get(player);
            amount++;
            if (amount >= 7) {
                projectile.setMetadata("ghost_arrow", new FixedMetadataValue(Woolbattle.getPlugin(), player.getName()));
                ghostArrow.put(player, 0);
                
                // Make the ghost arrow visually distinct
                projectile.setGlowing(true);
                if (projectile instanceof Arrow arrow) {
                    arrow.setColor(org.bukkit.Color.fromRGB(138, 43, 226)); // Purple/violet color
                }
                
                // Store the initial velocity from bow shoot - this is the most accurate!
                Vector initialVelocity = projectile.getVelocity().clone();
                projectile.setMetadata("ghost_velocity", new FixedMetadataValue(Woolbattle.getPlugin(), initialVelocity));
                
                // Create task to show particle trail
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!projectile.isValid() || projectile.isDead() || projectile.isOnGround()) {
                            this.cancel();
                            return;
                        }
                        
                        // Spawn purple particle trail
                        player.getWorld().spawnParticle(Particle.WITCH, projectile.getLocation(), 1, 0.05, 0.05, 0.05, 0);
                    }
                }.runTaskTimer(Woolbattle.getPlugin(), 0L, 2L);
            } else {
                ghostArrow.put(player, amount);
                // Show arrow counter to player
                player.sendActionBar("§7Ghost Arrow: §d" + amount + "§8/§d7");
            }
        }
        
        arrows.add((Projectile) projectile);
        addParticleEffect((Projectile) projectile);
    }
    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Arrow arrow){
            // Handle ghost arrows - make them pass through ONE surface up to 5 blocks deep
            if(arrow.hasMetadata("ghost_arrow") && e.getHitBlock() != null && !arrow.hasMetadata("ghost_used")){
                // Mark as used so it only passes through one surface
                arrow.setMetadata("ghost_used", new FixedMetadataValue(Woolbattle.getPlugin(), true));
                
                // Get the initial velocity stored when arrow was shot
                Vector velocity;
                if(arrow.hasMetadata("ghost_velocity")){
                    velocity = (Vector) arrow.getMetadata("ghost_velocity").get(0).value();
                    velocity = velocity.clone(); // Clone to avoid modifying the stored version
                } else {
                    // Fallback - shouldn't happen, but just in case
                    velocity = arrow.getLocation().getDirection().multiply(3.0);
                }
                
                Location hitLocation = arrow.getLocation().clone();
                
                // Find the other side of the wall (max 5 blocks deep)
                int MAX_DEPTH = 5;
                Location checkLocation = hitLocation.clone();
                Vector direction = velocity.clone().normalize();
                
                int blocksThrough = 0;
                Location exitLocation = null;
                
                // Scan through blocks in the direction of the arrow
                for(int i = 0; i < MAX_DEPTH * 2; i++){ // Check more frequently (0.5 block steps)
                    checkLocation.add(direction.clone().multiply(0.5));
                    org.bukkit.block.Block block = checkLocation.getBlock();
                    
                    if(!block.getType().isSolid() || block.getType().equals(Material.BARRIER)){
                        // Found air/non-solid - this is the exit point
                        exitLocation = checkLocation.clone();
                        break;
                    }
                    if(i % 2 == 1) blocksThrough++; // Count every full block
                }
                
                if(exitLocation != null && blocksThrough > 0){
                    // Successfully found exit - spawn new arrow on the other side
                    
                    // Spawn particles at entry
                    arrow.getWorld().spawnParticle(Particle.WITCH, hitLocation, 10, 0.2, 0.2, 0.2, 0);
                    arrow.getWorld().spawnParticle(Particle.PORTAL, hitLocation, 15, 0.2, 0.2, 0.2, 0);
                    
                    // Spawn a new arrow at the exit with the same properties
                    Arrow newArrow = arrow.getWorld().spawnArrow(exitLocation, velocity, (float) velocity.length(), 0.0f);
                    newArrow.setShooter(arrow.getShooter());
                    newArrow.setPickupStatus(arrow.getPickupStatus());
                    newArrow.setCritical(arrow.isCritical());
                    newArrow.setGlowing(true);
                    newArrow.setColor(org.bukkit.Color.fromRGB(138, 43, 226));
                    
                    // Mark as already used so it doesn't pass through again
                    newArrow.setMetadata("ghost_arrow", new FixedMetadataValue(Woolbattle.getPlugin(), arrow.getMetadata("ghost_arrow").get(0).value()));
                    newArrow.setMetadata("ghost_used", new FixedMetadataValue(Woolbattle.getPlugin(), true));
                    
                    // Remove old arrow and add new one to tracking
                    arrow.remove();
                    arrows.add(newArrow);
                    
                    // Spawn particles at exit
                    Location finalExit = exitLocation;
                    Bukkit.getScheduler().runTaskLater(Woolbattle.getPlugin(), () -> {
                        newArrow.getWorld().spawnParticle(Particle.WITCH, finalExit, 10, 0.2, 0.2, 0.2, 0);
                        newArrow.getWorld().spawnParticle(Particle.PORTAL, finalExit, 15, 0.2, 0.2, 0.2, 0);
                    }, 1L);
                    
                    return;
                } else {
                    // Wall too thick or no exit found - arrow stops normally
                    arrow.removeMetadata("ghost_arrow", Woolbattle.getPlugin());
                }
            }
            
            arrows.remove(arrow);
            
            // Ghost arrows stay longer so you can see them behind walls
            if(arrow.hasMetadata("ghost_arrow")){
                if(e.getHitBlock() != null){
                    // Ghost arrows despawn quickly when stuck in blocks, like regular arrows
                    Bukkit.getScheduler().runTaskLater(Woolbattle.getPlugin(), () -> arrow.remove(), 10);
                }
            } else {
                // Regular arrows vanish quickly when hitting blocks (10 ticks = 0.5 seconds)
                if(e.getHitBlock() != null){
                    Bukkit.getScheduler().runTaskLater(Woolbattle.getPlugin(), () -> arrow.remove(), 10);
                }
            }
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

        Collection<Entity> list = location.getNearbyEntities(5,5,5);
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

    public static void addParticleEffect(final Projectile entity){
        // Start global particle task if not running
        if(globalParticleTask == null || globalParticleTask.isCancelled()){
            startGlobalParticleTask();
        }
    }

    private static void startGlobalParticleTask(){
        globalParticleTask = new BukkitRunnable(){
            @Override
            public void run() {
                // Clean up invalid arrows and stop task if no arrows remain
                arrows.removeIf(arrow -> !arrow.isValid() || arrow.isDead());

                if(arrows.isEmpty()){
                    this.cancel();
                    globalParticleTask = null;
                    return;
                }

                // Display particles for all active arrows
                for(Projectile arrow : arrows){
                    if(arrow.isValid() && !arrow.isDead()){
                        for(Player player : Bukkit.getOnlinePlayers()){
                            if(player.getWorld() == arrow.getWorld()){
                                player.spawnParticle(Particle.GLOW, arrow.getLocation(), 2);
                            }
                        }
                    }
                }
            }
        };
        globalParticleTask.runTaskTimer(Woolbattle.getPlugin(), 0, 1);
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), () -> {
            if (Items.amount(player, Items.getWoolColor(player)) == 0) {
                ItemStack arrow = new ItemStack(Material.ARROW);
                player.getInventory().removeItem(arrow);
            }
        }, 20);
    }

    public static void enable() {
        bomb.clear();
        ghostArrow.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            codes.Elix.Woolbattle.game.HelpClasses.Team team = CustomPlayer.getCustomPlayer(player).getTeam();
            if (team == null || Objects.equals(team.getName(), "spectator")) continue;
            if (Objects.equals(Items.perks.get(player).getpassivePerk(), "exploding_arrow"))
                bomb.put(player, 0);
            if (Objects.equals(Items.perks.get(player).getpassivePerk(), "ghost_arrow"))
                ghostArrow.put(player, 0);
        }
    }

    public static void cleanup() {
        arrows.clear();
        bomb.clear();
        ghostArrow.clear();
        if(globalParticleTask != null && !globalParticleTask.isCancelled()){
            globalParticleTask.cancel();
            globalParticleTask = null;
        }
    }



}