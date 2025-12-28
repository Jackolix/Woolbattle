package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.config.PerkConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadPerks implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be executed by players.", NamedTextColor.RED));
            return true;
        }
        
        if (!player.hasPermission("woolbattle.admin.reload") && !player.isOp()) {
            player.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }
        
        try {
            // Reload the perk configuration
            PerkConfig.getInstance().reloadConfig();
            
            player.sendMessage(Component.text("Perk configuration reloaded successfully!", NamedTextColor.GREEN));
            player.sendMessage(Component.text("All perks will now use the updated settings.", NamedTextColor.GRAY));
            
        } catch (Exception e) {
            player.sendMessage(Component.text("Error reloading perk configuration: " + e.getMessage(), NamedTextColor.RED));
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}