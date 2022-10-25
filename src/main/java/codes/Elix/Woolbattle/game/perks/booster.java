// This class was created by Elix on 01.08.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class booster implements Listener {

    private ArrayList<Player> available = new ArrayList<>();
    private HashMap<Player, Integer> scheduler = new HashMap<>();
    int taskID;
    int cooldown = 15;
    private int cost = 5;

    @EventHandler
    public void onBoosterInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() != Material.TRIPWIRE_HOOK) return;
        if (available.contains(event.getPlayer()))  return;
        Player player = event.getPlayer();

        if (!(Items.amount(player, Material.BLACK_WOOL) >= cost)) return;
        ItemStack item = new ItemStack(Material.BLACK_WOOL);
        item.setAmount(cost);
        player.getInventory().removeItem(item);

        available.add(event.getPlayer());

        Vector vector = player.getLocation().getDirection().multiply(2.75D).setY(2D);

        player.setVelocity(vector);
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 1F, 1F);

        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        visualCooldown(event.getPlayer(), cooldown, Material.TRIPWIRE_HOOK, slot);
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
                    Items.create(player.getInventory(), perk, "§3Booster", slot);
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
        Bukkit.getPluginManager().registerEvents(new booster(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
