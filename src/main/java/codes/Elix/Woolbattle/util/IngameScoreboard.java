// This class was created by Elix on 02.08.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.LiveSystem;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngameScoreboard {

    public static String Colorname1 = "§cRot";
    public static String Colorname2 = "§bBlau";
    public static String Colorname3 = "§aGreen";
    public static String Colorname4 = "§eGelb";
    private static final String MapName = "§3Custom";
    private static final Map<Player, FastBoard> boards = new HashMap<>();

    public static void setup(Player player) {
        FastBoard board = new FastBoard(player);
        board.updateTitle("§f§lWOOLBATTLE");
        
        List<String> lines = new ArrayList<>();
        lines.add("   ");
        lines.add("§8» §7Dein Team");
        lines.add(" §7• " + team(player));
        lines.add("  ");
        lines.add("§8» §7Map");
        lines.add(" §7• " + MapName);
        lines.add(" ");
        lines.add("§8» §7Leben");
        lines.add(" §7• §4❤ " + LiveSystem.Team.get("red").getLifes() + " §8- " + Colorname1);
        lines.add(" §7• §4❤ " + LiveSystem.Team.get("blue").getLifes() + " §8- " + Colorname2);
        
        if (LiveSystem.Teams >= 3)
            lines.add(" §7• §4❤ " + LiveSystem.Team.get("green").getLifes() + " §8- " + Colorname3);
        if (LiveSystem.Teams >= 4)
            lines.add(" §7• §4❤ " + LiveSystem.Team.get("yellow").getLifes() + " §8- " + Colorname4);
            
        board.updateLines(lines);
        boards.put(player, board);
    }
    
    public static void remove(Player player) {
        FastBoard board = boards.remove(player);
        if (board != null) {
            board.delete();
        }
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
