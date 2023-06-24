// This class was created by Elix on 24.09.22


package codes.Elix.Woolbattle.game;


import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
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

import java.util.Arrays;
import java.util.List;


public class Chat implements Listener, CommandExecutor {

    @EventHandler
    public void onDefaultChat(AsyncPlayerChatEvent event) {
        if (GameStateManager.getCurrentGameState() instanceof IngameState) return;
        Player player = event.getPlayer();
        String msg = event.getMessage();
        send(false, player, msg);
        event.setCancelled(true);
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

        if (msg.contains("@a")) {
            msg = msg.replace("@a ", "");
            send(true, player, msg);
        } else {
            CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
            List<Player> team = customPlayer.getTeam().getMembers();

            if (team.size() <= 1) {
                send(true, player, msg);
                event.setCancelled(true);
                return;
            }

            for (Player teammates : team)
                teammates.sendMessage(message(false, player, msg));
        }
        event.setCancelled(true);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            String msg = Arrays.toString(args);
            msg = msg.replace("[", "");
            msg = msg.replace("]", "");

            if (GameStateManager.getCurrentGameState() instanceof IngameState) {
                if (IngameState.getSpectator().contains(player)) {
                    for (Player spectators : IngameState.getSpectator())
                        spectators.sendMessage(ChatColor.DARK_GRAY + player.getName() + ": " + msg);
                } else
                    send(true, player, msg);
            } else
                send(true, player, msg);
        }
        return false;
    }

    public void send(Boolean state, Player player, String message) {
        CustomPlayer p = CustomPlayer.getCustomPlayer(player);
        String prefix = "§7";
        if (p.getTeam() != null)
            prefix = p.getTeam().getPREFIX();

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (state) {
                players.sendMessage(prefix + "All : " + player.getName() + " : " + ChatColor.WHITE + message);
                continue;
            }
            players.sendMessage(prefix + player.getName() + " : " + ChatColor.WHITE + message);




        }
    }
    public String message(Boolean state, Player player, String message) {
        CustomPlayer p = CustomPlayer.getCustomPlayer(player);
        String prefix = "§7";
        if (p.getTeam() != null)
            prefix = p.getTeam().getPREFIX();

        if (state)
            return p.getTeam().getPREFIX() + "All : " + player.getName() + " : " + ChatColor.WHITE + message;
        return p.getTeam().getPREFIX() + player.getName() + " : " + ChatColor.WHITE + message;
    }
}
