package codes.Elix.Woolbattle.commands;

import codes.Elix.Woolbattle.main.Woolbattle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VisualizeWool implements CommandExecutor {

    private static final List<BlockDisplay> displayEntities = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("Woolbattle.visualize")) {
            player.sendMessage(Woolbattle.PREFIX.append(Component.text("No permission!", NamedTextColor.RED)));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {
            clearVisualizations();
            player.sendMessage(Woolbattle.PREFIX.append(Component.text("Cleared all wool visualizations!", NamedTextColor.GREEN)));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("unprotected")) {
            visualizeUnprotectedWool(player);
            return true;
        }

        visualizeProtectedWool(player);
        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Visualizing protected wool blocks! Use '/visualizewool clear' to remove.", NamedTextColor.GREEN)));
        
        return true;
    }

    private void visualizeProtectedWool(Player player) {
        clearVisualizations();
        
        World world = player.getWorld();
        
        for (Block block : Woolbattle.blocks) {
            if (block.getWorld().equals(world)) {
                createOutline(block);
            }
        }
        
        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Showing " + Woolbattle.blocks.size() + " protected wool blocks with green outlines.", NamedTextColor.GREEN)));
    }

    private void visualizeUnprotectedWool(Player player) {
        clearVisualizations();
        
        World world = player.getWorld();
        int radius = 100; // Check within 100 blocks of player
        Location center = player.getLocation();
        
        int unprotectedCount = 0;
        for (int x = center.getBlockX() - radius; x <= center.getBlockX() + radius; x++) {
            for (int y = world.getMinHeight(); y <= world.getMaxHeight(); y++) {
                for (int z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().name().contains("WOOL") && !Woolbattle.blocks.contains(block)) {
                        createOutline(block, Material.RED_CONCRETE);
                        unprotectedCount++;
                    }
                }
            }
        }
        
        player.sendMessage(Woolbattle.PREFIX.append(Component.text("Showing " + unprotectedCount + " unprotected wool blocks with red outlines.", NamedTextColor.RED)));
    }

    private void createOutline(Block block) {
        createOutline(block, Material.LIME_CONCRETE);
    }

    private void createOutline(Block block, Material outlineMaterial) {
        Location loc = block.getLocation();
        
        // Create outline using block displays at the edges
        double[][] offsets = {
            // Bottom edges
            {0, -0.1, 0, 1, 0.05, 0.05},     // Bottom X edge
            {0, -0.1, 0.95, 1, 0.05, 0.05},  // Bottom X edge 2
            {0, -0.1, 0, 0.05, 0.05, 1},     // Bottom Z edge 1
            {0.95, -0.1, 0, 0.05, 0.05, 1},  // Bottom Z edge 2
            
            // Top edges  
            {0, 1, 0, 1, 0.05, 0.05},     // Top X edge 1
            {0, 1, 0.95, 1, 0.05, 0.05},  // Top X edge 2
            {0, 1, 0, 0.05, 0.05, 1},     // Top Z edge 1
            {0.95, 1, 0, 0.05, 0.05, 1},  // Top Z edge 2
            
            // Vertical edges
            {0, 0, 0, 0.05, 1, 0.05},        // Corner 1
            {0.95, 0, 0, 0.05, 1, 0.05},     // Corner 2
            {0, 0, 0.95, 0.05, 1, 0.05},     // Corner 3
            {0.95, 0, 0.95, 0.05, 1, 0.05}  // Corner 4
        };
        
        for (double[] offset : offsets) {
            Location displayLoc = loc.clone().add(offset[0], offset[1], offset[2]);
            
            BlockDisplay display = (BlockDisplay) loc.getWorld().spawnEntity(displayLoc, EntityType.BLOCK_DISPLAY);
            display.setBlock(outlineMaterial.createBlockData());
            
            // Set the size of the display
            display.setTransformation(new org.bukkit.util.Transformation(
                new org.joml.Vector3f(0, 0, 0),
                new org.joml.Quaternionf(),
                new org.joml.Vector3f((float) offset[3], (float) offset[4], (float) offset[5]),
                new org.joml.Quaternionf()
            ));
            
            displayEntities.add(display);
        }
    }

    private void clearVisualizations() {
        for (BlockDisplay display : displayEntities) {
            if (display != null && !display.isDead()) {
                display.remove();
            }
        }
        displayEntities.clear();
    }
}