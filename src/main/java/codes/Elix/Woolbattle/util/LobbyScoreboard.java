// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class LobbyScoreboard {

    private final static String MapName = "§3Custom";
    private static final Map<Player, FastBoard> boards = new HashMap<>();

    public static void setup(Player player) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        
        try {
            // Try FastBoard first
            FastBoard board = new FastBoard(player);
            board.updateTitle("§f§lWOOLBATTLE");
            
            board.updateLines(
                " ",
                " §7• §3" + Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS,
                "§8» §7Spieler",
                "  ",
                " §7• §3" + lobbyState.getCountdown().getSeconds(),
                "§8» §7Wartezeit",
                "   ",
                " §7• " + MapName,
                "§8» §7Current Map",
                "    ",
                " §7• " + IngameScoreboard.team(player),
                "§8» §7Dein Team",
                "     "
            );
            
            boards.put(player, board);
        } catch (Exception e) {
            // Fallback to vanilla Bukkit scoreboard
            setupVanillaScoreboard(player, lobbyState);
        }
    }
    
    private static void setupVanillaScoreboard(Player player, LobbyState lobbyState) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("woolbattle", "dummy");
        objective.setDisplayName("§f§lWOOLBATTLE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        objective.getScore("     ").setScore(12);
        objective.getScore("§8» §7Dein Team").setScore(11);
        objective.getScore(" §7• " + IngameScoreboard.team(player)).setScore(10);
        objective.getScore("    ").setScore(9);
        objective.getScore("§8» §7Current Map").setScore(8);
        objective.getScore(" §7• " + MapName).setScore(7);
        objective.getScore("   ").setScore(6);
        objective.getScore("§8» §7Wartezeit").setScore(5);
        objective.getScore(" §7• §3" + lobbyState.getCountdown().getSeconds()).setScore(4);
        objective.getScore("  ").setScore(3);
        objective.getScore("§8» §7Spieler").setScore(2);
        objective.getScore(" §7• §3" + Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS).setScore(1);
        objective.getScore(" ").setScore(0);
        
        player.setScoreboard(scoreboard);
    }

    public static void change(Player player) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        
        try {
            for (Map.Entry<Player, FastBoard> entry : boards.entrySet()) {
                FastBoard board = entry.getValue();
                Player boardPlayer = entry.getKey();
                
                board.updateLines(
                    " ",
                    " §7• §3" + Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS,
                    "§8» §7Spieler",
                    "  ",
                    " §7• §3" + lobbyState.getCountdown().getSeconds(),
                    "§8» §7Wartezeit",
                    "   ",
                    " §7• " + MapName,
                    "§8» §7Current Map",
                    "    ",
                    " §7• " + IngameScoreboard.team(boardPlayer),
                    "§8» §7Dein Team",
                    "     "
                );
            }
        } catch (Exception e) {
            // If FastBoard fails, update will require recreation with vanilla API
            // For simplicity, just ignore updates in fallback mode
        }
    }

    public static void remove(Player player) {
        FastBoard board = boards.remove(player);
        if (board != null) {
            board.delete();
        }
    }

}
