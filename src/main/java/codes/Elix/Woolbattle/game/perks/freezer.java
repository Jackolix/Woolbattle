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
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class freezer implements Listener {
    
    // Track frozen players
    private static final Set<UUID> frozenPlayers = new HashSet<>();
    
    // Track frozen blocks with their original materials
    private static final Map<Block, Material> frozenBlocks = new HashMap<>();
    private static final Map<Block, BukkitRunnable> frozenBlockTasks = new HashMap<>();
    
    // Track player locations when frozen (for no-gravity effect)
    private static final Map<UUID, Location> frozenPlayerLocations = new HashMap<>();

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("freezer");
    }

    @EventHandler
    public void onFreezerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.PACKED_ICE) return;
        
        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();
        Action action = event.getAction();
        
        // Right-click: Throw freezer projectile
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            // Check if perk is already on cooldown
            if (Items.isPerkOnCooldown(player, "§3Freezer")) return;

            if (!Woolbattle.debug)
                if (!Items.cost(player, settings.getCost())) return;

            Vector direction = player.getLocation().getDirection();
            Vector velocity = direction.multiply(settings.getProjectileVelocity());
            Projectile snowball = player.launchProjectile(Snowball.class, velocity);
            snowball.setMetadata("Freezer", new FixedMetadataValue(Woolbattle.getPlugin(), player.getUniqueId().toString()));

            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger") 
                ? settings.getCooldownRecharger() 
                : settings.getCooldown();
            
            int slot = player.getInventory().getHeldItemSlot();
            if (!Woolbattle.debug)
                Items.visualCooldown(player, cooldown, Material.PACKED_ICE, slot, "§3Freezer");
        }
        // Left-click: Self-freeze
        else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true); // Prevent block breaking

            // Check if perk is already on cooldown
            if (Items.isPerkOnCooldown(player, "§3Freezer")) return;

            if (!Woolbattle.debug)
                if (!Items.cost(player, settings.getCost())) return;
            
            // Apply freeze effect to self
            freezePlayer(player, null, settings);
            
            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger") 
                ? settings.getCooldownRecharger() 
                : settings.getCooldown();
            
            int slot = player.getInventory().getHeldItemSlot();
            if (!Woolbattle.debug)
                Items.visualCooldown(player, cooldown, Material.PACKED_ICE, slot, "§3Freezer");
        }
    }

    @EventHandler
    public void onFreezerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Snowball snowball)) return;
        if (!(e.getEntity() instanceof Player player)) return;

        if (!(snowball.hasMetadata("Freezer"))) return;
        
        // Cancel damage
        e.setCancelled(true);

        Player shooter = (Player) snowball.getShooter();
        if (shooter == null) return;

        CustomPlayer customShooter = CustomPlayer.getCustomPlayer(shooter);
        CustomPlayer customHitted = CustomPlayer.getCustomPlayer(player);

        // Team check - don't freeze teammates
        if (customHitted.getTeam().equals(customShooter.getTeam())) return;
        
        PerkConfig.PerkSettings settings = getSettings();
        
        // Freeze the player
        freezePlayer(player, shooter, settings);
    }
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball snowball)) return;
        if (!snowball.hasMetadata("Freezer")) return;
        
        // Get hit location
        Location hitLoc = snowball.getLocation();
        Block hitBlock = event.getHitBlock();
        
        // If hit a block, freeze ground around it
        if (hitBlock != null) {
            PerkConfig.PerkSettings settings = getSettings();
            freezeGround(hitBlock.getLocation(), settings);
        } else {
            // If didn't hit a block (hit entity or nothing), freeze ground below
            Location groundLoc = hitLoc.clone();
            groundLoc.setY(Math.floor(hitLoc.getY()));
            PerkConfig.PerkSettings settings = getSettings();
            freezeGround(groundLoc, settings);
        }
    }
    
    // Freeze a player with complete stun
    private void freezePlayer(Player player, Player attacker, PerkConfig.PerkSettings settings) {
        UUID playerUUID = player.getUniqueId();
        
        // Mark player as frozen
        frozenPlayers.add(playerUUID);
        
        // Store current location for no-gravity effect
        if (settings.isDisableGravity()) {
            frozenPlayerLocations.put(playerUUID, player.getLocation().clone());
        }
        
        // Apply complete stun effects
        // Slowness 255 = can't move
        // Jump Boost -128 (negative) = can't jump
        // Mining Fatigue 255 = can't break blocks
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 
            (int)(settings.getPlayerFreezeDuration() * 20), 255, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 
            (int)(settings.getPlayerFreezeDuration() * 20), -128, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 
            (int)(settings.getPlayerFreezeDuration() * 20), 255, false, false));
        
        // Track the hit for kill attribution if there's an attacker
        if (attacker != null) {
            CustomPlayer customHitted = CustomPlayer.getCustomPlayer(player);
            customHitted.addHitted(attacker);
        }
        
        // Schedule unfreeze
        new BukkitRunnable() {
            @Override
            public void run() {
                unfreezePlayer(playerUUID);
            }
        }.runTaskLater(Woolbattle.getPlugin(), (long)(settings.getPlayerFreezeDuration() * 20));
    }
    
    // Unfreeze a player
    private void unfreezePlayer(UUID playerUUID) {
        frozenPlayers.remove(playerUUID);
        frozenPlayerLocations.remove(playerUUID);
    }
    
    // Freeze ground blocks in radius
    private void freezeGround(Location center, PerkConfig.PerkSettings settings) {
        int radius = settings.getGroundFreezeRadius();
        int duration = settings.getGroundFreezeDuration();
        
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Check if within circular radius
                if (Math.sqrt(x * x + z * z) <= radius) {
                    Block block = center.clone().add(x, 0, z).getBlock();
                    
                    // Only freeze solid blocks that aren't air
                    if (block.getType() != Material.AIR && 
                        block.getType() != Material.PACKED_ICE &&
                        block.getType() != Material.ICE &&
                        !frozenBlocks.containsKey(block)) {
                        
                        // Store original material
                        frozenBlocks.put(block, block.getType());
                        
                        // Change to packed ice
                        block.setType(Material.PACKED_ICE);
                        
                        // Schedule unfreeze
                        BukkitRunnable task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                unfreezeBlock(block);
                            }
                        };
                        task.runTaskLater(Woolbattle.getPlugin(), duration * 20L);
                        frozenBlockTasks.put(block, task);
                    }
                }
            }
        }
    }
    
    // Unfreeze a block
    private void unfreezeBlock(Block block) {
        Material originalMaterial = frozenBlocks.remove(block);
        BukkitRunnable task = frozenBlockTasks.remove(block);
        
        if (task != null) {
            task.cancel();
        }
        
        if (originalMaterial != null) {
            block.setType(originalMaterial);
        }
    }
    
    // Prevent frozen blocks from being broken
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        
        if (frozenBlocks.containsKey(block)) {
            event.setCancelled(true);
        }
    }
    
    // Prevent frozen players from being damaged
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFrozenPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        if (frozenPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
    
    // Prevent frozen players from being damaged by entities (perks/weapons)
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFrozenPlayerDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (frozenPlayers.contains(player.getUniqueId())) {
            // Play item break sound when hitting a frozen player
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            event.setCancelled(true);
        }
    }
    
    // Implement no-gravity effect for frozen players
    @EventHandler(priority = EventPriority.HIGH)
    public void onFrozenPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        
        if (!frozenPlayerLocations.containsKey(playerUUID)) return;
        
        Location frozenLoc = frozenPlayerLocations.get(playerUUID);
        Location currentLoc = player.getLocation();
        
        // If player has moved vertically (fallen), teleport back to frozen location
        if (Math.abs(currentLoc.getY() - frozenLoc.getY()) > 0.1) {
            Location newLoc = currentLoc.clone();
            newLoc.setY(frozenLoc.getY());
            player.teleport(newLoc);
        }
    }
    
    public static void enable() { 
        Bukkit.getPluginManager().registerEvents(new freezer(), Woolbattle.getPlugin()); 
    }
    
    public static void disable() {
        // Clear all frozen players
        frozenPlayers.clear();
        frozenPlayerLocations.clear();
        
        // Restore all frozen blocks
        for (Block block : new HashSet<>(frozenBlocks.keySet())) {
            unfreezeBlockStatic(block);
        }
    }
    
    // Static method to unfreeze a block (for cleanup)
    private static void unfreezeBlockStatic(Block block) {
        Material originalMaterial = frozenBlocks.remove(block);
        BukkitRunnable task = frozenBlockTasks.remove(block);
        
        if (task != null) {
            task.cancel();
        }
        
        if (originalMaterial != null) {
            block.setType(originalMaterial);
        }
    }
}
