// This class was created by Elix on 06.08.22


package codes.Elix.Woolbattle.util;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.sk89q.worldedit.world.World;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Worldloader {

    public static void paste(Location location, File file) {

        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
        Clipboard clipboard;

        BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if (clipboardFormat != null) {
            try (ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(file))) {

                if (location.getWorld() == null)
                    throw new NullPointerException("Failed to paste schematic due to world being null");

                World world = BukkitAdapter.adapt(location.getWorld());

                EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();

                clipboard = clipboardReader.read();

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(blockVector3)
                        .ignoreAirBlocks(true)
                        .build();

                try {
                    Operations.complete(operation);
                    editSession.close();
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void remove(Location pos1, Location pos2) {

        World world = BukkitAdapter.adapt(pos1.getWorld());
        BlockState air = BukkitAdapter.adapt(Material.AIR.createBlockData());

        BlockVector3 position1 = BlockVector3.at(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ());
        BlockVector3 position2 = BlockVector3.at(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ());
        CuboidRegion region = new CuboidRegion(position1, position2);

        EditSession editsession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
        try {
            // editsession.setBlocks((Region) region, new BaseBlock(air));
            editsession.setBlocks((Region) region, new BaseBlock(air));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

        Console.send("Removed blocks");


        /*
        BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        World world = BukkitAdapter.adapt(location.getWorld());

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
        try {
            editSession.setBlocks()
        }

         */
    }

    public static void teleport(Location location) {
        for (Player p : Bukkit.getOnlinePlayers())
            p.teleport(location);
    }
}
