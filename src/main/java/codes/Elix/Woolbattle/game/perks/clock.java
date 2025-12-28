package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class clock implements Listener {
    int taskID;

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("clock");
    }

    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.CLOCK) return;
        
        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();
        
        if (!Woolbattle.debug)
            if (!Items.cost(player, settings.getCost())) return;

        Location location = player.getLocation();
        // Can only be triggered synchronously //TODO is it possible asynchronous?
        Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                () -> player.teleport(location), 20 * settings.getEffectDuration());

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 1F);

        int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
            ? settings.getCooldownRecharger()
            : settings.getCooldown();
            
        int slot = player.getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.CLOCK, slot, "§3Clock");
    }
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new clock(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
