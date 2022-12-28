// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import com.xism4.sternalboard.SternalBoardHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LobbyScoreboard {

    private final static String MapName = "§3Custom";
    static ArrayList<SternalBoardHandler> boards = new ArrayList<>();

    public static void setup(Player player) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        /*
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("abcd", "abcd");
        objective.setDisplayName("§f§lWOOLBATTLE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(" ").setScore(1);
        objective.getScore(" §7• §3" + Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS).setScore(2);
        objective.getScore("§8» §7Spieler").setScore(3);
        objective.getScore("  ").setScore(4);

        if (lobbyState.getCountdown().isRunning()) {
            objective.getScore(" §7• §3" + lobbyState.getCountdown().getSeconds()).setScore(5);
        } else
            objective.getScore(" §7• §3" + LobbyCountdown.COUNTDOWN_TIME).setScore(5);

        objective.getScore("§8» §7Wartezeit").setScore(6);
        objective.getScore("   ").setScore(7);
        objective.getScore(" §7• " + MapName).setScore(8);
        objective.getScore("§8» §7Current Map").setScore(9);
        objective.getScore("    ").setScore(10);
        objective.getScore(" §7• " + IngameScoreboard.team(player)).setScore(11);
        objective.getScore("§8» §7Dein Team").setScore(12);
        objective.getScore("     ").setScore(13);
        player.setScoreboard(scoreboard);
         */
        SternalBoardHandler board = new SternalBoardHandler(player);
        board.updateTitle("§f§lWOOLBATTLE");
        board.updateLine(0, "     ");
        board.updateLine(1, "§8» §7Dein Team");
        board.updateLine(2, " §7• " + IngameScoreboard.team(player));
        board.updateLine(3, "    ");
        board.updateLine(4, "§8» §7Current Map");
        board.updateLine(5, " §7• " + MapName);
        board.updateLine(6, "   ");
        board.updateLine(7, "§8» §7Wartezeit");
        board.updateLine(8, " §7• §3" + lobbyState.getCountdown().getSeconds());
        board.updateLine(9, "  ");
        board.updateLine(10, "§8» §7Spieler");
        board.updateLine(11, " §7• §3" + Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS);
        board.updateLine(12, " ");
        boards.add(board);
    }

    public static void change(Player player) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        for (SternalBoardHandler boar : boards) {
            boar.updateLine(8, " §7• §3" +  lobbyState.getCountdown().getSeconds());
            boar.updateLine(2, " §7• " + IngameScoreboard.team(player));
        }
    }

}
