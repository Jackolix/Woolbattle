package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.PerkItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Switchteam implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Team team = null;
        Player player = null;
        boolean isSpectator = false;

        switch (args[0]) {
            case "red" -> team = LiveSystem.Team.get("red");
            case "blue" -> team = LiveSystem.Team.get("blue");
            case "green" -> team = LiveSystem.Team.get("green");
            case "yellow" -> team = LiveSystem.Team.get("yellow");
            case "spectator" -> isSpectator = true;
            default -> args[0] = null;
        }

        if (args[0] == null) return false;
        if (team == null && !isSpectator) return false;

        if (sender instanceof Player) {
            if (args.length == 1) {
                player = (Player) sender;
                Console.send(player + " is the player himself");
            } else if (args.length == 2) {
                player = Bukkit.getPlayer(args[1]);
                if (isSpectator) {
                    player.sendMessage(ChatColor.GREEN + "The Player " + args[1] + " is now a Spectator");
                } else {
                    player.sendMessage(ChatColor.GREEN + "The Player " + args[1] + "is now in Team " + Items.getColor(args[0]) + args[0]);
                }
                Console.send(player + " is another player");
            } else
                sender.sendMessage("Use /switchteam <team> (player)");
        } else if (args.length == 2) {
            player = Bukkit.getPlayer(args[1]);
            Console.send(args[1] + " is another player");
            if (isSpectator) {
                player.sendMessage(ChatColor.GREEN + "The Player " + args[1] + " is now a Spectator");
            } else {
                player.sendMessage(ChatColor.GREEN + "The Player " + args[1] + "is now in Team " + Items.getColor(args[0]) + args[0]);
            }
        } else
            sender.sendMessage("Use /switchteam <team> <player>");

        if (player == null) return false;
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);

        // Remove from spectator list if currently a spectator
        if (IngameState.spectator.contains(player)) {
            IngameState.spectator.remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
            // Make player visible to everyone again
            for (Player current : Bukkit.getOnlinePlayers())
                current.showPlayer(Woolbattle.getPlugin(), player);
        }

        if (isSpectator) {
            // Switch to spectator mode
            if (customPlayer.getTeam() != null) {
                customPlayer.getTeam().removeMembers(player);
            }
            IngameState.addSpectator(player);
            player.sendMessage(ChatColor.GREEN + "You are now a Spectator");
        } else {
            // Switch to a game team
            if (customPlayer.getTeam() != null) {
                customPlayer.getTeam().removeMembers(player);
            }
            team.addMember(player);
            customPlayer.setTeam(team);

            // IngameState.teamUpdate();
            for (Player current : Bukkit.getOnlinePlayers())
                LobbyScoreboard.change(current);
            player.getInventory().clear();
            Items.standartitems(player);
            PerkItems.equip(player);
            IngameState.boots();

            player.sendMessage(ChatColor.GREEN + "You are now in Team " + Items.getColor(args[0]) + args[0]);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> completerlist = new ArrayList<>();
        completerlist.add("red");
        completerlist.add("blue");
        completerlist.add("green");
        completerlist.add("yellow");
        completerlist.add("spectator");
        if (args.length == 1)
            return completerlist;
        return null;
    }
}
