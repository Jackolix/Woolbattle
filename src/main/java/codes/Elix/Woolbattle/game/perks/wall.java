package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;


public class wall implements Listener {
    int cooldown = 8;

    //number of blocks infornt of the player where the wall should be spawned
    float front = 4;
    int cost = 5;

    @EventHandler
    public void onWallInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.RED_STAINED_GLASS_PANE) {
            Player player = event.getPlayer();

            if (!Items.cost(player, cost)) return;

            Vector forwardVec = new Vector(0,0,0);
            Vector rightVec   = new Vector(0,0,0);
            double rotation = (event.getPlayer().getLocation().getYaw() - 90) % 360;
            if (rotation < -45) {
                rotation += 360.0;
            }
            /*if (0 <= rotation && rotation < 22.5) {
                //west
                forwardVec = new Vector(-1, 0, 0);
                rightVec = new Vector(0,0,1);
            } else if (67.5 <= rotation && rotation < 112.5) {
                //south
                forwardVec = new Vector(0,0,-1);
                rightVec = new Vector(1,0,0);
            } else if (157.5 <= rotation && rotation < 202.5) {
                //east
                forwardVec = new Vector(1,0,0);
                rightVec = new Vector(0,0,1);
            } else if (247.5 <= rotation && rotation < 292.5) {
                //north
                forwardVec = new Vector(0,0,1);
                rightVec = new Vector(-1,0,0);
            }*/

            if (-45 <= rotation && rotation < 45) {
                //west
                forwardVec = new Vector(-1, 0, 0);
                rightVec = new Vector(0,0,1);
            } else if (45 <= rotation && rotation < 135) {
                //south
                forwardVec = new Vector(0,0,-1);
                rightVec = new Vector(1,0,0);
            } else if (135 <= rotation && rotation < 225) {
                //east
                forwardVec = new Vector(1,0,0);
                rightVec = new Vector(0,0,1);
            } else if (225 <= rotation && rotation <= 315) {
                //north
                forwardVec = new Vector(0, 0, 1);
                rightVec = new Vector(-1, 0, 0);
            }

            placeBlocks(player.getLocation(), Items.getWoolColor(player), forwardVec, rightVec);
            int slot = player.getInventory().getHeldItemSlot();

            Items.visualCooldown(player, cooldown, Material.RED_STAINED_GLASS_PANE, slot, "§3Wall");
            }
        }

        private void placeBlocks(Location location, Material material, Vector forwardVec, Vector rightVec) {
            //move the location front with the value in front
            location.add(forwardVec.multiply(front));
            location.add(rightVec);

            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec.multiply(-1)).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(0,1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec.multiply(-1)).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(0,1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec.multiply(-1)).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        }

    public static void enable() { Bukkit.getPluginManager().registerEvents(new codes.Elix.Woolbattle.game.perks.wall(), Woolbattle.getPlugin()); }
    public static void disable() {}
}
