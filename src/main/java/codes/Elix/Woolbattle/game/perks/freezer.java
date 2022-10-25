package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

public class freezer implements Listener {

    private int cooldown = 4;
    private int freeze_time_sec = 5;
    private int slowness_strength = 5;
    private int cost = 5;
    int taskID;


    private ArrayList<Player> available = new ArrayList<>();
    private HashMap<Player, Integer> scheduler = new HashMap<>();

    @EventHandler
    public void onFreezerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (available.contains(event.getPlayer()))  return;
        if (event.getItem().getType() == Material.PACKED_ICE) {
            Player player = event.getPlayer();

            if (!(Items.amount(player, Material.BLACK_WOOL) >= cost)) return;
            ItemStack item = new ItemStack(Material.BLACK_WOOL);
            item.setAmount(cost);
            player.getInventory().removeItem(item);

            available.add(player);

            Projectile snowball = player.launchProjectile(Snowball.class);
            snowball.setMetadata("Freezer", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));

            int slot = player.getInventory().getHeldItemSlot();
            visualCooldown(player, cooldown, Material.PACKED_ICE, slot);
        }
    }

    @EventHandler
    public void onFreezerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Snowball)) return;
        if (!(e.getEntity() instanceof Player player)) return;

        Projectile snowball = (Projectile) e.getDamager();

        if (!(snowball.hasMetadata("Freezer"))) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, freeze_time_sec * 20, slowness_strength));
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
                    Items.create(player.getInventory(), perk, "§3Freezer", slot);
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
    public static void enable() { Bukkit.getPluginManager().registerEvents(new freezer(), Woolbattle.getPlugin()); }
    public static void disable() {}
}