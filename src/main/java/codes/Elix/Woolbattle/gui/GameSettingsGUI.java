package codes.Elix.Woolbattle.gui;

import codes.Elix.Woolbattle.config.GameConfig;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * GUI for configuring game settings in-game.
 * Similar to PerkConfigGUI but for general game configuration.
 */
public class GameSettingsGUI implements Listener {
    
    private static final Map<Player, String> waitingForInput = new HashMap<>();
    private static final Map<Player, ConfigContext> configContext = new HashMap<>();
    private static final NamespacedKey SETTING_KEY = new NamespacedKey(Woolbattle.getPlugin(), "game_setting_key");
    private static final NamespacedKey CATEGORY_KEY = new NamespacedKey(Woolbattle.getPlugin(), "category_key");
    
    private static class ConfigContext {
        String category;
        String settingKey;
        
        ConfigContext(String category, String settingKey) {
            this.category = category;
            this.settingKey = settingKey;
        }
    }
    
    /**
     * Open the main settings menu with all categories.
     */
    public static void openMainMenu(Player player) {
        if (!player.hasPermission("woolbattle.admin.gamesettings") && !player.isOp()) {
            player.sendMessage(Component.text("You don't have permission to access game settings.", NamedTextColor.RED));
            return;
        }
        
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("Game Settings", NamedTextColor.GOLD));
        
        // Category selection items
        addCategoryItem(inventory, 10, Material.DIAMOND_SWORD, "Combat", "combat", "Configure combat and damage settings");
        addCategoryItem(inventory, 11, Material.CLOCK, "Game Flow", "game_flow", "Configure game timing and flow");
        addCategoryItem(inventory, 12, Material.PLAYER_HEAD, "Teams", "teams", "Configure team settings");
        addCategoryItem(inventory, 13, Material.GOLDEN_APPLE, "Lives & Respawn", "lives_respawn", "Configure lives and respawn settings");
        addCategoryItem(inventory, 14, Material.REDSTONE, "Advanced", "advanced", "Configure advanced settings");
        
        // Link to PerkConfigGUI for perk settings
        addLinkItem(inventory, 16, Material.COMMAND_BLOCK, "Perk Configuration", "Open Perk Config GUI");
        
