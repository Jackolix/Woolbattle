package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;

import java.util.*;

public class portal implements Listener {
    
    // Track portal state for each player
    private static final Map<Player, PortalState> portalStates = new HashMap<>();
    
    // Track portal pairs: Location -> Portal owner and pair location
    private static final Map<Location, PortalData> portalLocations = new HashMap<>();
    
    // Track portal display entities for cleanup
    private static final Map<Location, List<BlockDisplay>> portalDisplays = new HashMap<>();
    
    // Track particle tasks
    private static final Map<Location, BukkitTask> particleTasks = new HashMap<>();
    
    // Track player portal cooldowns (1s cooldown after using portal)
    private static final Map<Player, Long> playerPortalCooldowns = new HashMap<>();
    
    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("portal");
    }
    
    @EventHandler
    public void onPortalInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.ENDER_EYE) return;
        
        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();
        
        event.setCancelled(true);
        
        PortalState state = portalStates.get(player);
        
        // First activation - place first portal
        if (state == null || state.firstPortalLocation == null) {
            if (!Woolbattle.debug) {
                if (!Items.cost(player, settings.getCost())) return;
            }
            
            Location firstPortal = player.getLocation().getBlock().getLocation();
            
            // Check if location is valid
            if (!canPlacePortal(firstPortal)) {
                player.sendMessage("§cCannot place portal here!");
                return;
            }
            
            // Create first portal
            spawnPortal(firstPortal, player);
            
            // Initialize portal state
            PortalState newState = new PortalState();
            newState.firstPortalLocation = firstPortal;
            newState.owner = player;
            
            // Start 5 second timeout for second portal
            newState.timeoutTask = Bukkit.getScheduler().scheduleSyncDelayedTask(
                Woolbattle.getPlugin(), 
                () -> {
                    if (portalStates.containsKey(player)) {
                        PortalState currentState = portalStates.get(player);
                        if (currentState.secondPortalLocation == null) {
                            // Timeout - remove first portal without cooldown
                            removePortal(currentState.firstPortalLocation);
                            portalStates.remove(player);
                            player.sendMessage("§cPortal placement timeout!");
                        }
                    }
                }, 
                100 // 5 seconds = 100 ticks
            );
            
            portalStates.put(player, newState);
            player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1.0f, 1.2f);
            player.sendMessage("§aFirst portal placed! Place second portal within 5 seconds.");
            
        } 
        // Second activation - place second portal
        else if (state.secondPortalLocation == null) {
            Location secondPortal = player.getLocation().getBlock().getLocation();
            
            // Check if location is valid
            if (!canPlacePortal(secondPortal)) {
                player.sendMessage("§cCannot place portal here!");
                return;
            }
            
            // Check if too close to first portal
            if (secondPortal.distance(state.firstPortalLocation) < 3) {
                player.sendMessage("§cPortals too close together!");
                return;
            }
            
            // Cancel timeout task
            if (state.timeoutTask != null) {
                Bukkit.getScheduler().cancelTask(state.timeoutTask);
            }
            
            // Create second portal
            spawnPortal(secondPortal, player);
            state.secondPortalLocation = secondPortal;
            
            // Link the portals
            PortalData firstData = new PortalData(player, secondPortal);
            PortalData secondData = new PortalData(player, state.firstPortalLocation);
            portalLocations.put(state.firstPortalLocation, firstData);
            portalLocations.put(secondPortal, secondData);
            
            player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1.0f, 1.5f);
            player.sendMessage("§aPortal pair created!");
            
            // Apply cooldown
            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
                ? settings.getCooldownRecharger()
                : settings.getCooldown();
            
            int slot = player.getInventory().getHeldItemSlot();
            Items.visualCooldown(player, cooldown, Material.ENDER_EYE, slot, "§5Portal");
            
            // Schedule portal cleanup when cooldown expires
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                Woolbattle.getPlugin(),
                () -> {
                    cleanupPlayerPortals(player);
                },
                cooldown * 20
            );
        }
    }
    
    /**
     * Checks if a portal can be placed at the given location
     */
    private boolean canPlacePortal(Location location) {
        // Check if there's enough space for 2x2x2 portal
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    Block block = location.clone().add(x, y, z).getBlock();
                    if (block.getType() != Material.AIR) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Spawns a 2x2x2 portal made of block display entities
     */
    private void spawnPortal(Location location, Player owner) {
        List<BlockDisplay> displays = new ArrayList<>();
        
        // Use portal block for the display
        BlockData portalBlock = Material.NETHER_PORTAL.createBlockData();
        
        // Create 2x2x2 frame of block displays
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    Location blockLoc = location.clone().add(x + 0.5, y, z + 0.5);
                    
                    BlockDisplay display = location.getWorld().spawn(blockLoc, BlockDisplay.class, (entity) -> {
                        entity.setBlock(portalBlock);
                        
                        // Set transformation for proper size and positioning
                        Transformation transformation = entity.getTransformation();
                        transformation.getScale().set(0.98f, 0.98f, 0.98f); // Slightly smaller for gaps
                        transformation.getTranslation().set(-0.49f, 0f, -0.49f); // Center the display
                        entity.setTransformation(transformation);
                        
                        // Make it glow and visible
                        entity.setBrightness(new Display.Brightness(15, 15));
                        entity.setGlowing(true);
                        entity.setViewRange(128.0f);
                        
                        // Add interpolation for smooth appearance
                        entity.setInterpolationDuration(10);
                        entity.setInterpolationDelay(0);
                    });
                    
                    displays.add(display);
                }
            }
        }
        
        portalDisplays.put(location, displays);
        
        // Start particle effect
        startPortalParticles(location);
    }
    
    /**
     * Starts particle effects around the portal
     */
    private void startPortalParticles(Location location) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Woolbattle.getPlugin(), () -> {
            // Spawn portal particles in the center of the 2x2x2 area
            Location center = location.clone().add(1, 1, 1);
            location.getWorld().spawnParticle(Particle.PORTAL, center, 10, 0.5, 0.5, 0.5, 0.5);
            location.getWorld().spawnParticle(Particle.REVERSE_PORTAL, center, 5, 0.5, 0.5, 0.5, 0.1);
        }, 0L, 5L); // Every 5 ticks
        
        particleTasks.put(location, task);
    }
    
    /**
     * Removes a portal from the world
     */
    private void removePortal(Location location) {
        if (location == null) return;
        
        // Remove display entities
        List<BlockDisplay> displays = portalDisplays.get(location);
        if (displays != null) {
            for (BlockDisplay display : displays) {
                if (display != null && display.isValid()) {
                    display.remove();
                }
            }
            portalDisplays.remove(location);
        }
        
        // Cancel particle task
        BukkitTask particleTask = particleTasks.get(location);
        if (particleTask != null) {
            particleTask.cancel();
            particleTasks.remove(location);
        }
        
        portalLocations.remove(location);
    }
    
    /**
     * Cleans up all portals placed by a player
     */
    private void cleanupPlayerPortals(Player player) {
        PortalState state = portalStates.get(player);
        if (state != null) {
            removePortal(state.firstPortalLocation);
            removePortal(state.secondPortalLocation);
            portalStates.remove(player);
        }
    }
    
    /**
     * Handles player movement to detect portal entry
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        
        // Check if player is on portal cooldown
        if (playerPortalCooldowns.containsKey(player)) {
            long cooldownEnd = playerPortalCooldowns.get(player);
            if (System.currentTimeMillis() < cooldownEnd) {
                return; // Still on cooldown
            } else {
                playerPortalCooldowns.remove(player);
            }
        }
        
        // Check if player is inside a portal (check all 8 blocks of 2x2x2 portal)
        Location portalBase = findPortalAtLocation(playerLoc);
        if (portalBase == null) return;
        
        PortalData portalData = portalLocations.get(portalBase);
        if (portalData == null) return;
        
        // Check if player or teammate
        Player portalOwner = portalData.owner;
        if (!canUsePortal(player, portalOwner)) return;
        
        // Get destination portal
        Location destination = portalData.pairedLocation;
        if (destination == null) return;
        
        // Teleport to center of destination portal
        Location teleportLoc = destination.clone().add(1, 0, 1); // Center of 2x2x2
        teleportLoc.setYaw(player.getLocation().getYaw());
        teleportLoc.setPitch(player.getLocation().getPitch());
        
        // Spawn particles at entry point
        Location entryCenter = portalBase.clone().add(1, 1, 1);
        player.getWorld().spawnParticle(Particle.PORTAL, entryCenter, 50, 0.5, 0.5, 0.5, 1.0);
        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, entryCenter, 20, 0.5, 0.5, 0.5, 0.5);
        player.playSound(entryCenter, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        player.teleport(teleportLoc);
        
        // Spawn particles at exit point
        Location exitCenter = destination.clone().add(1, 1, 1);
        player.getWorld().spawnParticle(Particle.PORTAL, exitCenter, 50, 0.5, 0.5, 0.5, 1.0);
        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, exitCenter, 20, 0.5, 0.5, 0.5, 0.5);
        player.playSound(exitCenter, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
        
        // Apply 1 second cooldown to this player
        playerPortalCooldowns.put(player, System.currentTimeMillis() + 1000);
    }
    
    /**
     * Finds if the given location is inside any portal
     * Returns the base location of the portal if found
     */
    private Location findPortalAtLocation(Location playerLoc) {
        // Check all registered portals to see if player is inside any of them
        for (Location portalBase : portalLocations.keySet()) {
            if (isInsidePortal(playerLoc, portalBase)) {
                return portalBase;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if a location is inside the 2x2x2 portal volume
     */
    private boolean isInsidePortal(Location playerLoc, Location portalBase) {
        double minX = portalBase.getX();
        double maxX = portalBase.getX() + 2;
        double minY = portalBase.getY();
        double maxY = portalBase.getY() + 2;
        double minZ = portalBase.getZ();
        double maxZ = portalBase.getZ() + 2;
        
        double px = playerLoc.getX();
        double py = playerLoc.getY();
        double pz = playerLoc.getZ();
        
        return px >= minX && px <= maxX &&
               py >= minY && py <= maxY &&
               pz >= minZ && pz <= maxZ;
    }
    
    /**
     * Checks if a player can use the portal (owner or teammate)
     */
    private boolean canUsePortal(Player player, Player owner) {
        if (player.equals(owner)) return true;
        
        CustomPlayer playerCustom = CustomPlayer.getCustomPlayer(player);
        CustomPlayer ownerCustom = CustomPlayer.getCustomPlayer(owner);
        
        if (playerCustom != null && ownerCustom != null) {
            if (playerCustom.getTeam() != null && ownerCustom.getTeam() != null) {
                return playerCustom.getTeam().equals(ownerCustom.getTeam());
            }
        }
        
        return false;
    }
    
    /**
     * Portal state for tracking placement progress
     */
    private static class PortalState {
        Location firstPortalLocation;
        Location secondPortalLocation;
        Player owner;
        Integer timeoutTask;
    }
    
    /**
     * Portal data for tracking linked portals
     */
    private static class PortalData {
        Player owner;
        Location pairedLocation;
        
        PortalData(Player owner, Location pairedLocation) {
            this.owner = owner;
            this.pairedLocation = pairedLocation;
        }
    }
    
    /**
     * Clean up when plugin disables
     */
    public static void disable() {
        // Remove all portal displays
        for (List<BlockDisplay> displays : portalDisplays.values()) {
            for (BlockDisplay display : displays) {
                if (display != null && display.isValid()) {
                    display.remove();
                }
            }
        }
        
        // Cancel all particle tasks
        for (BukkitTask task : particleTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        
        portalStates.clear();
        portalLocations.clear();
        playerPortalCooldowns.clear();
        portalDisplays.clear();
        particleTasks.clear();
    }
    
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new portal(), Woolbattle.getPlugin());
    }
}
