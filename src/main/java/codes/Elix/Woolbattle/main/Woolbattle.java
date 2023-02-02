package codes.Elix.Woolbattle.main;


import codes.Elix.Woolbattle.commands.*;
import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.game.BowShoot;
import codes.Elix.Woolbattle.game.Chat;
import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.VoidTeleport;
import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.listeners.GameProtectionListener;
import codes.Elix.Woolbattle.listeners.KeepDayTask;
import codes.Elix.Woolbattle.listeners.PlayerLobbyConnectionListener;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.mongo.Database;
import codes.Elix.Woolbattle.util.Worldloader;
import com.sun.management.OperatingSystemMXBean;
import org.bukkit.*;
import org.bukkit.block.Block;
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
    public static ArrayList<Player> players;

    public static List<Block> blocks = new ArrayList<>();
    private static Woolbattle plugin;
    public static boolean debug = false;
    public static boolean useDB = true;


    @Override
    public void onEnable() {
        plugin = this;
        gameStateManager = new GameStateManager(this);
        players = new ArrayList<>();

        initmonitor();
        PluginMessage();

        LobbyMap();
        gameStateManager.setGameState(GameState.LOBBY_STATE);
        addplayers();

        init(Bukkit.getPluginManager());
        Console.send("Woolbattle wurde aktiviert!");
    }

    private void init(PluginManager pluginManager) {
        Chat chat = new Chat();

        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("test").setExecutor(new test());
        getCommand("countdown").setExecutor(new SetCountdown());
        getCommand("all").setExecutor(chat);
        getCommand("setlive").setExecutor(new SetLive());
        getCommand("build").setExecutor(new Build());
        getCommand("hitted").setExecutor(new SetHitted());
        getCommand("debug").setExecutor(new Debug());
        getCommand("switchteam").setExecutor(new Switchteam());
        getCommand("fix").setExecutor(new Fix());

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(), this);
        pluginManager.registerEvents(new DoubleJump(), this);
        pluginManager.registerEvents(new GameProtectionListener(), this);
        pluginManager.registerEvents(new LobbyItems(), this);
        pluginManager.registerEvents(new VoidTeleport(), this);
        pluginManager.registerEvents(new BowShoot(), this);
        pluginManager.registerEvents(chat, this);

        new KeepDayTask().runTaskTimer(this, 0L, 100L);
        new Database();

    }

    private void initmonitor() {

        // Start periodic task
        getCommand("performance").setExecutor(new Performance());

        // Log related items which are unchanging
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        getLogger().info("Number of processors: " + Runtime.getRuntime().availableProcessors());
        getLogger().info("Physical memory: " + os.getTotalMemorySize()/1048576L + " MB");
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
        boolean enabled = debug;
        World w = Bukkit.getServer().getWorlds().get(0);
        for (Chunk c : w.getLoadedChunks()) {
            int cx = c.getX() << 4;
            int cz = c.getZ() << 4;
            for (int x = cx; x < cx + 16; x++) {
                for (int z = cz; z < cz + 16; z++) {
                    for (int y = 0; y < 128; y++) {
                        if (Tag.WOOL.isTagged(w.getBlockAt(x, y, z).getType())) {
                            blocks.add(w.getBlockAt(x,y,z));
                            if (enabled)
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

    public static ArrayList<Player> getPlayers() {
        return players;
    }
    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
    public static Woolbattle getPlugin() { return plugin; }
    private void PluginMessage() {
        Console.send("""

                 __          __         _            \s
                 \\ \\        / /        | |           \s
                  \\ \\  /\\  / /__   ___ | |           \s
                   \\ \\/  \\/ / _ \\ / _ \\| |           \s
                    \\  /\\  / (_) | (_) | |           \s
                     \\/  \\/_\\___/ \\___/|_| _   _     \s
                          | |         | | | | | |    \s
                          | |__   __ _| |_| |_| | ___\s
                          | '_ \\ / _` | __| __| |/ _ \\
                          | |_) | (_| | |_| |_| |  __/
                          |_.__/ \\__,_|\\__|\\__|_|\\___|
                                                     \s
                """);
    }


}
