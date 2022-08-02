// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.game.Doublejumplistener;
import codes.Elix.Woolbattle.game.perks.booster;
import codes.Elix.Woolbattle.game.perks.switcher;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IngameState extends GameState {

    private Woolbattle plugin;


    public IngameState (Woolbattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        for (Player current : Bukkit.getOnlinePlayers()) {
            Items.standartitems(current);
        }
        Doublejumplistener.enable();
        switcher.enable();
        booster.enable();

    }

    @Override
    public void stop() {
        Doublejumplistener.disable();
    }
}
