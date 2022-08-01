package codes.Elix.Woolbattle.main;


import codes.Elix.Woolbattle.commands.SetupCommand;
import codes.Elix.Woolbattle.commands.StartCommand;
import codes.Elix.Woolbattle.game.Doublejumplistener;
import codes.Elix.Woolbattle.game.perks.switcher;
import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.listeners.GameProtectionListener;
import codes.Elix.Woolbattle.listeners.PlayerLobbyConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Woolbattle extends JavaPlugin {

    public static final String PREFIX = "§7[§cWOOLBATTLE§7] §r", NO_PERMISSION = PREFIX + "§cDazu hast du keine Rechte!";
    private GameStateManager gameStateManager;
    private ArrayList<Player> players;

    public static List<Block> blocks = new ArrayList<>();
    private static Woolbattle plugin;
    private File customConfigFile;
    private FileConfiguration customConfig;


    @Override
    public void onEnable() {
        plugin = this;
        gameStateManager = new GameStateManager(this);
        players = new ArrayList<>();
        createCustomConfig();

        gameStateManager.setGameState(GameState.LOBBY_STATE);
        safewool();

        init(Bukkit.getPluginManager());
        System.out.println("Safed all Woolblocks!");
        System.out.println("Woolbattle wurde aktiviert!");

    }

    private void init(PluginManager pluginManager) {
        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
        pluginManager.registerEvents(new Doublejumplistener(), this);
        pluginManager.registerEvents(new GameProtectionListener(), this);
        pluginManager.registerEvents(new LobbyItems(), this);

    }

    @Override
    public void onDisable() {


    }
    private void safewool() {
        World w = Bukkit.getWorld("world");
        for (Chunk c : w.getLoadedChunks()) {
            int cx = c.getX() << 4;
            int cz = c.getZ() << 4;
            for (int x = cx; x < cx + 16; x++) {
                for (int z = cz; z < cz + 16; z++) {
                    for (int y = 0; y < 128; y++) {
                        if (w.getBlockAt(x, y, z).getType() == Material.WOOL) {
                            blocks.add(w.getBlockAt(x,y,z));
                            System.out.println("X: " + x + " Y: " + y + " Z: " + z);
                        }
                    }
                }
            }
        }
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "PlayerPerkConfig.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("PlayerPerkConfig.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        /* User Edit:
            Instead of the above Try/Catch, you can also use
            YamlConfiguration.loadConfiguration(customConfigFile)
        */
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
    public static Woolbattle getPlugin() { return plugin; }
    public FileConfiguration getCustomConfig() { return this.customConfig; }


}
