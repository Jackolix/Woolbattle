// This class was created by Elix on 01.08.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
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
    int cooldown = 15;
    int cost = 5;

    @EventHandler
    public void onBoosterInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() != Material.TRIPWIRE_HOOK) return;
        if (!Items.cost(event.getPlayer(), cost)) return;
        Player player = event.getPlayer();

        Vector vector = player.getLocation().getDirection().multiply(2.75D).setY(2D);

        player.setVelocity(vector);
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 1F, 1F);

        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        Items.visualCooldown(player, cooldown, Material.TRIPWIRE_HOOK, slot, "§3booster");
    }
    public static void enable() { Bukkit.getPluginManager().registerEvents(new booster(), Woolbattle.getPlugin()); }
    public static void disable() {}
}
