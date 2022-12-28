// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.listeners;


import codes.Elix.Woolbattle.countdowns.LobbyCountdown;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.Perk;
import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.items.Voting;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.LobbyScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayerLobbyConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) {
            IngameState.addSpectator(event.getPlayer());
            return;
        }
        Player player = event.getPlayer();
        Woolbattle.players.add(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();

        for (Player current : Bukkit.getOnlinePlayers()) {
            current.showPlayer(Woolbattle.getPlugin(), player);
            player.showPlayer(Woolbattle.getPlugin(), current);
        }

        LobbyItems.Lobby(player);
        LobbyScoreboard.setup(player);

        // Neuer Weg um Nachrichten zu senden
        TextComponent text = Component.text("Click to Copy Name to Clipboard");
        final @NotNull TextComponent textComponent = Component.text(Woolbattle.PREFIX)
                .append(Component.text(player.getName(), NamedTextColor.GREEN))
                .append(Component.text(" ist dem Spiel beigetreten. [", NamedTextColor.GRAY))
                .append(Component.text(Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS + "]", NamedTextColor.GRAY))
                .hoverEvent(HoverEvent.showText(text))
                .clickEvent(ClickEvent.suggestCommand(player.getName()))
                .clickEvent(ClickEvent.copyToClipboard(player.getName()));
        event.joinMessage(textComponent);

        // Alter Weg
        // event.setJoinMessage(Woolbattle.PREFIX + "§a" + player.getDisplayName() + " §7ist dem Spiel beigetreten. [" +
               //  Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS + "]");

        player.teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, 70, 0));

        LobbyCountdown countdown = lobbyState.getCountdown();
        if (Woolbattle.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
            if (!countdown.isRunning()) {
                countdown.stopIdle();
                countdown.start();
            }
        }
        /*
        // Add player to perk database
        FileConfiguration config = Woolbattle.getPlugin().getConfig();
        String FirstPerk = (String) config.get(player.getName() + ".1Perk");
        String SecondPerk = (String) config.get(player.getName() + ".2Perk");
        String PassivePerk = (String) config.get(player.getName() + ".passive");
        Perk perk = new Perk(player, FirstPerk, SecondPerk, PassivePerk);
        Items.perks.put(player, perk);
        System.out.println(Items.perks);
         */
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;
        Player player = event.getPlayer();
        Woolbattle.getPlayers().remove(player);
        // event.setQuitMessage(Woolbattle.PREFIX + "§c" + player.getDisplayName() + " §7hat das Spiel verlassen. [" +
                // Woolbattle.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");

        TextComponent text = Component.text("Click to Copy Name to Clipboard");
        final @NotNull TextComponent textComponent = Component.text(Woolbattle.PREFIX)
                .append(Component.text(player.getName(), NamedTextColor.GREEN))
                .append(Component.text(" hat das Spiel verlassen. [", NamedTextColor.GRAY))
                .append(Component.text(Woolbattle.players.size() + "/" + LobbyState.MAX_PLAYERS + "]", NamedTextColor.GRAY))
                .hoverEvent(HoverEvent.showText(text))
                .clickEvent(ClickEvent.suggestCommand(player.getName()));
        event.quitMessage(textComponent);

        LobbyCountdown countdown = lobbyState.getCountdown();
        if (Woolbattle.getPlayers().size() < LobbyState.MIN_PLAYERS) {
            if (countdown.isRunning()) {
                countdown.stop();
                countdown.startIdle();
            }
        }
/*
        Bukkit.getScheduler().runTaskLaterAsynchronously(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    Items.perks.remove(player);
            }
        }, 20*5);
 */

        Voting.voted.remove(player);
        Voting.update();

         if (!LiveSystem.VotedPlayers.containsKey(player))
             return;
         Console.send(ChatColor.RED + "Player is in a team");
         Console.send(ChatColor.GREEN + "Trying to remove " + ChatColor.WHITE + player.getName());

        ArrayList<Player> team = LiveSystem.VotedPlayers.get(player); //get the arrayList (team) where the player was before
        if (team != null)
            team.remove(player); //remove the player from the team
        LiveSystem.VotedPlayers.remove(player);
        IngameState.teamUpdate();
        Console.send(ChatColor.GREEN + "Successfully removed " + ChatColor.WHITE + player.getName());
    }


}
