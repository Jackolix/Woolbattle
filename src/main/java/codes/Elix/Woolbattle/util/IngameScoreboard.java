// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.items.LobbyItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class IngameScoreboard {

    public static String Colorname1 = "§bBlau";
    public static String Colorname2 = "§cRot";
    public static String Colorname3 = "§aGreen";
    public static String Colorname4 = "§eGelb";
    private static String MapName = "§3Whistles";

    public static void setup(Player player) {
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("abcd", "abcd");
        objective.setDisplayName("§f§lWOOLBATTLE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(" §7• §4︎❤ " + LiveSystem.TeamLifes.get(LiveSystem.TeamRed) + " §8- " + Colorname2).setScore(1);
        objective.getScore(" §7• §4︎❤ " + LiveSystem.TeamLifes.get(LiveSystem.TeamBlue) + " §8- " + Colorname1).setScore(2);
        if (LobbyItems.Teams >= 3)
            objective.getScore(" §7• §4❤ " + LiveSystem.TeamLifes.get(LiveSystem.TeamGreen) + " §8- " + Colorname3).setScore(3);
        if (LobbyItems.Teams >= 4)
            objective.getScore(" §7• §4❤ " + LiveSystem.TeamLifes.get(LiveSystem.TeamYellow) + " §8- " + Colorname4).setScore(4);
        objective.getScore("§8» §7Leben").setScore(5);
        objective.getScore(" ").setScore(6);
        objective.getScore(" §7• " + MapName).setScore(7);
        objective.getScore("§8» §7Map").setScore(8);
        objective.getScore("  ").setScore(9);
        objective.getScore(" §7• §8Spectator").setScore(10);
        objective.getScore("§8» §7Dein Team").setScore(11);
        objective.getScore("   ").setScore(12);
        player.setScoreboard(scoreboard);

    }

}
