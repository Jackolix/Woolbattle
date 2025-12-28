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

        // Apply speed boost effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 
            settings.getSpeedDuration() * 20, settings.getSpeedStrength()));
            
        // Apply jump boost effect  
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST,
            settings.getJumpDuration() * 20, settings.getJumpStrength()));
            
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 1F, 1F);

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
