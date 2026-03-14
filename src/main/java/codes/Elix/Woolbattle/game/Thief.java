package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.items.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class Thief implements Listener {

    // Key: "attackerUUID:victimUUID", Value: hit count
    private static final HashMap<String, Integer> hitCounters = new HashMap<>();

    private static PerkConfig.PassivePerkSettings getSettings() {
        return PerkConfig.getInstance().getPassivePerkSettings("thief");
    }

    public static boolean hasThiefPerk(Player player) {
        String passive = Items.perks.get(player) != null
                ? Items.perks.get(player).getpassivePerk() : null;
        return Objects.equals(passive, "thief");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        Player attacker = null;

        if (event.getDamager() instanceof Arrow arrow) {
            if (arrow.getShooter() instanceof Player p) attacker = p;
        } else if (event.getDamager() instanceof Player p) {
            Material held = p.getInventory().getItemInMainHand().getType();
            if (held == Material.SHEARS) attacker = p;
        }

        if (attacker == null || attacker == victim) return;
        if (!hasThiefPerk(attacker)) return;

        PerkConfig.PassivePerkSettings settings = getSettings();
        String key = attacker.getUniqueId() + ":" + victim.getUniqueId();
        int count = hitCounters.getOrDefault(key, 0) + 1;

        if (count < settings.getThiefHitsToSteal()) {
            hitCounters.put(key, count);
            attacker.sendActionBar("§6Thief: §e" + count + "§8/§e" + settings.getThiefHitsToSteal() + " §7hits");
            return;
        }

        // Nth hit — trigger steal, reset counter
        hitCounters.put(key, 0);

        int stealAmount = settings.getThiefWoolStealAmount();
        int victimWool = Items.woolAmount(victim);
        int actualSteal = Math.min(stealAmount, victimWool);
        if (actualSteal <= 0) {
            attacker.sendActionBar("§6Thief: §7victim has no wool!");
            return;
        }

        Items.cost(victim, actualSteal);

        Material attackerWool = Items.getWoolColor(attacker);
        if (attackerWool != null) {
            int canReceive = Math.max(0, WoolGenerator.getWoolLimit(attacker) - Items.woolAmount(attacker));
            int toAdd = Math.min(actualSteal, canReceive);
            if (toAdd > 0) {
                attacker.getInventory().addItem(new ItemStack(attackerWool, toAdd));
                codes.Elix.Woolbattle.game.DoubleJump.updateFlightBasedOnWool(attacker);
            }
        }

        attacker.sendActionBar("§6Thief: §estole §c" + actualSteal + " §ewool!");
        victim.sendActionBar("§cThief stole §e" + actualSteal + " §cwool from you!");
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.5f);
    }

    public static void cleanup() {
        hitCounters.clear();
    }
}
