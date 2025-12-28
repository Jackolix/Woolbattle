package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
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

import java.util.Objects;


public class wall implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("wall");
    }

    @EventHandler
    public void onWallInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.RED_STAINED_GLASS_PANE) {
            Player player = event.getPlayer();
            PerkConfig.PerkSettings settings = getSettings();
            
            if (!Woolbattle.debug)
                if (!Items.cost(player, settings.getCost())) return;

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

            if (!Woolbattle.debug) {
                placeBlocks(player.getLocation(), Items.getWoolColor(player), forwardVec, rightVec);
                return;
            }

            int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
                ? settings.getCooldownRecharger()
                : settings.getCooldown();
                
            int slot = player.getInventory().getHeldItemSlot();
            Items.visualCooldown(player, cooldown, Material.RED_STAINED_GLASS_PANE, slot, "§3Wall");
            }
        }

        private void placeBlocks(Location location, Material material, Vector forwardVec, Vector rightVec) {
            PerkConfig.PerkSettings settings = getSettings();
            //move the location front with the value in front  
            location.add(forwardVec.multiply(settings.getMaxDistance()));
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
