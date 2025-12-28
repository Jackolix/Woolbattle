package codes.Elix.Woolbattle.main;


import codes.Elix.Woolbattle.commands.*;
import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.game.*;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.gamestates.GameState;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.listeners.ConnectionListener;
import codes.Elix.Woolbattle.listeners.GameProtectionListener;
import codes.Elix.Woolbattle.listeners.KeepDayTask;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.SchematicManager;
import codes.Elix.Woolbattle.util.mongo.Database;
import com.sun.management.OperatingSystemMXBean;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Woolbattle extends JavaPlugin {

    //public static final String PREFIX = "§7[§cWOOLBATTLE§7] §r";
    public static final @NotNull TextComponent PREFIX = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("Woolbattle", NamedTextColor.RED))
            .append(Component.text("] ", NamedTextColor.GRAY));
    public static FileConfiguration config;
    private GameStateManager gameStateManager;
    public static ArrayList<Player> players;

    public static List<Block> blocks = new ArrayList<>();
    private static Woolbattle plugin;
    public static boolean debug;
    public static boolean useDB;

    @Override
    public void onEnable() {
        plugin = this;
        gameStateManager = new GameStateManager(this);
        players = new ArrayList<>();
        saveDefaultConfig();
        config = getConfig();
        debug = getConfig().getBoolean("Debug");
        useDB = getConfig().getBoolean("MongoDB");
        checkFiles();
        SchematicManager.scanSchematics();

        initmonitor();
        PluginMessage();

        LobbyMap();
        gameStateManager.setGameState(GameState.LOBBY_STATE);
        addplayers();

        init(Bukkit.getPluginManager());
        Console.send(Component.text("Woolbattle wurde aktiviert!", NamedTextColor.WHITE));
    }

    private void init(PluginManager pluginManager) {
        Chat chat = new Chat();

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
        getCommand("visualizewool").setExecutor(new VisualizeWool());
        getCommand("reloadperks").setExecutor(new ReloadPerks());
        getCommand("perkconfig").setExecutor(new PerkConfigCommand());

        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new DoubleJump(), this);
        pluginManager.registerEvents(new GameProtectionListener(), this);
        pluginManager.registerEvents(new LobbyItems(), this);
        pluginManager.registerEvents(new VoidTeleport(), this);
        pluginManager.registerEvents(new BowShoot(), this);
        pluginManager.registerEvents(chat, this);
        pluginManager.registerEvents(new codes.Elix.Woolbattle.gui.PerkConfigGUI(), this);

        new KeepDayTask().runTaskTimer(this, 0L, 100L);
        if (useDB) {
            new Database();
            Console.send(Component.text("MongoDB connection enabled", NamedTextColor.GREEN));
        } else {
            Console.send(Component.text("MongoDB connection disabled - running without database", NamedTextColor.YELLOW));
        }
        initTeams();
        cloudStartMessenger();
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

        // Clear previous blocks to prevent stale references
        blocks.clear();

        // Get world height limits (1.21.8 supports -64 to 319)
        int minY = w.getMinHeight();  // -64
        int maxY = w.getMaxHeight();  // 320 (exclusive)

        for (Chunk c : w.getLoadedChunks()) {
            int cx = c.getX() << 4;
            int cz = c.getZ() << 4;
            for (int x = cx; x < cx + 16; x++) {
                for (int z = cz; z < cz + 16; z++) {
                    for (int y = minY; y < maxY; y++) {
                        if (Tag.WOOL.isTagged(w.getBlockAt(x, y, z).getType())) {
                            blocks.add(w.getBlockAt(x,y,z));
                            if (enabled)
                                Console.send("X: " + x + " Y: " + y + " Z: " + z);
                        }
                    }
                }
            }
        }
        Console.send(Component.text("Safed all Woolblocks! (" + blocks.size() + " blocks)", NamedTextColor.GREEN));
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

    private void initTeams() { //TODO Correct RGB Colors
        Team red = new Team(new ArrayList<>(), "red", 0, false, "§c", TextColor.color(19,248,50));
        Team blue = new Team(new ArrayList<>(), "blue", 0, false, "§9", TextColor.color(19,248,50));
        Team green = new Team(new ArrayList<>(), "green", 0, false, "§a", TextColor.color(19,248,50));
        Team yellow = new Team(new ArrayList<>(), "yellow", 0, false, "§e", TextColor.color(19,248,50));
        Team spectator = new Team(new ArrayList<>(), "spectator", 0, true, "§7", TextColor.color(19,248,50));
        LiveSystem.Team.put("red", red);
        LiveSystem.Team.put("blue", blue);
        LiveSystem.Team.put("green", green);
        LiveSystem.Team.put("yellow", yellow);
        LiveSystem.Team.put("spectator", spectator);
    }

    private void LobbyMap() {
        // Use the new config-based system for loading lobby schematic
        String lobbySchematic = "Lobby1.schem";
        SchematicManager.loadSchematicWithConfig(lobbySchematic);
    }

    private void cloudStartMessenger() {
        /*
        ServerObject server = TimoCloudAPI.getBukkitAPI().getThisServer();
        PluginMessage pluginMessage = new PluginMessage("START").set("name", server.getName());

        TimoCloudAPI.getMessageAPI().sendMessageToServer(pluginMessage, "Lobby");

         */
    }

    private void checkFiles() {
        Component error = Component.text("ERROR ", NamedTextColor.RED);
        if (!Files.exists(Path.of("plugins/Woolbattle/Lobby1.schem"))) {
            Console.debug(Component.text("Files are missing, trying to copy"));
            try {
                Files.copy(getClass().getResourceAsStream("/Lobby1.schem"), Path.of("plugins/Woolbattle/Lobby1.schem"));
            } catch (IOException e) {
                Console.debug(error.append(Component.text(e.toString())));
            }
        }
        if (!Files.exists(Path.of("plugins/Woolbattle/Game1.schem"))) {
            Console.debug(Component.text("Files are missing, trying to copy"));
            try {
                Files.copy(getClass().getResourceAsStream("/Game1.schem"), Path.of("plugins/Woolbattle/Game1.schem"));
            } catch (IOException e) {
                Console.debug(error.append(Component.text(e.toString())));
            }
        }
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
