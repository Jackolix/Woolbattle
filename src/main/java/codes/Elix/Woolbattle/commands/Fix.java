package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.PerkItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fix implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        // Clean up stuck cooldown state before fixing inventory
        if (Items.interact.contains(player)) {
            player.sendMessage(ChatColor.YELLOW + "Clearing stuck cooldown state...");
            Items.interact.remove(player);

            // Cancel any running cooldown tasks
            if (Items.tasks.containsKey(player)) {
                Items.tasks.get(player).values().forEach(taskId ->
                    Bukkit.getScheduler().cancelTask(taskId)
                );
                Items.tasks.remove(player);
            }
        }

        player.getInventory().clear();
        Items.standartitems(player);
        PerkItems.equip(player);
        player.sendMessage(ChatColor.GREEN + "Inventory fixed!");

        return true;
    }
}
