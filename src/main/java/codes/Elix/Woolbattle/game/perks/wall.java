package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class wall implements Listener {
    private ArrayList<Player> available = new ArrayList<>();
    private long cooldown = 8;

    //number of blocks infornt of the player where the wall should be spawned
    private float front = 4;

    @EventHandler
    public void onWallInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)            return;
        if (available.contains(event.getPlayer()))  return;
        if (event.getItem().getType() == Material.RED_STAINED_GLASS_PANE) {
            available.add(event.getPlayer());
            for(Enchantment ench: event.getItem().getEnchantments().keySet()){
                event.getItem().removeEnchantment(ench);
            }

            Vector forwardVec = new Vector(0,0,0);
            Vector rightVec   = new Vector(0,0,0);
            double rotation = (event.getPlayer().getLocation().getYaw() - 90) % 360;
            if (rotation < 0) {
                rotation += 360.0;
            }
            if (0 <= rotation && rotation < 22.5) {
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
            }
            placeBlocks(event.getPlayer().getLocation(), forwardVec, rightVec);

            //TODO: Cooldown for wall (visual representation)
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> activate(event.getPlayer(), event.getItem()), 20 * cooldown);
            }
        }

        private void activate(Player player, ItemStack item) {
            available.remove(player);
            //item.addEnchantment(Enchantment.KNOCKBACK, 1); //Specified enchantment cannot be applied to this itemstack
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemmeta);
        }

        private void placeBlocks(Location location, Vector forwardVec, Vector rightVec) {
            //move the location front with the value in front
            location.add(forwardVec.multiply(front));
            location.add(rightVec);

            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec.multiply(-1)).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(0,1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec.multiply(-1)).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(0,1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec.multiply(-1)).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
            if (location.add(rightVec).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        }

        public static void enable() {
            Bukkit.getPluginManager().registerEvents(new codes.Elix.Woolbattle.game.perks.wall(), Woolbattle.getPlugin());
        }

        public static void disable() {
    }
}
