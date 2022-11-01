package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
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
import java.util.HashMap;

public class wall implements Listener {
    private ArrayList<Player> available = new ArrayList<>();
    private HashMap<Player, Integer> scheduler = new HashMap<>();
    private long cooldown = 8;

    //number of blocks infornt of the player where the wall should be spawned
    private float front = 4;
    private int cost = 5;
    int taskID;

    @EventHandler
    public void onWallInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)            return;
        if (available.contains(event.getPlayer()))  return;
        if (event.getItem().getType() == Material.RED_STAINED_GLASS_PANE) {
            Player player = event.getPlayer();

            if (!(Items.amount(player, Material.BLACK_WOOL) >= cost)) return;
            ItemStack item = new ItemStack(Material.BLACK_WOOL);
            item.setAmount(cost);
            player.getInventory().removeItem(item);

            available.add(player);

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

            placeBlocks(player.getLocation(), forwardVec, rightVec);
            int slot = player.getInventory().getHeldItemSlot();

            visualCooldown(player, (int) cooldown, Material.RED_STAINED_GLASS_PANE, slot);
            }
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

    private void visualCooldown(Player player, int cooldown, Material perk, int slot) {
        Items.interact.add(player);
        taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Woolbattle.getPlugin(), new Runnable() {
            int count = cooldown;

            @Override
            public void run() {
                Items.createcooldown(player.getInventory(), Material.GRAY_DYE, count, "Cooldown", slot);
                count--;
                if (count == 0) {
                    Items.create(player.getInventory(), perk, "§3Wall", slot);
                    cancel(scheduler.get(player));
                    Items.interact.remove(player);
                    scheduler.remove(player);
                    available.remove(player);
                }
            }
        }, 0, 20);
        scheduler.put(player, taskID);
    }

    private void cancel(Integer taskID) {
        Bukkit.getScheduler().cancelTask(taskID);
    }
    public static void enable() { Bukkit.getPluginManager().registerEvents(new codes.Elix.Woolbattle.game.perks.wall(), Woolbattle.getPlugin()); }
    public static void disable() {}
}
