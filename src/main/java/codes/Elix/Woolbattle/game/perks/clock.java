package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class clock implements Listener {

    private ArrayList<Player> available = new ArrayList<>();
    private HashMap<Player, Integer> scheduler = new HashMap<>();
    //time of teleportation after activation
    private long teleportTime = 4;
    private long cooldown = 15;
    int taskID;
    private int cost = 5;

    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() != Material.CLOCK) return;
        Player player = event.getPlayer();
        if (available.contains(event.getPlayer()))  return;

        if (!(Items.amount(player, Material.BLACK_WOOL) >= cost)) return;
        ItemStack item = new ItemStack(Material.BLACK_WOOL);
        item.setAmount(cost);
        player.getInventory().removeItem(item);

        available.add(player);

        Location location = player.getLocation();
        Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                () -> player.teleport(location), 20 * teleportTime);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 1F);

        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        visualCooldown(event.getPlayer(), (int) cooldown, Material.CLOCK, slot);
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
                    Items.create(player.getInventory(), perk, "§3Clock", slot);
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
        Bukkit.getPluginManager().registerEvents(new clock(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
