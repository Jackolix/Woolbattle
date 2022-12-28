// This class was created by Elix on 01.10.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EnderPearl implements Listener {
    int cooldown = 3;
    int cost = 3;

    @EventHandler
    public void enderpearl(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() != Material.ENDER_PEARL) return;

        Player player = event.getPlayer();
        if (!Items.cost(player, cost)) {
            event.setCancelled(true);
            return;
        }

        player.launchProjectile(org.bukkit.entity.EnderPearl.class);
        event.setCancelled(true);

        if (Objects.equals(PerkHelper.passive(player), "recharger"))
            cooldown = 2;
        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.ENDER_PEARL, slot, "§3EnderPerle");
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new EnderPearl(), Woolbattle.getPlugin());
    }
    public static void disable() {}
}

