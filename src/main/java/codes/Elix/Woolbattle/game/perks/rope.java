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

public class rope implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("rope");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.VINE) return;

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
        Items.visualCooldown(player, cooldown, Material.VINE, slot, "§3Rope");

    }

    private void placeBlocks(Location location, Material material) {
        if (material == null) material = Material.BLACK_WOOL;
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new rope(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
