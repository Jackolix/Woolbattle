package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class platform implements Listener {

    private long cooldown = 15;

    //number of blocks under the player where the platform should be spawned
    private float depth = 2;

    private boolean ready = true;
    @EventHandler
    public void onClockInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)    return;
        if (!ready)                     return;
        if (event.getItem().getType() == Material.BLAZE_ROD) {
            ready = false;
            for(Enchantment ench: event.getItem().getEnchantments().keySet()){
                event.getItem().removeEnchantment(ench);
            }

            //TODO: change the color of the wool based on the team the player is in
            placeBlocks(event.getPlayer().getLocation());

            //TODO: Cooldown for platform (visual representation)
            Woolbattle.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Woolbattle.getPlugin(),
                    () -> activate(true, event.getItem()), 20 * cooldown);
        }
    }

    private void activate(boolean rdy, ItemStack item) {
        ready = rdy;
        item.addEnchantment(Enchantment.DIG_SPEED, 1);
    }

    private void placeBlocks(Location location) {
        //move the location down with the value in depth
        location.add(0, -depth, 0);

        if (location.add(2,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(-1,0,0).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,-1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(-1,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
        if (location.add(0,0,1).getBlock().getType() == Material.AIR) location.getBlock().setType(Material.WOOL);
    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new platform(), Woolbattle.getPlugin());
    }


    public static void disable() {}
}
