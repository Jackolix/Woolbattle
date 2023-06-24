// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.main.Woolbattle;

public class GameStateManager {

    private Woolbattle plugin;
    private GameState[] gamestates;
    private static GameState currentGameState;

    public GameStateManager(Woolbattle plugin) {
        this.plugin = plugin;
        gamestates = new GameState[3];
        LobbyState.MIN_PLAYERS = Woolbattle.getPlugin().getConfig().getInt("PlayersToStartCountdown");
        LobbyState.MAX_PLAYERS = Woolbattle.getPlugin().getConfig().getInt("MaxPlayers");

        gamestates[GameState.LOBBY_STATE] = new LobbyState(this);
        gamestates[GameState.INGAME_STATE] = new IngameState();
        gamestates[GameState.ENDING_STATE] = new EndingState();
    }

    public void setGameState(int gameStateID) {
        if (currentGameState != null)
            currentGameState.stop();
        currentGameState = gamestates[gameStateID];
        currentGameState.start();
    }

    public void stopCurrentGameState() {
        if (currentGameState != null) {
            currentGameState.stop();
            currentGameState = null;
        }
    }

    public static GameState getCurrentGameState() {
        return currentGameState;
    }

    public Woolbattle getPlugin() {
        return plugin;
    }
}
