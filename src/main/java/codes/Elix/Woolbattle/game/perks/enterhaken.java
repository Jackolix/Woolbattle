package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class enterhaken implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("enterhaken");
    }

    @EventHandler
    public void onInteract(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook h = event.getHook();
        PerkConfig.PerkSettings settings = getSettings();
        
        if (!Woolbattle.debug)
            if (!Items.cost(player, settings.getCost())) {
                event.setCancelled(true);
                return;
            }

        int slot = player.getInventory().getHeldItemSlot();
        if (((event.getState().equals(PlayerFishEvent.State.IN_GROUND) || event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) || event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)) && Bukkit.getWorlds().get(0).getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ()).getType() != Material.AIR && Bukkit.getWorlds().get(0).getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ()).getType() != Material.WATER)) {
            Vector vector = event.getHook().getLocation().toVector().subtract(player.getLocation().toVector());
            vector.normalize().multiply(5.0);
            player.setVelocity(vector);

            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
                ? settings.getCooldownRecharger()
                : settings.getCooldown();
                
            if (!Woolbattle.debug)
                Items.visualCooldown(player, cooldown, Material.FISHING_ROD, slot, "§3Enterhaken");
        }
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new enterhaken(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
