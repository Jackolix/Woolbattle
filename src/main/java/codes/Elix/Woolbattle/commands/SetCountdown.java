// This class was created by Elix on 10.09.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
                                player.sendMessage(Woolbattle.PREFIX.append(Component.text("Der Countdown wurde gestoppt.", NamedTextColor.GREEN)));
                            } else {
                                lobbyState.getCountdown().setSeconds(Integer.parseInt(args[0]));
                                player.sendMessage(Woolbattle.PREFIX.append(Component.text("Der Countdown wurde auf ", NamedTextColor.GREEN)
                                        .append(Component.text(args[0], NamedTextColor.WHITE))
                                        .append(Component.text(" gesetzt.", NamedTextColor.GREEN))));
                            }
                        } else if (args[0].equalsIgnoreCase("start")) {
                            lobbyState.getCountdown().startIdle();
                            player.sendMessage(Woolbattle.PREFIX.append(Component.text("Der Countdown wurde gestartet.", NamedTextColor.GREEN)));
                        } else
                            player.sendMessage(Woolbattle.PREFIX.append(Component.text("Das Spiel ist bereits gestartet.", NamedTextColor.WHITE)));
                    } else
                        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Das Spiel ist bereits gestartet.", NamedTextColor.WHITE)));
                } else
                    player.sendMessage(Woolbattle.PREFIX.append(Component.text("Bitte benutze /countdown <amount>!", NamedTextColor.RED)));
            } else
                player.sendMessage(Component.text("Sorry, you cant do that.", NamedTextColor.RED));
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
