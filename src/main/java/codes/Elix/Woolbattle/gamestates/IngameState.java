// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.perks.booster;
import codes.Elix.Woolbattle.game.perks.switcher;
import codes.Elix.Woolbattle.game.perks.clock;
import codes.Elix.Woolbattle.game.perks.platform;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IngameState extends GameState {

    private Woolbattle plugin;


    public IngameState (Woolbattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        setLifes();
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.closeInventory();
            current.getInventory().clear();
            Items.standartitems(current);
            IngameScoreboard.setup(current);
        }
        DoubleJump.enable();
        switcher.enable();
        booster.enable();
        clock.enable();
        platform.enable();

    }

    @Override
    public void stop() {
        DoubleJump.disable();
    }

    public void setLifes() {
        if (LobbyItems.VotedLives == 0)
            LobbyItems.VotedLives = 6;
        LiveSystem.TeamLifes.put(LiveSystem.TeamRed, LobbyItems.VotedLives);
        LiveSystem.TeamLifes.put(LiveSystem.TeamYellow, LobbyItems.VotedLives);
        LiveSystem.TeamLifes.put(LiveSystem.TeamBlue, LobbyItems.VotedLives);
        LiveSystem.TeamLifes.put(LiveSystem.TeamGreen, LobbyItems.VotedLives);

    }

}
