// This class was created by Elix on 01.10.22


package codes.Elix.Woolbattle.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Build implements CommandExecutor {

    public static ArrayList<Player> BuildPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("Woolbattle.build")) {
                if(args.length == 0) {
                    if (!(BuildPlayers.contains(player))) {
                        BuildPlayers.add(player);
                        player.sendMessage("§2Build activated!");
                    } else {
                        BuildPlayers.remove(player);
                        player.sendMessage("§2Build disabled!");
                    }
                } else if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!(BuildPlayers.contains(target))) {
                            BuildPlayers.add(target);
                            target.sendMessage("§aBuild activated!");
                            player.sendMessage("§aBuild for the player §5" + target.getName() + " §adeactivated");
                        } else {
                            BuildPlayers.remove(target);
                            target.sendMessage("§2Build deactivated");
                            player.sendMessage("§aBuild for the player §5" + target.getName() + " §adeactivated");
                        }
                    } else
                        player.sendMessage("§cThe player §6" + args[0] + " §cis not online.");
                } else
                    player.sendMessage("§cPlease use §6/build <PLAYER>§c!");
            } else
                player.sendMessage("§cNo permissions!");
        }
        return false;
    }
}
