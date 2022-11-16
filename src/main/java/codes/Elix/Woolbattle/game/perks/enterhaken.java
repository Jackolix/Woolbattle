package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftFishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class enterhaken implements Listener {

    int cost = 5;
    int cooldown = 16;
    /*
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.FISHING_ROD) return;

    }

     */

    @EventHandler
    public void onBlockHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof CraftFishHook)) return; // NMS code
        if (event.getHitBlock() == null) return;
        Player player = (Player) event.getEntity().getShooter();

        Vector vector = event.getHitBlock().getLocation().toVector().subtract(player.getLocation().toVector());
        vector.normalize().multiply(5.0);
        player.setVelocity(vector);
        // Vector vector = event.getHitBlock().getLocation().toVector().normalize();
        // player.setVelocity(vector);

        int slot = player.getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.FISHING_ROD, slot, "§3Enterhaken");

    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new enterhaken(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
