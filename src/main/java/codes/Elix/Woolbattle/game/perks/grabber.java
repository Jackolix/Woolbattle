package codes.Elix.Woolbattle.game.perks;

import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.items.Items;
import codes.Elix.Woolbattle.main.Woolbattle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

public class grabber implements Listener {

    private static HashMap<Player, Entity> hitted = new HashMap<>();
    int cost = 5;
    int cooldown = 10;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if ((event.getItem().getType() == Material.CARROT_ON_A_STICK)) {
            Player player = event.getPlayer();
            if (!Woolbattle.debug)
                if (!Items.cost(player, cost)) {
                    event.setCancelled(true);
                    return;
                }
            Vector direction = player.getLocation().getDirection();
            Vector velocity = direction.multiply(1.5);
            Projectile egg = player.launchProjectile(Egg.class, velocity);
            egg.setMetadata("grabber", new FixedMetadataValue(Woolbattle.getPlugin(), "keineAhnungWiesoIchDasBrauch"));
            event.setCancelled(true);

            Bukkit.getScheduler().runTaskLaterAsynchronously(Woolbattle.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (hitted.containsKey(player)) return;
                    System.out.println("Player didnt hit a entity");
                    if (Objects.equals(PerkHelper.passive(player), "recharger"))
                        cooldown = 8;
                    int slot = player.getInventory().getHeldItemSlot();
                    if (!Woolbattle.debug)
                        Items.visualCooldown(player, cooldown, Material.CARROT_ON_A_STICK, slot, "§3Grabber");
                }
            }, 8*20);

        } else if (event.getItem().getType() == Material.STICK) {
            if (!event.getItem().getItemMeta().hasEnchant(Enchantment.FORTUNE)) return;
            Entity player = hitted.get(event.getPlayer());
            Vector vector = event.getPlayer().getLocation().toVector().subtract(player.getLocation().toVector());
            System.out.println(vector);
            player.setVelocity(vector);
            hitted.remove(event.getPlayer());
            if (Objects.equals(PerkHelper.passive(event.getPlayer()), "recharger"))
                cooldown = 8;
            int slot = event.getPlayer().getInventory().getHeldItemSlot();
            if (!Woolbattle.debug) {
                Items.visualCooldown(event.getPlayer(), cooldown, Material.CARROT_ON_A_STICK, slot, "§3Grabber");
                return;
            }
            Items.create(event.getPlayer().getInventory(), Material.CARROT_ON_A_STICK, "§3The Grabber", slot);
        }
    }

    @EventHandler
    public void entityHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;
        if (!(event.getEntity().getShooter() instanceof Player player)) return;
        if (!(event.getEntity().hasMetadata("grabber"))) return;
        Entity hittedPlayer = event.getEntity();
        hitted.put(player, hittedPlayer);
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text("The Grabber", NamedTextColor.AQUA));
        itemMeta.addEnchant(Enchantment.FORTUNE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(itemMeta);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), item);

        Bukkit.getScheduler().runTaskLaterAsynchronously(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (!hitted.containsKey(player))
                    return;
                System.out.println("Player didnt grab an entity");
                if (Objects.equals(PerkHelper.passive(player), "recharger"))
                    cooldown = 8;
                int slot = player.getInventory().getHeldItemSlot();
                if (!Woolbattle.debug)
                    Items.visualCooldown(player, cooldown, Material.CARROT_ON_A_STICK, slot, "§3Grabber");
            }
        }, 80);


    }

    public static void enable() {
        Bukkit.getPluginManager().registerEvents(new grabber(), Woolbattle.getPlugin());
    }
    public static void disable() {}

}
