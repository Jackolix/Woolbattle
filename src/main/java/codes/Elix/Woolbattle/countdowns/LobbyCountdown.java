// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.countdowns;

import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;

public class LobbyCountdown extends Countdown{

    private static final int COUNTDOWN_TIME = 30, IDLE_TIME = 30;
    private GameStateManager gameStateManager;
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
                    case 30: case 20: case 10: case 5: case 3: case 2:
                        Bukkit.broadcastMessage(Woolbattle.PREFIX + "§7Das Spiel startet in §a" + seconds + " Sekunden§7.");
                        break;
                    case 1:
                        Bukkit.broadcastMessage(Woolbattle.PREFIX + "§7Das Spiel startet in §aeiner Sekunde§7.");
                        break;
                    case 0:
                        gameStateManager.setGameState(GameState.INGAME_STATE);
                        break;
                    default:
                        break;
                }
                seconds--;
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
                Bukkit.broadcastMessage(Woolbattle.PREFIX + "§7Bis zum Spielstart fehlen noch §6" +
                        (LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getPlayers().size()) + " §7Spieler.");
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
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}