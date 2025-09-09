// This class was created by Elix on 19.06.22

package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;

public class DoubleJump implements Listener {

    //TODO: can be changed from rocket jump
    double dj_height = 1.5D;
    //cooldown between double jumps
    long cooldown = 2L;
    //strength of the double jumps
    float strength = 0.75f;
    int cost = 5;

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (IngameState.spectator.contains(player)) return;

        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);

            if (!Woolbattle.debug)
                if (!Items.cost(player, cost)) return;

            if (Objects.equals(PerkHelper.passive(player), "rocket_jump"))
                dj_height = 2D;
            //give player the velocity
            Vector walk_vector = player.getVelocity().normalize();
            Vector vector = new Vector(walk_vector.getX(), dj_height, walk_vector.getZ());
            vector.multiply(strength);
            player.setVelocity(vector);

            player.setAllowFlight(false);

            //player.setFoodLevel(0);

            //change xp for the cooldown
            for (int i = 0; i <= 10; i++) {
                setEXP(i, player);
            }

            if (Objects.equals(PerkHelper.passive(player), "recharger"))
                cooldown = 1L;
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> player.setAllowFlight(true), 20 * cooldown);

        }
    }
    private void setEXP(int i, Player player)
    {
        Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                //() -> player.setFoodLevel(i), 20 * i * delay/20);
                () -> player.setExp((float)i/10), 20 * i * cooldown/10);
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
            current.getPlayer().setAllowFlight(true);
    }

    public static void disable() {
        for (Player current : Bukkit.getOnlinePlayers())
            current.getPlayer().setAllowFlight(false);
    }
}