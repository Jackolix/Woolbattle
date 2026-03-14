package codes.Elix.Woolbattle.config;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Singleton configuration manager for game settings.
 * Manages loading, saving, and accessing all game configuration values.
 */
public class GameConfig {
    
    private static GameConfig instance;
    private File configFile;
    private FileConfiguration config;
    
    private GameConfig() {
        loadConfig();
    }
    
    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }
    
    /**
     * Load configuration from file. Creates default file if it doesn't exist.
     */
    public void loadConfig() {
        try {
            // Get plugin data folder
            File dataFolder = Woolbattle.getPlugin().getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            
            // Create config file
            configFile = new File(dataFolder, "game-settings.yml");
            
            // Copy default config if file doesn't exist
            if (!configFile.exists()) {
                try (InputStream in = Woolbattle.getPlugin().getResource("game-settings.yml")) {
                    if (in != null) {
                        Files.copy(in, configFile.toPath());
                    }
                }
            }
            
            // Load config
            config = YamlConfiguration.loadConfiguration(configFile);
            
            System.out.println("[GameConfig] Configuration loaded successfully");
            
        } catch (IOException e) {
            System.err.println("[GameConfig] Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Reload configuration from file.
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        System.out.println("[GameConfig] Configuration reloaded");
    }
    
    /**
     * Save current configuration to file.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
            System.out.println("[GameConfig] Configuration saved");
        } catch (IOException e) {
            System.err.println("[GameConfig] Error saving configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ============================================
    // Combat & Damage Settings
    // ============================================
    
    public int getHitTimerSeconds() {
        return config.getInt("combat.hit_timer_seconds", 10);
    }
    
    public void setHitTimerSeconds(int seconds) {
        config.set("combat.hit_timer_seconds", seconds);
    }
    
    public long getHitTimerMillis() {
        return getHitTimerSeconds() * 1000L;
    }
    
    public double getGlobalKnockbackMultiplier() {
        return config.getDouble("combat.global_knockback_multiplier", 1.0);
    }
    
    public void setGlobalKnockbackMultiplier(double multiplier) {
        config.set("combat.global_knockback_multiplier", multiplier);
    }
    
    public boolean isFallDamageEnabled() {
        return config.getBoolean("combat.fall_damage_enabled", false);
    }
    
    public void setFallDamageEnabled(boolean enabled) {
        config.set("combat.fall_damage_enabled", enabled);
    }
    
    public boolean isFireDamageEnabled() {
        return config.getBoolean("combat.fire_damage_enabled", false);
    }
    
    public void setFireDamageEnabled(boolean enabled) {
        config.set("combat.fire_damage_enabled", enabled);
    }
    
    public boolean isPvpEnabled() {
        return config.getBoolean("combat.pvp_enabled", true);
    }

    public void setPvpEnabled(boolean enabled) {
        config.set("combat.pvp_enabled", enabled);
    }

    public int getBowComboRequired() {
        return config.getInt("combat.bow_combo_required", 5);
    }

    public void setBowComboRequired(int count) {
        config.set("combat.bow_combo_required", count);
    }

    public int getBowComboWindowSeconds() {
        return config.getInt("combat.bow_combo_window_seconds", 5);
    }

    public void setBowComboWindowSeconds(int seconds) {
        config.set("combat.bow_combo_window_seconds", seconds);
    }
    
    // ============================================
    // Game Flow Settings
    // ============================================
    
    public int getLobbyCountdownSeconds() {
        return config.getInt("game_flow.lobby_countdown_seconds", 30);
    }
    
    public void setLobbyCountdownSeconds(int seconds) {
        config.set("game_flow.lobby_countdown_seconds", seconds);
    }
    
    public int getMinPlayersToStart() {
        return config.getInt("game_flow.min_players_to_start", 2);
    }
    
    public void setMinPlayersToStart(int players) {
        config.set("game_flow.min_players_to_start", players);
    }
    
    public int getMaxPlayers() {
        return config.getInt("game_flow.max_players", 4);
    }
    
    public void setMaxPlayers(int players) {
        config.set("game_flow.max_players", players);
    }
    
    public boolean isAutoStartWhenFull() {
        return config.getBoolean("game_flow.auto_start_when_full", true);
    }
    
    public void setAutoStartWhenFull(boolean enabled) {
        config.set("game_flow.auto_start_when_full", enabled);
    }
    
    public int getGameEndCountdown() {
        return config.getInt("game_flow.game_end_countdown", 10);
    }
    
    public void setGameEndCountdown(int seconds) {
        config.set("game_flow.game_end_countdown", seconds);
    }
    
    // ============================================
    // Team Settings
    // ============================================
    
    public int getTeamSize() {
        return config.getInt("teams.team_size", 2);
    }
    
    public void setTeamSize(int size) {
        config.set("teams.team_size", size);
    }
    
    public int getNumberOfTeams() {
        return config.getInt("teams.number_of_teams", 2);
    }
    
    public void setNumberOfTeams(int teams) {
        config.set("teams.number_of_teams", Math.max(2, Math.min(4, teams)));
    }
    
    public boolean isTeamSwitchingAllowed() {
        return config.getBoolean("teams.allow_team_switching", true);
    }
    
    public void setTeamSwitchingAllowed(boolean allowed) {
        config.set("teams.allow_team_switching", allowed);
    }
    
    public boolean isAutoTeamBalance() {
        return config.getBoolean("teams.auto_team_balance", true);
    }
    
    public void setAutoTeamBalance(boolean enabled) {
        config.set("teams.auto_team_balance", enabled);
    }
    
    public boolean isFriendlyFire() {
        return config.getBoolean("teams.friendly_fire", false);
    }
    
    public void setFriendlyFire(boolean enabled) {
        config.set("teams.friendly_fire", enabled);
    }
    
    // ============================================
    // Wool & Building Settings
    // ============================================
    
    public boolean isWoolRegenerationEnabled() {
        return config.getBoolean("wool_building.wool_regeneration_enabled", true);
    }
    
    public void setWoolRegenerationEnabled(boolean enabled) {
        config.set("wool_building.wool_regeneration_enabled", enabled);
    }
    
    public int getWoolRegenIntervalTicks() {
        return config.getInt("wool_building.wool_regen_interval_ticks", 20);
    }
    
    public void setWoolRegenIntervalTicks(int ticks) {
        config.set("wool_building.wool_regen_interval_ticks", ticks);
    }
    
    public int getMaxWoolPerPlayer() {
        return config.getInt("wool_building.max_wool_per_player", 64);
    }
    
    public void setMaxWoolPerPlayer(int amount) {
        config.set("wool_building.max_wool_per_player", amount);
    }
    
    public int getPlacedWoolCooldownTicks() {
        return config.getInt("wool_building.placed_wool_cooldown_ticks", 10);
    }
    
    public void setPlacedWoolCooldownTicks(int ticks) {
        config.set("wool_building.placed_wool_cooldown_ticks", ticks);
    }
    
    public boolean isInfiniteArrows() {
        return config.getBoolean("wool_building.infinite_arrows", true);
    }
    
    public void setInfiniteArrows(boolean enabled) {
        config.set("wool_building.infinite_arrows", enabled);
    }
    
    public boolean isBlockBreakingAllowed() {
        return config.getBoolean("wool_building.allow_block_breaking", false);
    }
    
    public void setBlockBreakingAllowed(boolean allowed) {
        config.set("wool_building.allow_block_breaking", allowed);
    }
    
    // ============================================
    // Lives & Respawn Settings
    // ============================================
    
    public int getDefaultLives() {
        return config.getInt("lives_respawn.default_lives", 12);
    }
    
    public void setDefaultLives(int lives) {
        config.set("lives_respawn.default_lives", lives);
    }
    
    public int getRespawnDelaySeconds() {
        return config.getInt("lives_respawn.respawn_delay_seconds", 3);
    }
    
    public void setRespawnDelaySeconds(int seconds) {
        config.set("lives_respawn.respawn_delay_seconds", seconds);
    }
    
    public int getVoidDeathYLevel() {
        return config.getInt("lives_respawn.void_death_y_level", 0);
    }
    
    public void setVoidDeathYLevel(int yLevel) {
        config.set("lives_respawn.void_death_y_level", yLevel);
    }
    
    public boolean isKeepInventoryOnDeath() {
        return config.getBoolean("lives_respawn.keep_inventory_on_death", true);
    }
    
    public void setKeepInventoryOnDeath(boolean keep) {
        config.set("lives_respawn.keep_inventory_on_death", keep);
    }
    
    public int getInvulnerabilityOnSpawnSeconds() {
        return config.getInt("lives_respawn.invulnerability_on_spawn_seconds", 3);
    }
    
    public void setInvulnerabilityOnSpawnSeconds(int seconds) {
        config.set("lives_respawn.invulnerability_on_spawn_seconds", seconds);
    }
    
    // ============================================
    // Passive Perk Settings
    // ============================================
    
    public boolean isDoubleJumpEnabled() {
        return config.getBoolean("passive_perks.double_jump_enabled", true);
    }
    
    public void setDoubleJumpEnabled(boolean enabled) {
        config.set("passive_perks.double_jump_enabled", enabled);
    }
    
    public double getDoubleJumpHeight() {
        return config.getDouble("passive_perks.double_jump_height", 0.8);
    }
    
    public void setDoubleJumpHeight(double height) {
        config.set("passive_perks.double_jump_height", height);
    }
    
    public boolean isRocketJumpEnabled() {
        return config.getBoolean("passive_perks.rocket_jump_enabled", true);
    }
    
    public void setRocketJumpEnabled(boolean enabled) {
        config.set("passive_perks.rocket_jump_enabled", enabled);
    }
    
    public double getRocketJumpHeight() {
        return config.getDouble("passive_perks.rocket_jump_height", 0.5);
    }
    
    public void setRocketJumpHeight(double height) {
        config.set("passive_perks.rocket_jump_height", height);
    }
    
    public boolean isRechargerEnabled() {
        return config.getBoolean("passive_perks.recharger_enabled", true);
    }
    
    public void setRechargerEnabled(boolean enabled) {
        config.set("passive_perks.recharger_enabled", enabled);
    }
    
    public double getRechargerCooldownReduction() {
        return config.getDouble("passive_perks.recharger_cooldown_reduction", 0.5);
    }
    
    public void setRechargerCooldownReduction(double reduction) {
        config.set("passive_perks.recharger_cooldown_reduction", reduction);
    }
    
    // ============================================
    // Advanced Settings
    // ============================================
    
    public boolean isDebugMode() {
        return config.getBoolean("advanced.debug_mode", false);
    }
    
    public void setDebugMode(boolean enabled) {
        config.set("advanced.debug_mode", enabled);
    }
    
    public boolean isPerformanceMode() {
        return config.getBoolean("advanced.performance_mode", false);
    }
    
    public void setPerformanceMode(boolean enabled) {
        config.set("advanced.performance_mode", enabled);
    }
    
    public double getGlobalCooldownMultiplier() {
        return config.getDouble("advanced.global_cooldown_multiplier", 1.0);
    }
    
    public void setGlobalCooldownMultiplier(double multiplier) {
        config.set("advanced.global_cooldown_multiplier", multiplier);
    }
    
    public boolean isMongoDBEnabled() {
        return config.getBoolean("advanced.mongodb_enabled", false);
    }
    
    public void setMongoDBEnabled(boolean enabled) {
        config.set("advanced.mongodb_enabled", enabled);
    }
    
    public boolean isScoreboardEnabled() {
        return config.getBoolean("advanced.scoreboard_enabled", true);
    }
    
    public void setScoreboardEnabled(boolean enabled) {
        config.set("advanced.scoreboard_enabled", enabled);
    }
    
    public boolean isParticlesEnabled() {
        return config.getBoolean("advanced.particles_enabled", true);
    }
    
    public void setParticlesEnabled(boolean enabled) {
        config.set("advanced.particles_enabled", enabled);
    }
    
    // ============================================
    // Map & World Settings
    // ============================================
    
    public boolean isMapVotingEnabled() {
        return config.getBoolean("maps.map_voting_enabled", true);
    }
    
    public void setMapVotingEnabled(boolean enabled) {
        config.set("maps.map_voting_enabled", enabled);
    }
    
    public int getVotingDurationSeconds() {
        return config.getInt("maps.voting_duration_seconds", 20);
    }
    
    public void setVotingDurationSeconds(int seconds) {
        config.set("maps.voting_duration_seconds", seconds);
    }
    
    public boolean isRandomOnTie() {
        return config.getBoolean("maps.random_on_tie", true);
    }
    
    public void setRandomOnTie(boolean enabled) {
        config.set("maps.random_on_tie", enabled);
    }
    
    public boolean isShowVoteCounts() {
        return config.getBoolean("maps.show_vote_counts", true);
    }
    
    public void setShowVoteCounts(boolean show) {
        config.set("maps.show_vote_counts", show);
    }
    
    public boolean isVoidTeleportEnabled() {
        return config.getBoolean("maps.void_teleport_enabled", true);
    }
    
    public void setVoidTeleportEnabled(boolean enabled) {
        config.set("maps.void_teleport_enabled", enabled);
    }
    
    // ============================================
    // Generic Getters/Setters for GUI
    // ============================================
    
    /**
     * Get a setting value by its full path key.
     */
    public Object getValue(String key) {
        return config.get(key);
    }
    
    /**
     * Set a setting value by its full path key.
     */
    public void setValue(String key, Object value) {
        config.set(key, value);
    }
}
