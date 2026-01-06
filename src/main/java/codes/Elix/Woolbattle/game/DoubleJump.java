// This class was created by Elix on 19.06.22

package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DoubleJump implements Listener {

    private static DoubleJump instance;
    //TODO: can be changed from rocket jump
    double dj_height;
    double rocket_jump_height;
    //cooldown between double jumps
    long cooldown;
    //strength of the double jumps
    float strength = 0.75f;
    int cost;

    public DoubleJump() {
        instance = this;
        loadConfig();
    }

    private void loadConfig() {
        PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings("doublejump");
        this.cooldown = settings.getCooldown();
        this.cost = settings.getCost();
        this.dj_height = settings.getDoubleJumpHeight();
        this.rocket_jump_height = settings.getRocketJumpHeight();
    }

    public static void reloadConfig() {
        if (instance != null) {
            instance.loadConfig();
            System.out.println("[DoubleJump] Config reloaded - cooldown: " + instance.cooldown + ", cost: " + instance.cost + ", height: " + instance.dj_height + ", rocket_jump_height: " + instance.rocket_jump_height);
        }
    }

    // Track players on cooldown
    private static final Set<Player> onCooldown = new HashSet<>();

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (IngameState.spectator.contains(player)) return;

        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);

            // Add to cooldown BEFORE checking wool to prevent flight from being re-enabled
            onCooldown.add(player);

            // Check if player has enough wool
            if (!Woolbattle.debug) {
                if (!Items.cost(player, cost)) {
                    // No wool available - remove from cooldown since we didn't jump
                    onCooldown.remove(player);
                    return;
                }
            }

            // Player has wool - perform double jump
            double jumpHeight = dj_height;
            if (Objects.equals(PerkHelper.passive(player), "rocket_jump"))
                jumpHeight = rocket_jump_height;
            //give player the velocity
            Vector walk_vector = player.getVelocity().normalize();
            Vector vector = new Vector(walk_vector.getX(), jumpHeight, walk_vector.getZ());
            vector.multiply(strength);
            player.setVelocity(vector);
            player.setAllowFlight(false);
            //player.setFoodLevel(0);

            //change xp for the cooldown
            long appliedCooldown = cooldown;
            if (Objects.equals(PerkHelper.passive(player), "recharger")) {
                PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings("doublejump");
                appliedCooldown = settings.getCooldownRecharger();
            }

            for (int i = 0; i <= 10; i++) {
                setEXP(i, player, appliedCooldown);
            }

            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> {
                        onCooldown.remove(player);
                        updateFlightBasedOnWool(player);
                    }, 20 * appliedCooldown);

        }
    }
    private void setEXP(int i, Player player, long appliedCooldown)
    {
        Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                //() -> player.setFoodLevel(i), 20 * i * delay/20);
                () -> player.setExp((float)i/10), 20 * i * appliedCooldown/10);
    }


    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (event.getNewGameMode() == GameMode.SURVIVAL)
                    event.getPlayer().setAllowFlight(true);
            }
        }, 10);
    }

    public static void enable() {
        for (Player current : Bukkit.getOnlinePlayers())
            updateFlightBasedOnWool(current);
    }

    public static void disable() {
        for (Player current : Bukkit.getOnlinePlayers())
            current.getPlayer().setAllowFlight(false);
    }

    /**
     * Updates player's flight ability based on whether they have wool
     * Should be called after wool is added or removed from inventory
     */
    public static void updateFlightBasedOnWool(Player player) {
        if (IngameState.spectator.contains(player)) return;

        // Don't enable flight if player is on cooldown
        if (onCooldown.contains(player)) {
            player.setAllowFlight(false);
            IngameScoreboard.update(player);
            return;
        }

        boolean hasWool = Items.woolAmount(player) > 0;
        player.setAllowFlight(hasWool);
        IngameScoreboard.update(player);
    }
}