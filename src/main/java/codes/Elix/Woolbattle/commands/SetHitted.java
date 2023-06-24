// This class was created by Elix on 09.10.22


package codes.Elix.Woolbattle.commands;


import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetHitted implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("Woolbattle.hitted")) {
                if (args.length == 0) {
                    CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
                    if (!customPlayer.isHitted()) {
                        customPlayer.addHitted(null);
                        Console.send("Added " + player.getName() + " to hitted");

                    } else
                        LiveSystem.hitted.remove(player);

                } else if (args.length == 1) {
                    CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(Bukkit.getPlayer(args[0]));
                    if (!customPlayer.isHitted()) {
                        // Player p = Bukkit.getPlayer(args[0]);
                        // LiveSystem.hitted.add(p);
                        customPlayer.addHitted(null);
                    }

                } else if (args.length == 2) {

                    CustomPlayer p = CustomPlayer.getCustomPlayer(Bukkit.getPlayer(args[0]));
                    // LiveSystem.hitted.add(p);
                    p.addHitted(null);

                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            // LiveSystem.hitted.remove(args[0]);
                            p.removeHitted();
                            Console.send("Removed " + args[0].toString() + " from hitted.");
                        }}, 20L *Integer.parseInt(args[1]));

                } else
                    player.sendMessage("Use /hitted <player> <countdown>");

            }

        } else {
            if (args.length == 0) {
                Console.send("Use: hitted <player> <countdown>");

            } else if (args.length == 1) {
                Player p = Bukkit.getPlayer(args[0]);
                CustomPlayer player = CustomPlayer.getCustomPlayer(p);
                if (!player.isHitted())
                    player.addHitted(null);
                    /*
                if (!(LiveSystem.hitted.contains(args[0]))) {
                    Player p = Bukkit.getPlayer(args[0]);
                    CustomPlayer player = CustomPlayer.getCustomPlayer(p);
                    player.addHitted(null);
                    LiveSystem.hitted.add(p);
                }
                     */

            } else if (args.length == 2) {
                Player p = Bukkit.getPlayer(args[0]);
                CustomPlayer player = CustomPlayer.getCustomPlayer(p);
                if (!player.isHitted()) {
                    player.addHitted(null);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            player.removeHitted();
                            Console.send("Removed " + p.getName() + " from hitted.");
                        }}, 20L *Integer.parseInt(args[1]));
                }
                /*
                LiveSystem.hitted.add(p);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        LiveSystem.hitted.remove(args[0]);
                        Console.send("Removed " + args[0] + " from hitted.");
                    }}, 20L *Integer.parseInt(args[1]));

                 */

            } else
                Console.send("Use /hitted <player> <countdown>");

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> completerlist = new ArrayList<>();
        for (Player players : Bukkit.getOnlinePlayers())
            completerlist.add(players.getName());

        if (args.length == 1) {
            return completerlist; // Collections.singletonList(Bukkit.getOnlinePlayers().stream().toList().toString());
        }
        return null;
    }
}
