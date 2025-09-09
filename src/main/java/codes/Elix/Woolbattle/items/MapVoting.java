package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.SchematicManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.*;

public class MapVoting {

    public static HashMap<Player, String> votedMaps = new HashMap<>();
    public static HashMap<String, List<Player>> mapVotes = new HashMap<>();

    public static void voteForMap(Player player, String fileName) {
        // Remove previous vote if exists
        String previousVote = votedMaps.get(player);
        if (previousVote != null) {
            List<Player> previousVoters = mapVotes.get(previousVote);
            if (previousVoters != null) {
                previousVoters.remove(player);
                if (previousVoters.isEmpty()) {
                    mapVotes.remove(previousVote);
                }
            }
        }
        
        // Add new vote
        votedMaps.put(player, fileName);
        mapVotes.computeIfAbsent(fileName, k -> new ArrayList<>()).add(player);
        
        // Give OP players double vote weight (like in Leben voting)
        if (player.hasPermission("Woolbattle.OP")) {
            mapVotes.get(fileName).add(player); // Add second vote for OP
        }
        
        updateMapSelection();
    }

    public static String getWinningMap() {
        if (mapVotes.isEmpty()) {
            return "Game1.schem"; // Default fallback
        }
        
        String winningMap = null;
        int maxVotes = 0;
        
        for (Map.Entry<String, List<Player>> entry : mapVotes.entrySet()) {
            int votes = entry.getValue().size();
            if (votes > maxVotes) {
                maxVotes = votes;
                winningMap = entry.getKey();
            }
        }
        
        Console.send("");
        Console.send(Component.text("-------- Map Voting Results --------", NamedTextColor.GOLD));
        
        List<SchematicManager.SchematicInfo> schematics = SchematicManager.getAvailableSchematics();
        for (Map.Entry<String, List<Player>> entry : mapVotes.entrySet()) {
            String fileName = entry.getKey();
            int votes = entry.getValue().size();
            
            // Find display name
            String displayName = fileName;
            for (SchematicManager.SchematicInfo schematic : schematics) {
                if (schematic.getFileName().equals(fileName)) {
                    displayName = schematic.getDisplayName().replace("§6", "");
                    break;
                }
            }
            
            Console.send(Component.text(displayName + ": " + votes + " votes", NamedTextColor.WHITE));
        }
        Console.send(Component.text("Winner: " + (winningMap != null ? winningMap : "Game1.schem"), NamedTextColor.GREEN));
        Console.send("");
        
        return winningMap != null ? winningMap : "Game1.schem";
    }
    
    public static int getVotesForMap(String fileName) {
        List<Player> voters = mapVotes.get(fileName);
        return voters != null ? voters.size() : 0;
    }
    
    public static void updateMapSelection() {
        String winningMap = getWinningMap();
        SchematicManager.selectMap(winningMap);
    }
    
    public static void clearVotes() {
        votedMaps.clear();
        mapVotes.clear();
        SchematicManager.selectMap("Game1.schem"); // Reset to default
    }
    
    public static boolean hasVoted(Player player) {
        return votedMaps.containsKey(player);
    }
    
    public static String getPlayerVote(Player player) {
        return votedMaps.get(player);
    }
    
    public static String getCurrentWinningMap() {
        if (mapVotes.isEmpty()) {
            return "Game1.schem"; // Default fallback
        }
        
        String winningMap = null;
        int maxVotes = 0;
        
        for (Map.Entry<String, List<Player>> entry : mapVotes.entrySet()) {
            int votes = entry.getValue().size();
            if (votes > maxVotes) {
                maxVotes = votes;
                winningMap = entry.getKey();
            }
        }
        
        return winningMap != null ? winningMap : "Game1.schem";
    }
}