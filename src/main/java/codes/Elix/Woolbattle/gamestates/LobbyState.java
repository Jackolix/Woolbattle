// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.util.Console;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LobbyState extends GameState {

    public static int MIN_PLAYERS;
    public static int MAX_PLAYERS;
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
        Console.send(Component.text("Game has started", NamedTextColor.GOLD));
        Console.send(" ");
    }


    public LobbyCountdown getCountdown() {
        return countdown;
    }
}
