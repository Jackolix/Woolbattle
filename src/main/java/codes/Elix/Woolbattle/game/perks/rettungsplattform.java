package codes.Elix.Woolbattle.game.perks;

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

public class rettungsplattform implements Listener {

    int cooldown = 15;
    int cost = 5;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.RED_STAINED_GLASS) return;

        if (Woolbattle.debug) {
            placeBlocks(player.getLocation(), Items.getWoolColor(player));
            return;
        }

        if (!Items.cost(player, cost)) return;
        if (Objects.equals(PerkHelper.passive(player), "recharger"))
            cooldown = 13;
        int slot = player.getInventory().getHeldItemSlot();
        placeBlocks(player.getLocation(), Items.getWoolColor(player));
        player.teleport(player.getLocation());
        Items.visualCooldown(player, cooldown, Material.RED_STAINED_GLASS, slot, "§3Rettungsplattform");

    }

    private void placeBlocks(Location location, Material material) {
        if (material == null) material = Material.BLACK_WOOL;
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,1,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,0,-2).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(2,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(0,-1,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material);
        if (location.add(-1,2,0).getBlock().getType() == Material.AIR) location.getBlock().setType(material); // Top wool
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new rettungsplattform(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}
