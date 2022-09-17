// This class was created by Elix on 10.09.22


package codes.Elix.Woolbattle.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Console {

    public static void send(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public static void send(String message, ChatColor color) {
        Bukkit.getConsoleSender().sendMessage(color + message);
    }

}
