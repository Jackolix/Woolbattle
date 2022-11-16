package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
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
        ArrayList<Player> arrayList = null;
        Player player = null;
        switch (args[0]) {
            case "red" -> arrayList = LiveSystem.TeamRed;
            case "blue" -> arrayList = LiveSystem.TeamBlue;
            case "green" -> arrayList = LiveSystem.TeamGreen;
            case "yellow" -> arrayList = LiveSystem.TeamYellow;
            default -> args[0] = null;
        }

        if (args[0] == null) return false;
        if (arrayList == null) return false;

        if (sender instanceof Player) {
            if (args.length == 1) {
                player = (Player) sender;
                System.out.println(player + " is the player himself");

            } else if (args.length == 2) {
                player = Bukkit.getPlayer(args[1]);
                player.sendMessage(ChatColor.GREEN + "The Player " +  args[1] + "is now in Team " + Items.getColor(args[0]) + args[0]);
                System.out.println(player + " is another player");
            } else
                sender.sendMessage("Use /switchteam <team> (player)");
        } else if (args.length == 2) {
            player = Bukkit.getPlayer(args[1]);
            System.out.println(args[1] + " is another player");
            player.sendMessage(ChatColor.GREEN + "The Player " +  args[1] + "is now in Team " + Items.getColor(args[0]) + args[0]);
        } else
            sender.sendMessage("Use /switchteam <team> <player>");

        if (player == null) return false;
        arrayList.add(player);
        ArrayList<Player> team = LiveSystem.VotedPlayers.get(player);
        if (team != null)
            team.remove(player);
        LiveSystem.Team.put(player, args[0]);
        LiveSystem.VotedPlayers.put(player, arrayList);

        IngameState.teamupdate();
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
