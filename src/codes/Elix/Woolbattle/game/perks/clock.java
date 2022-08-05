package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class clock implements Listener {

    //time of teleportation after activation
    private long teleportTime = 4;
    private long cooldown = 15;

    private boolean ready = true;
    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (!ready)                     return;
        if (event.getItem().getType() == Material.WATCH) {
            Player player = event.getPlayer();
            ready = false;

            Location location = player.getLocation();
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> player.teleport(location), 20 * teleportTime);

            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);

            //TODO: Add sounds
            //TODO: Cooldown for clock (visual representation)
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> setReady(true), 20 * cooldown);
        }
    }

    private void setReady(boolean rdy) {
        ready = rdy;
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new clock(), Woolbattle.getPlugin());
    }


    public static void disable() {}
}
