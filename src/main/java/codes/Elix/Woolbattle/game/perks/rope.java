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

public class rope implements Listener {

    int cooldown = 25;
    int cost = 12;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() != Material.VINE) return;

        if (Woolbattle.debug) {
            placeBlocks(player.getLocation(), Items.getWoolColor(player));
            return;
        }
        if (!Items.cost(player, cost)) return;
        if (Objects.equals(PerkHelper.passive(player), "recharger"))
            cooldown = 20;
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
