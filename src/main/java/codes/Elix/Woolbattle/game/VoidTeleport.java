// This class was created by Elix on 28.08.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import codes.Elix.Woolbattle.util.IngameTabList;
import codes.Elix.Woolbattle.util.SchematicManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class VoidTeleport implements Listener {

    @EventHandler
    public void onDeath(PlayerMoveEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof IngameState)) return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        
        // Get map-specific void level instead of hardcoded -25
        String selectedMap = SchematicManager.getSelectedMap();
        int voidLevel = SchematicManager.getVoidLevel(selectedMap);
        
        if (player.getLocation().getBlockY() <= voidLevel) {
            CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
            Team team = customPlayer.getTeam();

            // If player has no team, make them spectator
            if (team == null) {
                IngameState.addSpectator(player);
                Location spectatorSpawn = SchematicManager.getSpectatorSpawn(selectedMap);
                player.teleport(spectatorSpawn);
                return;
            }

            Integer lifes = team.getLifes();

            if (lifes == 0) {
                team.setDead(true);
                IngameState.addSpectator(player);
                // Use schematic-based spectator spawn instead of hardcoded location
                Location spectatorSpawn = SchematicManager.getSpectatorSpawn(selectedMap);
                player.teleport(spectatorSpawn);
                if (checkTeams() != null) {
                    winner(checkTeams());
                }
                return;
            }

            // Check if player was hit within the last 10 seconds
            if (customPlayer.isHitValid()) {
                String damager;
                String prefix;
                TextColor damagerColor = TextColor.color(255, 0, 0); // Default red

                team.setLifes(lifes - 1);
                if (customPlayer.getDamager() == null) {
                    damager = "Console";
                    prefix = "§4";
                } else {
                    damager = customPlayer.getDamager().getName();
                    Team damagerTeam = CustomPlayer.getCustomPlayer(customPlayer.getDamager()).getTeam();
                    if (damagerTeam != null) {
                        prefix = damagerTeam.getPREFIX();
                        damagerColor = damagerTeam.getTextColor();
                    } else {
                        prefix = "§7";
                    }
                }


                Component deathMessage = Woolbattle.PREFIX
                        .append(Component.text(customPlayer.getPlayer().getName(), team.getTextColor()))
                        .append(Component.text(" died to ", NamedTextColor.GRAY))
                        .append(Component.text(damager, damagerColor));

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(deathMessage);
                }
                customPlayer.removeHitted();
            }

            // Use schematic-based team spawn instead of hardcoded location
            String teamKey = team.getName().toLowerCase();
            Location teamSpawn = SchematicManager.getTeamSpawn(selectedMap, teamKey);
            player.teleport(teamSpawn);

            for (Player players : Bukkit.getOnlinePlayers())
                IngameScoreboard.setup(players);

            // Update tab lists to reflect life changes
            IngameTabList.updateAll();
        }
    }

    private Team checkTeams() {
        boolean red = LiveSystem.Team.get("red").isDead();
        boolean blue = LiveSystem.Team.get("blue").isDead();
        boolean green = LiveSystem.Team.get("green").isDead();
        boolean yellow = LiveSystem.Team.get("yellow").isDead();

        if (!red && !blue && !green) {
            return LiveSystem.Team.get("yellow");
        }
        if (!red && !blue && !yellow) {
            return LiveSystem.Team.get("green");
        }
        if (!red && !green && !yellow) {
            return LiveSystem.Team.get("blue");
        }
        if (!blue && !green && !yellow) {
            return LiveSystem.Team.get("red");
        }
        return null;
    }

    public static void winner(Team team) {
        Bukkit.getServer().showTitle(new Title() {
            @Override
            public @NotNull Component title() {
                return Component.text(ChatColor.RED + "GAME IS OVER");
            }

            @Override
            public @NotNull Component subtitle() {
                return Component.text(team.getName() + ChatColor.GREEN + " WINS");
            }

            @Override
            public @Nullable Times times() {
                return null;
            }

            @Override
            public <T> @UnknownNullability T part(@NotNull TitlePart<T> part) {
                return null;
            }
        });
        
        // Shutdown server after 10 seconds to let players see the results
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Component shutdownMsg = Woolbattle.PREFIX
                        .append(Component.text("Server shutting down in 5 seconds...", NamedTextColor.RED));
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(shutdownMsg);
                }
            }
        }, 20 * 5); // 5 seconds delay
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().shutdown();
            }
        }, 20 * 10); // 10 seconds delay
    }

}
