// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLocationUtil {

    private Woolbattle plugin;
    private Location location;
    private String root;

    public ConfigLocationUtil(Woolbattle plugin, Location location, String root) {
        this.plugin = plugin;
        this.location = location;
        this.root = root;
    }

    public ConfigLocationUtil(Woolbattle plugin, String root) {
        this(plugin, null, root);
    }

    public void saveLocation() {
        FileConfiguration config = plugin.getConfig();
        config.set(root +  ".World", location.getWorld().getName());
        config.set(root + ".X", location.getX());
        config.set(root + ".Y", location.getY());
        config.set(root + ".Z", location.getZ());
        config.set(root + ".Yaw", location.getYaw());
        config.set(root + ".Pitch", location.getPitch());
        plugin.saveConfig();
    }

    public Location loadLocation() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains(root)) {
            World world = Bukkit.getWorld(config.getString(root + ".World"));
            double x = config.getDouble(root + ".X"),
                    y = config.getDouble(root + ".Y"),
                    z = config.getDouble(root + ".Z");
            float yaw = (float) config.getDouble(root + ".Yaw"),
                    pitch = (float) config.getDouble(root + ".Pitch");
            return new Location(world, x, y, z, yaw, pitch);
        } else
            return null;
    }

}
