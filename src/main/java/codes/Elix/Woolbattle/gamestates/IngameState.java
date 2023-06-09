// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.game.DoubleJump;
import codes.Elix.Woolbattle.game.EnderPearl;
import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.HelpClasses.Team;
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
import java.util.List;

public class IngameState extends GameState {
    public static ArrayList<Player> spectator;


    public IngameState () {
        spectator = new ArrayList<>();
    }

    @Override
    public void start() {
        checkTeams();
        setLifes();
        IngameStatus();
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

        for (Player player : Items.perks.keySet())
            if (!player.isOnline())
                Items.perks.remove(player);
    }

    @Override
    public void stop() {
        DoubleJump.disable();
    }

    public void setLifes() {
        int lives = Voting.winner();
        if (lives == 0)
            lives = 6;
        LiveSystem.NewTeams.get("red").setLifes(lives);
        LiveSystem.NewTeams.get("blue").setLifes(lives);
        LiveSystem.NewTeams.get("green").setLifes(lives);
        LiveSystem.NewTeams.get("yellow").setLifes(lives);

        /*
        LiveSystem.TeamLifes.put("red", lives);
        LiveSystem.TeamLifes.put("blue", lives);
        LiveSystem.TeamLifes.put("green", lives);
        LiveSystem.TeamLifes.put("yellow", lives);
         */
    }

    public void checkTeams() {
        Console.send(ChatColor.GOLD + "---------- Teams ----------");
        Console.send("Checking teams...");
        /*
        Team red = new Team(new ArrayList<>(), "red", 0, false);
        Team blue = new Team(new ArrayList<>(), "blue", 0, false);
        Team green = new Team(new ArrayList<>(), "green", 0, false);
        Team yellow = new Team(new ArrayList<>(), "yellow", 0, false);
        LiveSystem.NewTeams.put("red", red);
        LiveSystem.NewTeams.put("blue", blue);
        LiveSystem.NewTeams.put("green", green);
        LiveSystem.NewTeams.put("yellow", yellow);

         */

        for (Player player : Bukkit.getOnlinePlayers()) {
            // if (LiveSystem.VotedPlayers.containsKey(player)) continue;
            if (LiveSystem.newVotedPlayers.contains(CustomPlayer.getCustomPlayer(player))) continue;
            Console.send("Trying to add " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to team");
            addToEmptyTeam(player);
        }
        Console.send(" ");
        Console.send(ChatColor.RED + "Team Red: " + LiveSystem.NewTeams.get("red").getMembers().size());
        Console.send(ChatColor.BLUE + "Team Blue: " + LiveSystem.NewTeams.get("blue").getMembers().size());
        Console.send(ChatColor.GREEN + "Team Green: " + LiveSystem.NewTeams.get("green").getMembers().size());
        Console.send(ChatColor.YELLOW + "Team Yellow: " + LiveSystem.NewTeams.get("yellow").getMembers().size());

        // teamUpdate();
    }

    /*
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
     */

    public void addToEmptyTeam(Player player) {
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        Team red = LiveSystem.NewTeams.get("red");
        Team blue = LiveSystem.NewTeams.get("blue");
        Team green = LiveSystem.NewTeams.get("green");
        Team yellow = LiveSystem.NewTeams.get("yellow");


        if (red.getMembers().size() < LiveSystem.TeamSize) {
            red.addMember(player);
            customPlayer.setTeam(red);
            customPlayer.setTeamName("red");
            LiveSystem.newVotedPlayers.add(customPlayer);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.RED + " Red");
        } else if (blue.getMembers().size() < LiveSystem.TeamSize) {
            blue.addMember(player);
            customPlayer.setTeam(blue);
            customPlayer.setTeamName("blue");
            LiveSystem.newVotedPlayers.add(customPlayer);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.BLUE + " Blue");
        } else if (LiveSystem.Teams >= 3) {
            if (green.getMembers().size() < LiveSystem.TeamSize) {
                green.addMember(player);
                customPlayer.setTeam(green);
                customPlayer.setTeamName("green");
                LiveSystem.newVotedPlayers.add(customPlayer);
                Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.GREEN + " Green");
            }
        } else if (LiveSystem.Teams >= 3) {
            if (yellow.getMembers().size() < LiveSystem.TeamSize) {
                yellow.addMember(player);
                customPlayer.setTeam(yellow);
                customPlayer.setTeamName("yellow");
                LiveSystem.newVotedPlayers.add(customPlayer);
                Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.YELLOW + " Yellow");
            }
        } else {
            Console.send(ChatColor.RED + "All Teams are full!");
            addSpectator(player);
        }

        if (Woolbattle.debug) {
            Console.send(" ");
            Console.send(ChatColor.RED + "Team Red: " + ChatColor.RESET + red);
            Console.send(ChatColor.BLUE + "Team Blue: " + ChatColor.RESET + blue);
            Console.send(ChatColor.GREEN + "Team Green: " + ChatColor.RESET + green);
            Console.send(ChatColor.YELLOW + "Team Yellow: " + ChatColor.RESET + yellow);

            Console.send(" ");
            Console.send("CustomPlayer list");
            for (CustomPlayer players : LiveSystem.newVotedPlayers) {
                Console.send(ChatColor.AQUA + "Player " + ChatColor.RESET + players.getPlayer().getName());
                Console.send(ChatColor.AQUA + "TeamName " + ChatColor.RESET + players.getTeamName());
                Console.send(ChatColor.AQUA + "Team " + ChatColor.RESET + players.getTeam());
                Console.send(ChatColor.AQUA + "TeamMembers " + ChatColor.RESET + players.getTeam().getMembers());
            }
        }

        /*
        if (red.getMembers().size() < LiveSystem.TeamSize) {
            LiveSystem.TeamRed.add(player);
            LiveSystem.VotedPlayers.put(player, LiveSystem.TeamRed);
            Console.send("Added " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to Team" + ChatColor.RED + " Red");
        } else if (blue.getMembers().size() < LiveSystem.TeamSize) {
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
         */
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
        Team red = LiveSystem.NewTeams.get("red");
        Team blue = LiveSystem.NewTeams.get("blue");
        Team green = LiveSystem.NewTeams.get("green");
        Team yellow = LiveSystem.NewTeams.get("yellow");

        for (Player player : red.getMembers())
            player.getInventory().setBoots(Items.boots(Color.RED));

        for (Player player : blue.getMembers())
            player.getInventory().setBoots(Items.boots(Color.BLUE));

        for (Player player : green.getMembers())
            player.getInventory().setBoots(Items.boots(Color.GREEN));

        for (Player player : yellow.getMembers())
            player.getInventory().setBoots(Items.boots(Color.YELLOW));

        /*
        for (Player player : LiveSystem.TeamRed)
            player.getInventory().setBoots(Items.boots(Color.RED));

        for (Player player : LiveSystem.TeamBlue)
            player.getInventory().setBoots(Items.boots(Color.BLUE));

        for (Player player : LiveSystem.TeamGreen)
            player.getInventory().setBoots(Items.boots(Color.GREEN));

        for (Player player : LiveSystem.TeamYellow)
            player.getInventory().setBoots(Items.boots(Color.YELLOW));
         */
    }

    private void IngameStatus() {
        /*
        ServerObject thisServer = TimoCloudAPI.getBukkitAPI().getThisServer();
        System.out.println(thisServer.getState());
        thisServer.setState("INGAME");
        System.out.println(thisServer.getState());

         */
    }


    public static ArrayList<Player> getSpectator() {
        return spectator;
    }
}
