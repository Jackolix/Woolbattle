package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
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
        switch (args[0]) {
            case "red" -> team = LiveSystem.Team.get("red");
            case "blue" -> team = LiveSystem.Team.get("blue");
            case "green" -> team = LiveSystem.Team.get("green");
            case "yellow" -> team = LiveSystem.Team.get("yellow");
            default -> args[0] = null;
        }

        if (args[0] == null) return false;
        if (team == null) return false;

        if (sender instanceof Player) {
            if (args.length == 1) {
                player = (Player) sender;
                Console.send(player + " is the player himself");
            } else if (args.length == 2) {
                player = Bukkit.getPlayer(args[1]);
                player.sendMessage(ChatColor.GREEN + "The Player " +  args[1] + "is now in Team " + Items.getColor(args[0]) + args[0]);
                Console.send(player + " is another player");
            } else
                sender.sendMessage("Use /switchteam <team> (player)");
        } else if (args.length == 2) {
            player = Bukkit.getPlayer(args[1]);
            Console.send(args[1] + " is another player");
            player.sendMessage(ChatColor.GREEN + "The Player " +  args[1] + "is now in Team " + Items.getColor(args[0]) + args[0]);
        } else
            sender.sendMessage("Use /switchteam <team> <player>");

        if (player == null) return false;
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        customPlayer.getTeam().removeMembers(player);
        team.addMember(player);
        /*
        ArrayList<Player> team = LiveSystem.VotedPlayers.get(player);
        if (team != null)
            team.remove(player);
        LiveSystem.Team.put(player, args[0]);
        LiveSystem.VotedPlayers.put(player, arrayList);
         */
        customPlayer.setTeam(team);

        // IngameState.teamUpdate();
        for (Player current : Bukkit.getOnlinePlayers())
            LobbyScoreboard.change(current);
        IngameState.boots();

        player.sendMessage(ChatColor.GREEN + "You are now in Team " + Items.getColor(args[0]) + args[0]);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> completerlist = new ArrayList<>();
        completerlist.add("red");
        completerlist.add("blue");
        completerlist.add("green");
        completerlist.add("yellow");
        if (args.length == 1)
            return completerlist;
        return null;
    }
}
