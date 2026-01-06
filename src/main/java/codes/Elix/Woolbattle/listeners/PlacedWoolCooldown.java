package codes.Elix.Woolbattle.listeners;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Manages automatic removal of placed wool blocks with proximity-based timer pausing.
 *
 * Features:
 * - Wool blocks expire after 3 minutes (3600 ticks)
 * - Timer pauses when a player is within 5 blocks
 * - When player leaves, timer resumes and adds 30 seconds bonus (up to 3 times)
 * - After 3 bonuses, timer only pauses (no more bonus time)
 * - Maximum countdown capped at 3 minutes to prevent infinite block lifespan
 * - Damage animation in last 60 seconds continues independently (not affected by bonus time)
 *
 * Logging:
 * - INFO level: Important events (block expired, entered damage phase, bonus with damage freeze)
 * - FINE level: Detailed tracking (block placed/broken, pause/resume, damage stage changes)
 *
 * To enable debug logging, add this to your server's logging.properties or bukkit.yml:
 *   PlacedWoolCooldown.level = FINE
 *
 * Or set it programmatically:
 *   Logger.getLogger("PlacedWoolCooldown").setLevel(Level.FINE);
 */
public class PlacedWoolCooldown implements Listener {

    private static class BlockData {
        int timeLeft;
        int lastDamageStage;
        boolean isPaused;
        int bonusesReceived;
        int damageTimer; // Separate timer for damage animation that doesn't get bonus time

        BlockData(int timeLeft) {
            this.timeLeft = timeLeft;
            this.lastDamageStage = -1;
            this.isPaused = false;
            this.bonusesReceived = 0;
            this.damageTimer = 1200; // Start at 60 seconds for damage phase
        }
    }

    private final ConcurrentHashMap<Block, BlockData> blocks = new ConcurrentHashMap<>();
    private BukkitTask task;
    private static final double PLAYER_PROXIMITY_RADIUS = 5.0; // Blocks
    private static final int TIME_BONUS_ON_LEAVE = 600; // 30 seconds in ticks
    private static final int MAX_COUNTDOWN_TIME = 3600;
    private static final int MAX_BONUSES = 3; // Maximum number of times bonus time can be added
    private static final Logger logger = Logger.getLogger("PlacedWoolCooldown");

    public PlacedWoolCooldown() {
        logger.fine("PlacedWoolCooldown initialized");
        startCountdownTask();
    }

