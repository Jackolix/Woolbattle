// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.items.LobbyItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class IngameScoreboard {

    private static int Team1 = LobbyItems.VotedLives;
    private static int Team2 = LobbyItems.VotedLives;
    private static Integer Team3 = LobbyItems.VotedLives;
    private static Integer Team4 = LobbyItems.VotedLives;
    private static String Colorname1 = "§bBlau";
    private static String Colorname2 = "§cRot";
    private static String Colorname3 = "§aGreen";
    private static String Colorname4 = "§eGelb";

    public static void setup(Player player) {
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("abcd", "abcd");
        objective.setDisplayName("§f§lWOOLBATTLE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        if (Team4 != null){
            objective.getScore("§6» §4❤ " + Team4 + " §4❤" + " §6« " + Colorname4).setScore(4);
        }
        if (Team3 != null){
            objective.getScore("§6» §4❤ " + Team3 + " §4❤" + " §6« " + Colorname3).setScore(3);
        }
        objective.getScore("§6» §4❤ " + Team1 + " §4❤" + " §6« " + Colorname1).setScore(2);
        objective.getScore("§6» §4❤ " + Team2 + " §4❤" + " §6« " + Colorname2).setScore(1);
        player.setScoreboard(scoreboard);

    }

}
