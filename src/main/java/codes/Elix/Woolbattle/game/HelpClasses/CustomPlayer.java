package codes.Elix.Woolbattle.game.HelpClasses;

import org.bukkit.entity.Player;

import java.util.HashMap;


public class CustomPlayer {

    private static HashMap<Player, CustomPlayer> playerhelper = new HashMap<>();
    private final Player player;
    private String teamName;
    private Team team;
    private boolean hitted;
    private Player damager;

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

    public void addHitted(Player player) {
        this.hitted = true;
        this.damager = player;
    }

    public void removeHitted() {
        this.hitted = false;
    }

    public Player getDamager() {
        return damager;
    }
}
