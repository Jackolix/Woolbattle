// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.listeners;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class GameProtectionListener implements Listener {

    @EventHandler
    public void onFoodlevelchange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() != Material.WOOL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setDamage(0.00000000001D);
        ((Player) event.getEntity()).setHealth(20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event instanceof EntityDamageByEntityEvent) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockbreak(BlockBreakEvent event) {
        if (!(event instanceof Player)) {
            Player player = event.getPlayer();
            if (GameStateManager.getCurrentGameState() instanceof IngameState) {
                if (event.getBlock().getType() == Material.WOOL) {
                    if (Woolbattle.blocks.contains(event.getBlock())) {
                        event.setCancelled(true);
                        ItemStack item = new ItemStack(Material.WOOL);
                        player.getInventory().addItem(item);
                    }
                } else event.setCancelled(true);
            } else
                event.setCancelled(true);

        } else
            event.setCancelled(true);
    }
}
