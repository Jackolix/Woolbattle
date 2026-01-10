// This class was created by Elix on 01.10.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

public class EnderPearl implements Listener {
    private static EnderPearl instance;
    private static HashMap<Player, Integer> ready = new HashMap<>();
    int cooldown;
    int cost;
    double velocity;

    public EnderPearl() {
        instance = this;
        loadConfig();
    }

    private void loadConfig() {
        PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings("enderpearl");
        this.cooldown = settings.getCooldown();
        this.cost = settings.getCost();
        this.velocity = settings.getEnderpearlVelocity();
    }

    public static void reloadConfig() {
        if (instance != null) {
            instance.loadConfig();
            System.out.println("[EnderPearl] Config reloaded - cooldown: " + instance.cooldown + ", cost: " + instance.cost + ", velocity: " + instance.velocity);
        }
    }

    @EventHandler
    public void enderpearl(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (event.getItem().getType() != Material.ENDER_PEARL) return;

        // Only handle right-click actions (normal way to throw items)
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_AIR &&
            event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        event.setCancelled(true);

        // Check if enderpearl is on cooldown for this player
        if (Items.isPerkOnCooldown(player, "§3EnderPerle")) {
            return;
        }

        if (!Items.cost(player, cost)) {
            return;
        }
        Vector direction = player.getLocation().getDirection();
        Vector velocityVector = direction.multiply(velocity);
        
        Projectile enderPearl;
        if (Objects.equals(Items.perks.get(player).getpassivePerk(), "aufzug")) {
            int amount = ready.get(player);
            amount++;
            if (amount >= 3) {
                enderPearl = player.launchProjectile(org.bukkit.entity.EnderPearl.class, velocityVector);
                enderPearl.setMetadata("teleport", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
                ready.put(player, 0);
            } else {
                ready.put(player, amount);
                enderPearl = player.launchProjectile(org.bukkit.entity.EnderPearl.class, velocityVector);
                enderPearl.setMetadata("teleport", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
            }
        } else {
            enderPearl = player.launchProjectile(org.bukkit.entity.EnderPearl.class, velocityVector);
            enderPearl.setMetadata("teleport", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
        }

        int appliedCooldown = cooldown;
        if (Objects.equals(PerkHelper.passive(player), "recharger")) {
            PerkConfig.PerkSettings settings = PerkConfig.getInstance().getPerkSettings("enderpearl");
            appliedCooldown = settings.getCooldownRecharger();
        }
        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        if (!Woolbattle.debug)
            Items.visualCooldown(player, appliedCooldown, Material.ENDER_PEARL, slot, "§3EnderPerle");
    }

    @EventHandler
    public void onEnderPearlTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> {
            player.setVelocity(new Vector(0, 0, 0));
        });
    }

    // Prevent players from bugging inside blocks with enderpearl - but commented since it does not happen with newer minecraft versions anymore?
    /*@EventHandler
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


    }*/

    public static void enable() {
        ready.clear();
        Bukkit.getPluginManager().registerEvents(new EnderPearl(), Woolbattle.getPlugin());
        for (Player player : Bukkit.getOnlinePlayers()) {
            codes.Elix.Woolbattle.game.HelpClasses.Team team = CustomPlayer.getCustomPlayer(player).getTeam();
            if (team == null || Objects.equals(team.getName(), "spectator")) continue;
            if (Objects.equals(Items.perks.get(player).getpassivePerk(), "aufzug"))
                ready.put(player, 0);
        }
    }
    public static void disable() {
        ready.clear();
    }
}

