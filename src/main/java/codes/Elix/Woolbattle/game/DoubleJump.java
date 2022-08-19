// This class was created by Elix on 19.06.22

package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class DoubleJump implements Listener {

    //TODO: can be changed from rocket jump
    private double dj_height = 1.25D;
    //cooldown between double jumps
    private long cooldown = 4L;
    //strength of the double jumps
    private float strength = 0.5f;

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);

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

            //cooldown
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
        if (event.getNewGameMode() == GameMode.SURVIVAL || event.getNewGameMode() == GameMode.ADVENTURE) {
            event.getPlayer().setAllowFlight(true);
        }
    }

    public static void enable() {
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.getPlayer().setAllowFlight(true);
        }
    }

    public static void disable() {
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.getPlayer().setAllowFlight(false);
        }
    }
}