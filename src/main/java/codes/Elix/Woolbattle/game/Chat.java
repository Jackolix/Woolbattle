// This class was created by Elix on 24.09.22


package codes.Elix.Woolbattle.game;


import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class Chat implements Listener, CommandExecutor {

    @EventHandler
    public void onDefaultChat(AsyncPlayerChatEvent event) {
        if (GameStateManager.getCurrentGameState() instanceof IngameState) return;
        Player player = event.getPlayer();
        String msg = event.getMessage();
        event.setFormat(message(false, player, msg));
    }

    @EventHandler
    public void onIngameChat(AsyncPlayerChatEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof IngameState)) return;

        Player player = event.getPlayer();
        String msg = event.getMessage();


        if (IngameState.getSpectator().contains(player)) {
            event.setCancelled(true);
            for (Player spectators : IngameState.getSpectator()) {
                spectators.sendMessage(ChatColor.DARK_GRAY + player.getName() + ": " + msg);
                return;
            }
        }

        // im not sure if this works with "contains"
        if (msg.contains("@a")) {
            msg = msg.replace("@a", "");
            event.setFormat(message(true, player, msg));
        } else {
            ArrayList<Player> team = LiveSystem.VotedPlayers.get(player);
            event.setCancelled(true);

            if (team.size() <= 1) {
                message(true, player, msg);
                return;
            }

            for (Player teammates : team)
                teammates.sendMessage(message(false, player, msg));
        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (GameStateManager.getCurrentGameState() instanceof IngameState) {
                if (IngameState.getSpectator().contains(player)) {
                    for (Player spectators : IngameState.getSpectator()) {
                        String msg = Arrays.toString(args);
                        msg = msg.replace("[", "");
                        msg = msg.replace("]", "");
                        spectators.sendMessage(ChatColor.DARK_GRAY + player.getName() + ": " + msg);
                    }
                } else {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        String msg = Arrays.toString(args);
                        msg = msg.replace("[", "");
                        msg = msg.replace("]", "");
                        players.sendMessage(message(true, player, msg));
                    }
                }

            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    String msg = Arrays.toString(args);
                    msg = msg.replace("[", "");
                    msg = msg.replace("]", "");
                    players.sendMessage(message(true, player, msg));
                }
            }
        }
        return false;
    }

    public String message(Boolean state, Player player, String message) {

        ChatColor color;
        String stringcolor = LiveSystem.Team.get(player);

        if (stringcolor == null)
            stringcolor = "gray";

        switch (stringcolor) {
            case "red" -> color = ChatColor.RED;
            case "blue" -> color = ChatColor.BLUE;
            case "yellow" -> color = ChatColor.YELLOW;
            case "green" -> color = ChatColor.GREEN;
            default -> color = ChatColor.GRAY;
        }
        if (state)
            return color + "All: " + player.getName() + ": " + ChatColor.WHITE + message;
        return color + player.getName() + ": " + ChatColor.WHITE + message;

    }
}
