package codes.Elix.Woolbattle.config;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerkConfig {
    private static PerkConfig instance;
    private FileConfiguration config;
    private File configFile;
    private Map<String, PerkSettings> perkSettings;
    
    private PerkConfig() {
        this.perkSettings = new HashMap<>();
        loadConfig();
    }
    
    public static PerkConfig getInstance() {
        if (instance == null) {
            instance = new PerkConfig();
        }
        return instance;
    }
    
    private void loadConfig() {
        configFile = new File(Woolbattle.getPlugin().getDataFolder(), "perks.yml");

        if (!configFile.exists()) {
            createDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        loadPerkSettings();
    }

    private void createDefaultConfig() {
        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            
            FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(configFile);
            
            // Freezer perk defaults
            defaultConfig.set("perks.freezer.cooldown", 5);
            defaultConfig.set("perks.freezer.cooldown_recharger", 3);
            defaultConfig.set("perks.freezer.cost", 5);
            defaultConfig.set("perks.freezer.freeze_time_seconds", 3);
            defaultConfig.set("perks.freezer.slowness_strength", 5);
            defaultConfig.set("perks.freezer.projectile_velocity", 1.6);
            
            // Grabber perk defaults
            defaultConfig.set("perks.grabber.cooldown", 10);
            defaultConfig.set("perks.grabber.cooldown_recharger", 8);
            defaultConfig.set("perks.grabber.cost", 5);
            defaultConfig.set("perks.grabber.projectile_velocity", 1.5);
            defaultConfig.set("perks.grabber.grab_timeout_seconds", 4);
            defaultConfig.set("perks.grabber.hit_timeout_seconds", 8);
            
            // Woolbomb perk defaults
            defaultConfig.set("perks.woolbomb.cooldown", 13);
            defaultConfig.set("perks.woolbomb.cooldown_recharger", 6);
            defaultConfig.set("perks.woolbomb.cost", 8);
            defaultConfig.set("perks.woolbomb.projectile_velocity", 1.6);
            defaultConfig.set("perks.woolbomb.explosion_radius", 5);
            defaultConfig.set("perks.woolbomb.fuse_ticks", 10);
            defaultConfig.set("perks.woolbomb.knockback_multiplier", 1.0);
            defaultConfig.set("perks.woolbomb.knockback_y_velocity", 0.5);
            defaultConfig.set("perks.woolbomb.explosion_delay_ticks", 10);
            
            // Switcher perk defaults
            defaultConfig.set("perks.switcher.cooldown", 8);
            defaultConfig.set("perks.switcher.cooldown_recharger", 5);
            defaultConfig.set("perks.switcher.cost", 4);
            defaultConfig.set("perks.switcher.projectile_velocity", 1.5);
            
            // Platform perk defaults
            defaultConfig.set("perks.platform.cooldown", 6);
            defaultConfig.set("perks.platform.cooldown_recharger", 4);
            defaultConfig.set("perks.platform.cost", 3);
            defaultConfig.set("perks.platform.platform_size", 3);
            defaultConfig.set("perks.platform.platform_duration", 5);
            defaultConfig.set("perks.platform.max_distance", 20);
            
            // Wall perk defaults
            defaultConfig.set("perks.wall.cooldown", 8);
            defaultConfig.set("perks.wall.cooldown_recharger", 6);
            defaultConfig.set("perks.wall.cost", 5);
            defaultConfig.set("perks.wall.wall_height", 3);
            defaultConfig.set("perks.wall.wall_width", 5);
            defaultConfig.set("perks.wall.wall_duration", 8);
            defaultConfig.set("perks.wall.max_distance", 15);
            
            // Rope perk defaults
            defaultConfig.set("perks.rope.cooldown", 7);
            defaultConfig.set("perks.rope.cooldown_recharger", 5);
            defaultConfig.set("perks.rope.cost", 3);
            defaultConfig.set("perks.rope.rope_length", 20);
            defaultConfig.set("perks.rope.pull_strength", 1.2);
            
            // Booster perk defaults
            defaultConfig.set("perks.booster.cooldown", 12);
            defaultConfig.set("perks.booster.cooldown_recharger", 8);
            defaultConfig.set("perks.booster.cost", 4);
            defaultConfig.set("perks.booster.speed_duration", 5);
            defaultConfig.set("perks.booster.speed_strength", 2);
            defaultConfig.set("perks.booster.jump_duration", 5);
            defaultConfig.set("perks.booster.jump_strength", 2);
            
            // Enterhaken perk defaults
            defaultConfig.set("perks.enterhaken.cooldown", 6);
            defaultConfig.set("perks.enterhaken.cooldown_recharger", 4);
            defaultConfig.set("perks.enterhaken.cost", 2);
            defaultConfig.set("perks.enterhaken.hook_velocity", 2.0);
            defaultConfig.set("perks.enterhaken.pull_velocity", 1.5);
            defaultConfig.set("perks.enterhaken.max_distance", 25);
            
            // Clock perk defaults
            defaultConfig.set("perks.clock.cooldown", 15);
            defaultConfig.set("perks.clock.cooldown_recharger", 10);
            defaultConfig.set("perks.clock.cost", 6);
            defaultConfig.set("perks.clock.effect_radius", 8);
            defaultConfig.set("perks.clock.effect_duration", 4);
            defaultConfig.set("perks.clock.slowness_strength", 3);
            
            // Rettungsplattform perk defaults
            defaultConfig.set("perks.rettungsplattform.cooldown", 20);
            defaultConfig.set("perks.rettungsplattform.cooldown_recharger", 15);
            defaultConfig.set("perks.rettungsplattform.cost", 8);
            defaultConfig.set("perks.rettungsplattform.platform_size", 5);
            defaultConfig.set("perks.rettungsplattform.platform_duration", 10);
            defaultConfig.set("perks.rettungsplattform.auto_trigger_height", 5);

            // EnderPearl defaults
            defaultConfig.set("perks.enderpearl.cooldown", 3);
            defaultConfig.set("perks.enderpearl.cooldown_recharger", 2);
            defaultConfig.set("perks.enderpearl.cost", 3);
            defaultConfig.set("perks.enderpearl.velocity", 1.8);

            // DoubleJump defaults
            defaultConfig.set("perks.doublejump.cooldown", 2);
            defaultConfig.set("perks.doublejump.cooldown_recharger", 1);
            defaultConfig.set("perks.doublejump.cost", 5);
            defaultConfig.set("perks.doublejump.height", 1.5);
            defaultConfig.set("perks.doublejump.rocket_jump_height", 2.0);

            defaultConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadPerkSettings() {
        ConfigurationSection perksSection = config.getConfigurationSection("perks");
        if (perksSection == null) return;
        
        for (String perkName : perksSection.getKeys(false)) {
            ConfigurationSection perkSection = perksSection.getConfigurationSection(perkName);
            if (perkSection == null) continue;
            
            PerkSettings settings = new PerkSettings(
                perkSection.getInt("cooldown", 5),
                perkSection.getInt("cooldown_recharger", 3),
                perkSection.getInt("cost", 5),
                perkSection.getDouble("projectile_velocity", 1.5)
            );
            
            // Load perk-specific settings
            switch (perkName.toLowerCase()) {
                case "freezer":
                    settings.setFreezeTimeSeconds(perkSection.getInt("freeze_time_seconds", 3));
                    settings.setSlownessStrength(perkSection.getInt("slowness_strength", 5));
                    break;
                case "grabber":
                    settings.setGrabTimeoutSeconds(perkSection.getInt("grab_timeout_seconds", 4));
                    settings.setHitTimeoutSeconds(perkSection.getInt("hit_timeout_seconds", 8));
                    break;
                case "woolbomb":
                    settings.setExplosionRadius(perkSection.getInt("explosion_radius", 5));
                    settings.setFuseTicks(perkSection.getInt("fuse_ticks", 10));
                    settings.setKnockbackMultiplier(perkSection.getDouble("knockback_multiplier", 1.0));
                    settings.setKnockbackYVelocity(perkSection.getDouble("knockback_y_velocity", 0.5));
                    settings.setExplosionDelayTicks(perkSection.getInt("explosion_delay_ticks", 10));
                    break;
                case "booster":
                    settings.setSpeedDuration(perkSection.getInt("speed_duration", 5));
                    settings.setSpeedStrength(perkSection.getInt("speed_strength", 2));
                    settings.setJumpDuration(perkSection.getInt("jump_duration", 5));
                    settings.setJumpStrength(perkSection.getInt("jump_strength", 2));
                    break;
                case "platform":
                    settings.setPlatformSize(perkSection.getInt("platform_size", 3));
                    settings.setPlatformDuration(perkSection.getInt("platform_duration", 5));
                    settings.setMaxDistance(perkSection.getInt("max_distance", 20));
                    break;
                case "wall":
                    settings.setWallHeight(perkSection.getInt("wall_height", 3));
                    settings.setWallWidth(perkSection.getInt("wall_width", 5));
                    settings.setWallDuration(perkSection.getInt("wall_duration", 8));
                    settings.setMaxDistance(perkSection.getInt("max_distance", 15));
                    break;
                case "rope":
                    settings.setRopeLength(perkSection.getInt("rope_length", 20));
                    settings.setPullStrength(perkSection.getDouble("pull_strength", 1.2));
                    break;
                case "enterhaken":
                    settings.setHookVelocity(perkSection.getDouble("hook_velocity", 2.0));
                    settings.setPullVelocity(perkSection.getDouble("pull_velocity", 1.5));
                    settings.setMaxDistance(perkSection.getInt("max_distance", 25));
                    break;
                case "clock":
                    settings.setEffectRadius(perkSection.getInt("effect_radius", 8));
                    settings.setEffectDuration(perkSection.getInt("effect_duration", 4));
                    settings.setSlownessStrengthClock(perkSection.getInt("slowness_strength", 3));
                    break;
                case "rettungsplattform":
                    settings.setPlatformSizeEmergency(perkSection.getInt("platform_size", 5));
                    settings.setPlatformDurationEmergency(perkSection.getInt("platform_duration", 10));
                    settings.setAutoTriggerHeight(perkSection.getInt("auto_trigger_height", 5));
                    break;
                case "enderpearl":
                    settings.setEnderpearlVelocity(perkSection.getDouble("velocity", 1.8));
                    break;
                case "doublejump":
                    settings.setDoubleJumpHeight(perkSection.getDouble("height", 1.5));
                    settings.setRocketJumpHeight(perkSection.getDouble("rocket_jump_height", 2.0));
                    break;
            }

            perkSettings.put(perkName.toLowerCase(), settings);
        }
    }
    
    public PerkSettings getPerkSettings(String perkName) {
        return perkSettings.getOrDefault(perkName.toLowerCase(), getDefaultSettings());
    }
    
    private PerkSettings getDefaultSettings() {
        return new PerkSettings(5, 3, 5, 1.5);
    }
    
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        perkSettings.clear();
        loadPerkSettings();
        System.out.println("[PERK CONFIG] Configuration reloaded, loaded " + perkSettings.size() + " perk settings");
    }
    
    public void saveConfig() {
        try {
            // Update the config with current perk settings
            for (Map.Entry<String, PerkSettings> entry : perkSettings.entrySet()) {
                String perkName = entry.getKey();
                PerkSettings settings = entry.getValue();
                
                System.out.println("[PERK CONFIG] Saving " + perkName + " - cooldown: " + settings.getCooldown() + ", cost: " + settings.getCost());
                config.set("perks." + perkName + ".cooldown", settings.getCooldown());
                config.set("perks." + perkName + ".cooldown_recharger", settings.getCooldownRecharger());
                config.set("perks." + perkName + ".cost", settings.getCost());
                config.set("perks." + perkName + ".projectile_velocity", settings.getProjectileVelocity());
                
                // Save perk-specific settings
                switch (perkName.toLowerCase()) {
                    case "freezer":
                        config.set("perks." + perkName + ".freeze_time_seconds", settings.getFreezeTimeSeconds());
                        config.set("perks." + perkName + ".slowness_strength", settings.getSlownessStrength());
                        break;
                    case "grabber":
                        config.set("perks." + perkName + ".grab_timeout_seconds", settings.getGrabTimeoutSeconds());
                        config.set("perks." + perkName + ".hit_timeout_seconds", settings.getHitTimeoutSeconds());
                        break;
                    case "woolbomb":
                        config.set("perks." + perkName + ".explosion_radius", settings.getExplosionRadius());
                        config.set("perks." + perkName + ".fuse_ticks", settings.getFuseTicks());
                        config.set("perks." + perkName + ".knockback_multiplier", settings.getKnockbackMultiplier());
                        config.set("perks." + perkName + ".knockback_y_velocity", settings.getKnockbackYVelocity());
                        config.set("perks." + perkName + ".explosion_delay_ticks", settings.getExplosionDelayTicks());
                        break;
                    case "booster":
                        config.set("perks." + perkName + ".speed_duration", settings.getSpeedDuration());
                        config.set("perks." + perkName + ".speed_strength", settings.getSpeedStrength());
                        config.set("perks." + perkName + ".jump_duration", settings.getJumpDuration());
                        config.set("perks." + perkName + ".jump_strength", settings.getJumpStrength());
                        break;
                    case "platform":
                        config.set("perks." + perkName + ".platform_size", settings.getPlatformSize());
                        config.set("perks." + perkName + ".platform_duration", settings.getPlatformDuration());
                        config.set("perks." + perkName + ".max_distance", settings.getMaxDistance());
                        break;
                    case "wall":
                        config.set("perks." + perkName + ".wall_height", settings.getWallHeight());
                        config.set("perks." + perkName + ".wall_width", settings.getWallWidth());
                        config.set("perks." + perkName + ".wall_duration", settings.getWallDuration());
                        config.set("perks." + perkName + ".max_distance", settings.getMaxDistance());
                        break;
                    case "rope":
                        config.set("perks." + perkName + ".rope_length", settings.getRopeLength());
                        config.set("perks." + perkName + ".pull_strength", settings.getPullStrength());
                        break;
                    case "enterhaken":
                        config.set("perks." + perkName + ".hook_velocity", settings.getHookVelocity());
                        config.set("perks." + perkName + ".pull_velocity", settings.getPullVelocity());
                        config.set("perks." + perkName + ".max_distance", settings.getMaxDistance());
                        break;
                    case "clock":
                        config.set("perks." + perkName + ".effect_radius", settings.getEffectRadius());
                        config.set("perks." + perkName + ".effect_duration", settings.getEffectDuration());
                        config.set("perks." + perkName + ".slowness_strength", settings.getSlownessStrengthClock());
                        break;
                    case "rettungsplattform":
                        config.set("perks." + perkName + ".platform_size", settings.getPlatformSizeEmergency());
                        config.set("perks." + perkName + ".platform_duration", settings.getPlatformDurationEmergency());
                        config.set("perks." + perkName + ".auto_trigger_height", settings.getAutoTriggerHeight());
                        break;
                    case "enderpearl":
                        config.set("perks." + perkName + ".velocity", settings.getEnderpearlVelocity());
                        break;
                    case "doublejump":
                        config.set("perks." + perkName + ".height", settings.getDoubleJumpHeight());
                        config.set("perks." + perkName + ".rocket_jump_height", settings.getRocketJumpHeight());
                        break;
                }
            }

            config.save(configFile);
            System.out.println("[PERK CONFIG] Configuration saved to " + configFile.getName());
            
            // Debug: Re-read the file to verify it was written correctly
            FileConfiguration verifyConfig = YamlConfiguration.loadConfiguration(configFile);
            System.out.println("[PERK CONFIG] Verification - checking saved values in file:");
            for (String perkName : perkSettings.keySet()) {
                int savedCooldown = verifyConfig.getInt("perks." + perkName + ".cooldown", -1);
                int savedCost = verifyConfig.getInt("perks." + perkName + ".cost", -1);
                System.out.println("[PERK CONFIG] File contains - " + perkName + ": cooldown=" + savedCooldown + ", cost=" + savedCost);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class PerkSettings {
        private int cooldown;
        private int cooldownRecharger;
        private int cost;
        private double projectileVelocity;
        
        // Freezer specific
        private int freezeTimeSeconds = 3;
        private int slownessStrength = 5;
        
        // Grabber specific
        private int grabTimeoutSeconds = 4;
        private int hitTimeoutSeconds = 8;
        
        // Woolbomb specific
        private int explosionRadius = 5;
        private int fuseTicks = 10;
        private double knockbackMultiplier = 1.0;
        private double knockbackYVelocity = 0.5;
        private int explosionDelayTicks = 10;
        
        // Booster specific
        private int speedDuration = 5;
        private int speedStrength = 2;
        private int jumpDuration = 5;
        private int jumpStrength = 2;
        
        // Platform specific  
        private int platformSize = 3;
        private int platformDuration = 5;
        private int maxDistance = 20;
        
        // Wall specific
        private int wallHeight = 3;
        private int wallWidth = 5;
        private int wallDuration = 8;
        
        // Rope specific
        private int ropeLength = 20;
        private double pullStrength = 1.2;
        
        // Enterhaken specific
        private double hookVelocity = 2.0;
        private double pullVelocity = 1.5;
        
        // Clock specific
        private int effectRadius = 8;
        private int effectDuration = 4;
        private int slownessStrengthClock = 3;
        
        // Rettungsplattform specific
        private int platformSizeEmergency = 5;
        private int platformDurationEmergency = 10;
        private int autoTriggerHeight = 5;

        // EnderPearl specific
        private double enderpearlVelocity = 1.8;

        // DoubleJump specific
        private double doubleJumpHeight = 1.5;
        private double rocketJumpHeight = 2.0;

        public PerkSettings(int cooldown, int cooldownRecharger, int cost, double projectileVelocity) {
            this.cooldown = cooldown;
            this.cooldownRecharger = cooldownRecharger;
            this.cost = cost;
            this.projectileVelocity = projectileVelocity;
        }
        
        // Getters
        public int getCooldown() { return cooldown; }
        public int getCooldownRecharger() { return cooldownRecharger; }
        public int getCost() { return cost; }
        public double getProjectileVelocity() { return projectileVelocity; }
        public int getFreezeTimeSeconds() { return freezeTimeSeconds; }
        public int getSlownessStrength() { return slownessStrength; }
        public int getGrabTimeoutSeconds() { return grabTimeoutSeconds; }
        public int getHitTimeoutSeconds() { return hitTimeoutSeconds; }
        public int getExplosionRadius() { return explosionRadius; }
        public int getFuseTicks() { return fuseTicks; }
        public double getKnockbackMultiplier() { return knockbackMultiplier; }
        public double getKnockbackYVelocity() { return knockbackYVelocity; }
        public int getExplosionDelayTicks() { return explosionDelayTicks; }
        public int getSpeedDuration() { return speedDuration; }
        public int getSpeedStrength() { return speedStrength; }
        public int getJumpDuration() { return jumpDuration; }
        public int getJumpStrength() { return jumpStrength; }
        public int getPlatformSize() { return platformSize; }
        public int getPlatformDuration() { return platformDuration; }
        public int getMaxDistance() { return maxDistance; }
        public int getWallHeight() { return wallHeight; }
        public int getWallWidth() { return wallWidth; }
        public int getWallDuration() { return wallDuration; }
        public int getRopeLength() { return ropeLength; }
        public double getPullStrength() { return pullStrength; }
        public double getHookVelocity() { return hookVelocity; }
        public double getPullVelocity() { return pullVelocity; }
        public int getEffectRadius() { return effectRadius; }
        public int getEffectDuration() { return effectDuration; }
        public int getSlownessStrengthClock() { return slownessStrengthClock; }
        public int getPlatformSizeEmergency() { return platformSizeEmergency; }
        public int getPlatformDurationEmergency() { return platformDurationEmergency; }
        public int getAutoTriggerHeight() { return autoTriggerHeight; }
        public double getEnderpearlVelocity() { return enderpearlVelocity; }
        public double getDoubleJumpHeight() { return doubleJumpHeight; }
        public double getRocketJumpHeight() { return rocketJumpHeight; }

        // Setters
        public void setCooldown(int cooldown) { this.cooldown = cooldown; }
        public void setCooldownRecharger(int cooldownRecharger) { this.cooldownRecharger = cooldownRecharger; }
        public void setCost(int cost) { this.cost = cost; }
        public void setProjectileVelocity(double projectileVelocity) { this.projectileVelocity = projectileVelocity; }
        public void setFreezeTimeSeconds(int freezeTimeSeconds) { this.freezeTimeSeconds = freezeTimeSeconds; }
        public void setSlownessStrength(int slownessStrength) { this.slownessStrength = slownessStrength; }
        public void setGrabTimeoutSeconds(int grabTimeoutSeconds) { this.grabTimeoutSeconds = grabTimeoutSeconds; }
        public void setHitTimeoutSeconds(int hitTimeoutSeconds) { this.hitTimeoutSeconds = hitTimeoutSeconds; }
        public void setExplosionRadius(int explosionRadius) { this.explosionRadius = explosionRadius; }
        public void setFuseTicks(int fuseTicks) { this.fuseTicks = fuseTicks; }
        public void setKnockbackMultiplier(double knockbackMultiplier) { this.knockbackMultiplier = knockbackMultiplier; }
        public void setKnockbackYVelocity(double knockbackYVelocity) { this.knockbackYVelocity = knockbackYVelocity; }
        public void setExplosionDelayTicks(int explosionDelayTicks) { this.explosionDelayTicks = explosionDelayTicks; }
        public void setSpeedDuration(int speedDuration) { this.speedDuration = speedDuration; }
        public void setSpeedStrength(int speedStrength) { this.speedStrength = speedStrength; }
        public void setJumpDuration(int jumpDuration) { this.jumpDuration = jumpDuration; }
        public void setJumpStrength(int jumpStrength) { this.jumpStrength = jumpStrength; }
        public void setPlatformSize(int platformSize) { this.platformSize = platformSize; }
        public void setPlatformDuration(int platformDuration) { this.platformDuration = platformDuration; }
        public void setMaxDistance(int maxDistance) { this.maxDistance = maxDistance; }
        public void setWallHeight(int wallHeight) { this.wallHeight = wallHeight; }
        public void setWallWidth(int wallWidth) { this.wallWidth = wallWidth; }
        public void setWallDuration(int wallDuration) { this.wallDuration = wallDuration; }
        public void setRopeLength(int ropeLength) { this.ropeLength = ropeLength; }
        public void setPullStrength(double pullStrength) { this.pullStrength = pullStrength; }
        public void setHookVelocity(double hookVelocity) { this.hookVelocity = hookVelocity; }
        public void setPullVelocity(double pullVelocity) { this.pullVelocity = pullVelocity; }
        public void setEffectRadius(int effectRadius) { this.effectRadius = effectRadius; }
        public void setEffectDuration(int effectDuration) { this.effectDuration = effectDuration; }
        public void setSlownessStrengthClock(int slownessStrengthClock) { this.slownessStrengthClock = slownessStrengthClock; }
        public void setPlatformSizeEmergency(int platformSizeEmergency) { this.platformSizeEmergency = platformSizeEmergency; }
        public void setPlatformDurationEmergency(int platformDurationEmergency) { this.platformDurationEmergency = platformDurationEmergency; }
        public void setAutoTriggerHeight(int autoTriggerHeight) { this.autoTriggerHeight = autoTriggerHeight; }
        public void setEnderpearlVelocity(double enderpearlVelocity) { this.enderpearlVelocity = enderpearlVelocity; }
        public void setDoubleJumpHeight(double doubleJumpHeight) { this.doubleJumpHeight = doubleJumpHeight; }
        public void setRocketJumpHeight(double rocketJumpHeight) { this.rocketJumpHeight = rocketJumpHeight; }
    }
}