package codes.Elix.Woolbattle.main;


import codes.Elix.Woolbattle.commands.*;
import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.VoidTeleport;
import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.listeners.GameProtectionListener;
import codes.Elix.Woolbattle.listeners.PlayerLobbyConnectionListener;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.Worldloader;
import com.sun.management.OperatingSystemMXBean;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

public class Woolbattle extends JavaPlugin {

    public static final String PREFIX = "§7[§cWOOLBATTLE§7] §r", NO_PERMISSION = PREFIX + "§cDazu hast du keine Rechte!";
    private GameStateManager gameStateManager;
    private ArrayList<Player> players;

    public static List<Block> blocks = new ArrayList<>();
    private static Woolbattle plugin;
    private FileConfiguration customConfig;


    @Override
    public void onEnable() {
        plugin = this;
        gameStateManager = new GameStateManager(this);
        players = new ArrayList<>();

        /*
        try {
            ExportResource("/Lobby1.schem");
            ExportResource("/game1.schem");
        } catch (Exception e) { throw new RuntimeException(e); }
         */
        createFiles();

        initmonitor();
        PluginMessage();

        LobbyMap();
        gameStateManager.setGameState(GameState.LOBBY_STATE);
        // safewool();
        addplayers();

        init(Bukkit.getPluginManager());
        Console.send("Woolbattle wurde aktiviert!");
    }

    private void init(PluginManager pluginManager) {
        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("test").setExecutor(new test());
        getCommand("countdown").setExecutor(new SetCountdown());

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
        pluginManager.registerEvents(new DoubleJump(), this);
        pluginManager.registerEvents(new GameProtectionListener(), this);
        pluginManager.registerEvents(new LobbyItems(), this);
        pluginManager.registerEvents(new VoidTeleport(), this);

    }

    private void initmonitor() {

        // Start periodic task
        getCommand("performance").setExecutor(new Performance());

        // Log related items which are unchanging
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        getLogger().info("Number of processors: " + Runtime.getRuntime().availableProcessors());
        getLogger().info("Physical memory: " + os.getTotalPhysicalMemorySize()/1048576L + " MB");
        getLogger().info("Maximum heap: " + Runtime.getRuntime().maxMemory()/1048576L + " MB");

        // Log maximum metaspace
        for (MemoryPoolMXBean memoryMXBean : ManagementFactory.getMemoryPoolMXBeans())
        {
            if ("Metaspace".equals(memoryMXBean.getName()))
            {
                long maxMetaspace = memoryMXBean.getUsage().getMax();
                if (maxMetaspace >= 0)
                {
                    getLogger().info("Maximum metaspace: " + maxMetaspace/1048576L + " MB");
                }
                break;
            }
        }


        // Log server view distance
        getLogger().info("server.properties view-distance: " + getServer().getViewDistance());

        // Log command line options
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        getLogger().info("Command line options: ");
        for (String str : arguments)
        {
            getLogger().info("  " + str);
        }
    }


    @Override
    public void onDisable() {}


    public static void safewool() {
        World w = Bukkit.getServer().getWorlds().get(0);
        for (Chunk c : w.getLoadedChunks()) {
            int cx = c.getX() << 4;
            int cz = c.getZ() << 4;
            for (int x = cx; x < cx + 16; x++) {
                for (int z = cz; z < cz + 16; z++) {
                    for (int y = 0; y < 128; y++) {
                        if (w.getBlockAt(x, y, z).getType() == Material.LEGACY_WOOL) {
                            blocks.add(w.getBlockAt(x,y,z));
                            Console.send("X: " + x + " Y: " + y + " Z: " + z);
                        }
                    }
                }
            }
        }
        Console.send("Safed all Woolblocks!");
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

    private void LobbyMap() {
        Worldloader.paste(new Location(Bukkit.getServer().getWorlds().get(0), 2, 70, 1), new File("./plugins/Woolbattle/Lobby1.schem"));
    }
    // new File("./plugins/Woolbattle/Lobby1.schem")
    private void createFiles() {

        File customConfigFile = new File(getDataFolder(), "PlayerPerkConfig.yml");
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

    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    static public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = Woolbattle.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Woolbattle.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream("./plugins/Woolbattle" + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }
        return jarFolder + resourceName;
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
        Console.send("\n" +
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
