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


    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);

            Vector vector = new Vector(0, 0, 0);
            vector.setY(1.25D);
            event.getPlayer().setVelocity(vector);
            event.getPlayer().setAllowFlight(false);
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> event.getPlayer().setAllowFlight(true), 20 * 8L);
        }
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