package codes.Elix.Woolbattle.gui;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.EnderPearl;
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

public class PerkConfigGUI implements Listener {
    
    private static final Map<Player, String> waitingForInput = new HashMap<>();
    private static final Map<Player, ConfigContext> configContext = new HashMap<>();
    private static final NamespacedKey SETTING_KEY = new NamespacedKey(Woolbattle.getPlugin(), "setting_key");
    
    private static class ConfigContext {
        String perkName;
        String settingKey;
        
        ConfigContext(String perkName, String settingKey) {
            this.perkName = perkName;
            this.settingKey = settingKey;
        }
    }
    
    public static void openMainConfigGUI(Player player) {
        if (!player.hasPermission("woolbattle.admin.config") && !player.isOp()) {
            player.sendMessage(Component.text("You don't have permission to access perk configuration.", NamedTextColor.RED));
            return;
        }
        
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("Perk Configuration", NamedTextColor.DARK_PURPLE));
        
        // Add perk selection items
        addPerkConfigItem(inventory, 10, Material.PACKED_ICE, "Freezer", "Configure freezer perk settings");
        addPerkConfigItem(inventory, 11, Material.CARROT_ON_A_STICK, "Grabber", "Configure grabber perk settings");
        addPerkConfigItem(inventory, 12, Material.TNT, "Woolbomb", "Configure woolbomb perk settings");
        addPerkConfigItem(inventory, 13, Material.SNOWBALL, "Switcher", "Configure switcher perk settings");
        addPerkConfigItem(inventory, 14, Material.BLAZE_ROD, "Platform", "Configure platform perk settings");
        addPerkConfigItem(inventory, 15, Material.RED_STAINED_GLASS_PANE, "Wall", "Configure wall perk settings");
        addPerkConfigItem(inventory, 16, Material.VINE, "Rope", "Configure rope perk settings");
        
        addPerkConfigItem(inventory, 19, Material.TRIPWIRE_HOOK, "Booster", "Configure booster perk settings");
        addPerkConfigItem(inventory, 20, Material.FISHING_ROD, "Enterhaken", "Configure enterhaken perk settings");
        addPerkConfigItem(inventory, 21, Material.CLOCK, "Clock", "Configure clock perk settings");
        addPerkConfigItem(inventory, 22, Material.EMERALD, "Rettungsplattform", "Configure emergency platform settings");

        addPerkConfigItem(inventory, 28, Material.ENDER_PEARL, "EnderPearl", "Configure enderpearl settings");
        addPerkConfigItem(inventory, 29, Material.FEATHER, "DoubleJump", "Configure double jump settings");

        // Control buttons
        Items.create(inventory, Material.EMERALD_BLOCK, "§a§lReload Config", 45);
        Items.create(inventory, Material.REDSTONE_BLOCK, "§c§lClose", 53);
        
