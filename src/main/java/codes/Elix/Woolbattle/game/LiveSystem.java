// This class was created by Elix on 03.08.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class LiveSystem {

    public static int TeamSize = Woolbattle.config.getInt("TeamSize"); // Amount of players in a Team
    public static int Teams = Woolbattle.config.getInt("Teams"); // How many teams are; up to 4
    public static ArrayList<Player> hitted = new ArrayList<>();
    public static HashMap<Player, Perk> players= new HashMap<>();

    // New Stuff
    public static HashMap<String, Team> Team = new HashMap<>();
    // public static List<CustomPlayer> newVotedPlayers = new ArrayList<>();

    public static void teamalive() {

    }
}
