package codes.Elix.Woolbattle.game.HelpClasses;

import codes.Elix.Woolbattle.config.GameConfig;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class CustomPlayer {

    private static HashMap<Player, CustomPlayer> playerhelper = new HashMap<>();
    
    private final Player player;
    private String teamName;
    private Team team;
    private boolean hitted;
    private Player damager;
    private long hitTimestamp;

    public CustomPlayer(Player player) {
        this.player = player;
        playerhelper.put(player, this);
    }

    public static CustomPlayer getCustomPlayer(Player player) {
        return playerhelper.get(player);
    }

    public Player getPlayer() {
        return player;
    }

    public String getTeamName() {
        return teamName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        this.teamName = team.getName();
    }

    public boolean isHitted() {
        return hitted;
    }

    /**
     * Marks this player as hit by another player.
     * The hit status will be checked when the player dies (falls into void).
     * 
     * @param attacker The player who hit this player
     */
    public void addHitted(Player attacker) {
        this.hitted = true;
        this.damager = attacker;
        this.hitTimestamp = System.currentTimeMillis();
        IngameScoreboard.update(this.player);
    }

    public void removeHitted() {
        this.hitted = false;
        this.damager = null;
        this.hitTimestamp = 0;
        IngameScoreboard.update(this.player);
    }

    public Player getDamager() {
        return damager;
    }
    
    /**
     * Checks if the hit is still valid (within configured hit timer duration).
     * 
     * @return true if the player was hit within the configured time window, false otherwise
     */
    public boolean isHitValid() {
        if (!hitted || hitTimestamp == 0) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsedMillis = currentTime - hitTimestamp;
        long hitDurationMillis = GameConfig.getInstance().getHitTimerMillis();
        
        return elapsedMillis <= hitDurationMillis;
    }
    
    /**
     * Clears all player helpers.
     * Call this when the game ends.
     */
    public static void cleanup() {
        playerhelper.clear();
    }
}
