package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class slime implements Listener {
    
    private static final Map<Location, Long> bounceZones = new HashMap<>();
    private static final Map<Player, Long> lastBounceTimes = new HashMap<>();
    private static final Map<Player, Boolean> wasOnGround = new HashMap<>();
    private static final long BOUNCE_COOLDOWN = 2000;
    private static slime instance;

    private static PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("slime");
    }

    @EventHandler
    public void onSlimeInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.SLIME_BALL) return;

        PerkConfig.PerkSettings settings = getSettings();
        
        if (!Woolbattle.debug && !Items.cost(player, settings.getCost())) return;
        
        int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
            ? settings.getCooldownRecharger()
            : settings.getCooldown();
            
        int slot = player.getInventory().getHeldItemSlot();
        placeSlimePlatform(player);
        
        if (!Woolbattle.debug) {
            Items.visualCooldown(player, cooldown, Material.SLIME_BALL, slot, "§aSlime");
        }
    }

    private void placeSlimePlatform(Player player) {
        PerkConfig.PerkSettings settings = getSettings();
        Location location = player.getLocation();
        Material material = Items.getWoolColor(player);
        
        // Launch player upward with height-dependent velocity
        double upwardVelocity = calculateVelocity(player, settings);
        player.setVelocity(new Vector(0, upwardVelocity, 0));
        player.playSound(location, Sound.BLOCK_SLIME_BLOCK_FALL, 1F, 1.2F);
        
        // Place platform and register bounce zones
        List<Location> platformLocations = placePlatformBlocks(location, material);
        long expirationTime = System.currentTimeMillis() + (settings.getSlimePlatformDuration() * 1000L);
        
        // Register bounce zones (2 blocks above each slime block)
        List<Location> bounceLocations = new ArrayList<>();
        for (Location slimeLoc : platformLocations) {
            bounceLocations.add(slimeLoc.clone().add(0, 1, 0).getBlock().getLocation());
            bounceLocations.add(slimeLoc.clone().add(0, 2, 0).getBlock().getLocation());
        }
        bounceLocations.forEach(loc -> bounceZones.put(loc, expirationTime));
        
        // Schedule platform removal
        int platformDuration = settings.getSlimePlatformDuration() * 20;
        Bukkit.getScheduler().runTaskLater(Woolbattle.getPlugin(), () -> {
            removePlatformBlocks(location);
            bounceLocations.forEach(bounceZones::remove);
        }, platformDuration);
    }

    /**
     * Calculate the upward velocity based on player's Y position.
     * The lower the player is, the stronger the boost.
     */
    private static double calculateVelocity(Player player, PerkConfig.PerkSettings settings) {
        double currentY = player.getLocation().getY();
        double minVelocity = settings.getSlimeMinVelocity();
        double maxVelocity = settings.getSlimeMaxVelocity();
        int referenceY = settings.getSlimeReferenceY();
        
        // If player is at or below reference Y, give max velocity
        if (currentY <= referenceY) {
            return maxVelocity;
        }
        
        // Calculate velocity that decreases as player gets higher
        double heightDifference = currentY - referenceY;
        double velocityRange = maxVelocity - minVelocity;
        double velocityReduction = (heightDifference / 10.0) * 0.1 * velocityRange;
        double calculatedVelocity = maxVelocity - velocityReduction;
        
        // Clamp between min and max velocity
        return Math.max(minVelocity, Math.min(maxVelocity, calculatedVelocity));
    }

    /**
     * Place a 3x3 platform of slime blocks underneath the player
     * @return List of slime block locations for tracking
     */
    private List<Location> placePlatformBlocks(Location location, Material material) {
        List<Location> slimeLocations = new ArrayList<>();
        Location slimeLoc = location.clone().add(0, -2, 0);
        Location woolLoc = location.clone().add(0, -3, 0);
        
        // Place wool base layer and slime platform
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block woolBlock = woolLoc.clone().add(x, 0, z).getBlock();
                if (woolBlock.getType() == Material.AIR) {
                    woolBlock.setType(material);
                }
                
                Block slimeBlock = slimeLoc.clone().add(x, 0, z).getBlock();
                if (slimeBlock.getType() == Material.AIR) {
                    slimeBlock.setType(Material.SLIME_BLOCK);
                    slimeLocations.add(slimeBlock.getLocation());
                }
            }
        }
        
        return slimeLocations;
    }

    /**
     * Remove the entire platform (slime + wool) after duration expires
     */
    private void removePlatformBlocks(Location location) {
        Location slimeLoc = location.clone().add(0, -2, 0);
        Location woolLoc = location.clone().add(0, -3, 0);
        
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block slimeBlock = slimeLoc.clone().add(x, 0, z).getBlock();
                if (slimeBlock.getType() == Material.SLIME_BLOCK) {
                    slimeBlock.setType(Material.AIR);
                }
                
                Block woolBlock = woolLoc.clone().add(x, 0, z).getBlock();
                if (woolBlock.getType().name().contains("WOOL")) {
                    woolBlock.setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        boolean currentlyOnGround = player.isOnGround();
        Boolean previouslyOnGround = wasOnGround.put(player, currentlyOnGround);
        
        // Only trigger when player lands (air → ground transition)
        if (previouslyOnGround == null || previouslyOnGround || !currentlyOnGround) {
            return;
        }
        
        // Check if player is in a registered bounce zone
        Location playerBlockLoc = player.getLocation().getBlock().getLocation();
        Long expirationTime = bounceZones.get(playerBlockLoc);
        if (expirationTime == null || System.currentTimeMillis() > expirationTime) {
            return;
        }
        
        // Check bounce cooldown
        Long lastBounce = lastBounceTimes.get(player);
        if (lastBounce != null && System.currentTimeMillis() - lastBounce < BOUNCE_COOLDOWN) {
            return;
        }
        
        // Apply velocity boost
        PerkConfig.PerkSettings settings = getSettings();
        double upwardVelocity = calculateVelocity(player, settings);
        Vector velocity = player.getVelocity();
        velocity.setY(upwardVelocity);
        player.setVelocity(velocity);
        
        player.playSound(player.getLocation(), Sound.BLOCK_SLIME_BLOCK_FALL, 1F, 1.2F);
        lastBounceTimes.put(player, System.currentTimeMillis());
    }
    
    public static void enable() {
        if (instance == null) {
            instance = new slime();
            Bukkit.getPluginManager().registerEvents(instance, Woolbattle.getPlugin());
        }
    }
    
    public static void disable() {
        bounceZones.clear();
        lastBounceTimes.clear();
        wasOnGround.clear();
        instance = null;
    }
}
