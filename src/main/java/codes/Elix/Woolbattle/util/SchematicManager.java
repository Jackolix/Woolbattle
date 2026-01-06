package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchematicManager {
    
    private static List<SchematicInfo> availableSchematics = new ArrayList<>();
    private static final String SCHEMATICS_FOLDER = "plugins/Woolbattle/";
    private static final String CONFIG_FOLDER = SCHEMATICS_FOLDER + "configs/";
    private static String selectedMap = "Game1.schem"; // Default map
    
    public static class SchematicInfo {
        private final String fileName;
        private final String displayName;
        private final Material material;
        
        public SchematicInfo(String fileName, String displayName, Material material) {
            this.fileName = fileName;
            this.displayName = displayName;
            this.material = material;
        }
        
        public String getFileName() { return fileName; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
    }
    
    public static void scanSchematics() {
        availableSchematics.clear();
        
        File schematicDir = new File(SCHEMATICS_FOLDER);
        if (!schematicDir.exists() || !schematicDir.isDirectory()) {
            Console.send("Schematic directory not found: " + SCHEMATICS_FOLDER);
            return;
        }
        
        FilenameFilter schematicFilter = (dir, name) -> 
            name.toLowerCase().endsWith(".schem") || name.toLowerCase().endsWith(".schematic");
        
        File[] schematicFiles = schematicDir.listFiles(schematicFilter);
        
        if (schematicFiles != null) {
            Arrays.sort(schematicFiles, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            
            for (File file : schematicFiles) {
                String fileName = file.getName();
                String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                String displayName = formatDisplayName(baseName);
                Material material = getMaterialForSchematic(baseName);
                
                availableSchematics.add(new SchematicInfo(fileName, displayName, material));
                
                // Generate config file if it doesn't exist
                generateConfigIfNeeded(fileName, baseName);
            }
        }
        
        Console.send("Found " + availableSchematics.size() + " schematic files");
        for (SchematicInfo info : availableSchematics) {
            System.out.println("[Woolbattle] - " + info.getDisplayName().replace("§6", "") + " (" + info.getFileName() + ") Material: " + info.getMaterial());
        }
    }
    
    private static String formatDisplayName(String baseName) {
        // Remove legacy color codes, we'll handle coloring in the UI
        return baseName.replace("_", " ").replace("-", " ");
    }
    
    private static Material getMaterialForSchematic(String baseName) {
        String lowerName = baseName.toLowerCase();
        
        if (lowerName.contains("lobby")) {
            return Material.EMERALD_BLOCK;
        } else if (lowerName.contains("game") || lowerName.contains("map")) {
            return Material.DIAMOND_BLOCK;
        } else if (lowerName.contains("arena")) {
            return Material.REDSTONE_BLOCK;
        } else if (lowerName.contains("spawn")) {
            return Material.GOLD_BLOCK;
        }
        
        return Material.GRASS_BLOCK;
    }
    
    public static List<SchematicInfo> getAvailableSchematics() {
        return new ArrayList<>(availableSchematics);
    }
    
    public static List<SchematicInfo> getGameSchematics() {
        List<SchematicInfo> gameSchematics = new ArrayList<>();
        for (SchematicInfo info : availableSchematics) {
            FileConfiguration config = getSchematicConfig(info.getFileName());
            String type = config.getString("type", "game"); // Default to "game" if not specified
            
            if ("game".equals(type)) {
                gameSchematics.add(info);
            }
        }
        return gameSchematics;
    }
    
    public static File getSchematicFile(String fileName) {
        return new File(SCHEMATICS_FOLDER + fileName);
    }
    
    public static int getSchematicCount() {
        return availableSchematics.size();
    }
    
    public static void selectMap(String fileName) {
        selectedMap = fileName;
        Console.send("Map selected: " + fileName);
    }
    
    public static String getSelectedMap() {
        return selectedMap;
    }
    
    public static File getSelectedMapFile() {
        return new File(SCHEMATICS_FOLDER + selectedMap);
    }
    
    private static void generateConfigIfNeeded(String schematicFileName, String baseName) {
        String configFileName = baseName + ".yml";
        File configDir = new File(CONFIG_FOLDER);
        File configFile = new File(configDir, configFileName);
        
        // Create config directory if it doesn't exist
        if (!configDir.exists()) {
            configDir.mkdirs();
            Console.send("Created config directory: " + CONFIG_FOLDER);
        }
        
        // Generate config file if it doesn't exist
        if (!configFile.exists()) {
            generateDefaultConfig(configFile, schematicFileName, baseName);
            Console.send("Generated config file: " + configFileName);
        }
    }
    
    private static void generateDefaultConfig(File configFile, String schematicFileName, String baseName) {
        FileConfiguration config = new YamlConfiguration();
        
        // Basic information
        config.set("schematic.file", schematicFileName);
        config.set("schematic.display-name", formatDisplayName(baseName));
        config.set("schematic.description", "Auto-generated config for " + baseName);
        
        // Game settings
        int maxTeams = Woolbattle.config.getInt("Teams", 2);
        int teamSize = Woolbattle.config.getInt("TeamSize", 2);
        
        config.set("game.max-teams", maxTeams);
        config.set("game.team-size", teamSize);
        config.set("game.max-players", maxTeams * teamSize);
        
        // Team spawn locations (default values - need to be set manually)
        config.set("teams.red.name", "Red Team");
        config.set("teams.red.color", "RED");
        config.set("teams.red.spawn.x", 0.0);
        config.set("teams.red.spawn.y", 70.0);
        config.set("teams.red.spawn.z", 10.0);
        config.set("teams.red.spawn.yaw", 0.0f);
        config.set("teams.red.spawn.pitch", 0.0f);
        
        config.set("teams.blue.name", "Blue Team");
        config.set("teams.blue.color", "BLUE");
        config.set("teams.blue.spawn.x", 0.0);
        config.set("teams.blue.spawn.y", 70.0);
        config.set("teams.blue.spawn.z", -10.0);
        config.set("teams.blue.spawn.yaw", 180.0f);
        config.set("teams.blue.spawn.pitch", 0.0f);
        
        // Add additional teams if needed
        if (maxTeams > 2) {
            config.set("teams.green.name", "Green Team");
            config.set("teams.green.color", "GREEN");
            config.set("teams.green.spawn.x", 10.0);
            config.set("teams.green.spawn.y", 70.0);
            config.set("teams.green.spawn.z", 0.0);
            config.set("teams.green.spawn.yaw", 270.0f);
            config.set("teams.green.spawn.pitch", 0.0f);
        }
        
        if (maxTeams > 3) {
            config.set("teams.yellow.name", "Yellow Team");
            config.set("teams.yellow.color", "YELLOW");
            config.set("teams.yellow.spawn.x", -10.0);
            config.set("teams.yellow.spawn.y", 70.0);
            config.set("teams.yellow.spawn.z", 0.0);
            config.set("teams.yellow.spawn.yaw", 90.0f);
            config.set("teams.yellow.spawn.pitch", 0.0f);
        }
        
        // Other spawn locations
        config.set("spawns.spectator.x", 0.0);
        config.set("spawns.spectator.y", 80.0);
        config.set("spawns.spectator.z", 0.0);
        config.set("spawns.spectator.yaw", 0.0f);
        config.set("spawns.spectator.pitch", -45.0f);
        
        // Lobby removal bounds (for clearing lobby map before loading game map)
        config.set("lobby-bounds.min.x", -50.0);
        config.set("lobby-bounds.min.y", 60.0);
        config.set("lobby-bounds.min.z", -50.0);
        config.set("lobby-bounds.max.x", 50.0);
        config.set("lobby-bounds.max.y", 100.0);
        config.set("lobby-bounds.max.z", 50.0);
        
        // Schematic placement settings
        config.set("placement.x", 0);
        config.set("placement.y", 60);
        config.set("placement.z", 0);
        
        // Special settings based on schematic type
        String lowerName = baseName.toLowerCase();
        if (lowerName.contains("lobby")) {
            config.set("type", "lobby");
            config.set("game.pvp-enabled", false);
        } else {
            config.set("type", "game");
            config.set("game.pvp-enabled", true);
            config.set("game.build-height", 120);
            config.set("game.void-level", 40);
        }
        
        // Comments
        config.setComments("schematic", List.of("Schematic file information"));
        config.setComments("game", List.of("Game-specific settings"));
        config.setComments("teams", List.of("Team configurations and spawn locations", "Coordinates are ABSOLUTE world coordinates, not relative to schematic placement"));
        config.setComments("teams.red.spawn", List.of("Red team spawn location (absolute world coordinates)"));
        config.setComments("teams.blue.spawn", List.of("Blue team spawn location (absolute world coordinates)"));
        config.setComments("spawns", List.of("Other important spawn locations (absolute world coordinates)"));
        config.setComments("placement", List.of("Where to place the schematic in the world"));
        
        try {
            config.save(configFile);
        } catch (IOException e) {
            Console.send("Failed to save config file: " + configFile.getName());
            e.printStackTrace();
        }
    }
    
    public static FileConfiguration getSchematicConfig(String schematicFileName) {
        String baseName = schematicFileName.substring(0, schematicFileName.lastIndexOf('.'));
        String configFileName = baseName + ".yml";
        File configFile = new File(CONFIG_FOLDER, configFileName);
        
        if (configFile.exists()) {
            return YamlConfiguration.loadConfiguration(configFile);
        }
        
        // If config doesn't exist, generate it and return
        generateConfigIfNeeded(schematicFileName, baseName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
    
    public static File getConfigFile(String schematicFileName) {
        String baseName = schematicFileName.substring(0, schematicFileName.lastIndexOf('.'));
        String configFileName = baseName + ".yml";
        return new File(CONFIG_FOLDER, configFileName);
    }
    
    public static void loadSchematicWithConfig(String schematicFileName) {
        File schematicFile = getSchematicFile(schematicFileName);
        if (!schematicFile.exists()) {
            Console.send("Schematic file not found: " + schematicFileName);
            return;
        }
        
        FileConfiguration config = getSchematicConfig(schematicFileName);
        
        // Get placement coordinates from config
        double x = config.getDouble("placement.x", 0);
        double y = config.getDouble("placement.y", 70);
        double z = config.getDouble("placement.z", 0);
        
        Location location = new Location(
            org.bukkit.Bukkit.getServer().getWorlds().get(0), 
            x, y, z
        );
        
        Console.send("Loading schematic: " + schematicFileName + " at " + x + ", " + y + ", " + z);
        Worldloader.paste(location, schematicFile);
        
        // Log team spawn information
        if (config.contains("teams")) {
            Console.send("Team spawns configured:");
            for (String teamKey : config.getConfigurationSection("teams").getKeys(false)) {
                String teamName = config.getString("teams." + teamKey + ".name", teamKey);
                double spawnX = config.getDouble("teams." + teamKey + ".spawn.x", 0);
                double spawnY = config.getDouble("teams." + teamKey + ".spawn.y", 70);
                double spawnZ = config.getDouble("teams." + teamKey + ".spawn.z", 0);
                Console.send("  - " + teamName + ": " + spawnX + ", " + spawnY + ", " + spawnZ);
            }
        }
    }
    
    public static Location getTeamSpawn(String schematicFileName, String teamKey) {
        FileConfiguration config = getSchematicConfig(schematicFileName);

        // Get base placement coordinates
        double baseX = config.getDouble("placement.x", 0);
        double baseY = config.getDouble("placement.y", 70);
        double baseZ = config.getDouble("placement.z", 0);

        // Get team-specific spawn coordinates
        double spawnX = config.getDouble("teams." + teamKey + ".spawn.x", 0);
        double spawnY = config.getDouble("teams." + teamKey + ".spawn.y", 70);
        double spawnZ = config.getDouble("teams." + teamKey + ".spawn.z", 0);
        float yaw = (float) config.getDouble("teams." + teamKey + ".spawn.yaw", 0.0);
        float pitch = (float) config.getDouble("teams." + teamKey + ".spawn.pitch", 0.0);

        // Apply the schematic origin offset to spawn coordinates
        // This ensures spawn points move with the schematic when origin offset is applied
        com.sk89q.worldedit.math.BlockVector3 offset = Worldloader.getLastOriginOffset();
        spawnX -= offset.x();
        spawnY -= offset.y();
        spawnZ -= offset.z();

        return new Location(
            org.bukkit.Bukkit.getServer().getWorlds().get(0),
            spawnX, spawnY, spawnZ, yaw, pitch
        );
    }
    
    public static Location getSpectatorSpawn(String schematicFileName) {
        FileConfiguration config = getSchematicConfig(schematicFileName);

        // Get spectator spawn coordinates
        double spawnX = config.getDouble("spawns.spectator.x", 0);
        double spawnY = config.getDouble("spawns.spectator.y", 80);
        double spawnZ = config.getDouble("spawns.spectator.z", 0);
        float yaw = (float) config.getDouble("spawns.spectator.yaw", 0.0);
        float pitch = (float) config.getDouble("spawns.spectator.pitch", -45.0);

        // Apply the schematic origin offset to spawn coordinates
        com.sk89q.worldedit.math.BlockVector3 offset = Worldloader.getLastOriginOffset();
        spawnX -= offset.x();
        spawnY -= offset.y();
        spawnZ -= offset.z();

        return new Location(
            org.bukkit.Bukkit.getServer().getWorlds().get(0),
            spawnX, spawnY, spawnZ, yaw, pitch
        );
    }
    
    public static Location[] getLobbyBounds() {
        // Use the lobby schematic config for bounds, fallback to default lobby config
        String lobbySchematic = Woolbattle.config.getString("Lobby", "Lobby1.schem");
        FileConfiguration config = getSchematicConfig(lobbySchematic);
        
        double minX = config.getDouble("lobby-bounds.min.x", -50.0);
        double minY = config.getDouble("lobby-bounds.min.y", 60.0);
        double minZ = config.getDouble("lobby-bounds.min.z", -50.0);
        double maxX = config.getDouble("lobby-bounds.max.x", 50.0);
        double maxY = config.getDouble("lobby-bounds.max.y", 100.0);
        double maxZ = config.getDouble("lobby-bounds.max.z", 50.0);
        
        Location pos1 = new Location(org.bukkit.Bukkit.getServer().getWorlds().get(0), minX, minY, minZ);
        Location pos2 = new Location(org.bukkit.Bukkit.getServer().getWorlds().get(0), maxX, maxY, maxZ);
        
        return new Location[]{pos1, pos2};
    }
}