package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class blink implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("blink");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!(event.getItem().getType() == Material.TRIPWIRE_HOOK)) return;

        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();

        // Check wool cost
        if (!Woolbattle.debug)
            if (!Items.cost(player, settings.getCost())) return;

        // Get dynamic max distance
        int maxDistance = settings.getBlinkMaxDistance();
        
        Location playerLocation = player.getLocation();

        for (int i = maxDistance; i >= 0; i--) {

            Vector direction = playerLocation.getDirection();
            direction.setY(0).normalize();
            direction.multiply(i);

            Location baseLocation = player.getLocation().clone().add(direction);
            Location[] locationsToTry = {
                    baseLocation.clone(),
                    baseLocation.clone().add(0, 1, 0),
                    baseLocation.clone().add(0, -1, 0),
                    baseLocation.clone().add(getLeftVector(player)),
                    baseLocation.clone().add(getRightVector(player))
            };

            for (Location teleportLocation : locationsToTry) {
                teleportLocation.setYaw(player.getYaw());
                teleportLocation.setPitch(player.getPitch());

                if (isSafeLocation(teleportLocation)) {
                    // Apply cooldown with recharger check
                    int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
                            ? settings.getCooldownRecharger()
                            : settings.getCooldown();
                    
                    Items.visualCooldown(player, cooldown, Material.TRIPWIRE_HOOK, player.getInventory().getHeldItemSlot(), "§3Blink");
                    player.teleport(teleportLocation);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    return;
                }
            }
        }

        // player.teleport(new Location(player.getWorld(), player.getX()+5, player.getY(), player.getZ(), player.getYaw(), player.getPitch()));

        event.setCancelled(true);
    }

    private Vector getLeftVector(Player player) {
        Vector direction = player.getLocation().getDirection();
        direction.setY(0).normalize();
        return new Vector(-direction.getZ(), 0, direction.getX());
    }

    private Vector getRightVector(Player player) {
        Vector direction = player.getLocation().getDirection();
        direction.setY(0).normalize();
        return new Vector(direction.getZ(), 0, -direction.getX());
    }

    private boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        Block head = location.clone().add(0, 1, 0).getBlock();
        Block ground = location.clone().add(0, -1, 0).getBlock();

        return feet.isPassable()
                && head.isPassable()
                && !ground.isPassable();
    }

    public static void enable() { Bukkit.getPluginManager().registerEvents(new blink(), Woolbattle.getPlugin()); }
    public static void disable() {}
}
