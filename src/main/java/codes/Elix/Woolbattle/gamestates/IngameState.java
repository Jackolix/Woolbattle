// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.EnderPearl;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.perks.*;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.PerkItems;
import codes.Elix.Woolbattle.items.Voting;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.IngameScoreboard;
import codes.Elix.Woolbattle.util.LobbyScoreboard;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IngameState extends GameState {
    public static ArrayList<Player> spectator;


    public IngameState () {
        spectator = new ArrayList<>();
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
        boots();
        EnderPearl.enable();
        DoubleJump.enable();
        switcher.enable();
        booster.enable();
        clock.enable();
        platform.enable();
        rettungsplattform.enable();
        wall.enable();
        freezer.enable();
        woolbomb.enable();
        rope.enable();
        enterhaken.enable();
    }

    @Override
    public void stop() {
        DoubleJump.disable();
    }

    public void setLifes() {
        int lives = Voting.winner();
        if (lives == 0)
            lives = 6;
        LiveSystem.TeamLifes.put("red", lives);
        LiveSystem.TeamLifes.put("blue", lives);
        LiveSystem.TeamLifes.put("green", lives);
        LiveSystem.TeamLifes.put("yellow", lives);

    }

    public void checkTeams() {
        Console.send(ChatColor.GOLD + "---------- Teams ----------");
        Console.send("Checking teams...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (LiveSystem.VotedPlayers.containsKey(player)) continue;
            Console.send("Trying to add " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to team");
            addToEmptyTeam(player);
        }
        Console.send(" ");
        Console.send(ChatColor.RED + "Team Red: " + LiveSystem.TeamRed.size());
        Console.send(ChatColor.BLUE + "Team Blue: " + LiveSystem.TeamBlue.size());
        Console.send(ChatColor.GREEN + "Team Green: " + LiveSystem.TeamGreen.size());
        Console.send(ChatColor.YELLOW + "Team Yellow: " + LiveSystem.TeamYellow.size());

        teamUpdate();
    }

    public static void teamUpdate() {
        LiveSystem.Team.clear();

        for (int i = 0; i < LiveSystem.TeamRed.size(); i++)
            LiveSystem.Team.put(LiveSystem.TeamRed.get(i), "red");

        for (int i = 0; i < LiveSystem.TeamYellow.size(); i++)
            LiveSystem.Team.put(LiveSystem.TeamYellow.get(i), "yellow");

        for (int i = 0; i < LiveSystem.TeamBlue.size(); i++)
            LiveSystem.Team.put(LiveSystem.TeamBlue.get(i), "blue");

        for (int i = 0; i < LiveSystem.TeamGreen.size(); i++)
            LiveSystem.Team.put(LiveSystem.TeamGreen.get(i), "green");

        for (Player current : Bukkit.getOnlinePlayers())
            LobbyScoreboard.change(current);
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
        } else if (LiveSystem.Teams >= 3) {
            if (LiveSystem.TeamGreen.size() < LiveSystem.TeamSize) {
                LiveSystem.TeamGreen.add(player);
                LiveSystem.VotedPlayers.put(player, LiveSystem.TeamGreen);
                Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.GREEN + " Green");
            }
        } else if (LiveSystem.Teams >= 3) {
            if (LiveSystem.TeamYellow.size() < LiveSystem.TeamSize) {
                LiveSystem.TeamYellow.add(player);
                LiveSystem.VotedPlayers.put(player, LiveSystem.TeamYellow);
                Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.YELLOW + " Yellow");
            }
        } else {
            Console.send(ChatColor.RED + "All Teams are full!");
            addSpectator(player);
        }
    }

    public static void addSpectator(Player player) {
        spectator.add(player);
        player.setAllowFlight(true);
        player.getInventory().clear();

        for (Player current : Bukkit.getOnlinePlayers())
            current.hidePlayer(Woolbattle.getPlugin(), player);

        player.teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, 50, 0));
        LiveSystem.Team.put(player, "spectator");
        IngameScoreboard.setup(player);
    }

    public static void boots() {
        for (Player player : LiveSystem.TeamRed)
            player.getInventory().setBoots(Items.boots(Color.RED));

        for (Player player : LiveSystem.TeamBlue)
            player.getInventory().setBoots(Items.boots(Color.BLUE));

        for (Player player : LiveSystem.TeamGreen)
            player.getInventory().setBoots(Items.boots(Color.GREEN));

        for (Player player : LiveSystem.TeamYellow)
            player.getInventory().setBoots(Items.boots(Color.YELLOW));

    }


    public static ArrayList<Player> getSpectator() {
        return spectator;
    }
}