    @EventHandler
    public void onWoolPlace(BlockPlaceEvent event) {
        if (event.getPlayer() == null) return;
        if (!Tag.WOOL.isTagged(event.getBlock().getType())) return;

        Block block = event.getBlock();
        // Store block with initial countdown: 2 minutes (2400 ticks) + 1 minute (1200 ticks) = 3600 ticks total
        blocks.put(block, new BlockData(3600));
        logger.fine(String.format("Wool block placed at %s by %s - Timer started (3 minutes)",
                formatLocation(block), event.getPlayer().getName()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!Tag.WOOL.isTagged(block.getType())) return;

        // Clear damage effect before removing from tracking
        BlockData data = blocks.get(block);
        if (data != null) {
            if (data.lastDamageStage >= 0) {
                clearBlockDamage(block);
            }
            logger.fine(String.format("Wool block broken at %s by %s (Time left: %d ticks)",
                    formatLocation(block), event.getPlayer().getName(), data.timeLeft));
        }
        blocks.remove(block);
    }

    private void startCountdownTask() {
        // Run asynchronously every 20 ticks (1 second) to avoid blocking the main thread
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(Woolbattle.getPlugin(), () -> {
            List<Block> blocksToRemove = new ArrayList<>();

            for (Map.Entry<Block, BlockData> entry : blocks.entrySet()) {
                Block block = entry.getKey();
                BlockData data = entry.getValue();

                // Check if any player is near the block
                boolean playerNearby = isPlayerNearby(block);

                // Handle pause/unpause logic
                if (playerNearby && !data.isPaused) {
                    // Player just came near - pause the timer
                    data.isPaused = true;
                    logger.fine(String.format("Timer paused for block at %s - Player nearby (Time left: %d ticks, Bonuses: %d/%d)",
                            formatLocation(block), data.timeLeft, data.bonusesReceived, MAX_BONUSES));
                } else if (!playerNearby && data.isPaused) {
                    // Player just left - unpause
                    data.isPaused = false;
                    int oldTime = data.timeLeft;

                    // Only add bonus time if we haven't reached the limit
                    if (data.bonusesReceived < MAX_BONUSES) {
                        data.timeLeft += TIME_BONUS_ON_LEAVE;
                        // Cap at maximum countdown time
                        if (data.timeLeft > MAX_COUNTDOWN_TIME) {
                            data.timeLeft = MAX_COUNTDOWN_TIME;
                        }
                        data.bonusesReceived++;

                        // Note: We don't clear damage animation - it stays frozen at current stage
                        // The damageTimer will continue from where it left off when we re-enter damage phase
                        if (oldTime <= 1200) {
                            logger.info(String.format("Timer resumed, +30s bonus, damage frozen at stage %d at %s (Bonuses: %d/%d)",
                                    data.lastDamageStage, formatLocation(block), data.bonusesReceived, MAX_BONUSES));
                        } else {
                            logger.fine(String.format("Timer resumed for block at %s - Player left, +30s bonus (Time: %d -> %d ticks, Bonuses: %d/%d)",
                                    formatLocation(block), oldTime, data.timeLeft, data.bonusesReceived, MAX_BONUSES));
                        }
                    } else {
                        // Just unpause, no more bonus time
                        logger.fine(String.format("Timer resumed for block at %s - Player left, NO bonus (max reached, Time: %d ticks)",
                                formatLocation(block), data.timeLeft));
                    }
                }

                // Only decrease countdown if not paused
                if (!data.isPaused) {
                    // Decrease countdown by 20 ticks (1 second)
                    data.timeLeft -= 20;

                    // Only decrease damage timer when timeLeft has caught up to damageTimer
                    // This ensures damage progression stops after bonus time and only resumes when timeLeft reaches damageTimer again
                    if (data.timeLeft <= 1200 && data.timeLeft <= data.damageTimer) {
                        data.damageTimer -= 20;
                    }
                }

                if (data.timeLeft <= 0) {
                    // Time's up - schedule block removal on main thread
                    blocksToRemove.add(block);
                    logger.info(String.format("Wool block expired at %s - Removing block",
                            formatLocation(block)));
                    Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> {
                        if (Tag.WOOL.isTagged(block.getType())) {
                            if (data.lastDamageStage >= 0) {
                                clearBlockDamage(block);
                            }
                            block.setType(Material.AIR);
                        }
                    });
                } else if (data.damageTimer < 1200 || data.timeLeft <= 1200) {
                    // Show damage if: damage has started (damageTimer < 1200) OR we're in damage phase (timeLeft <= 1200)
                    // This keeps damage visible even when bonus time pushes timeLeft above 1200
                    if (!data.isPaused) {
                        int damageStage = getDamageStage(data.damageTimer);

                        // Always send damage packet to prevent client from clearing it
                        // Only log when damage stage actually changes
                        if (damageStage != data.lastDamageStage) {
                            if (damageStage == 0 && data.timeLeft <= 1200) {
                                logger.info(String.format("Block at %s entered damage phase", formatLocation(block)));
                            }
                            logger.fine(String.format("Block at %s damage stage: %d -> %d (timeLeft: %d, damageTimer: %d)",
                                    formatLocation(block), data.lastDamageStage, damageStage, data.timeLeft, data.damageTimer));
                            data.lastDamageStage = damageStage;
                        }
                        // Send damage packet every tick to keep client from auto-clearing it
                        sendBlockDamage(block, damageStage);
                    }
                    // Damage stage is based on damageTimer, which only progresses when timeLeft catches up
                }
            }

            // Remove blocks that expired
            blocksToRemove.forEach(blocks::remove);

            // Clean up blocks that were modified/removed externally (check on main thread)
            Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> {
                blocks.entrySet().removeIf(entry -> {
                    Block block = entry.getKey();
                    BlockData data = entry.getValue();
                    if (block.getType().equals(Material.AIR) || !Tag.WOOL.isTagged(block.getType())) {
                        if (data.lastDamageStage >= 0) {
                            clearBlockDamage(block);
                        }
                        return true;
                    }
                    return false;
                });
            });
        }, 20L, 20L); // Run every 20 ticks (1 second)
    }

    private int getDamageStage(int damageTimer) {
        // Map 1200 ticks (60 seconds) to damage stages 0-9
        // 0 = no damage, 9 = almost broken
        // Note: damageTimer counts down independently and doesn't receive bonus time
        int elapsed = 1200 - damageTimer;
        return Math.min(9, (elapsed * 10) / 1200);
    }

    private int getEntityId(Block block) {
        // Generate a consistent entity ID based on block location
        // Use a large negative number to avoid conflicts with real entities
        return -1000000 - (block.getX() * 73856093 ^ block.getY() * 19349663 ^ block.getZ() * 83492791);
    }

    private void sendBlockDamage(Block block, int damageStage) {
        // Convert damage stage (0-9) to damage value (0.0-0.9)
        // Note: Never send 1.0f as that means completely broken
        float damage = Math.min(0.9f, damageStage / 10.0f);
        int entityId = getEntityId(block);

        // Send block damage packet to all nearby players with consistent entity ID
        block.getWorld().getPlayers().forEach(player -> {
            if (player.getLocation().distance(block.getLocation()) <= 64) {
                player.sendBlockDamage(block.getLocation(), damage, entityId);
            }
        });
    }

    private void clearBlockDamage(Block block) {
        // Clear the damage effect by sending damage 0 or removing it
        int entityId = getEntityId(block);
        block.getWorld().getPlayers().forEach(player -> {
            if (player.getLocation().distance(block.getLocation()) <= 64) {
                player.sendBlockDamage(block.getLocation(), 0.0f, entityId);
            }
        });
    }

    private boolean isPlayerNearby(Block block) {
        // Check if any player is within the proximity radius
        return block.getWorld().getPlayers().stream()
                .anyMatch(player -> player.getLocation().distance(block.getLocation()) <= PLAYER_PROXIMITY_RADIUS);
    }

    private String formatLocation(Block block) {
        return String.format("(%d, %d, %d) in %s",
                block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
    }

    public void shutdown() {
        logger.fine(String.format("Shutting down PlacedWoolCooldown - Clearing %d tracked blocks", blocks.size()));
        if (task != null) {
            task.cancel();
        }

        // Clear all damage effects before shutdown (run on main thread)
        Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> {
            blocks.forEach((block, data) -> {
                if (data.lastDamageStage >= 0) {
                    clearBlockDamage(block);
                }
            });
            blocks.clear();
        });
    }

    /**
     * Manually track a block for automatic removal.
     * This should be called by perks and other systems that place wool blocks programmatically.
     *
     * @param block The block to track
     */
    public void trackBlock(Block block) {
        if (block == null) return;
        if (!Tag.WOOL.isTagged(block.getType())) return;

        // Store block with initial countdown: 3 minutes (3600 ticks)
        blocks.put(block, new BlockData(3600));
        logger.fine(String.format("Wool block manually tracked at %s - Timer started (3 minutes)",
                formatLocation(block)));
    }
}
