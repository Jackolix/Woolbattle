// This class was created by Elix on 15.08.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.util.Worldloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class test implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("test");
        return false;
    }
}
