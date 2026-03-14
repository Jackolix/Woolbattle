package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.config.PerkConfig;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class Stomper implements Listener {

    private static final HashMap<Player, Boolean> wasFalling = new HashMap<>();
    private static final HashMap<Player, Double> fallStartY = new HashMap<>();

    private static PerkConfig.PassivePerkSettings getSettings() {
        return PerkConfig.getInstance().getPassivePerkSettings("stomper");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Must have stomper passive perk
        String passivePerk = Items.perks.get(player) != null ? Items.perks.get(player).getpassivePerk() : null;
        if (!Objects.equals(passivePerk, "stomper")) return;

        // Must be in ingame state and not a spectator
        if (IngameState.spectator != null && IngameState.spectator.contains(player)) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        if (to.getY() < from.getY() - 0.05) {
            // Player is descending
            if (!wasFalling.getOrDefault(player, false)) {
                fallStartY.put(player, from.getY());
                wasFalling.put(player, true);
            }
        } else if (wasFalling.getOrDefault(player, false)) {
            // Player stopped descending — check if they landed
            double startY = fallStartY.getOrDefault(player, to.getY());
            double fallDist = startY - to.getY();

            if (fallDist >= getSettings().getStomperMinFallHeight()) {
                triggerShockwave(player, to);
            }

            wasFalling.put(player, false);
            fallStartY.put(player, to.getY());
        }
    }

    private void triggerShockwave(Player player, Location landingLoc) {
        PerkConfig.PassivePerkSettings settings = getSettings();
        double shockwaveRadius = settings.getStomperShockwaveRadius();
        double destroyRadius = settings.getStomperDestroyRadius();
        double knockbackStrength = settings.getStomperKnockbackStrength();
        double knockbackY = settings.getStomperKnockbackY();

        // Visual effect — ring of particles
        for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / 16) {
            double x = Math.cos(angle) * shockwaveRadius;
            double z = Math.sin(angle) * shockwaveRadius;
            landingLoc.getWorld().spawnParticle(Particle.CLOUD,
                    landingLoc.clone().add(x, 0.1, z), 1, 0, 0, 0, 0);
        }
        landingLoc.getWorld().playSound(landingLoc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.6f, 1.2f);

        // Block destruction in inner zone (surface below landing)
        for (double dx = -destroyRadius; dx <= destroyRadius; dx++) {
            for (double dz = -destroyRadius; dz <= destroyRadius; dz++) {
                if (Math.sqrt(dx * dx + dz * dz) > destroyRadius) continue;
                Block block = landingLoc.getWorld().getBlockAt(
                        (int) Math.floor(landingLoc.getX() + dx),
                        (int) Math.floor(landingLoc.getY() - 1),
                        (int) Math.floor(landingLoc.getZ() + dz));
                if (Tag.WOOL.isTagged(block.getType()) && Woolbattle.blocks.contains(block)) {
                    Woolbattle.blocks.remove(block);
                    block.setType(org.bukkit.Material.AIR);
                }
            }
        }

        // Enemy knockback in full shockwave radius
        Collection<Entity> nearby = landingLoc.getWorld().getNearbyEntities(
                landingLoc, shockwaveRadius, shockwaveRadius, shockwaveRadius);

        codes.Elix.Woolbattle.game.HelpClasses.Team stomperTeam =
                CustomPlayer.getCustomPlayer(player).getTeam();

        for (Entity entity : nearby) {
            if (!(entity instanceof Player enemy)) continue;
            if (enemy == player) continue;

            // Skip spectators
            if (IngameState.spectator != null && IngameState.spectator.contains(enemy)) continue;

            // Skip teammates
            codes.Elix.Woolbattle.game.HelpClasses.Team enemyTeam =
                    CustomPlayer.getCustomPlayer(enemy).getTeam();
            if (stomperTeam != null && enemyTeam != null
                    && Objects.equals(stomperTeam.getName(), enemyTeam.getName())) continue;

            // Y-level filter: same level + one block above
            if (Math.abs(enemy.getLocation().getY() - landingLoc.getY()) > 1.5) continue;

            // Apply knockback
            Vector direction = enemy.getLocation().toVector().subtract(landingLoc.toVector());
            direction.setY(0);
            if (direction.lengthSquared() > 0) {
                direction.normalize();
            }
            direction.multiply(knockbackStrength);
            direction.setY(knockbackY);
            enemy.setVelocity(direction);

            // Register hit for kill credit
            CustomPlayer.getCustomPlayer(enemy).addHitted(player);
        }
    }

    public static void enable() {
        wasFalling.clear();
        fallStartY.clear();
    }

    public static void cleanup() {
        wasFalling.clear();
        fallStartY.clear();
    }
}
