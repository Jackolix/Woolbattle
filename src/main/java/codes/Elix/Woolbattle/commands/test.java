// This class was created by Elix on 15.08.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.CustomInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;

public class test implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Console.send("test");
        if (!(sender instanceof Player player)) return false;
        Title title = Title.title(Component.text("KEKW"), Component.text("KEKW"));
        player.showTitle(title);
        /*

        Inventory minecraftInventory = null; // Das ursprüngliche net.minecraft.world.entity.player.Inventory-Objekt
        CustomInventory customInventory = new CustomInventory(minecraftInventory);

        customInventory.setSlotHandler(0, (player, item) -> {
            // Aktion, wenn der Spieler den Slot 0 klickt
        });

        customInventory.setSlotHandler(1, (player, item) -> {
            // Aktion, wenn der Spieler den Slot 1 klickt
        });

        // Weitere Slot-Handler hinzufügen...

        Player player = // Der Spieler, für den das benutzerdefinierte Inventar erstellt wird
        int clickedSlot = // Der geklickte Slot

                customInventory.handleClick(player, clickedSlot);

        // PerkHelper.setPerk(player, new Perk("booster", "rope", "recharger", false, 6, 7));
        // player.sendMessage(Component.text("Injected to database"));
        /*
        // PerkHelper.getPerks(player);
        System.out.println(Items.perks);
        Perk perk = PerkHelper.getPerks(player);
        System.out.println("Firstperk: " + perk.getfirstPerk());
        System.out.println("SecondPerk: " + perk.getsecondPerk());
        System.out.println("Passiveperk: " + perk.getpassivePerk());

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
            if (player.getTargetBlockExact(5) == null) {
                completerlist.add(String.valueOf(player.getLocation().getBlockX()));
            } else
                completerlist.add(String.valueOf(player.getTargetBlockExact(5).getLocation().getBlockX()));
        if (args.length == 2)
            if (player.getTargetBlockExact(5) == null) {
                completerlist.add(String.valueOf(player.getLocation().getBlockY()));
            } else
                completerlist.add(String.valueOf(player.getTargetBlockExact(5).getLocation().getBlockY()));
        if (args.length == 3)
            if (player.getTargetBlockExact(5) == null) {
                completerlist.add(String.valueOf(player.getLocation().getBlockZ()));
            } else
                completerlist.add(String.valueOf(player.getTargetBlockExact(5).getLocation().getBlockZ()));
        return completerlist;
    }
}