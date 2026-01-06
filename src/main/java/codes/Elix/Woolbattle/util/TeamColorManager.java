package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Manages player name colors in tab list and above heads using Bukkit scoreboard teams.
 * Colors players based on their Woolbattle team assignment.
 */
public class TeamColorManager {

    private static Scoreboard scoreboard;

    /**
     * Initialize the scoreboard team system. Should be called on plugin enable.
     */
    public static void initialize() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create or get scoreboard teams for each Woolbattle team
        setupScoreboardTeam("wb_red", NamedTextColor.RED);
        setupScoreboardTeam("wb_blue", NamedTextColor.BLUE);
        setupScoreboardTeam("wb_green", NamedTextColor.GREEN);
        setupScoreboardTeam("wb_yellow", NamedTextColor.YELLOW);
        setupScoreboardTeam("wb_spectator", NamedTextColor.GRAY);
        setupScoreboardTeam("wb_none", NamedTextColor.WHITE);
    }

    /**
     * Create or retrieve a scoreboard team with the given color.
     */
    private static void setupScoreboardTeam(String teamName, NamedTextColor color) {
        org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.getTeam(teamName);
        if (scoreboardTeam == null) {
            scoreboardTeam = scoreboard.registerNewTeam(teamName);
        }
        scoreboardTeam.color(color);
    }

    /**
     * Update a player's name color based on their current Woolbattle team.
     * Call this when a player joins a team or changes teams.
     *
     * @param player The player to update
     */
    public static void updatePlayerColor(Player player) {
        if (!player.isOnline()) return;

        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        Team team = customPlayer != null ? customPlayer.getTeam() : null;

        // Remove player from all scoreboard teams first
        removeFromAllTeams(player);

        // Add to appropriate scoreboard team based on Woolbattle team
        String scoreboardTeamName = "wb_none";
        if (team != null) {
            String teamName = team.getName().toLowerCase();
            scoreboardTeamName = switch (teamName) {
                case "red" -> "wb_red";
                case "blue" -> "wb_blue";
                case "green" -> "wb_green";
                case "yellow" -> "wb_yellow";
                case "spectator" -> "wb_spectator";
                default -> "wb_none";
            };
        }

        org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.getTeam(scoreboardTeamName);
        if (scoreboardTeam != null) {
            scoreboardTeam.addEntry(player.getName());
        }

        // Ensure player is using the main scoreboard
        if (!player.getScoreboard().equals(scoreboard)) {
            player.setScoreboard(scoreboard);
        }
    }

    /**
     * Update colors for all online players.
     * Useful when game state changes or during initialization.
     */
    public static void updateAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerColor(player);
        }
    }

    /**
     * Remove a player from all Woolbattle scoreboard teams.
     * Call this when a player leaves the server.
     *
     * @param player The player to remove
     */
    public static void removePlayer(Player player) {
        removeFromAllTeams(player);
    }

    /**
     * Remove a player from all scoreboard teams.
     */
    private static void removeFromAllTeams(Player player) {
        String playerName = player.getName();
        for (org.bukkit.scoreboard.Team team : scoreboard.getTeams()) {
            if (team.getName().startsWith("wb_")) {
                team.removeEntry(playerName);
            }
        }
    }

    /**
     * Clear all players from team color system.
     * Useful when resetting the game state.
     */
    public static void clearAll() {
        for (org.bukkit.scoreboard.Team team : scoreboard.getTeams()) {
            if (team.getName().startsWith("wb_")) {
                for (String entry : team.getEntries()) {
                    team.removeEntry(entry);
                }
            }
        }
    }
}
