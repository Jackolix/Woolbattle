// This class was created by Elix on 15.08.22


package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.game.LiveSystem;
import codes.Elix.Woolbattle.gamestates.IngameState;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.items.PerkItems;
import codes.Elix.Woolbattle.main.Woolbattle;
import codes.Elix.Woolbattle.util.Console;
import codes.Elix.Woolbattle.util.ConsoleAnimator;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class test implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("test");

        return false;
    }
}

// Logger
/*
on load / paste the file
[13:52:29 WARN]: java.util.zip.ZipException: Not in GZIP format
[13:52:29 WARN]: 	at java.base/java.util.zip.GZIPInputStream.readHeader(GZIPInputStream.java:165)
[13:52:29 WARN]: 	at java.base/java.util.zip.GZIPInputStream.<init>(GZIPInputStream.java:79)
[13:52:29 WARN]: 	at java.base/java.util.zip.GZIPInputStream.<init>(GZIPInputStream.java:91)
[13:52:29 WARN]: 	at FastAsyncWorldEdit-Bukkit-2.4.5-SNAPSHOT-273.jar//com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat$1.getReader(BuiltInClipboardFormat.java:66)
[13:52:29 WARN]: 	at Woolbattle-0.6.5.jar//codes.Elix.Woolbattle.util.Worldloader.paste(Worldloader.java:40)
[13:52:29 WARN]: 	at Woolbattle-0.6.5.jar//codes.Elix.Woolbattle.main.Woolbattle.LobbyMap(Woolbattle.java:161)
[13:52:29 WARN]: 	at Woolbattle-0.6.5.jar//codes.Elix.Woolbattle.main.Woolbattle.onEnable(Woolbattle.java:59)
[13:52:29 WARN]: 	at org.bukkit.plugin.java.JavaPlugin.setEnabled(JavaPlugin.java:264)
[13:52:29 WARN]: 	at org.bukkit.plugin.java.JavaPluginLoader.enablePlugin(JavaPluginLoader.java:370)
[13:52:29 WARN]: 	at org.bukkit.plugin.SimplePluginManager.enablePlugin(SimplePluginManager.java:542)
[13:52:29 WARN]: 	at org.bukkit.craftbukkit.v1_19_R1.CraftServer.enablePlugin(CraftServer.java:565)
[13:52:29 WARN]: 	at org.bukkit.craftbukkit.v1_19_R1.CraftServer.enablePlugins(CraftServer.java:479)
[13:52:29 WARN]: 	at net.minecraft.server.MinecraftServer.loadWorld0(MinecraftServer.java:636)
[13:52:29 WARN]: 	at net.minecraft.server.MinecraftServer.loadLevel(MinecraftServer.java:422)
[13:52:29 WARN]: 	at net.minecraft.server.dedicated.DedicatedServer.e(DedicatedServer.java:306)
[13:52:29 WARN]: 	at net.minecraft.server.MinecraftServer.v(MinecraftServer.java:1126)
[13:52:29 WARN]: 	at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:305)
[13:52:29 WARN]: 	at java.base/java.lang.Thread.run(Thread.java:833)
 */