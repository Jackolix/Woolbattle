package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PerkHelper {
    private static FileConfiguration config = Woolbattle.getPlugin().getConfig();
    public static String passive(Player player) {
        Object Pperk = config.get(player.getName() + ".passive");
        assert Pperk != null;
        return Pperk.toString();
    }
}
