// This class was created by Elix on 01.08.22


package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;


public class booster implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("booster");
    }

    @EventHandler
    public void onBoosterInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.TRIPWIRE_HOOK) return;
        
        Player player = event.getPlayer();
        PerkConfig.PerkSettings settings = getSettings();
        
        if (!Woolbattle.debug)
            if (!Items.cost(player, settings.getCost())) return;

        // Apply velocity boost (original method)
        Vector vector = player.getLocation().getDirection().multiply(2.75D).setY(2D);
        player.setVelocity(vector);
        
        // Apply speed effect if duration > 0
        if (settings.getSpeedDuration() > 0 && settings.getSpeedStrength() > 0) {
            int duration = settings.getSpeedDuration() * 20; // Convert seconds to ticks
            int amplifier = settings.getSpeedStrength() - 1; // Amplifier is 0-based (0 = level I, 1 = level II, etc.)
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier, false, true));
        }
        
        // Apply jump boost effect if duration > 0
        if (settings.getJumpDuration() > 0 && settings.getJumpStrength() > 0) {
            int duration = settings.getJumpDuration() * 20; // Convert seconds to ticks
            int amplifier = settings.getJumpStrength() - 1; // Amplifier is 0-based (0 = level I, 1 = level II, etc.)
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, duration, amplifier, false, true));
        }
            
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 1F, 1F);

        // Cooldown implementation
        int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
            ? settings.getCooldownRecharger()
            : settings.getCooldown();
            
        int slot = player.getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.TRIPWIRE_HOOK, slot, "§3booster");
    }
    public static void enable() { Bukkit.getPluginManager().registerEvents(new booster(), Woolbattle.getPlugin()); }
    public static void disable() {}
}
