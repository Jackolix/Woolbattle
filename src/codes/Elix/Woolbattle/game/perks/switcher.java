// This class was created by Elix on 31.07.22


package codes.Elix.Woolbattle.game.perks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class switcher implements Listener {

    private boolean enabled;

    @EventHandler
    public void onswitch(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Projectile) { //Does not work somehow
            System.out.println("Projectile damage");
            Projectile projectile = (Projectile) event.getDamager();
            Player shooter = (Player) projectile.getShooter();
            Entity hitted = (Entity) event.getDamager();


            Location location1 = shooter.getLocation();
            Location location2 = hitted.getLocation();
            shooter.teleport(location2);
            hitted.teleport(location1);

        }
    }

}
