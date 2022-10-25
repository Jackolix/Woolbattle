package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class platform implements Listener {

    private ArrayList<Player> available = new ArrayList<>();
    private HashMap<Player, Integer> scheduler = new HashMap<>();
    private long cooldown = 15;

    //number of blocks under the player where the platform should be spawned
    private float depth = 2;
    private int cost = 5;
    int taskID;

    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)            return;
        Player player = event.getPlayer();
        if (available.contains(player))  return;
        if (event.getItem().getType() == Material.BLAZE_ROD) {

            if (!(Items.amount(player, Material.BLACK_WOOL) >= cost)) return;
            ItemStack item = new ItemStack(Material.BLACK_WOOL);
            item.setAmount(cost);
            player.getInventory().removeItem(item);


            available.add(player);

            int slot = player.getInventory().getHeldItemSlot();
            //TODO: change the color of the wool based on the team the player is in
            placeBlocks(player.getLocation());
            visualCooldown(player, (int) cooldown, Material.BLAZE_ROD, slot);
        }
    }

    private void placeBlocks(Location location) {
        //move the location down with the value in depth
        location.add(0, -depth, 0);

        if (location.add(2,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.BLACK_WOOL);
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
                    Items.create(player.getInventory(), perk, "§3Plattform", slot);
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
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new platform(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
