package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class clock implements Listener {
    long teleportTime = 4;
    int cooldown = 15;
    int taskID;
    int cost = 5;

    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() != Material.CLOCK) return;
        Player player = event.getPlayer();

        if (!Items.cost(player, cost)) return;

        Location location = player.getLocation();
        Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                () -> player.teleport(location), 20 * teleportTime);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 1F);

        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        Items.visualCooldown(player, cooldown, Material.CLOCK, slot, "§3Clock");
    }
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new clock(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
