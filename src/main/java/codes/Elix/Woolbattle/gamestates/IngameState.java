// This class was created by Elix on 19.06.22


package codes.Elix.Woolbattle.gamestates;

import codes.Elix.Woolbattle.game.BowShoot;
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
import codes.Elix.Woolbattle.util.SchematicManager;
import codes.Elix.Woolbattle.util.mongo.UpdateObjekt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
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
        teleportTeamsToSpawns(); // Teleport teams to their spawn locations
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
        BowShoot.enable();
        grabber.enable();

        for (Player player : Items.perks.keySet())
            if (!player.isOnline())
                Items.perks.remove(player);
        UpdateObjekt.update();
    }

    @Override
    public void stop() {
        DoubleJump.disable();
    }

    public void setLifes() {
        int lives = Voting.winner();
        if (lives == 0)
            lives = 6;
        LiveSystem.Team.get("red").setLifes(lives);
        LiveSystem.Team.get("blue").setLifes(lives);
        LiveSystem.Team.get("green").setLifes(lives);
        LiveSystem.Team.get("yellow").setLifes(lives);

        /*
        LiveSystem.TeamLifes.put("red", lives);
        LiveSystem.TeamLifes.put("blue", lives);
        LiveSystem.TeamLifes.put("green", lives);
        LiveSystem.TeamLifes.put("yellow", lives);
         */
    }

    public void checkTeams() {
        Console.send(Component.text("---------- Teams ----------", NamedTextColor.GOLD));
        Console.send(Component.text("Checking teams...", NamedTextColor.WHITE));
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
            //if (LiveSystem.newVotedPlayers.contains(CustomPlayer.getCustomPlayer(player))) continue;
            if (CustomPlayer.getCustomPlayer(player).getTeam() != null) continue;
            // Console.send(("Trying to add " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " to team"));
            // Console.send(Component.text("Trying to add ", NamedTextColor.WHITE)
                    // .append(Component.text(player.getName(), NamedTextColor.GREEN))
                    // .append(Component.text(" to team", NamedTextColor.WHITE)));
            addToEmptyTeam(player);
        }
        Console.send(" ");
        Console.send(Component.text("Team Red: " + LiveSystem.Team.get("red").getMembers().size(), NamedTextColor.RED));
        Console.send(Component.text("Team Blue: " + LiveSystem.Team.get("blue").getMembers().size(), NamedTextColor.BLUE));
        Console.send(Component.text("Team Green: " + LiveSystem.Team.get("green").getMembers().size(), NamedTextColor.GREEN));
        Console.send(Component.text("Team Yellow: " + LiveSystem.Team.get("yellow").getMembers().size(), NamedTextColor.YELLOW));

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
        Team red = LiveSystem.Team.get("red");
        Team blue = LiveSystem.Team.get("blue");
        Team green = LiveSystem.Team.get("green");
        Team yellow = LiveSystem.Team.get("yellow");

        if (LiveSystem.Teams == 2) { // TODO
            if (Bukkit.getOnlinePlayers().size() == 2)
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getName().equals(player.getName()))
                        return;
                    CustomPlayer customPlayer1 = CustomPlayer.getCustomPlayer(players);
                    if (customPlayer1.getTeam() == null) {
                        customPlayer.setTeam(red);
                        customPlayer1.setTeam(blue);
                    } else {
                        if (customPlayer1.getTeam() == red) {
                            customPlayer.setTeam(blue);
                        } else
                            customPlayer.setTeam(red);
                    }
                }
        }


        if (red.getMembers().size() < LiveSystem.TeamSize) {
            red.addMember(player);
            customPlayer.setTeam(red);
            Console.send(Component.text("Added ", NamedTextColor.WHITE)
                    .append(Component.text(player.getName(), NamedTextColor.GREEN))
                    .append(Component.text(" to Team", NamedTextColor.WHITE))
                    .append(Component.text(" Red", NamedTextColor.RED)));
        } else if (blue.getMembers().size() < LiveSystem.TeamSize) {
            blue.addMember(player);
            customPlayer.setTeam(blue);
            Console.send(Component.text("Added ", NamedTextColor.WHITE)
                    .append(Component.text(player.getName(), NamedTextColor.GREEN))
                    .append(Component.text(" to Team", NamedTextColor.WHITE))
                    .append(Component.text(" Blue", NamedTextColor.BLUE)));
        } else if (LiveSystem.Teams >= 3) {
            if (green.getMembers().size() < LiveSystem.TeamSize) {
                green.addMember(player);
                customPlayer.setTeam(green);
                Console.send(Component.text("Added ", NamedTextColor.WHITE)
                        .append(Component.text(player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" to Team", NamedTextColor.WHITE))
                        .append(Component.text(" Green", NamedTextColor.GREEN)));
            }
        } else if (LiveSystem.Teams >= 3) {
            if (yellow.getMembers().size() < LiveSystem.TeamSize) {
                yellow.addMember(player);
                customPlayer.setTeam(yellow);
                Console.send(Component.text("Added ", NamedTextColor.WHITE)
                        .append(Component.text(player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" to Team", NamedTextColor.WHITE))
                        .append(Component.text(" Yellow", NamedTextColor.YELLOW)));
            }
        } else {
            Console.send(Component.text("All Teams are full!", NamedTextColor.RED));
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
            for (Player p : Bukkit.getOnlinePlayers()) {
                CustomPlayer players = CustomPlayer.getCustomPlayer(p);
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
        CustomPlayer p = CustomPlayer.getCustomPlayer(player);
        p.getTeam().addMember(player);
        spectator.add(player);
        player.setAllowFlight(true);
        player.getInventory().clear();

        for (Player current : Bukkit.getOnlinePlayers())
            current.hidePlayer(Woolbattle.getPlugin(), player);

        // Use config-based spectator spawn location
        Location spectatorSpawn = getSpectatorSpawnLocation();
        player.teleport(spectatorSpawn);
        // LiveSystem.Team.put(player, "spectator");
        IngameScoreboard.setup(player);
    }
    
    private static Location getSpectatorSpawnLocation() {
        try {
            // Get the selected map from SchematicManager
            String selectedMap = SchematicManager.getSelectedMap();
            return SchematicManager.getSpectatorSpawn(selectedMap);
        } catch (Exception e) {
            Console.send("Failed to get spectator spawn from config, using default: " + e.getMessage());
            // Fallback to default location
            return new Location(Bukkit.getServer().getWorlds().get(0), 0, 80, 0);
        }
    }
    
    private void teleportTeamsToSpawns() {
        Console.send(Component.text("Teleporting teams to spawn locations...", NamedTextColor.YELLOW));
        
        try {
            String selectedMap = SchematicManager.getSelectedMap();
            Team red = LiveSystem.Team.get("red");
            Team blue = LiveSystem.Team.get("blue");
            Team green = LiveSystem.Team.get("green");
            Team yellow = LiveSystem.Team.get("yellow");
            
            // Teleport red team
            if (red != null && !red.getMembers().isEmpty()) {
                Location redSpawn = SchematicManager.getTeamSpawn(selectedMap, "red");
                for (Player player : red.getMembers()) {
                    player.teleport(redSpawn);
                    Console.send(Component.text("Teleported ", NamedTextColor.WHITE)
                        .append(Component.text(player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" to ", NamedTextColor.WHITE))
                        .append(Component.text("Red", NamedTextColor.RED))
                        .append(Component.text(" spawn", NamedTextColor.WHITE)));
                }
            }
            
            // Teleport blue team
            if (blue != null && !blue.getMembers().isEmpty()) {
                Location blueSpawn = SchematicManager.getTeamSpawn(selectedMap, "blue");
                for (Player player : blue.getMembers()) {
                    player.teleport(blueSpawn);
                    Console.send(Component.text("Teleported ", NamedTextColor.WHITE)
                        .append(Component.text(player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" to ", NamedTextColor.WHITE))
                        .append(Component.text("Blue", NamedTextColor.BLUE))
                        .append(Component.text(" spawn", NamedTextColor.WHITE)));
                }
            }
            
            // Teleport green team (if exists)
            if (green != null && !green.getMembers().isEmpty() && LiveSystem.Teams >= 3) {
                Location greenSpawn = SchematicManager.getTeamSpawn(selectedMap, "green");
                for (Player player : green.getMembers()) {
                    player.teleport(greenSpawn);
                    Console.send(Component.text("Teleported ", NamedTextColor.WHITE)
                        .append(Component.text(player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" to ", NamedTextColor.WHITE))
                        .append(Component.text("Green", NamedTextColor.GREEN))
                        .append(Component.text(" spawn", NamedTextColor.WHITE)));
                }
            }
            
            // Teleport yellow team (if exists)
            if (yellow != null && !yellow.getMembers().isEmpty() && LiveSystem.Teams >= 4) {
                Location yellowSpawn = SchematicManager.getTeamSpawn(selectedMap, "yellow");
                for (Player player : yellow.getMembers()) {
                    player.teleport(yellowSpawn);
                    Console.send(Component.text("Teleported ", NamedTextColor.WHITE)
                        .append(Component.text(player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" to ", NamedTextColor.WHITE))
                        .append(Component.text("Yellow", NamedTextColor.YELLOW))
                        .append(Component.text(" spawn", NamedTextColor.WHITE)));
                }
            }
            
        } catch (Exception e) {
            Console.send("Failed to teleport teams to config-based spawns: " + e.getMessage());
            Console.send("Teams will use default spawn locations");
        }
    }

    public static void boots() {
        Team red = LiveSystem.Team.get("red");
        Team blue = LiveSystem.Team.get("blue");
        Team green = LiveSystem.Team.get("green");
        Team yellow = LiveSystem.Team.get("yellow");

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
