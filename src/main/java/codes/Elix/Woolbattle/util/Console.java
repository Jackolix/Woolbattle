// This class was created by Elix on 10.09.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.mongo.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Console {

    public static void send(String message) {
        if (message.contains(Database.PREFIX))
            if (!Woolbattle.debug)
                return;
        Bukkit.getConsoleSender().sendMessage(Woolbattle.PREFIX + message);
    }

    public static void send(String message, ChatColor color) {
        Bukkit.getConsoleSender().sendMessage(color + message);
    }

}
