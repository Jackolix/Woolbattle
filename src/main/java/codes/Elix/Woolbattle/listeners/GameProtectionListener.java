// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.listeners;

import codes.Elix.Woolbattle.commands.Build;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public void onPlayerPickup(EntityPickupItemEvent event) {
        /*
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack item = new ItemStack(Material.BLACK_WOOL);
        if (!event.getItem().getItemStack().equals(item)) return;
        Console.send("Item ist Wolle");
        if (Items.amount((Player) event.getEntity(), Material.BLACK_WOOL) >= 196) // Items.getWoolColor(player) idk ob das funktioniert
            event.setCancelled(true);

         */
        // event.setCancelled(true);
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
        if (event.getDamager() instanceof Player) {
            if (IngameState.spectator.contains((Player) event.getDamager())) {
                event.setCancelled(true);
                return;
            }
        }

        if (!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Arrow) && !(event.getDamager() instanceof EnderPearl) && !(event.getDamager() instanceof TNTPrimed))
            return;

        if (event.getDamager() instanceof EnderPearl) {
            event.setCancelled(true);
            return;
        }

        if (event.getDamager() instanceof TNTPrimed) {
            event.setDamage(0.00000000001D);
            ((Player) event.getEntity()).setHealth(20);
            return;
        }

        Entity causePlayer;
        if ((event.getDamager() instanceof Arrow arrow)) {
            causePlayer = (Entity) arrow.getShooter();
            if (!(causePlayer instanceof Player)) return;
        } else
            causePlayer = event.getDamager();

        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer((Player) causePlayer);
        Team team = customPlayer.getTeam();
        // ArrayList<Player> team = LiveSystem.VotedPlayers.get((Player) causePlayer);

        if (team.getMembers().contains((Player) event.getEntity())) {
            event.setCancelled(true);
            return;
        }

        // LiveSystem.hitted.add((Player) event.getEntity());
        CustomPlayer.getCustomPlayer((Player) event.getEntity()).addHitted((Player) causePlayer);

        event.setDamage(0.00000000001D);
        ((Player) event.getEntity()).setHealth(20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                //LiveSystem.hitted.remove((Player) event.getEntity());
                CustomPlayer.getCustomPlayer((Player) event.getEntity()).removeHitted();
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
    public void onBlockbreak(BlockBreakEvent event) { //TODO: Wolle in der mitte kann nur mit op abgebaut werden warum auch immer wtf
        if (event instanceof Player) return;
        Player player = event.getPlayer();
        if (Build.BuildPlayers.contains(player)) return;
        if (GameStateManager.getCurrentGameState() instanceof LobbyState) {
            event.setCancelled(true);
            return;
        }

        if (IngameState.spectator.contains(player)) {
            event.setCancelled(true);
            return;
        }
        if (Tag.WOOL.isTagged(event.getBlock().getType())) {
            if (!(Woolbattle.blocks.contains(event.getBlock()))) {
                event.setDropItems(false);
                return;
            }
            // System.out.println("Wool amount: " + Items.amount(player, Items.getWoolColor(player)));
            if (Items.amount(player, Items.getWoolColor(player)) >= 192) {
                event.setCancelled(true);
                return;
            } else if (Items.amount(player, Items.getWoolColor(player)) == 191) {
                ItemStack item = new ItemStack(Items.getWoolColor(player));
                item.setAmount(1);
                player.getInventory().addItem(item);
            } else {
                ItemStack item = new ItemStack(Items.getWoolColor(player));
                item.setAmount(2);
                player.getInventory().addItem(item);
            }

            if (Items.amount(player, Material.ARROW) == 0) {
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
        if (IngameState.spectator.contains(player)) event.setCancelled(true);
        if (Tag.WOOL.isTagged(event.getBlock().getType())) return;
        if (Build.BuildPlayers.contains(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.TNT) {
            List<Block> destroyed = event.blockList();
            Iterator<Block> it = destroyed.iterator();
            while (it.hasNext()) {
                Block block = (Block) it.next();
                if (Woolbattle.blocks.contains(block) || !toDestroy.contains(block.getType()))
                    it.remove();
            }
            event.setYield(0);
        }
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        event.message(Component.text(""));
    }

    @EventHandler
    public void onRecipeDiscover(PlayerRecipeDiscoverEvent event) {
        event.setCancelled(true);
    }

    public static final Set<Material> toDestroy = new HashSet<Material>();
    static {
        toDestroy.add(Material.RED_WOOL);
        toDestroy.add(Material.BLUE_WOOL);
        toDestroy.add(Material.GREEN_WOOL);
        toDestroy.add(Material.YELLOW_WOOL);
        toDestroy.add(Material.BLACK_WOOL);
        toDestroy.add(Material.ORANGE_WOOL);
    }
}
