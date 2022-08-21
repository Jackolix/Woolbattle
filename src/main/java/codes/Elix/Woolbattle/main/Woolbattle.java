package codes.Elix.Woolbattle.main;


import codes.Elix.Woolbattle.commands.SetupCommand;
import codes.Elix.Woolbattle.commands.StartCommand;
import codes.Elix.Woolbattle.commands.test;
import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
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
        PluginMessage();

        gameStateManager.setGameState(GameState.LOBBY_STATE);
        safewool();
        addplayers();

        init(Bukkit.getPluginManager());
        System.out.println("Safed all Woolblocks!");
        System.out.println("Woolbattle wurde aktiviert!");

    }

    private void init(PluginManager pluginManager) {
        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("test").setExecutor(new test());

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
        pluginManager.registerEvents(new DoubleJump(), this);
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
                        if (w.getBlockAt(x, y, z).getType() == Material.LEGACY_WOOL) {
                            blocks.add(w.getBlockAt(x,y,z));
                            System.out.println("X: " + x + " Y: " + y + " Z: " + z);
                        }
                    }
                }
            }
        }
    }

    private void addplayers() {
        for (Player current : Bukkit.getOnlinePlayers()) {
            players.add(current);
            LobbyItems.Lobby(current);
        }
        LobbyState lobbyState = (LobbyState) GameStateManager.getCurrentGameState();
        LobbyCountdown countdown = lobbyState.getCountdown();
        if (players.size() >= LobbyState.MIN_PLAYERS) {
            if (!countdown.isRunning()) {
                countdown.stopIdle();
                countdown.start();
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

    private void PluginMessage() {
        Bukkit.getServer().getConsoleSender().sendMessage("\n" +
                "\n __          __         _             \n" +
                " \\ \\        / /        | |            \n" +
                "  \\ \\  /\\  / /__   ___ | |            \n" +
                "   \\ \\/  \\/ / _ \\ / _ \\| |            \n" +
                "    \\  /\\  / (_) | (_) | |            \n" +
                "     \\/  \\/_\\___/ \\___/|_| _   _      \n" +
                "          | |         | | | | | |     \n" +
                "          | |__   __ _| |_| |_| | ___ \n" +
                "          | '_ \\ / _` | __| __| |/ _ \\\n" +
                "          | |_) | (_| | |_| |_| |  __/\n" +
                "          |_.__/ \\__,_|\\__|\\__|_|\\___|\n" +
                "                                      \n" +
                "                                      \n");
    }


}
