package codes.Elix.Woolbattle.game.HelpClasses;

import codes.Elix.Woolbattle.util.TeamColorManager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Team {

    private List<Player> members;
    private String name;
    private int lifes;
    private boolean dead;
    private final String prefix;
    private final TextColor textColor;

    public Team(List<Player> members, String name, int lifes, boolean dead, String prefix, TextColor textColor) {
        this.members = members;
        this.name = name;
        this.lifes = lifes;
        this.dead = dead;
        this.prefix = prefix;
        this.textColor = textColor;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void removeMembers(Player player) {
        members.remove(player);

        // Update player's name color when removed from team
        TeamColorManager.updatePlayerColor(player);
    }

    public String getName() {
        return name;
    }

    public boolean isDead() {
        return dead;
    }

    public int getLifes() {
        return lifes;
    }

    public void addMember(Player player) {
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        if (customPlayer.getTeam() != null) {
            customPlayer.getTeam().removeMembers(player);
        }
        members.add(player);
        customPlayer.setTeam(this);

        // Update player's name color in tab list and above head
        TeamColorManager.updatePlayerColor(player);
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public String getPREFIX() {
        return prefix;
    }

    public TextColor getTextColor() {
        return textColor;
    }
}
