// This class was created by Elix on 03.08.22


package codes.Elix.Woolbattle.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LiveSystem {

    public static int TeamSize = 2; // Amount of players in a Team
    public static ArrayList<Player> TeamRed = new ArrayList<>();
    public static ArrayList<Player> TeamBlue = new ArrayList<>();
    public static ArrayList<Player> TeamGreen = new ArrayList<>();
    public static ArrayList<Player> TeamYellow = new ArrayList<>();
    public static HashMap<Player, ArrayList<Player>> VotedPlayers = new HashMap<>();
    public static HashMap<ArrayList<Player>, Integer> TeamLifes = new HashMap<>();
    public static Collection<Player> getTeams() {
        //TODO: alle ArrayLists zurückgeben
        return null;
    }

    public static ArrayList<Perk> players = new ArrayList<>();
}
