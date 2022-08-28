// This class was created by Elix on 28.08.22


package codes.Elix.Woolbattle.game;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class VoidTeleport implements Listener {

    @EventHandler
    public void onDeath(PlayerMoveEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof IngameState)) return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.getLocation().getBlockY() <= 69) {
            ArrayList<Player> team = LiveSystem.VotedPlayers.get(player); // get the team of the player
            Integer lifes = LiveSystem.TeamLifes.get(team); // get the lifes of the team
            if (lifes == 0) {
                player.setGameMode(GameMode.SPECTATOR);
                return;
            }
            LiveSystem.TeamLifes.put(team, lifes - 1);
            player.teleport(new Location(player.getWorld(), 85,75,-122)); // TODO: richtige Location

            for (Player players : Bukkit.getOnlinePlayers())
                IngameScoreboard.setup(players);
        }
    }
}
