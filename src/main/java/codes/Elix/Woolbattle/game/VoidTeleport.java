// This class was created by Elix on 28.08.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import net.kyori.adventure.text.Component;
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
import org.bukkit.material.Wool;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;

public class VoidTeleport implements Listener {

    @EventHandler
    public void onDeath(PlayerMoveEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof IngameState)) return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.getLocation().getBlockY() <= -25) {
            CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
            Team team = customPlayer.getTeam();
            Integer lifes = team.getLifes();

            if (lifes == 0) {
                team.setDead(true);
                IngameState.addSpectator(player);
                player.teleport(new Location(player.getWorld(), 0, 56, 0));
                if (checkTeams() != null) {
                    winner(checkTeams());
                }
                return;
            }

            if (customPlayer.isHitted()) {
                String damager;
                String prefix;

                team.setLifes(lifes - 1);
                if (customPlayer.getDamager() == null) {
                    damager = "Console";
                    prefix = "§4";
                } else {
                    damager = customPlayer.getDamager().getName();
                    prefix = CustomPlayer.getCustomPlayer(customPlayer.getDamager()).getTeam().getPREFIX();
                }


                Bukkit.broadcast(Component.text(Woolbattle.PREFIX + customPlayer.getTeam().getPREFIX() + customPlayer.getPlayer().getName() + " §fdied to " + prefix +
                            damager));
                customPlayer.removeHitted();
            }

            player.teleport(new Location(player.getWorld(), 0,56,0));

            for (Player players : Bukkit.getOnlinePlayers())
                IngameScoreboard.setup(players);
        }
    }

    private Team checkTeams() {
        boolean red = LiveSystem.NewTeams.get("red").isDead();
        boolean blue = LiveSystem.NewTeams.get("blue").isDead();
        boolean green = LiveSystem.NewTeams.get("green").isDead();
        boolean yellow = LiveSystem.NewTeams.get("yellow").isDead();

        if (!red && !blue && !green) {
            return LiveSystem.NewTeams.get("yellow");
        }
        if (!red && !blue && !yellow) {
            return LiveSystem.NewTeams.get("green");
        }
        if (!red && !green && !yellow) {
            return LiveSystem.NewTeams.get("blue");
        }
        if (!blue && !green && !yellow) {
            return LiveSystem.NewTeams.get("red");
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
                return Component.text(team.getColor() + ChatColor.GREEN + " WINS");
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
    }

}
