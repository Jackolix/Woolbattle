// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.countdowns;

import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.items.MapVoting;
import codes.Elix.Woolbattle.util.LobbyScoreboard;
import codes.Elix.Woolbattle.util.SchematicManager;
import codes.Elix.Woolbattle.util.Worldloader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public class LobbyCountdown extends Countdown{

    public static final int COUNTDOWN_TIME = 30, IDLE_TIME = 30;
    private final GameStateManager gameStateManager;
    private int seconds;
    private int idleID;
    private boolean isIdling;
    private boolean isRunning;
    public LobbyCountdown(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        // seconds = COUNTDOWN_TIME;
        seconds = Woolbattle.getPlugin().getConfig().getInt("LobbyCountdown");
    }

    @Override
    public void start() {
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                TextComponent countdown = Woolbattle.PREFIX.append(Component.text("Das Spiel startet in ", NamedTextColor.GRAY)
                        .append(Component.text(seconds, NamedTextColor.GREEN))
                        .append(Component.text(" Sekunden.", NamedTextColor.GRAY)));
                TextComponent lastSecond = Woolbattle.PREFIX.append(Component.text("Das Spiel startet in ")
                        .append(Component.text(" einer ", NamedTextColor.GREEN))
                        .append(Component.text("Sekunde.", NamedTextColor.GRAY)));

                switch (seconds) {
                    case 30, 20, 10, 5, 4, 3, 2 ->
                            Bukkit.broadcast(countdown);
                    case 1 -> Bukkit.broadcast(lastSecond);
                    case 0 -> {
                        // Finalize map voting and determine winner
                        String winningMap = MapVoting.getWinningMap();
                        SchematicManager.selectMap(winningMap);
                        
                        // Use config-based loading for the game map
                        SchematicManager.loadSchematicWithConfig(winningMap);
                        System.out.println("[Woolbattle] Loading map with config: " + winningMap);
                        Woolbattle.safewool();
                        gameStateManager.setGameState(GameState.INGAME_STATE);
                        
                        // Remove lobby map before loading game map
                        Location[] lobbyBounds = SchematicManager.getLobbyBounds();
                        Worldloader.remove(lobbyBounds[0], lobbyBounds[1]);
                    }
                }
                seconds--;
                for (Player current : Bukkit.getOnlinePlayers()) {
                   LobbyScoreboard.change(current); // LobbyScoreboard.setup(current);
                }

            }
        }, 0, 20);

    }

    @Override
    public void stop() {

        if (isRunning) {
            Bukkit.getScheduler().cancelTask(taskID);
            isRunning = false;
            seconds = COUNTDOWN_TIME;
        }
    }

    public void startIdle() {
        isIdling = true;
        idleID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    final TextComponent message = Woolbattle.PREFIX
                            .append(Component.text("Bis zum Spielstart fehlen noch ", NamedTextColor.GRAY))
                            .append(Component.text(LobbyState.MIN_PLAYERS - Woolbattle.getPlayers().size(), NamedTextColor.GREEN))
                            .append(Component.text(" Spieler.", NamedTextColor.GRAY));
                    Bukkit.broadcast(message);
                }
            }
        }, 0, 20* IDLE_TIME);

    }

    public void stopIdle() {
        if (isIdling) {
            Bukkit.getScheduler().cancelTask(idleID);
            isIdling = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getSeconds() {
        if (isRunning)
            return seconds;
        return COUNTDOWN_TIME;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
