// This class was created by Elix on 01.10.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Objects;

public class EnderPearl implements Listener {
    private static HashMap<Player, Integer> ready = new HashMap<>();
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
        if (Objects.equals(Items.perks.get(player).getpassivePerk(), "aufzug")) {
            int amount = ready.get(player);
            amount++;
            if (amount >= 3) {
                Projectile enderPearl = player.launchProjectile(org.bukkit.entity.EnderPearl.class);
                enderPearl.setMetadata("teleport", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
                ready.put(player, 0);
            } else {
                ready.put(player, amount);
                player.launchProjectile(org.bukkit.entity.EnderPearl.class);
            }
        }
        // player.launchProjectile(org.bukkit.entity.EnderPearl.class);
        event.setCancelled(true);

        if (Objects.equals(PerkHelper.passive(player), "recharger"))
            cooldown = 2;
        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, cooldown, Material.ENDER_PEARL, slot, "§3EnderPerle");
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if (!(e.getEntity() instanceof org.bukkit.entity.EnderPearl)) return;
        if (!(e.getEntity().getShooter() instanceof Player player)) return;
        if (e.getHitEntity() instanceof Player) return;
        if (!(e.getEntity().hasMetadata("teleport"))) return;
        if (e.getHitBlock() == null) return;
        Location location = e.getHitBlock().getLocation();
        for (int i = 0; i < 10; i++) {
            if (Bukkit.getWorlds().get(0).getBlockAt(location.getBlockX(), location.getBlockY()+i+1, location.getBlockZ()).getType() == Material.AIR) {
                player.teleport(new Location(location.getWorld(), location.getBlockX(), location.getBlockY()+i+1, location.getBlockZ()));
                e.getEntity().remove();
                break;
            }
        }


    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new EnderPearl(), Woolbattle.getPlugin());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Objects.equals(CustomPlayer.getCustomPlayer(player).getTeam().getName(), "spectator")) continue;
            if (Objects.equals(Items.perks.get(player).getpassivePerk(), "aufzug"))
                ready.put(player, 0);
        }
    }
    public static void disable() {}
}

