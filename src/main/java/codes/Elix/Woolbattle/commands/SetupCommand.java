// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.ConfigLocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetupCommand  implements CommandExecutor {

    private Woolbattle plugin;

    public SetupCommand(Woolbattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Woolbattle.setup")) {
                if (args.length == 0) {
                    player.sendMessage(Woolbattle.PREFIX + "§cBitte nutze §6/setup <Lobby>");
                } else
                    if (args[0].equalsIgnoreCase("lobby")) {
                        if (args.length == 1) {
                            new ConfigLocationUtil(plugin, player.getLocation(), "Lobby").saveLocation();
                            player.sendMessage(Woolbattle.PREFIX + "§aDie Lobby wurde neu gesetzt.");

                        } else
                            player.sendMessage(Woolbattle.PREFIX + "§cBitte benutze §6/setup lobby§c!");
                    }

            } else
                player.sendMessage(Woolbattle.NO_PERMISSION);
        }


        return false;
    }
}
