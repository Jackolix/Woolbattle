// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.perks.*;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.LobbyItems;
import codes.Elix.Woolbattle.items.PerkItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class IngameState extends GameState {

    private Woolbattle plugin;


    public IngameState (Woolbattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        checkTeams();
        setLifes();
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.closeInventory();
            current.getInventory().clear();
            Items.standartitems(current);
            IngameScoreboard.setup(current);
            PerkItems.equip(current);
        }
        DoubleJump.enable();
        switcher.enable();
        booster.enable();
        clock.enable();
        platform.enable();
        wall.enable();
        freezer.enable();
    }

    @Override
    public void stop() {
        DoubleJump.disable();
    }

    public void setLifes() {
        if (LobbyItems.VotedLives == 0)
            LobbyItems.VotedLives = 6;
        LiveSystem.TeamLifes.put(LiveSystem.TeamRed, LobbyItems.VotedLives);
        LiveSystem.TeamLifes.put(LiveSystem.TeamYellow, LobbyItems.VotedLives);
        LiveSystem.TeamLifes.put(LiveSystem.TeamBlue, LobbyItems.VotedLives);
        LiveSystem.TeamLifes.put(LiveSystem.TeamGreen, LobbyItems.VotedLives);

    }

    public void checkTeams() {
        Console.send("Checking teams...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (LiveSystem.VotedPlayers.containsKey(player)) return;
            Console.send("Trying to add " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to team");
            addToEmptyTeam(player);
        }
        Console.send(" ");
        Console.send(ChatColor.RED + "Team Red: " + LiveSystem.TeamRed.size());
        Console.send(ChatColor.BLUE + "Team Blue: " + LiveSystem.TeamBlue.size());
        Console.send(ChatColor.GREEN + "Team Green: " + LiveSystem.TeamGreen.size());
        Console.send(ChatColor.YELLOW + "Team Yellow: " + LiveSystem.TeamYellow.size());
    }

    public void addToEmptyTeam(Player player) {
        if (LiveSystem.TeamRed.size() < LiveSystem.TeamSize) {
            LiveSystem.TeamRed.add(player);
            LiveSystem.VotedPlayers.put(player, LiveSystem.TeamRed);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.RED + " Red");
        } else if (LiveSystem.TeamBlue.size() < LiveSystem.TeamSize) {
            LiveSystem.TeamBlue.add(player);
            LiveSystem.VotedPlayers.put(player, LiveSystem.TeamBlue);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.BLUE + " Blue");
        } else if (LiveSystem.TeamGreen.size() < LiveSystem.TeamSize) {
            LiveSystem.TeamGreen.add(player);
            LiveSystem.VotedPlayers.put(player, LiveSystem.TeamGreen);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.GREEN + " Green");
        } else if (LiveSystem.TeamYellow.size() < LiveSystem.TeamSize) {
            LiveSystem.TeamYellow.add(player);
            LiveSystem.VotedPlayers.put(player, LiveSystem.TeamYellow);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.YELLOW + " Yellow");
        } else {
            Console.send(ChatColor.RED + "All Teams are full!");
            player.setGameMode(GameMode.SPECTATOR);
        }

    }

}
