package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.config.GameConfig;
import codes.Elix.Woolbattle.gui.GameSettingsGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Command executor for /gamesettings command.
 * Allows admins to configure game settings via GUI or reload configuration.
 */
public class GameSettingsCommand implements CommandExecutor, TabCompleter {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }
        
        // Check permissions
        if (!player.hasPermission("woolbattle.admin.gamesettings") && !player.isOp()) {
            player.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }
        
        // Handle subcommands
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            
            switch (subCommand) {
                case "reload" -> {
                    GameConfig.getInstance().reloadConfig();
                    player.sendMessage(Component.text("Game settings reloaded from file!", NamedTextColor.GREEN));
                    return true;
                }
                
                case "save" -> {
                    GameConfig.getInstance().saveConfig();
                    player.sendMessage(Component.text("Game settings saved to file!", NamedTextColor.GREEN));
                    return true;
                }
                
                case "help" -> {
                    sendHelpMessage(player);
                    return true;
                }
                
                default -> {
                    player.sendMessage(Component.text("Unknown subcommand. Use /gamesettings help for help.", NamedTextColor.RED));
                    return true;
                }
            }
        }
        
        // No arguments - open GUI
        GameSettingsGUI.openMainMenu(player);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Suggest subcommands
            completions.add("reload");
            completions.add("save");
            completions.add("help");
            
            // Filter by what user has typed
            String input = args[0].toLowerCase();
            completions.removeIf(completion -> !completion.startsWith(input));
        }
        
        return completions;
    }
    
    private void sendHelpMessage(Player player) {
        player.sendMessage(Component.text("═══════════════════════════════", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Game Settings Command Help", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("═══════════════════════════════", NamedTextColor.GOLD));
        player.sendMessage(Component.text(""));
        player.sendMessage(Component.text("/gamesettings", NamedTextColor.GREEN)
            .append(Component.text(" - Open settings GUI", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/gamesettings reload", NamedTextColor.GREEN)
            .append(Component.text(" - Reload config from file", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/gamesettings save", NamedTextColor.GREEN)
            .append(Component.text(" - Save current settings", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/gamesettings help", NamedTextColor.GREEN)
            .append(Component.text(" - Show this help message", NamedTextColor.GRAY)));
        player.sendMessage(Component.text(""));
        player.sendMessage(Component.text("═══════════════════════════════", NamedTextColor.GOLD));
    }
}
