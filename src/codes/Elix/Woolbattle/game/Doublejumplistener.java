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

public class Doublejumplistener implements Listener {
//TODO: change how the food level is implemented
    //delay between double jumps
    private long delay = 5L;
    //strength of the double jumps
    private float strength = 1.0f;

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);

            Vector vector = new Vector(0, 0, 0);
            vector.setY(1.25D);
            //Vector view_direction = event.getPlayer().getEyeLocation().getDirection().normalize();
            Vector walk_vector = event.getPlayer().getVelocity().normalize();
            vector.setX(walk_vector.getX());
            vector.setZ(walk_vector.getZ());
            vector.multiply(strength);
            event.getPlayer().setVelocity(vector);
            event.getPlayer().setAllowFlight(false);

            event.getPlayer().setFoodLevel(10);

            for (int i = 1; i <= 10; i++)
            {
                setFood(i, event);
            }

            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> event.getPlayer().setAllowFlight(true), 20 * delay);
        }
    }

    private void setFood(int i, PlayerToggleFlightEvent event)
    {
        Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                () -> event.getPlayer().setFoodLevel(10 + i), 20 * i * delay/10);
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