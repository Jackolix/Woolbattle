package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;


public class platform implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("platform");
    }

    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)            return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.BLAZE_ROD) return;

        if (Woolbattle.debug) {
            placeBlocks(player.getLocation(), Items.getWoolColor(player));
            return;
        }
        PerkConfig.PerkSettings settings = getSettings();
        
        if (!Items.cost(player, settings.getCost())) return;
        
        int cooldown = Objects.equals(PerkHelper.passive(player), "recharger")
            ? settings.getCooldownRecharger()
            : settings.getCooldown();
            
        int slot = player.getInventory().getHeldItemSlot();
        placeBlocks(player.getLocation(), Items.getWoolColor(player));
        Items.visualCooldown(player, cooldown, Material.BLAZE_ROD, slot, "§3Plattform");

    }

    private void placeBlocks(Location location, Material material) {
        PerkConfig.PerkSettings settings = getSettings();
        //move the location down with the platform size setting
        location.add(0, -2, 0);

        if (location.add(2,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
    }
    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new platform(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
