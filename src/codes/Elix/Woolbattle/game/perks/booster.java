// This class was created by Elix on 01.08.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class booster implements Listener {

    @EventHandler
    public void onBoosterInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() == Material.TRIPWIRE_HOOK) {
            Player player = event.getPlayer();

            Vector vector = player.getLocation().getDirection().multiply(2.75D).setY(2D);
            player.setVelocity(vector);

            player.playSound(player.getLocation(), Sound.CAT_HISS, 1F, 1F);
            //TODO: Cooldown for booster
        }
    }


    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new booster(), Woolbattle.getPlugin());
    }


    public static void disable() {}
}
