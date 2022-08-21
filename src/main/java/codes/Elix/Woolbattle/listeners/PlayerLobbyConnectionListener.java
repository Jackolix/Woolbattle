// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.listeners;


import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.ConfigLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLobbyConnectionListener implements Listener {

    private Woolbattle plugin;


    public PlayerLobbyConnectionListener(Woolbattle plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().add(player);
        LobbyItems.Lobby(player);
        event.setJoinMessage(Woolbattle.PREFIX + "§a" + player.getDisplayName() + " §7ist dem Spiel beigetreten. [" +
                plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");

        ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "world");
        if (locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        } else
            Bukkit.getConsoleSender().sendMessage("§cDie Lobby-Location wurde noch nicht gesetzt!");

        LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
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
        if (!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().remove(player);
        event.setQuitMessage(Woolbattle.PREFIX + "§c" + player.getDisplayName() + " §7hat das Spiel verlassen. [" +
                plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");

        LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
        LobbyCountdown countdown = lobbyState.getCountdown();
        if (plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
            if (countdown.isRunning()) {
                countdown.stop();
                countdown.startIdle();
            }
        }
    }


}