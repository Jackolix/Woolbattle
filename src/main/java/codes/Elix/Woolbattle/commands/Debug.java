package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Debug implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.hasPermission("Woolbattle.debug")) return false;
        if (Woolbattle.debug) {
            Woolbattle.debug = false;
            player.sendMessage(ChatColor.RED + "Debug deactivated");
            return false;
        }
        Woolbattle.debug = true;
        player.sendMessage(ChatColor.GREEN + "Debug activated");

        return false;
    }
}
