// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import org.bukkit.Bukkit;

public class LobbyState extends GameState {

    public static final int MIN_PLAYERS = 1, MAX_PLAYERS = 8;
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
        Bukkit.broadcastMessage("Das Spiel ist gestartet");
    }


    public LobbyCountdown getCountdown() {
        return countdown;
    }
}
