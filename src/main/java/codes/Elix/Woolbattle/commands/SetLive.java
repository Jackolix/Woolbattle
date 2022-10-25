// This class was created by Elix on 26.09.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.util.IngameScoreboard;
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

public class SetLive implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (GameStateManager.getCurrentGameState() instanceof IngameState) {
            if (sender instanceof Player player) {
                if (args.length >= 1)
                    set(args[0], Integer.parseInt(args[1]), player);
            }
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
        if (args.length == 1) {
            return completerlist;
        }
        return null;
    }


    private void set(String name, int value, Player player) {

        switch (name) {
            case "red" -> LiveSystem.TeamLifes.put(LiveSystem.TeamRed, value);
            case "blue" -> LiveSystem.TeamLifes.put(LiveSystem.TeamBlue, value);
            case "green" -> LiveSystem.TeamLifes.put(LiveSystem.TeamGreen, value);
            case "yellow" -> LiveSystem.TeamLifes.put(LiveSystem.TeamYellow, value);
            default -> player.sendMessage(ChatColor.RED + "Wrong input! Use /setlive <team> <number> instead.");
        }

        for (Player players : Bukkit.getOnlinePlayers())
            IngameScoreboard.setup(players);
    }
}
