// This class was created by Elix on 03.08.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.GameConfig;
import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class LiveSystem {

    // Use GameConfig for dynamic team settings
    public static int TeamSize = GameConfig.getInstance().getTeamSize(); // Amount of players in a Team
    public static int Teams = GameConfig.getInstance().getNumberOfTeams(); // How many teams are; up to 4
    public static ArrayList<Player> hitted = new ArrayList<>();
    public static HashMap<Player, Perk> players= new HashMap<>();

    // New Stuff
    public static HashMap<String, Team> Team = new HashMap<>();
    // public static List<CustomPlayer> newVotedPlayers = new ArrayList<>();

    public static void teamalive() {

    }
    
    /**
     * Reload team settings from GameConfig.
     * Call this after changing team settings in the GUI.
     */
    public static void reloadTeamSettings() {
        TeamSize = GameConfig.getInstance().getTeamSize();
        Teams = GameConfig.getInstance().getNumberOfTeams();
    }
}
