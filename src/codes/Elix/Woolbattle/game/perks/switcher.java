// This class was created by Elix on 31.07.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class switcher implements Listener {


    @EventHandler
    public void onSwitch(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Snowball) {
            System.out.println("Projectile damage");
            Projectile projectile = (Projectile) event.getDamager();
            Player shooter = (Player) projectile.getShooter();
            Player hitted = (Player) event.getEntity();

            Location location1 = shooter.getLocation();
            Location location2 = hitted.getLocation();
            shooter.teleport(location2);
            hitted.teleport(location1);
            shooter.playSound(shooter.getLocation(), Sound.BURP, 1F, 1F);
            hitted.playSound(hitted.getLocation(), Sound.BURP, 1F, 1F);

        }
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new switcher(), Woolbattle.getPlugin());
    }

}

