// This class was created by Elix on 26.09.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetLive implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(GameStateManager.getCurrentGameState() instanceof IngameState)) return false;
        if (!(sender instanceof Player player)) return false;
        if (!(args.length >= 1)) return false;
        /*
        if (!args[0].equals("red")) {
            player.sendMessage(Component.text(ChatColor.RED + "This is not a Team"));
            return false;
        }
         */

        LiveSystem.Team.get(args[0]).setLifes(Integer.parseInt(args[1]));
        //LiveSystem.TeamLifes.put(args[0], Integer.parseInt(args[1]));
        for (Player players : Bukkit.getOnlinePlayers())
            IngameScoreboard.setup(players);
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