        // Decorative items
        for (int i : Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,46,47,48,49,50,51,52)) {
            ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.displayName(Component.text(" "));
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            glass.setItemMeta(meta);
            inventory.setItem(i, glass);
        }
        
        player.openInventory(inventory);
    }
    
    private static boolean isProjectilePerk(String perkName) {
        // Perks that shoot projectiles and use projectile velocity
        return switch (perkName.toLowerCase()) {
            case "freezer", "grabber", "woolbomb", "switcher" -> true;
            default -> false;
        };
    }
    
    private static void addPerkConfigItem(Inventory inventory, int slot, Material material, String perkName, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Configure " + perkName, NamedTextColor.YELLOW));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(description, NamedTextColor.GRAY));
        lore.add(Component.text(""));
        lore.add(Component.text("Click to configure this perk", NamedTextColor.GREEN));
        meta.lore(lore);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    public static void openPerkConfigGUI(Player player, String perkName) {
        PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings(perkName.toLowerCase());
        
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("Configure " + perkName, NamedTextColor.DARK_PURPLE));
        
        // Basic settings - cooldown and cost for all perks
        addConfigSetting(inventory, 10, Material.CLOCK, "Cooldown", String.valueOf(settings.getCooldown()), "cooldown");
        addConfigSetting(inventory, 11, Material.CLOCK, "Cooldown (Recharger)", String.valueOf(settings.getCooldownRecharger()), "cooldown_recharger");
        addConfigSetting(inventory, 12, Material.EMERALD, "Cost", String.valueOf(settings.getCost()), "cost");
        
        // Projectile velocity only for perks that shoot projectiles
        if (isProjectilePerk(perkName.toLowerCase())) {
            addConfigSetting(inventory, 13, Material.ARROW, "Projectile Velocity", String.valueOf(settings.getProjectileVelocity()), "projectile_velocity");
        }
        
        // Perk-specific settings
        switch (perkName.toLowerCase()) {
            case "freezer":
                addConfigSetting(inventory, 19, Material.ICE, "Freeze Time (seconds)", String.valueOf(settings.getFreezeTimeSeconds()), "freeze_time_seconds");
                addConfigSetting(inventory, 20, Material.COBWEB, "Slowness Strength", String.valueOf(settings.getSlownessStrength()), "slowness_strength");
                break;
            case "grabber":
                addConfigSetting(inventory, 19, Material.FISHING_ROD, "Grab Timeout (seconds)", String.valueOf(settings.getGrabTimeoutSeconds()), "grab_timeout_seconds");
                addConfigSetting(inventory, 20, Material.TARGET, "Hit Timeout (seconds)", String.valueOf(settings.getHitTimeoutSeconds()), "hit_timeout_seconds");
                break;
            case "woolbomb":
                addConfigSetting(inventory, 19, Material.TNT, "Explosion Radius", String.valueOf(settings.getExplosionRadius()), "explosion_radius");
                addConfigSetting(inventory, 20, Material.REDSTONE, "Fuse Ticks", String.valueOf(settings.getFuseTicks()), "fuse_ticks");
                addConfigSetting(inventory, 21, Material.FEATHER, "Knockback Multiplier", String.valueOf(settings.getKnockbackMultiplier()), "knockback_multiplier");
                addConfigSetting(inventory, 22, Material.ELYTRA, "Y Velocity", String.valueOf(settings.getKnockbackYVelocity()), "knockback_y_velocity");
                addConfigSetting(inventory, 28, Material.COMPARATOR, "Explosion Delay Ticks", String.valueOf(settings.getExplosionDelayTicks()), "explosion_delay_ticks");
                break;
            case "booster":
                addConfigSetting(inventory, 19, Material.SUGAR, "Speed Duration (seconds)", String.valueOf(settings.getSpeedDuration()), "speed_duration");
                addConfigSetting(inventory, 20, Material.SUGAR, "Speed Strength", String.valueOf(settings.getSpeedStrength()), "speed_strength");
                addConfigSetting(inventory, 21, Material.RABBIT_FOOT, "Jump Duration (seconds)", String.valueOf(settings.getJumpDuration()), "jump_duration");
                addConfigSetting(inventory, 22, Material.RABBIT_FOOT, "Jump Strength", String.valueOf(settings.getJumpStrength()), "jump_strength");
                break;
            case "platform":
                addConfigSetting(inventory, 19, Material.SMOOTH_STONE, "Platform Size", String.valueOf(settings.getPlatformSize()), "platform_size");
                addConfigSetting(inventory, 20, Material.CLOCK, "Platform Duration (seconds)", String.valueOf(settings.getPlatformDuration()), "platform_duration");
                addConfigSetting(inventory, 21, Material.SPYGLASS, "Max Distance", String.valueOf(settings.getMaxDistance()), "max_distance");
                break;
            case "wall":
                addConfigSetting(inventory, 19, Material.BRICK_WALL, "Wall Height", String.valueOf(settings.getWallHeight()), "wall_height");
                addConfigSetting(inventory, 20, Material.BRICK, "Wall Width", String.valueOf(settings.getWallWidth()), "wall_width");
                addConfigSetting(inventory, 21, Material.CLOCK, "Wall Duration (seconds)", String.valueOf(settings.getWallDuration()), "wall_duration");
                addConfigSetting(inventory, 22, Material.SPYGLASS, "Max Distance", String.valueOf(settings.getMaxDistance()), "max_distance");
                break;
            case "rope":
                addConfigSetting(inventory, 19, Material.STRING, "Rope Length", String.valueOf(settings.getRopeLength()), "rope_length");
                addConfigSetting(inventory, 20, Material.PISTON, "Pull Strength", String.valueOf(settings.getPullStrength()), "pull_strength");
                break;
            case "enterhaken":
                addConfigSetting(inventory, 19, Material.ARROW, "Hook Velocity", String.valueOf(settings.getHookVelocity()), "hook_velocity");
                addConfigSetting(inventory, 20, Material.PISTON, "Pull Velocity", String.valueOf(settings.getPullVelocity()), "pull_velocity");
                addConfigSetting(inventory, 21, Material.SPYGLASS, "Max Distance", String.valueOf(settings.getMaxDistance()), "max_distance");
                break;
            case "clock":
                addConfigSetting(inventory, 19, Material.TARGET, "Effect Radius", String.valueOf(settings.getEffectRadius()), "effect_radius");
                addConfigSetting(inventory, 20, Material.CLOCK, "Effect Duration (seconds)", String.valueOf(settings.getEffectDuration()), "effect_duration");
                addConfigSetting(inventory, 21, Material.COBWEB, "Slowness Strength", String.valueOf(settings.getSlownessStrengthClock()), "slowness_strength");
                break;
            case "rettungsplattform":
                addConfigSetting(inventory, 19, Material.EMERALD_BLOCK, "Platform Size", String.valueOf(settings.getPlatformSizeEmergency()), "platform_size");
                addConfigSetting(inventory, 20, Material.CLOCK, "Platform Duration (seconds)", String.valueOf(settings.getPlatformDurationEmergency()), "platform_duration");
                addConfigSetting(inventory, 21, Material.FEATHER, "Auto Trigger Height", String.valueOf(settings.getAutoTriggerHeight()), "auto_trigger_height");
                break;
            case "enderpearl":
                addConfigSetting(inventory, 19, Material.ARROW, "Velocity", String.valueOf(settings.getEnderpearlVelocity()), "enderpearl_velocity");
                break;
            case "doublejump":
                addConfigSetting(inventory, 19, Material.FEATHER, "Jump Height", String.valueOf(settings.getDoubleJumpHeight()), "doublejump_height");
                break;
        }

        // Control buttons
        Items.create(inventory, Material.GREEN_WOOL, "§a§lSave & Apply", 45);
        Items.create(inventory, Material.YELLOW_WOOL, "§e§lReset to Defaults", 46);
        Items.create(inventory, Material.BLUE_WOOL, "§b§lBack to Main Menu", 53);
        
        // Decorative items
        for (int i : Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,47,48,49,50,51,52)) {
            if (inventory.getItem(i) == null) {
                ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = glass.getItemMeta();
                meta.displayName(Component.text(" "));
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                glass.setItemMeta(meta);
                inventory.setItem(i, glass);
            }
        }
        
        player.openInventory(inventory);
    }
    
    private static void addConfigSetting(Inventory inventory, int slot, Material material, String displayName, String currentValue, String settingKey) {
        System.out.println("[PERK CONFIG GUI] Adding setting to GUI: " + displayName + " = " + currentValue + " (key: " + settingKey + ")");
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName, NamedTextColor.AQUA));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Current: " + currentValue, NamedTextColor.YELLOW));
        lore.add(Component.text(""));
        lore.add(Component.text("Left Click: Set value", NamedTextColor.GREEN));
        lore.add(Component.text("Right Click: Increase by 1", NamedTextColor.BLUE));
        lore.add(Component.text("Shift+Right Click: Decrease by 1", NamedTextColor.RED));
        meta.lore(lore);
        
        // Store the setting key in the item's persistent data container for reliable identification
        meta.getPersistentDataContainer().set(SETTING_KEY, PersistentDataType.STRING, settingKey);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;
        
        // Use modern Paper Adventure API for title detection
        Component titleComponent = event.getView().title();
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(titleComponent);
        System.out.println("[PERK CONFIG GUI] Click detected in inventory: " + title);
        
        // Handle main config GUI
        if (title.contains("Perk Configuration")) {
            event.setCancelled(true);
            System.out.println("[PERK CONFIG GUI] Handling main config click for item: " + event.getCurrentItem().getType());
            handleMainConfigClick(player, event.getCurrentItem());
            return;
        }
        
        // Handle perk-specific config GUIs
        if (title.contains("Configure ")) {
            event.setCancelled(true);
            String perkName = extractPerkName(title);
            System.out.println("[PERK CONFIG GUI] Handling perk config click for: " + perkName + ", item: " + event.getCurrentItem().getType());
            handlePerkConfigClick(player, event.getCurrentItem(), perkName, event.isRightClick(), event.isShiftClick());
            return;
        }
    }
    
    private String extractPerkName(String title) {
        // Extract perk name from titles like "Configure Freezer" or similar
        if (title.contains("Configure ")) {
            String[] parts = title.split("Configure ");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        return "Unknown";
    }
    
    private void handleMainConfigClick(Player player, ItemStack item) {
        Material type = item.getType();
        System.out.println("[PERK CONFIG GUI] Handling click for material: " + type);
        
        switch (type) {
            case EMERALD_BLOCK -> {
                System.out.println("[PERK CONFIG GUI] Reloading configuration");
                PerkConfig.getInstance().reloadConfig();
                player.sendMessage(Component.text("Configuration reloaded!", NamedTextColor.GREEN));
                openMainConfigGUI(player); // Refresh GUI
            }
            case REDSTONE_BLOCK -> {
                System.out.println("[PERK CONFIG GUI] Closing inventory");
                player.closeInventory();
            }
            case PACKED_ICE -> {
                System.out.println("[PERK CONFIG GUI] Opening Freezer config");
                openPerkConfigGUI(player, "Freezer");
            }
            case CARROT_ON_A_STICK -> {
                System.out.println("[PERK CONFIG GUI] Opening Grabber config");
                openPerkConfigGUI(player, "Grabber");
            }
            case TNT -> {
                System.out.println("[PERK CONFIG GUI] Opening Woolbomb config");
                openPerkConfigGUI(player, "Woolbomb");
            }
            case SNOWBALL -> openPerkConfigGUI(player, "Switcher");
            case BLAZE_ROD -> openPerkConfigGUI(player, "Platform");
            case RED_STAINED_GLASS_PANE -> openPerkConfigGUI(player, "Wall");
            case VINE -> openPerkConfigGUI(player, "Rope");
            case TRIPWIRE_HOOK -> openPerkConfigGUI(player, "Booster");
            case FISHING_ROD -> openPerkConfigGUI(player, "Enterhaken");
            case CLOCK -> openPerkConfigGUI(player, "Clock");
            case EMERALD -> openPerkConfigGUI(player, "Rettungsplattform");
            case ENDER_PEARL -> openPerkConfigGUI(player, "EnderPearl");
            case FEATHER -> openPerkConfigGUI(player, "DoubleJump");
            default -> System.out.println("[PERK CONFIG GUI] No handler for material: " + type);
        }
    }
    
    private void handlePerkConfigClick(Player player, ItemStack item, String perkName, boolean isRightClick, boolean isShiftClick) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        Material type = item.getType();
        
        // Handle control buttons
        switch (type) {
            case GREEN_WOOL -> {
                PerkConfig.getInstance().saveConfig();
                // Reload the configs in real-time for EnderPearl and DoubleJump
                EnderPearl.reloadConfig();
                DoubleJump.reloadConfig();
                player.sendMessage(Component.text("Settings saved and applied!", NamedTextColor.GREEN));
                return;
            }
            case YELLOW_WOOL -> {
                player.sendMessage(Component.text("Reset to defaults functionality would go here", NamedTextColor.YELLOW));
                // TODO: Implement reset to defaults
                return;
            }
            case BLUE_WOOL -> {
                openMainConfigGUI(player);
                return;
            }
        }
        
        // Handle setting items
        if (meta.getPersistentDataContainer().has(SETTING_KEY, PersistentDataType.STRING)) {
            String settingKey = meta.getPersistentDataContainer().get(SETTING_KEY, PersistentDataType.STRING);
            if (settingKey != null) {
                handleSettingClick(player, perkName, settingKey, isRightClick, isShiftClick);
            }
        }
    }
    
    
    private void handleSettingClick(Player player, String perkName, String settingKey, boolean isRightClick, boolean isShiftClick) {
        if (!isRightClick) {
            // Left click - request custom input
            startCustomInput(player, perkName, settingKey);
        } else {
            // Right click - increment/decrement
            modifySettingValue(player, perkName, settingKey, isShiftClick ? -1 : 1);
        }
    }
    
    private void startCustomInput(Player player, String perkName, String settingKey) {
        player.closeInventory();
        waitingForInput.put(player, settingKey);
        configContext.put(player, new ConfigContext(perkName, settingKey));
        
        String friendlyName = settingKey.replace("_", " ").replace(" seconds", "").replace(" ticks", "");
        player.sendMessage(Component.text("Enter new value for " + friendlyName + " (type 'cancel' to abort):", NamedTextColor.YELLOW));
    }
    
    private void modifySettingValue(Player player, String perkName, String settingKey, int change) {
        try {
            System.out.println("[PERK CONFIG GUI] === MODIFYING SETTING ===");
            System.out.println("[PERK CONFIG GUI] Player: " + player.getName());
            System.out.println("[PERK CONFIG GUI] Perk: " + perkName + ", Setting: " + settingKey + ", Change: " + change);
            
            PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings(perkName.toLowerCase());
            System.out.println("[PERK CONFIG GUI] Got settings object: " + settings.hashCode());
            
            double currentValue = getCurrentSettingValue(settings, settingKey);
            System.out.println("[PERK CONFIG GUI] Current value: " + currentValue);
            
            double newValue = Math.max(0, currentValue + change);
            
            // For double values, ensure reasonable precision
            if (settingKey.contains("velocity") || settingKey.contains("multiplier")) {
                newValue = Math.round(newValue * 10.0) / 10.0; // Round to 1 decimal
            } else {
                newValue = Math.round(newValue); // Round to integer
            }
            
            System.out.println("[PERK CONFIG GUI] New value: " + newValue);
            
            setSettingValue(settings, settingKey, newValue);
            
            // Verify the value was actually set
            double verifyValue = getCurrentSettingValue(settings, settingKey);
            System.out.println("[PERK CONFIG GUI] Verified value after setting: " + verifyValue);
            
            player.sendMessage(Component.text("Updated " + settingKey + " to " + newValue, NamedTextColor.GREEN));
            
            // Refresh the GUI
            Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openPerkConfigGUI(player, perkName));
            
            System.out.println("[PERK CONFIG GUI] === END MODIFY SETTING ===");
            
        } catch (Exception e) {
            player.sendMessage(Component.text("Error modifying setting: " + e.getMessage(), NamedTextColor.RED));
            e.printStackTrace();
        }
    }
    
    private double getCurrentSettingValue(PerkConfig.PerkSettings settings, String settingKey) {
        return switch (settingKey) {
            case "cooldown" -> settings.getCooldown();
            case "cooldown_recharger" -> settings.getCooldownRecharger();
            case "cost" -> settings.getCost();
            case "projectile_velocity" -> settings.getProjectileVelocity();
            case "freeze_time_seconds" -> settings.getFreezeTimeSeconds();
            case "slowness_strength" -> settings.getSlownessStrength();
            case "grab_timeout_seconds" -> settings.getGrabTimeoutSeconds();
            case "hit_timeout_seconds" -> settings.getHitTimeoutSeconds();
            case "explosion_radius" -> settings.getExplosionRadius();
            case "fuse_ticks" -> settings.getFuseTicks();
            case "knockback_multiplier" -> settings.getKnockbackMultiplier();
            case "knockback_y_velocity" -> settings.getKnockbackYVelocity();
            case "explosion_delay_ticks" -> settings.getExplosionDelayTicks();
            case "speed_duration" -> settings.getSpeedDuration();
            case "speed_strength" -> settings.getSpeedStrength();
            case "jump_duration" -> settings.getJumpDuration();
            case "jump_strength" -> settings.getJumpStrength();
            case "platform_size" -> settings.getPlatformSize();
            case "platform_duration" -> settings.getPlatformDuration();
            case "max_distance" -> settings.getMaxDistance();
            case "wall_height" -> settings.getWallHeight();
            case "wall_width" -> settings.getWallWidth();
            case "wall_duration" -> settings.getWallDuration();
            case "rope_length" -> settings.getRopeLength();
            case "pull_strength" -> settings.getPullStrength();
            case "hook_velocity" -> settings.getHookVelocity();
            case "pull_velocity" -> settings.getPullVelocity();
            case "effect_radius" -> settings.getEffectRadius();
            case "effect_duration" -> settings.getEffectDuration();
            case "auto_trigger_height" -> settings.getAutoTriggerHeight();
            case "enderpearl_velocity" -> settings.getEnderpearlVelocity();
            case "doublejump_height" -> settings.getDoubleJumpHeight();
            default -> 0;
        };
    }
    
    private void setSettingValue(PerkConfig.PerkSettings settings, String settingKey, double value) {
        System.out.println("[PERK CONFIG GUI] Setting " + settingKey + " to " + value);
        switch (settingKey) {
            case "cooldown" -> {
                System.out.println("[PERK CONFIG GUI] Setting cooldown from " + settings.getCooldown() + " to " + (int)value);
                settings.setCooldown((int)value);
            }
            case "cooldown_recharger" -> {
                System.out.println("[PERK CONFIG GUI] Setting cooldown_recharger from " + settings.getCooldownRecharger() + " to " + (int)value);
                settings.setCooldownRecharger((int)value);
            }
            case "cost" -> {
                System.out.println("[PERK CONFIG GUI] Setting cost from " + settings.getCost() + " to " + (int)value);
                settings.setCost((int)value);
            }
            case "projectile_velocity" -> settings.setProjectileVelocity(value);
            case "freeze_time_seconds" -> settings.setFreezeTimeSeconds((int)value);
            case "slowness_strength" -> settings.setSlownessStrength((int)value);
            case "grab_timeout_seconds" -> settings.setGrabTimeoutSeconds((int)value);
            case "hit_timeout_seconds" -> settings.setHitTimeoutSeconds((int)value);
            case "explosion_radius" -> settings.setExplosionRadius((int)value);
            case "fuse_ticks" -> settings.setFuseTicks((int)value);
            case "knockback_multiplier" -> settings.setKnockbackMultiplier(value);
            case "knockback_y_velocity" -> settings.setKnockbackYVelocity(value);
            case "explosion_delay_ticks" -> settings.setExplosionDelayTicks((int)value);
            case "speed_duration" -> settings.setSpeedDuration((int)value);
            case "speed_strength" -> settings.setSpeedStrength((int)value);
            case "jump_duration" -> settings.setJumpDuration((int)value);
            case "jump_strength" -> settings.setJumpStrength((int)value);
            case "platform_size" -> settings.setPlatformSize((int)value);
            case "platform_duration" -> settings.setPlatformDuration((int)value);
            case "max_distance" -> settings.setMaxDistance((int)value);
            case "wall_height" -> settings.setWallHeight((int)value);
            case "wall_width" -> settings.setWallWidth((int)value);
            case "wall_duration" -> settings.setWallDuration((int)value);
            case "rope_length" -> settings.setRopeLength((int)value);
            case "pull_strength" -> settings.setPullStrength(value);
            case "hook_velocity" -> settings.setHookVelocity(value);
            case "pull_velocity" -> settings.setPullVelocity(value);
            case "effect_radius" -> settings.setEffectRadius((int)value);
            case "effect_duration" -> settings.setEffectDuration((int)value);
            case "auto_trigger_height" -> settings.setAutoTriggerHeight((int)value);
            case "enderpearl_velocity" -> settings.setEnderpearlVelocity(value);
            case "doublejump_height" -> settings.setDoubleJumpHeight(value);
        }
        System.out.println("[PERK CONFIG GUI] After setting: cooldown=" + settings.getCooldown() + ", cost=" + settings.getCost());
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
                Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openPerkConfigGUI(player, context.perkName));
                return;
            }
            
            try {
                double value = Double.parseDouble(input);
                
                if (value < 0) {
                    player.sendMessage(Component.text("Value must be positive!", NamedTextColor.RED));
                    Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openPerkConfigGUI(player, context.perkName));
                    return;
                }
                
                PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings(context.perkName.toLowerCase());
                setSettingValue(settings, settingKey, value);
                
                player.sendMessage(Component.text("Updated " + settingKey + " to " + value, NamedTextColor.GREEN));
                Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openPerkConfigGUI(player, context.perkName));
                
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Invalid number format! Please enter a valid number.", NamedTextColor.RED));
                Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> openPerkConfigGUI(player, context.perkName));
            }
        }
    }
}