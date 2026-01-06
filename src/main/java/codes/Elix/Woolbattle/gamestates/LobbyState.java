// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.items.MapVoting;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.LobbyTabList;
import codes.Elix.Woolbattle.util.TeamColorManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyState extends GameState {

    public static int MIN_PLAYERS;
    public static int MAX_PLAYERS;
    private final LobbyCountdown countdown;

    public LobbyState(GameStateManager gameStateManager) {
        countdown = new LobbyCountdown(gameStateManager);
    }

    @Override
    public void start() {
        MapVoting.clearVotes(); // Clear previous votes when returning to lobby
        countdown.startIdle();

        // Clear team colors from previous game
        TeamColorManager.clearAll();

        // Setup tab lists for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            LobbyTabList.setup(player);
        }
    }

    @Override
    public void stop() {
        // Remove lobby tab lists
        for (Player player : Bukkit.getOnlinePlayers()) {
            LobbyTabList.remove(player);
        }

        Console.send(Component.text("Game has started", NamedTextColor.GOLD));
        Console.send(" ");
    }


    public LobbyCountdown getCountdown() {
        return countdown;
    }
}
