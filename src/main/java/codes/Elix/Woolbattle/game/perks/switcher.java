// This class was created by Elix on 31.07.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;


public class switcher implements Listener {

    int cost = 5;
    int cooldown = 10;

    @EventHandler
    public void onSwitch(EntityDamageByEntityEvent event) {

        if ((event.getDamager() instanceof Player)) return;
        if (!(event.getDamager() instanceof Snowball)) return;

        Projectile projectile = (Projectile) event.getDamager();
        Player shooter = (Player) projectile.getShooter();
        Player hitted = (Player) event.getEntity();

        if (!projectile.hasMetadata("Switcher")) return;

        Location location1 = shooter.getLocation();
        Location location2 = hitted.getLocation();

        shooter.teleport(location2);
        hitted.teleport(location1);

        shooter.playSound(shooter.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);
            // hitted.playSound(hitted.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (!(event.getItem().getType() == Material.SNOWBALL)) return;
        Player player = event.getPlayer();
        if (!Woolbattle.debug)
            if (!Items.cost(player, cost)) {
                event.setCancelled(true);
                return;
            }
        // Snowball wird 2 mal losgeschickt, (TODO) keine Ahnung ob es jetzt noch funktioniert, müsste eigentlich
        Projectile snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("Switcher", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

        // player.launchProjectile(Snowball.class);
        event.setCancelled(true);
        if (Objects.equals(PerkHelper.passive(player), "recharger"))
            cooldown = 8;
        int slot = player.getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.SNOWBALL, slot, "§3Tauscher");
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new switcher(), Woolbattle.getPlugin());
    }
    public static void disable() {}

}

