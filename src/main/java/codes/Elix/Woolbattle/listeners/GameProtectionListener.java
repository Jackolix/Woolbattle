// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.listeners;

import codes.Elix.Woolbattle.commands.Build;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEnderPearl;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GameProtectionListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!Tag.WOOL.isTagged(event.getItemDrop().getItemStack().getType()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerpickup(PlayerPickupItemEvent event) {
        ItemStack item = new ItemStack(Material.BLACK_WOOL);
        if (event.getItem().getItemStack() != item) return;
        Console.send("Item ist wolle");
        if (Items.amount(event.getPlayer(), Material.BLACK_WOOL) >= 196)
            event.setCancelled(true);
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
        if (GameStateManager.getCurrentGameState() instanceof LobbyState) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Arrow) && !(event.getDamager() instanceof CraftEnderPearl))
            return;

        if (event.getDamager() instanceof EnderPearl) {
            event.setCancelled(true);
            return;
        }

        Entity causePlayer;
        if ((event.getDamager() instanceof Arrow arrow)) {
            Console.send("Damage kommt von ARROW");

            causePlayer = (Entity) arrow.getShooter();

            if (!(causePlayer instanceof Player)) return;
            /*
            ArrayList<Player> team1 = LiveSystem.VotedPlayers.get((Player) shooter);
            if (team1.contains((Player) shooter)) {
                event.setCancelled(true);
                return;
            }

             */
        } else
            causePlayer = event.getDamager();


        ArrayList<Player> team = LiveSystem.VotedPlayers.get((Player) causePlayer);

        if (team.contains((Player) event.getEntity())) {
            Console.send("Target is your Teammember");
            event.setCancelled(true);
            return;
        }


        LiveSystem.hitted.add((Player) event.getEntity());
        Console.send(((Player) event.getEntity()).getPlayer().getName() + "hitted.");

        event.setDamage(0.00000000001D);
        ((Player) event.getEntity()).setHealth(20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                LiveSystem.hitted.remove((Player) event.getEntity());
                Console.send("Removed " + ((Player) event.getEntity()).getPlayer().getName() + " from hitted.");
            }
        }, 20 * 10);
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event instanceof EntityDamageByEntityEvent) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockbreak(BlockBreakEvent event) {
        if (event instanceof Player) return;
        Player player = event.getPlayer();

        if (Build.BuildPlayers.contains(player)) return;
        if (GameStateManager.getCurrentGameState() instanceof LobbyState) {
            event.setCancelled(true);
            return;
        }

        if (Tag.WOOL.isTagged(event.getBlock().getType())) {
            if (!(Woolbattle.blocks.contains(event.getBlock()))) {
                event.setDropItems(false);
                return;
            }

            if (Items.amount(player, Material.BLACK_WOOL) >= 192) {
                event.setCancelled(true);
                return;
            } else if (Items.amount(player, Material.BLACK_WOOL) == 191) {
                ItemStack item = new ItemStack(Material.BLACK_WOOL);
                item.setAmount(1);
                player.getInventory().addItem(item);
            } else {
                ItemStack item = new ItemStack(Material.BLACK_WOOL);
                item.setAmount(2);
                player.getInventory().addItem(item);
            }

            if (Items.amount(player, Material.ARROW) == 0) {
                Console.send("Player has wool but no arrow");
                ItemStack arrow = new ItemStack(Material.ARROW);
                player.getInventory().setItem(30, arrow);
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBuild(BlockPlaceEvent event) {
        if (event instanceof Player) return;
        if (GameStateManager.getCurrentGameState() instanceof LobbyState) {
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        if (Tag.WOOL.isTagged(event.getBlock().getType())) return;
        if (Build.BuildPlayers.contains(player)) return;
        event.setCancelled(true);
    }
}
