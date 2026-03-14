package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class WoolGenerator implements Listener {

    public static HashMap<Player, BukkitRunnable> generatorTasks = new HashMap<>();

    public static final int BASE_WOOL_LIMIT = 196;
    public static final int EXTRA_STACK = 64;

    private static PerkConfig.PassivePerkSettings getSettings() {
        return PerkConfig.getInstance().getPassivePerkSettings("generator");
    }

    public static boolean hasGeneratorPerk(Player player) {
        String passive = Items.perks.get(player) != null
                ? Items.perks.get(player).getpassivePerk() : null;
        return Objects.equals(passive, "generator");
    }

    public static int getWoolLimit(Player player) {
        String passive = Items.perks.get(player) != null
                ? Items.perks.get(player).getpassivePerk() : null;
        if (Objects.equals(passive, "generator") || Objects.equals(passive, "thief")) {
            return BASE_WOOL_LIMIT + EXTRA_STACK;
        }
        return BASE_WOOL_LIMIT;
    }

    private static void startGeneratorTask(Player player) {
        PerkConfig.PassivePerkSettings settings = getSettings();
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    generatorTasks.remove(player);
                    return;
                }
                Material wool = Items.getWoolColor(player);
                if (wool == null) return;

                int current = Items.woolAmount(player);
                int limit = getWoolLimit(player);
                if (current >= limit) return;

                int toAdd = Math.min(settings.getGeneratorBlocksPerInterval(), limit - current);
                ItemStack woolItem = new ItemStack(wool, toAdd);
                player.getInventory().addItem(woolItem);

                // Visual feedback
                player.getWorld().spawnParticle(
                        Particle.HAPPY_VILLAGER,
                        player.getLocation().add(0, 1, 0),
                        3, 0.3, 0.3, 0.3, 0);
            }
        };
        task.runTaskTimer(Woolbattle.getPlugin(), 0L, settings.getGeneratorIntervalTicks());
        generatorTasks.put(player, task);
    }

    public static void enable() {
        cleanup();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasGeneratorPerk(player)) {
                startGeneratorTask(player);
            }
        }
    }

    public static void cleanup() {
        for (BukkitRunnable task : generatorTasks.values()) {
            if (task != null && !task.isCancelled()) task.cancel();
        }
        generatorTasks.clear();
    }
}
