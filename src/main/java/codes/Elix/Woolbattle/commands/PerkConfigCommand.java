package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.gui.PerkConfigGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PerkConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be executed by a player!", NamedTextColor.RED));
            return true;
        }

        if (!player.hasPermission("woolbattle.admin.config") && !player.isOp()) {
            player.sendMessage(Component.text("You don't have permission to access perk configuration.", NamedTextColor.RED));
            return true;
        }

        PerkConfigGUI.openMainConfigGUI(player);
        return true;
    }
}
