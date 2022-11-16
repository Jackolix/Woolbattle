// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LobbyState extends GameState {

    public static final int MIN_PLAYERS = 1, MAX_PLAYERS = 4;
    private final LobbyCountdown countdown;

    public LobbyState(GameStateManager gameStateManager) {
        countdown = new LobbyCountdown(gameStateManager);
    }

    @Override
    public void start() {
        countdown.startIdle();
    }

    @Override
    public void stop() {
        Console.send(ChatColor.GOLD + "Game has started");
        Console.send(" ");
    }


    public LobbyCountdown getCountdown() {
        return countdown;
    }
}
