// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.countdowns;

import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.LobbyScoreboard;
import codes.Elix.Woolbattle.util.Worldloader;
import net.kyori.adventure.text.Component;
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
        seconds = COUNTDOWN_TIME;
    }

    @Override
    public void start() {
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 30, 20, 10, 5, 4, 3, 2 ->
                            Bukkit.broadcast(Component.text(Woolbattle.PREFIX + "§7Das Spiel startet in §a" + seconds + " Sekunden§7."));
                    case 1 -> Bukkit.broadcast(Component.text( Woolbattle.PREFIX + "§7Das Spiel startet in §aeiner Sekunde§7."));
                    case 0 -> {
                        Worldloader.paste(new Location(Bukkit.getServer().getWorlds().get(0), -38, 52, 15), new File("./plugins/Woolbattle/game1.schem"));
                        Woolbattle.safewool();
                        gameStateManager.setGameState(GameState.INGAME_STATE);
                        Worldloader.teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, 50, 0));
                        Worldloader.remove();
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
        idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() > 0)
                    Bukkit.broadcast(Component.text(Woolbattle.PREFIX + "§7Bis zum Spielstart fehlen noch §6" +
                            (LobbyState.MIN_PLAYERS - Woolbattle.getPlayers().size()) + " §7Spieler."));
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
