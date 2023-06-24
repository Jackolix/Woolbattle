// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.LiveSystem;
import com.xism4.sternalboard.SternalBoardHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IngameScoreboard {

    public static String Colorname1 = "§cRot";
    public static String Colorname2 = "§bBlau";
    public static String Colorname3 = "§aGreen";
    public static String Colorname4 = "§eGelb";
    private static final String MapName = "§3Custom";

    public static void setup(Player player) {
        /*
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("abcd", "abcd");
        objective.setDisplayName("§f§lWOOLBATTLE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(" §7• §4❤ " + LiveSystem.TeamLifes.get("red") + " §8- " + Colorname2).setScore(1);
        objective.getScore(" §7• §4❤ " + LiveSystem.TeamLifes.get("blue") + " §8- " + Colorname1).setScore(2);
        if (LiveSystem.Teams >= 3)
            objective.getScore(" §7• §4❤ " + LiveSystem.TeamLifes.get("green") + " §8- " + Colorname3).setScore(3);
        if (LiveSystem.Teams >= 4)
            objective.getScore(" §7• §4❤ " + LiveSystem.TeamLifes.get("yellow") + " §8- " + Colorname4).setScore(4);
        objective.getScore("§8» §7Leben").setScore(5);
        objective.getScore(" ").setScore(6);
        objective.getScore(" §7• " + MapName).setScore(7);
        objective.getScore("§8» §7Map").setScore(8);
        objective.getScore("  ").setScore(9);
        objective.getScore(" §7• " + team(player)).setScore(10);
        objective.getScore("§8» §7Dein Team").setScore(11);
        objective.getScore("   ").setScore(12);
        player.setScoreboard(scoreboard);
         */

        SternalBoardHandler board = new SternalBoardHandler(player);
        board.updateTitle("§f§lWOOLBATTLE");
        board.updateLine(0, "     ");
        board.updateLine(1, "§8» §7Dein Team");
        board.updateLine(2, " §7• " + team(player));
        board.updateLine(3, "    ");
        board.updateLine(4, "§8» §7Current Map");
        board.updateLine(5, " §7• " + MapName);
        board.updateLine(6, "     ");
        board.updateLine(7, "§8» §7Leben");
        board.updateLine(8, " §7• §4❤ " + LiveSystem.Team.get("red").getLifes() + " §8- " + Colorname1);
        board.updateLine(9, " §7• §4❤ " + LiveSystem.Team.get("blue").getLifes() + " §8- " + Colorname2);
        if (LiveSystem.Teams >=3)
            board.updateLine(10, " §7• §4❤ " + LiveSystem.Team.get("green").getLifes() + " §8- " + Colorname3);
        if (LiveSystem.Teams >=4)
            board.updateLine(11, " §7• §4❤ " + LiveSystem.Team.get("yellow").getLifes() + " §8- " + Colorname4);
    }

    public static String team(Player player) {
        String color = null;
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        if (customPlayer != null)
            if (customPlayer.getTeam() != null)
                color = customPlayer.getTeam().getName();
        if (color == null) return "nicht ausgewählt";

        switch (color) {
            case "red" -> {
                return ChatColor.RED + "Rot";
            }
            case "blue" -> {
                return ChatColor.BLUE +"Blau";
            }
            case "yellow" -> {
                return ChatColor.YELLOW +"Gelb";
            }
            case "green" -> {
                return ChatColor.GREEN +"Grün";
            }
            default -> {
                return ChatColor.GRAY +"Spectator";
            }
        }
    }

}