        // Control buttons
        addControlButton(inventory, 45, Material.EMERALD_BLOCK, 
            Component.text("Reload Config", NamedTextColor.GREEN).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD),
            "Reload settings from file");
        addControlButton(inventory, 46, Material.BARRIER, 
            Component.text("Reset All to Defaults", NamedTextColor.RED).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD),
            "Reset all settings to defaults");
        addControlButton(inventory, 53, Material.REDSTONE_BLOCK, 
            Component.text("Close", NamedTextColor.RED).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD),
            "Close this menu");
        
        // Decorative items
        fillEmptySlots(inventory);
        
        player.openInventory(inventory);
    }
    
    private static void addCategoryItem(Inventory inventory, int slot, Material material, String displayName, String categoryKey, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName, NamedTextColor.YELLOW));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(description, NamedTextColor.GRAY));
        lore.add(Component.text(""));
        lore.add(Component.text("Click to configure", NamedTextColor.GREEN));
        meta.lore(lore);
        
        meta.getPersistentDataContainer().set(CATEGORY_KEY, PersistentDataType.STRING, categoryKey);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    private static void addLinkItem(Inventory inventory, int slot, Material material, String displayName, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName, NamedTextColor.LIGHT_PURPLE));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(description, NamedTextColor.GRAY));
        lore.add(Component.text(""));
        lore.add(Component.text("Click to open", NamedTextColor.GREEN));
        meta.lore(lore);
        
        meta.getPersistentDataContainer().set(CATEGORY_KEY, PersistentDataType.STRING, "perk_config_link");
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    private static void addControlButton(Inventory inventory, int slot, Material material, Component displayName, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        
        if (description != null && !description.isEmpty()) {
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(description, NamedTextColor.GRAY));
            meta.lore(lore);
        }
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    /**
     * Open a category-specific settings menu.
     */
    public static void openCategoryMenu(Player player, String category) {
        String title = getCategoryTitle(category);
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text(title, NamedTextColor.GOLD));
        
        GameConfig config = GameConfig.getInstance();
        
        switch (category) {
            case "combat" -> addCombatSettings(inventory, config);
            case "game_flow" -> addGameFlowSettings(inventory, config);
            case "teams" -> addTeamSettings(inventory, config);
            case "lives_respawn" -> addLivesRespawnSettings(inventory, config);
            case "advanced" -> addAdvancedSettings(inventory, config);
        }
        
        // Control buttons
        addControlButton(inventory, 45, Material.GREEN_WOOL, 
            Component.text("Save & Apply", NamedTextColor.GREEN).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD),
            "Save and apply changes");
        addControlButton(inventory, 46, Material.YELLOW_WOOL, 
            Component.text("Reset Category", NamedTextColor.YELLOW).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD),
            "Reset this category to defaults");
        addControlButton(inventory, 53, Material.BLUE_WOOL, 
            Component.text("Back to Main Menu", NamedTextColor.AQUA).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD),
            "Return to main settings menu");
        
        // Decorative items
        fillEmptySlots(inventory);
        
        player.openInventory(inventory);
    }
    
    private static String getCategoryTitle(String category) {
        return switch (category) {
            case "combat" -> "Combat & Damage Settings";
            case "game_flow" -> "Game Flow Settings";
            case "teams" -> "Team Settings";
            case "lives_respawn" -> "Lives & Respawn Settings";
            case "advanced" -> "Advanced Settings";
            default -> "Settings";
        };
    }
    
    // ============================================
    // Category Setting Builders
    // ============================================
    
    private static void addCombatSettings(Inventory inventory, GameConfig config) {
        addNumberSetting(inventory, 10, Material.CLOCK, "Hit Timer (seconds)", 
            config.getHitTimerSeconds(), "combat.hit_timer_seconds", 
            "Time for void kill credit");
        
        addNumberSetting(inventory, 11, Material.IRON_SWORD, "Knockback Multiplier", 
            config.getGlobalKnockbackMultiplier(), "combat.global_knockback_multiplier", 
            "Global knockback strength");
        
        addToggleSetting(inventory, 12, Material.FEATHER, "Fall Damage", 
            config.isFallDamageEnabled(), "combat.fall_damage_enabled", 
            "Enable fall damage");
        
        addToggleSetting(inventory, 13, Material.FIRE_CHARGE, "Fire Damage", 
            config.isFireDamageEnabled(), "combat.fire_damage_enabled", 
            "Enable fire damage");
        
        addToggleSetting(inventory, 14, Material.DIAMOND_SWORD, "PvP Enabled", 
            config.isPvpEnabled(), "combat.pvp_enabled", 
            "Enable player vs player");
    }
    
    private static void addGameFlowSettings(Inventory inventory, GameConfig config) {
        addNumberSetting(inventory, 10, Material.CLOCK, "Lobby Countdown (seconds)", 
            config.getLobbyCountdownSeconds(), "game_flow.lobby_countdown_seconds", 
            "Countdown before game starts");
        
        addNumberSetting(inventory, 11, Material.PLAYER_HEAD, "Min Players to Start", 
            config.getMinPlayersToStart(), "game_flow.min_players_to_start", 
            "Minimum players needed");
        
        addNumberSetting(inventory, 12, Material.BARRIER, "Max Players", 
            config.getMaxPlayers(), "game_flow.max_players", 
            "Maximum players allowed");
        
        addToggleSetting(inventory, 13, Material.EMERALD, "Auto Start When Full", 
            config.isAutoStartWhenFull(), "game_flow.auto_start_when_full", 
            "Start when max reached");
        
        addNumberSetting(inventory, 14, Material.CLOCK, "Game End Countdown", 
            config.getGameEndCountdown(), "game_flow.game_end_countdown", 
            "Countdown after game ends");
    }
    
    private static void addTeamSettings(Inventory inventory, GameConfig config) {
        addNumberSetting(inventory, 10, Material.PLAYER_HEAD, "Team Size", 
            config.getTeamSize(), "teams.team_size", 
            "Players per team");
        
        addNumberSetting(inventory, 11, Material.BEACON, "Number of Teams", 
            config.getNumberOfTeams(), "teams.number_of_teams", 
            "Total teams (2-4)");
        
        addToggleSetting(inventory, 12, Material.COMPASS, "Team Switching", 
            config.isTeamSwitchingAllowed(), "teams.allow_team_switching", 
            "Allow switching teams");
        
        addToggleSetting(inventory, 13, Material.GOLDEN_APPLE, "Auto Team Balance", 
            config.isAutoTeamBalance(), "teams.auto_team_balance", 
            "Balance teams automatically");
    }
    

    
    private static void addLivesRespawnSettings(Inventory inventory, GameConfig config) {
        addNumberSetting(inventory, 10, Material.GOLDEN_APPLE, "Default Lives", 
            config.getDefaultLives(), "lives_respawn.default_lives", 
            "Starting life count (overridden by voting)");
        
        addNumberSetting(inventory, 11, Material.TOTEM_OF_UNDYING, "Spawn Invulnerability (sec)", 
            config.getInvulnerabilityOnSpawnSeconds(), "lives_respawn.invulnerability_on_spawn_seconds", 
            "Invulnerability time after spawning");
    }
    
    private static void addAdvancedSettings(Inventory inventory, GameConfig config) {
        addToggleSetting(inventory, 10, Material.COMMAND_BLOCK, "Debug Mode", 
            config.isDebugMode(), "advanced.debug_mode", 
            "Enable debug logging");
        
        addToggleSetting(inventory, 11, Material.REDSTONE, "Performance Mode", 
            config.isPerformanceMode(), "advanced.performance_mode", 
            "Reduce effects");
        
        addNumberSetting(inventory, 12, Material.CLOCK, "Cooldown Multiplier", 
            config.getGlobalCooldownMultiplier(), "advanced.global_cooldown_multiplier", 
            "Global cooldown multiplier");
        
        addToggleSetting(inventory, 13, Material.BOOK, "MongoDB", 
            config.isMongoDBEnabled(), "advanced.mongodb_enabled", 
            "Enable database");
        
        addToggleSetting(inventory, 14, Material.PAINTING, "Scoreboard", 
            config.isScoreboardEnabled(), "advanced.scoreboard_enabled", 
            "Show scoreboard");
        
        addToggleSetting(inventory, 15, Material.BLAZE_POWDER, "Particles", 
            config.isParticlesEnabled(), "advanced.particles_enabled", 
            "Show particles");
    }
    

    
    // ============================================
    // Setting Item Builders
    // ============================================
    
    private static void addNumberSetting(Inventory inventory, int slot, Material material, 
                                         String displayName, Object currentValue, String settingKey, 
                                         String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName, NamedTextColor.AQUA));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Current: " + currentValue, NamedTextColor.YELLOW));
        lore.add(Component.text(description, NamedTextColor.GRAY));
        lore.add(Component.text(""));
        lore.add(Component.text("Left Click: Set value", NamedTextColor.GREEN));
        lore.add(Component.text("Right Click: +1", NamedTextColor.BLUE));
        lore.add(Component.text("Shift+Right: -1", NamedTextColor.RED));
        meta.lore(lore);
        
        meta.getPersistentDataContainer().set(SETTING_KEY, PersistentDataType.STRING, settingKey);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    private static void addToggleSetting(Inventory inventory, int slot, Material material, 
                                         String displayName, boolean currentValue, String settingKey, 
                                         String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName, NamedTextColor.AQUA));
        
        Component statusComponent;
        if (currentValue) {
            statusComponent = Component.text("Status: ", NamedTextColor.GRAY)
                .append(Component.text("✓ ENABLED", NamedTextColor.GREEN));
        } else {
            statusComponent = Component.text("Status: ", NamedTextColor.GRAY)
                .append(Component.text("✗ DISABLED", NamedTextColor.RED));
        }
        
        List<Component> lore = new ArrayList<>();
        lore.add(statusComponent);
        lore.add(Component.text(description, NamedTextColor.GRAY));
        lore.add(Component.text(""));
        lore.add(Component.text("Click to toggle", NamedTextColor.YELLOW));
        meta.lore(lore);
        
        meta.getPersistentDataContainer().set(SETTING_KEY, PersistentDataType.STRING, settingKey);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    // ============================================
    // Event Handlers
    // ============================================
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;
        
        Component titleComponent = event.getView().title();
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(titleComponent);
        
        // Handle main menu
        if (title.equals("Game Settings")) {
            event.setCancelled(true);
            handleMainMenuClick(player, event.getCurrentItem());
            return;
        }
        
        // Handle category menus
        if (title.contains("Settings")) {
            event.setCancelled(true);
            String category = getCategoryFromTitle(title);
            handleCategoryClick(player, event.getCurrentItem(), category, event.isRightClick(), event.isShiftClick());
        }
    }
    
    private void handleMainMenuClick(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        Material type = item.getType();
        
        // Handle control buttons
        if (type == Material.EMERALD_BLOCK) {
            GameConfig.getInstance().reloadConfig();
            player.sendMessage(Component.text("Configuration reloaded!", NamedTextColor.GREEN));
            openMainMenu(player);
            return;
        }
        
        if (type == Material.BARRIER) {
            player.sendMessage(Component.text("Reset functionality coming soon!", NamedTextColor.YELLOW));
            return;
        }
        
        if (type == Material.REDSTONE_BLOCK) {
            player.closeInventory();
            return;
        }
        
        // Handle category selection
        if (meta.getPersistentDataContainer().has(CATEGORY_KEY, PersistentDataType.STRING)) {
            String category = meta.getPersistentDataContainer().get(CATEGORY_KEY, PersistentDataType.STRING);
            if (category != null) {
                if (category.equals("perk_config_link")) {
                    // Open PerkConfigGUI instead
                    player.closeInventory();
                    PerkConfigGUI.openMainConfigGUI(player);
                } else {
                    openCategoryMenu(player, category);
                }
            }
        }
    }
    
    private void handleCategoryClick(Player player, ItemStack item, String category, 
                                     boolean isRightClick, boolean isShiftClick) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        Material type = item.getType();
        
        // Handle control buttons
        if (type == Material.GREEN_WOOL) {
            GameConfig.getInstance().saveConfig();
            
            // Reload dependent systems
            LobbyState.MIN_PLAYERS = GameConfig.getInstance().getMinPlayersToStart();
            LobbyState.MAX_PLAYERS = GameConfig.getInstance().getMaxPlayers();
            LiveSystem.reloadTeamSettings();
            
            player.sendMessage(Component.text("Settings saved and applied!", NamedTextColor.GREEN));
            return;
        }
        
        if (type == Material.YELLOW_WOOL) {
            player.sendMessage(Component.text("Reset category functionality coming soon!", NamedTextColor.YELLOW));
            return;
        }
        
        if (type == Material.BLUE_WOOL) {
            openMainMenu(player);
            return;
        }
        
        // Handle setting items
        if (meta.getPersistentDataContainer().has(SETTING_KEY, PersistentDataType.STRING)) {
            String settingKey = meta.getPersistentDataContainer().get(SETTING_KEY, PersistentDataType.STRING);
            if (settingKey != null) {
                handleSettingClick(player, category, settingKey, isRightClick, isShiftClick);
            }
        }
    }
    
    private void handleSettingClick(Player player, String category, String settingKey, 
                                    boolean isRightClick, boolean isShiftClick) {
        GameConfig config = GameConfig.getInstance();
        Object currentValue = config.getValue(settingKey);
        
        // Check if it's a boolean toggle
        if (currentValue instanceof Boolean) {
            config.setValue(settingKey, !(Boolean)currentValue);
            player.sendMessage(Component.text("Toggled " + settingKey + " to " + config.getValue(settingKey), NamedTextColor.GREEN));
            Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, category));
            return;
        }
        
        // Handle numeric values
        if (!isRightClick) {
            // Left click - custom input
            startCustomInput(player, category, settingKey);
        } else {
            // Right click - increment/decrement
            modifyNumericValue(player, category, settingKey, isShiftClick ? -1 : 1);
        }
    }
    
    private void startCustomInput(Player player, String category, String settingKey) {
        player.closeInventory();
        waitingForInput.put(player, settingKey);
        configContext.put(player, new ConfigContext(category, settingKey));
        
        String friendlyName = settingKey.substring(settingKey.lastIndexOf('.') + 1).replace("_", " ");
        player.sendMessage(Component.text("Enter new value for " + friendlyName + " (type 'cancel' to abort):", NamedTextColor.YELLOW));
    }
    
    private void modifyNumericValue(Player player, String category, String settingKey, int change) {
        GameConfig config = GameConfig.getInstance();
        Object currentValue = config.getValue(settingKey);
        
        double newValue;
        if (currentValue instanceof Integer) {
            newValue = Math.max(0, (Integer)currentValue + change);
        } else if (currentValue instanceof Double) {
            newValue = Math.max(0.0, (Double)currentValue + (change * 0.1));
            newValue = Math.round(newValue * 10.0) / 10.0;
        } else {
            player.sendMessage(Component.text("Cannot modify this setting type", NamedTextColor.RED));
            return;
        }
        
        config.setValue(settingKey, currentValue instanceof Integer ? (int)newValue : newValue);
        player.sendMessage(Component.text("Updated to " + newValue, NamedTextColor.GREEN));
        Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, category));
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (waitingForInput.containsKey(player)) {
            event.setCancelled(true);
            
            String input = event.getMessage().trim();
            String settingKey = waitingForInput.remove(player);
            ConfigContext context = configContext.remove(player);
            
            if (context == null) return;
            
            if (input.equalsIgnoreCase("cancel")) {
                player.sendMessage(Component.text("Input cancelled.", NamedTextColor.YELLOW));
                Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, context.category));
                return;
            }
            
            try {
                GameConfig config = GameConfig.getInstance();
                Object currentValue = config.getValue(settingKey);
                
                if (currentValue instanceof Integer) {
                    int value = Integer.parseInt(input);
                    if (value < 0) {
                        player.sendMessage(Component.text("Value must be positive!", NamedTextColor.RED));
                        Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, context.category));
                        return;
                    }
                    config.setValue(settingKey, value);
                } else if (currentValue instanceof Double) {
                    double value = Double.parseDouble(input);
                    if (value < 0) {
                        player.sendMessage(Component.text("Value must be positive!", NamedTextColor.RED));
                        Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, context.category));
                        return;
                    }
                    config.setValue(settingKey, value);
                }
                
                player.sendMessage(Component.text("Updated " + settingKey + " to " + input, NamedTextColor.GREEN));
                Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, context.category));
                
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Invalid number format!", NamedTextColor.RED));
                Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openCategoryMenu(player, context.category));
            }
        }
    }
    
    // ============================================
    // Utility Methods
    // ============================================
    
    private static void fillEmptySlots(Inventory inventory) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        meta.displayName(Component.text(" "));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        glass.setItemMeta(meta);
        
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }
    
    private String getCategoryFromTitle(String title) {
        if (title.contains("Combat")) return "combat";
        if (title.contains("Game Flow")) return "game_flow";
        if (title.contains("Team")) return "teams";
        if (title.contains("Lives")) return "lives_respawn";
        if (title.contains("Advanced")) return "advanced";
        return "unknown";
    }
}
