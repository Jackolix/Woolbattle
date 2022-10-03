// This class was created by Elix on 10.09.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetCountdown implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Woolbattle.setcountdown")) {
                if (args.length == 1) {
                    if (GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState) {
                        if (lobbyState.getCountdown().isRunning()) {
                            if (args[0].equalsIgnoreCase("stop")) {
                                lobbyState.getCountdown().stopIdle();
                                player.sendMessage(Woolbattle.PREFIX + "§cDer Countdown wurde gestoppt.");
                            } else {
                                lobbyState.getCountdown().setSeconds(Integer.parseInt(args[0]));
                                player.sendMessage(Woolbattle.PREFIX + "§aDer Countdown wurde auf " + args[0] + " gesetzt.");
                            }
                        } else if (args[0].equalsIgnoreCase("start")) {
                            lobbyState.getCountdown().startIdle();
                            player.sendMessage(Woolbattle.PREFIX + "§cDer Countdown wurde gestartet.");
                        } else
                            player.sendMessage(Woolbattle.PREFIX + "§cDas Spiel ist bereits gestartet.");
                    } else
                        player.sendMessage(Woolbattle.PREFIX + "§cDas Spiel ist bereits gestartet.");
                } else
                    player.sendMessage(Woolbattle.PREFIX + "§cBitte benutze §6/countdown <amount>§c!");
            } else
                player.sendMessage(Woolbattle.NO_PERMISSION);
        } else {
            if (args.length == 1) {
                if (GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState) {
                    if (lobbyState.getCountdown().isRunning()) {
                        if (args[0].equalsIgnoreCase("stop")) {
                            lobbyState.getCountdown().stopIdle();
                            Console.send("§cDer Countdown wurde gestoppt.");
                        } else {
                            lobbyState.getCountdown().setSeconds(Integer.parseInt(args[0]));
                            Console.send("§aDer Countdown wurde auf " + args[0] + " gesetzt.");
                        }
                    } else if (args[0].equalsIgnoreCase("start")) {
                        lobbyState.getCountdown().startIdle();
                        Console.send("§cDer Countdown wurde gestartet.");
                    } else
                        Console.send("§cDas Spiel ist bereits gestartet.");
                } else
                    Console.send("§cDas Spiel ist bereits gestartet.");
            } else
                Console.send("§cBitte benutze §6/countdown <amount>§c!");
        }

        return false;
    }
}
