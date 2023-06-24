// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private Woolbattle plugin;
    private static final int START_SECONDS = 5;

    public StartCommand(Woolbattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Woolbattle.start")) {
                if (args.length == 0) {
                    if (GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState) {
                        if (lobbyState.getCountdown().isRunning() && lobbyState.getCountdown().getSeconds() > START_SECONDS) {
                            lobbyState.getCountdown().setSeconds(START_SECONDS);
                            player.sendMessage(Woolbattle.PREFIX.append(Component.text("Der Spielstart wurde beschleunigt.", NamedTextColor.GREEN)));

                        } else
                            player.sendMessage(Woolbattle.PREFIX.append(Component.text("Das Spiel ist bereits gestartet.", NamedTextColor.GREEN)));
                    } else
                        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Das Spiel ist bereits gestartet.", NamedTextColor.GREEN)));
                } else
                    player.sendMessage(Woolbattle.PREFIX.append(Component.text("Bitte benutze ", NamedTextColor.GRAY))
                            .append(Component.text("/start", NamedTextColor.GREEN)));
            } else
                player.sendMessage(Component.text("Sorry, you cant do that.", NamedTextColor.RED));
        }

        return false;
    }
}
