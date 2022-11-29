package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.PerkItems;
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
        if (Items.interact.contains(player)) {
            player.sendMessage(ChatColor.RED + "You cannot do this while a Perk is on Cooldown");
            return false;
        }
        player.getInventory().clear();
        Items.standartitems(player);
        PerkItems.equip(player);

        return false;
    }
}
