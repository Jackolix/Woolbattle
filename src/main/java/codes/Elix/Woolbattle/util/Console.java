// This class was created by Elix on 10.09.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.mongo.Database;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class Console {

    public static void send(String message) {
        Bukkit.getConsoleSender().sendMessage(Woolbattle.PREFIX
                .append(Component.text(message)));
    }

    public static void send(Component message) {
        if (message.contains(Database.PREFIX))
            if (!Woolbattle.debug)
                return;
        Bukkit.getConsoleSender().sendMessage(Woolbattle.PREFIX
                .append(message));
    }
    @Deprecated
    public static void debug(String message) {
        if (!Woolbattle.debug) return;
        Bukkit.getConsoleSender().sendMessage(Woolbattle.PREFIX.append(Component.text(message)));
    }
    public static void debug(Component message) {
        if (!Woolbattle.debug) return;
        Bukkit.getConsoleSender().sendMessage(Woolbattle.PREFIX.append(message));
    }

}
