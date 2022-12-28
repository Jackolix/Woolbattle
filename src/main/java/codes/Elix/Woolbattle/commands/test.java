// This class was created by Elix on 15.08.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.game.Perk;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.PerkItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.Worldloader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class test implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("test");
        if (!(sender instanceof Player player)) return false;
        if (args[0] == null) args[0] = String.valueOf(player.getLocation().getX());
        if (args[1] == null) args[1] = String.valueOf(player.getLocation().getY());
        if (args[2] == null) args[2] = String.valueOf(player.getLocation().getZ());
        double posX = Double.parseDouble(args[0]);
        double posY = Double.parseDouble(args[1]);
        double posZ = Double.parseDouble(args[2]);

        Worldloader.pasteProtocols(player, player.getLocation(), new Location(player.getWorld(), posX, posY, posZ));

        /*
        HashMap<Player, Perk> perks = new HashMap<>();
        String FirstPerk = "1Perk";
        String SecondPerk = "2Perk";
        String PassivePerk = "Passive";
        Player player = Bukkit.getPlayer("Jackolix");

        Perk hey = new Perk(player, FirstPerk, SecondPerk, PassivePerk);
        perks.put(player, hey);
        System.out.println(perks);

        Perk playerperks = perks.get(player);
        String FirstPerk1 = playerperks.getfirstPerk();
        String SecondPerk1 = playerperks.getsecondtPerk();
        String PassivePerk1 = playerperks.getpassivePerk();
        System.out.println(FirstPerk1);
        System.out.println(SecondPerk1);
        System.out.println(PassivePerk1);

         */

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return null;
        ArrayList<String> completerlist = new ArrayList<>();
        if (args.length == 1)
            completerlist.add(String.valueOf(player.getLocation().getBlockX()));
        if (args.length == 2)
            completerlist.add(String.valueOf(player.getLocation().getBlockY()));
        if (args.length == 3)
            completerlist.add(String.valueOf(player.getLocation().getBlockZ()));
        return completerlist;
    }
}