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
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class mine implements Listener {
    
    // Track placed mines: Location -> Player who placed it
    private static final Map<Location, Player> placedMines = new HashMap<>();
    
    // Track multiple mines per player
    private static final Map<Player, Set<Location>> playerMines = new HashMap<>();
    
    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("mine");
    }

    @EventHandler
    public void onMinePlace(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.STONE_PRESSURE_PLATE) return;
        
        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();
        
        // Left-click: Instant explosion (only affects enemies)
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            
            if (Woolbattle.debug) {
                instantExplosion(player);
                return;
            }
            
            if (!Items.cost(player, settings.getCost())) {
                return;
            }
            
            instantExplosion(player);
            
            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
                ? settings.getCooldownRecharger()
                : settings.getCooldown();
            int slot = player.getInventory().getHeldItemSlot();
            Items.visualCooldown(player, cooldown, Material.STONE_PRESSURE_PLATE, slot, "§cMine");
            return;
        }
        
        // Right-click: Place mine
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock == null) return;
            
            // Get the location above the clicked block
            Location mineLocation = clickedBlock.getLocation().add(0, 1, 0);
            
            // Check if location is valid (not already occupied)
            if (mineLocation.getBlock().getType() != Material.AIR) {
                return;
            }
            
            if (Woolbattle.debug) {
                placeMine(player, mineLocation);
                return;
            }
            
            // Check max mines per player
            int maxMines = settings.getMaxMinesPerPlayer();
            Set<Location> mines = playerMines.getOrDefault(player, new HashSet<>());
            if (mines.size() >= maxMines) {
                // Remove oldest mine
                Location oldestMine = mines.iterator().next();
                removeMine(oldestMine);
            }
            
            if (!Items.cost(player, settings.getCost())) {
                return;
            }
            
            placeMine(player, mineLocation);
            
            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
                ? settings.getCooldownRecharger()
                : settings.getCooldown();
            int slot = player.getInventory().getHeldItemSlot();
            Items.visualCooldown(player, cooldown, Material.STONE_PRESSURE_PLATE, slot, "§cMine");
        }
    }
    
    /**
     * Places a mine at the specified location
     */
    private void placeMine(Player player, Location location) {
        PerkConfig.PerkSettings settings = getSettings();
        
        // Place the pressure plate
        location.getBlock().setType(Material.STONE_PRESSURE_PLATE);
        
        // Track the mine
        placedMines.put(location, player);
        playerMines.computeIfAbsent(player, k -> new HashSet<>()).add(location);
        
        // Play placement sound
        player.playSound(location, Sound.BLOCK_STONE_PLACE, 1.0f, 0.8f);
        
        // Auto-remove mine after duration
        int durationTicks = settings.getMineDurationSeconds() * 20;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), () -> {
            removeMine(location);
        }, durationTicks);
    }
    
    /**
     * Removes a mine from the world
     */
    private void removeMine(Location location) {
        if (!placedMines.containsKey(location)) return;
        
        Player owner = placedMines.get(location);
        
        // Remove block
        if (location.getBlock().getType() == Material.STONE_PRESSURE_PLATE) {
            location.getBlock().setType(Material.AIR);
        }
        
        // Remove from tracking
        placedMines.remove(location);
        if (playerMines.containsKey(owner)) {
            playerMines.get(owner).remove(location);
            if (playerMines.get(owner).isEmpty()) {
                playerMines.remove(owner);
            }
        }
    }
    
    /**
     * Handles player movement to detect mine triggers
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        
        // Check if player is standing on a mine
        Block blockBelow = playerLoc.subtract(0, 1, 0).getBlock();
        if (blockBelow.getType() != Material.STONE_PRESSURE_PLATE) return;
        
        Location mineLocation = blockBelow.getLocation();
        if (!placedMines.containsKey(mineLocation)) return;
        
        // Trigger the mine
        Player owner = placedMines.get(mineLocation);
        triggerMine(mineLocation, owner, false);
    }
    
    /**
     * Triggers a mine explosion
     * @param location The mine location
     * @param owner The player who placed the mine
     * @param onlyEnemies If true, only damage enemies
     */
    private void triggerMine(Location location, Player owner, boolean onlyEnemies) {
        PerkConfig.PerkSettings settings = getSettings();
        
        // Remove the mine first
        removeMine(location);
        
        // Play activation sound
        location.getWorld().playSound(location, Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
        
        // Spawn TNT with fuse
        TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class);
        tnt.setFuseTicks(settings.getMineFuseTicks());
        
        // Get nearby entities
        @NotNull Collection<Entity> nearbyEntities = location.getNearbyEntities(
            settings.getMineExplosionRadius(), 
            settings.getMineExplosionRadius(), 
            settings.getMineExplosionRadius()
        );
        
        ArrayList<Player> affectedPlayers = new ArrayList<>();
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player targetPlayer) {
                // Skip if onlyEnemies is true and target is the owner
                if (onlyEnemies && targetPlayer.equals(owner)) {
                    continue;
                }
                
                // Skip if onlyEnemies is true and target is on same team
                if (onlyEnemies) {
                    CustomPlayer ownerCustom = CustomPlayer.getCustomPlayer(owner);
                    CustomPlayer targetCustom = CustomPlayer.getCustomPlayer(targetPlayer);
                    if (ownerCustom != null && targetCustom != null 
                        && ownerCustom.getTeam() != null && targetCustom.getTeam() != null
                        && ownerCustom.getTeam().equals(targetCustom.getTeam())) {
                        continue;
                    }
                }
                
                affectedPlayers.add(targetPlayer);
            }
        }
        
        // Apply knockback after delay
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), () -> {
            for (Player target : affectedPlayers) {
                // Mark the player as hit by owner
                if (owner != null && !target.equals(owner)) {
                    CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(target);
                    if (customPlayer != null) {
                        customPlayer.addHitted(owner);
                    }
                }
                
                // Calculate knockback
                int xPos = location.getBlockX() - target.getLocation().getBlockX();
                int zPos = location.getBlockZ() - target.getLocation().getBlockZ();
                double redux = 0.25;
                Vector velocity = new Vector(
                    -(xPos / redux), 
                    settings.getMineKnockbackYVelocity(), 
                    -(zPos / redux)
                );
                target.setVelocity(velocity.normalize().multiply(settings.getMineKnockbackMultiplier()));
            }
        }, settings.getMineExplosionDelayTicks());
    }
    
    /**
     * Instant explosion around the player (only affects enemies)
     */
    private void instantExplosion(Player player) {
        PerkConfig.PerkSettings settings = getSettings();
        Location location = player.getLocation();
        
        // Play sound
        player.playSound(location, Sound.ENTITY_TNT_PRIMED, 1.0f, 1.2f);
        
        // Spawn TNT
        TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class);
        tnt.setFuseTicks(settings.getMineFuseTicks());
        
        // Get nearby entities
        @NotNull Collection<Entity> nearbyEntities = location.getNearbyEntities(
            settings.getMineExplosionRadius(), 
            settings.getMineExplosionRadius(), 
            settings.getMineExplosionRadius()
        );
        
        ArrayList<Player> affectedPlayers = new ArrayList<>();
        CustomPlayer playerCustom = CustomPlayer.getCustomPlayer(player);
        
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player target) {
                // Skip the player themselves
                if (target.equals(player)) continue;
                
                // Only affect enemies
                CustomPlayer targetCustom = CustomPlayer.getCustomPlayer(target);
                if (playerCustom != null && targetCustom != null 
                    && playerCustom.getTeam() != null && targetCustom.getTeam() != null
                    && playerCustom.getTeam().equals(targetCustom.getTeam())) {
                    continue;
                }
                
                affectedPlayers.add(target);
            }
        }
        
        // Apply knockback after delay
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), () -> {
            for (Player target : affectedPlayers) {
                // Mark the player as hit
                CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(target);
                if (customPlayer != null) {
                    customPlayer.addHitted(player);
                }
                
                // Calculate knockback
                int xPos = location.getBlockX() - target.getLocation().getBlockX();
                int zPos = location.getBlockZ() - target.getLocation().getBlockZ();
                double redux = 0.25;
                Vector velocity = new Vector(
                    -(xPos / redux), 
                    settings.getMineKnockbackYVelocity(), 
                    -(zPos / redux)
                );
                target.setVelocity(velocity.normalize().multiply(settings.getMineKnockbackMultiplier()));
            }
        }, settings.getMineExplosionDelayTicks());
    }
    
    /**
     * Clean up mines when plugin disables
     */
    public static void disable() {
        // Remove all placed mines
        for (Location location : new HashSet<>(placedMines.keySet())) {
            if (location.getBlock().getType() == Material.STONE_PRESSURE_PLATE) {
                location.getBlock().setType(Material.AIR);
            }
        }
        placedMines.clear();
        playerMines.clear();
    }
    
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new mine(), Woolbattle.getPlugin());
    }
}
