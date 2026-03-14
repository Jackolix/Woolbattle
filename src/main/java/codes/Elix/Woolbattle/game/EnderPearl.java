// This class was created by Elix on 01.10.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

public class EnderPearl implements Listener {
    private static EnderPearl instance;
    private static HashMap<Player, Integer> ready = new HashMap<>();
    private static HashMap<Player, Boolean> elevatorActive = new HashMap<>();
    private static HashMap<Player, Location> elevatorTarget = new HashMap<>();
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
            int triggerPearls = PerkConfig.getInstance().getPassivePerkSettings("aufzug").getAufzugTriggerPearls();
            int amount = ready.getOrDefault(player, 0) + 1;

            if (!elevatorActive.getOrDefault(player, false)) {
                if (amount >= triggerPearls) {
                    elevatorActive.put(player, true);
                    ready.put(player, 0);
                    player.sendActionBar("§6Aufzug bereit!");
                } else {
                    ready.put(player, amount);
                    player.sendActionBar("§6Aufzug: §e" + amount + "§8/§e" + triggerPearls);
                }
            }

            enderPearl = player.launchProjectile(org.bukkit.entity.EnderPearl.class, velocityVector);
            enderPearl.setMetadata("teleport", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
            if (elevatorActive.getOrDefault(player, false)) {
                enderPearl.setMetadata("elevator_active", new FixedMetadataValue(Woolbattle.getPlugin(), true));
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
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.EnderPearl pearl)) return;
        if (!(pearl.getShooter() instanceof Player player)) return;
        if (!pearl.hasMetadata("elevator_active")) return;
        if (event.getHitBlock() == null) return;
        // Only trigger elevator if pearl hit the bottom face of a block (thrown upward into underside)
        if (event.getHitBlockFace() != BlockFace.DOWN) return;

        int maxScan = PerkConfig.getInstance().getPassivePerkSettings("aufzug").getAufzugMaxScanHeight();
        org.bukkit.block.Block hitBlock = event.getHitBlock();

        // Scan upward to find top of structure (2 consecutive air/passable blocks = room for player)
        org.bukkit.block.Block topBlock = hitBlock;
        for (int i = 1; i <= maxScan; i++) {
            org.bukkit.block.Block above = hitBlock.getRelative(0, i, 0);
            if (above.getType().isAir() || above.isPassable()) {
                org.bukkit.block.Block head = hitBlock.getRelative(0, i + 1, 0);
                if (head.getType().isAir() || head.isPassable()) {
                    topBlock = above;
                    break;
                }
            }
            topBlock = above;
        }

        Location dest = topBlock.getLocation().add(0.5, 0, 0.5);
        dest.setYaw(player.getLocation().getYaw());
        dest.setPitch(player.getLocation().getPitch());
        elevatorTarget.put(player, dest);
    }

    @EventHandler
    public void onEnderPearlTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

        Player player = event.getPlayer();

        // If elevator target computed, override destination
        if (elevatorTarget.containsKey(player)) {
            event.setTo(elevatorTarget.remove(player));
            elevatorActive.put(player, false);
            player.sendActionBar("§aAufzug aktiviert!");
        }

        Bukkit.getScheduler().runTask(Woolbattle.getPlugin(), () -> {
            player.setVelocity(new Vector(0, 0, 0));
        });
    }

    public static void enable() {
        ready.clear();
        elevatorActive.clear();
        elevatorTarget.clear();
        Bukkit.getPluginManager().registerEvents(new EnderPearl(), Woolbattle.getPlugin());
        for (Player player : Bukkit.getOnlinePlayers()) {
            codes.Elix.Woolbattle.game.HelpClasses.Team team = CustomPlayer.getCustomPlayer(player).getTeam();
            if (team == null || Objects.equals(team.getName(), "spectator")) continue;
            if (Objects.equals(Items.perks.get(player).getpassivePerk(), "aufzug")) {
                ready.put(player, 0);
                elevatorActive.put(player, false);
            }
        }
    }

    public static void disable() {
        ready.clear();
        elevatorActive.clear();
        elevatorTarget.clear();
    }
}
