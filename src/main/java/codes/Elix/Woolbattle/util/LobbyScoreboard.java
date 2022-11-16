// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class LobbyScoreboard {

    private static String MapName = "§3Custom";

    public static void setup(Player player) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
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

    }

}
