package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.listeners.PlacedWoolCooldown;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class rettungsplattform implements Listener {

    private PerkConfig.PerkSettings getSettings() {
        return PerkConfig.getInstance().getPerkSettings("rettungsplattform");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.RED_STAINED_GLASS) return;

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
        Location placelocation = player.getLocation();
        placeBlocks(placelocation, Items.getWoolColor(player));
        Items.visualCooldown(player, cooldown, Material.RED_STAINED_GLASS, slot, "§3Rettungsplattform");

    }

    private void placeBlocks(Location location, Material material) {
        if (material == null) material = Material.BLACK_WOOL;
        PlacedWoolCooldown cooldown = Woolbattle.getPlacedWoolCooldown();

        // Below player
        Block block = location.clone().add(0,-1,0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        // Around player at feet level
        block = location.clone().add(0,0,1).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        block = location.clone().add(0,0,-1).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        block = location.clone().add(-1,0,0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        block = location.clone().add(1,0,0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        // Around player at head level
        block = location.clone().add(0,1,1).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        block = location.clone().add(0,1,-1).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        block = location.clone().add(-1,1,0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        block = location.clone().add(1,1,0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
        // Top
        block = location.clone().add(0,2,0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material);
            if (cooldown != null) cooldown.trackBlock(block);
        }
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new rettungsplattform(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
