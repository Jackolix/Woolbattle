// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.listeners;


import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.ConfigLocationUtil;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerLobbyConnectionListener implements Listener {

    private final Woolbattle plugin;


    public PlayerLobbyConnectionListener(Woolbattle plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) {
            IngameState.addSpectator(event.getPlayer());
            return;
        }
        Player player = event.getPlayer();
        plugin.getPlayers().add(player);
        player.setGameMode(GameMode.SURVIVAL);

        for (Player current : Bukkit.getOnlinePlayers()) {
            current.showPlayer(player);
            player.showPlayer(current);
        }

        LobbyItems.Lobby(player);
        event.setJoinMessage(Woolbattle.PREFIX + "§a" + player.getDisplayName() + " §7ist dem Spiel beigetreten. [" +
                plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");

        /*
        ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Lobby");
        if (locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        } else
            Bukkit.getConsoleSender().sendMessage("§cDie Lobby-Location wurde noch nicht gesetzt!");
         */
        player.teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, 70, 0));

        LobbyCountdown countdown = lobbyState.getCountdown();
        if (plugin.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
            if (!countdown.isRunning()) {
                countdown.stopIdle();
                countdown.start();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().remove(player);
        event.setQuitMessage(Woolbattle.PREFIX + "§c" + player.getDisplayName() + " §7hat das Spiel verlassen. [" +
                plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");

        LobbyCountdown countdown = lobbyState.getCountdown();
        if (plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
            if (countdown.isRunning()) {
                countdown.stop();
                countdown.startIdle();
            }
        }

         if (!LiveSystem.VotedPlayers.containsKey(player))
             return;
         Console.send(ChatColor.RED + "Player is in a team");
         Console.send(ChatColor.GREEN + "Trying to remove " + ChatColor.WHITE + player.getName());

        ArrayList<Player> team = LiveSystem.VotedPlayers.get(player); //get the arrayList (team) where the player was before
        if (team != null)
            team.remove(player); //remove the player from the team
        LiveSystem.VotedPlayers.remove(player);
        IngameState.teamupdate();
        Console.send(ChatColor.GREEN + "Successfully removed " + ChatColor.WHITE + player.getName());
    }


}
