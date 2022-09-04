// This class was created by Elix on 30.08.22


package codes.Elix.Woolbattle.game;

import org.bukkit.entity.Player;

public class Perk {

    private final Player player;
    private final String firstPerk;
    private final String secondPerk;
    private final String passivePerk;

    public Perk(Player player, String firstPerk, String secondPerk, String passivePerk) {
        this.player = player;
        this.firstPerk = firstPerk;
        this.secondPerk = secondPerk;
        this.passivePerk = passivePerk;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getfirstPerk() {
        return this.firstPerk;
    }

    public String getsecondtPerk() {
        return this.secondPerk;
    }

    public String getpassivePerk() {
        return this.passivePerk;
    }


}

