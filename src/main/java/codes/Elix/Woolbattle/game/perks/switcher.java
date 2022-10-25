// This class was created by Elix on 31.07.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;


public class switcher implements Listener {

    private int cost = 5;
    int taskID;
    private ArrayList<Player> available = new ArrayList<>();
    private HashMap<Player, Integer> scheduler = new HashMap<>();

    @EventHandler
    public void onSwitch(EntityDamageByEntityEvent event) {
        /*
        if(event.getDamager() instanceof Snowball) {
            System.out.println("Projectile damage");
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.hasMetadata("Freezer")) { {return;}
            Player shooter = (Player) projectile.getShooter();
            Player hitted = (Player) event.getEntity();
            Location location1 = shooter.getLocation();
            Location location2 = hitted.getLocation();
            shooter.teleport(location2);
            hitted.teleport(location1);
            shooter.playSound(shooter.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);
            hitted.playSound(hitted.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);

         */

        if ((event.getDamager() instanceof Player)) return;
        if (!(event.getDamager() instanceof Snowball)) return;

        Projectile projectile = (Projectile) event.getDamager();
        Player shooter = (Player) projectile.getShooter();
        Entity hitted = event.getEntity();

        if (projectile.hasMetadata("Freezer")) return;

        if (available.contains(shooter)) return;

        if (!(Items.amount(shooter, Material.BLACK_WOOL) >= cost)) return;
        ItemStack item = new ItemStack(Material.BLACK_WOOL);
        item.setAmount(cost);
        shooter.getInventory().removeItem(item);

        available.add(shooter);
        int slot = shooter.getInventory().getHeldItemSlot();
        visualCooldown(shooter, 10, Material.SNOWBALL, slot);

        Location location1 = shooter.getLocation();
        Location location2 = hitted.getLocation();
        shooter.teleport(location2);
        hitted.teleport(location1);
        shooter.playSound(shooter.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);
            // hitted.playSound(hitted.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);


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
                    Items.create(player.getInventory(), perk, "§3Tauscher", slot);
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
        Bukkit.getPluginManager().registerEvents(new switcher(), Woolbattle.getPlugin());
    }
    public static void disable() {}

}

