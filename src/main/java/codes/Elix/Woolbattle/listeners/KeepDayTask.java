// This class was created by Elix on 05.10.22


package codes.Elix.Woolbattle.listeners;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class KeepDayTask extends BukkitRunnable {


    @Override
    public void run() {
        Bukkit.getServer().getWorlds().get(0).setTime(0L);
    }
}
