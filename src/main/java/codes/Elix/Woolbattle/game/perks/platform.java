package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;


public class platform implements Listener {
    int cooldown = 15;

    //number of blocks under the player where the platform should be spawned
    float depth = 2;
    int cost = 5;

    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)            return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.BLAZE_ROD) return;

        if (Woolbattle.debug) {
            placeBlocks(player.getLocation(), Items.getWoolColor(player));
            return;
        }
        if (!Items.cost(player, cost)) return;
        int slot = player.getInventory().getHeldItemSlot();
        placeBlocks(player.getLocation(), Items.getWoolColor(player));
        Items.visualCooldown(player, cooldown, Material.BLAZE_ROD, slot, "§3Plattform");

    }

    private void placeBlocks(Location location, Material material) {
        //move the location down with the value in depth
        location.add(0, -depth, 0);

        if (location.add(2,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
    }
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new platform(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
