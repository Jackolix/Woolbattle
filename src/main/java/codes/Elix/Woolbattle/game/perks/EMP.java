// Minigun Passive Perk Implementation
// Activated after every 4th arrow, automatically fires arrows with reduced velocity and knockback

package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

public class EMP implements Listener {
    // Arrow counter for triggering minigun (every 4 arrows)
    public static HashMap<Player, Integer> arrowCounter = new HashMap<>();
    
    // Track if minigun is currently active
    public static HashMap<Player, Boolean> minigunActive = new HashMap<>();
    
    // Track the minigun task for each player
    public static HashMap<Player, BukkitRunnable> minigunTasks = new HashMap<>();
    
    // Store the 4th arrow's velocity for minigun arrows
    public static HashMap<Player, Vector> triggerArrowVelocity = new HashMap<>();
    
    /**
     * Get Minigun settings from config
     */
    private static PerkConfig.PassivePerkSettings getSettings() {
        return PerkConfig.getInstance().getPassivePerkSettings("minigun");
    }

    /**
     * Handle arrow shooting - increment counter and check if minigun should trigger
     */
    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        // Check if player has minigun passive perk
        String passivePerk = Items.perks.get(player) != null ? Items.perks.get(player).getpassivePerk() : null;
        if (!Objects.equals(passivePerk, "emp")) return;
        
        PerkConfig.PassivePerkSettings settings = getSettings();
        
        // Increment arrow counter for this player
        int count = arrowCounter.getOrDefault(player, 0);
        count++;
        
        // Check if we've reached the trigger count
        if (count >= settings.getTriggerArrows()) {
            // Capture the 4th arrow's velocity for minigun arrows
            AbstractArrow arrow = (AbstractArrow) event.getProjectile();
            Vector triggerVelocity = arrow.getVelocity().clone();
            triggerArrowVelocity.put(player, triggerVelocity);
            
            // Mark the arrow as minigun-triggered
            arrow.setMetadata("emp", new FixedMetadataValue(Woolbattle.getPlugin(), true));
            
            // Start the minigun effect
            startMinigun(player);
            
            // Reset counter
            count = 0;
        }
        
        arrowCounter.put(player, count);
        
        // Show counter to player
        if (count > 0) {
            player.sendActionBar("§6EMP: §e" + count + "§8/§e" + settings.getTriggerArrows());
        }
    }

    /**
     * Track when player switches items to detect bow being put down
     */
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        
        // Check if player has minigun
        String passivePerk = Items.perks.get(player) != null ? Items.perks.get(player).getpassivePerk() : null;
        if (!Objects.equals(passivePerk, "minigun")) return;
        
        // If minigun is active and player is no longer holding bow, cancel it
        if (minigunActive.getOrDefault(player, false)) {
            if (!isBowInHand(player)) {
                cancelMinigun(player);
                arrowCounter.put(player, 0); // Reset counter when bow is released
                player.sendActionBar("§cMinigun cancelled - bow released");
            }
        }
    }

    /**
     * Check if player is currently holding a bow
     */
    private boolean isBowInHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        return item.getType() == Material.BOW;
    }

    /**
     * Start the minigun effect - automatically fire arrows
     */
    private void startMinigun(Player player) {
        // Cancel any existing minigun for this player
        if (minigunTasks.containsKey(player)) {
            minigunTasks.get(player).cancel();
        }
        
        minigunActive.put(player, true);
        
        PerkConfig.PassivePerkSettings settings = getSettings();
        
        // Create task to fire arrows
        BukkitRunnable task = new BukkitRunnable() {
            int arrowsFired = 0;
            
            @Override
            public void run() {
                // Check if bow is still in hand - if not, cancel
                if (!isBowInHand(player)) {
                    cancelMinigun(player);
                    this.cancel();
                    return;
                }
                
                // Fire one arrow
                if (arrowsFired < settings.getArrowsToFire()) {
                    fireMinigunarrow(player);
                    arrowsFired++;
                    
                    // Particle effect at player location
                    player.getWorld().spawnParticle(Particle.FLAME, player.getEyeLocation(), 3, 0.2, 0.2, 0.2, 0.05);
                } else {
                    // Minigun effect complete
                    minigunActive.put(player, false);
                    minigunTasks.remove(player);
                    this.cancel();
                }
            }
        };
        
        minigunTasks.put(player, task);
        task.runTaskTimer(Woolbattle.getPlugin(), 0, settings.getFireIntervalTicks());
    }

    /**
     * Fire a single minigun arrow
     */
    private void fireMinigunarrow(Player player) {
        PerkConfig.PassivePerkSettings settings = getSettings();
        
        // Get the trigger arrow's velocity and reduce it by configured amount
        Vector triggerVelocity = triggerArrowVelocity.getOrDefault(player, player.getEyeLocation().getDirection().normalize());
        Vector minigunVelocity = triggerVelocity.clone().multiply(1.0 - settings.getVelocityReduction());
        
        // Spawn arrow at player's eye location
        Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(), minigunVelocity, (float) minigunVelocity.length(), 0.0f);
        arrow.setShooter(player);
        
        // Make arrow non-pickupable
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        
        // Mark as minigun arrow to reduce knockback
        arrow.setMetadata("minigun_arrow", new FixedMetadataValue(Woolbattle.getPlugin(), true));
        
        // Set knockback strength with reduction
        arrow.setKnockbackStrength(Math.max(0, (int) (arrow.getKnockbackStrength() * (1.0 - settings.getKnockbackReduction()))));
    }

    /**
     * Cancel the minigun effect for a player
     */
    public static void cancelMinigun(Player player) {
        if (minigunTasks.containsKey(player)) {
            BukkitRunnable task = minigunTasks.get(player);
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
            minigunTasks.remove(player);
        }
        minigunActive.put(player, false);
    }

    /**
     * Cleanup when game ends or player leaves
     */
    public static void cleanup() {
        for (BukkitRunnable task : minigunTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        minigunTasks.clear();
        arrowCounter.clear();
        minigunActive.clear();
        triggerArrowVelocity.clear();
    }

    /**
     * Enable minigun for all players with this perk at game start
     */
    public static void enable() {
        arrowCounter.clear();
        minigunActive.clear();
        triggerArrowVelocity.clear();
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            String passivePerk = Items.perks.get(player) != null ? Items.perks.get(player).getpassivePerk() : null;
            if (Objects.equals(passivePerk, "minigun")) {
                arrowCounter.put(player, 0);
                minigunActive.put(player, false);
            }
        }
    }

}
