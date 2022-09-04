// This class was created by Elix on 30.08.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PerkItems {
    //TODO: richtige Items
    private static FileConfiguration config = Woolbattle.getPlugin().getConfig();

    public static void equip(Player player) {
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");

        Integer Fperkslot = (Integer) config.get(player.getName() + ".1Perkslot");
        Integer Sperkslot = (Integer) config.get(player.getName() + ".2Perkslot");


        switch (Objects.requireNonNull(Fperk != null ? Fperk.toString() : null)) {
            case "booster" -> Items.create(player.getInventory(), Material.TRIPWIRE_HOOK, "§3Booster", Fperkslot);
            case "enterhaken" -> Items.create(player.getInventory(), Material.FISHING_ROD, "§3Enterhaken", Fperkslot);
            case "freezer" -> Items.create(player.getInventory(), Material.PACKED_ICE, "§3Freezer", Fperkslot);
            case "großvatersuhr" -> Items.create(player.getInventory(), Material.CLOCK, "§3Großvaters Uhr", Fperkslot);
            case "linebuilder" -> Items.create(player.getInventory(), Material.STICK, "§3Linebuilder", Fperkslot);
            case "minigun" -> Items.create(player.getInventory(), Material.DIAMOND_HORSE_ARMOR, "§3Minigun", Fperkslot);
            case "pfeilbombe" -> Items.create(player.getInventory(), Material.FIRE_CHARGE, "§3Pfeilbombe", Fperkslot);
            case "portal" -> Items.create(player.getInventory(), Material.OBSIDIAN, "§3Portal", Fperkslot);
            case "rettungskapsel" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS, "§3Rettungskapsel", Fperkslot);
            case "rettungsplattform" -> Items.create(player.getInventory(), Material.BLAZE_ROD, "§3Rettungsplattform", Fperkslot);
            case "rope" -> Items.create(player.getInventory(), Material.VINE, "§3Rope", Fperkslot);
            case "schutzschild" -> Items.create(player.getInventory(), Material.EMERALD, "§3Schuztschild", Fperkslot);
            case "slimeplattform" -> Items.create(player.getInventory(), Material.SLIME_BALL, "§3SlimePlattform", Fperkslot);
            case "sprengsatz" -> Items.create(player.getInventory(), Material.STONE_PRESSURE_PLATE, "§3Sprengsatz", Fperkslot);
            case "tauscher" -> Items.create(player.getInventory(), Material.SNOWBALL, "§3Tauscher", Fperkslot);
            case "klospülung" -> Items.create(player.getInventory(), Material.GLASS_BOTTLE, "§3Klospülung", Fperkslot);
            case "wandgenerator" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS_PANE, "§3Wandgenerator", Fperkslot);
            case "woolbombe" -> Items.create(player.getInventory(), Material.TNT, "§3WoolBombe", Fperkslot);
            case "thegrabber" -> Items.create(player.getInventory(), Material.FISHING_ROD, "§3The Grabber", Fperkslot);
        }
        switch (Objects.requireNonNull(Sperk != null ? Sperk.toString() : null)) {
            case "booster" -> Items.create(player.getInventory(), Material.TRIPWIRE_HOOK, "§3Booster", Sperkslot);
            case "enterhaken" -> Items.create(player.getInventory(), Material.FISHING_ROD, "§3Enterhaken", Sperkslot);
            case "freezer" -> Items.create(player.getInventory(), Material.PACKED_ICE, "§3Freezer", Sperkslot);
            case "großvatersuhr" -> Items.create(player.getInventory(), Material.CLOCK, "§3Großvaters Uhr", Sperkslot);
            case "linebuilder" -> Items.create(player.getInventory(), Material.STICK, "§3Linebuilder", Sperkslot);
            case "minigun" -> Items.create(player.getInventory(), Material.DIAMOND_HORSE_ARMOR, "§3Minigun", Sperkslot);
            case "pfeilbombe" -> Items.create(player.getInventory(), Material.FIRE_CHARGE, "§3Pfeilbombe", Sperkslot);
            case "portal" -> Items.create(player.getInventory(), Material.OBSIDIAN, "§3Portal", Sperkslot);
            case "rettungskapsel" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS, "§3Rettungskapsel", Sperkslot);
            case "rettungsplattform" -> Items.create(player.getInventory(), Material.BLAZE_ROD, "§3Rettungsplattform", Sperkslot);
            case "rope" -> Items.create(player.getInventory(), Material.VINE, "§3Rope", Sperkslot);
            case "schutzschild" -> Items.create(player.getInventory(), Material.EMERALD, "§3Schuztschild", Sperkslot);
            case "slimeplattform" -> Items.create(player.getInventory(), Material.SLIME_BALL, "§3SlimePlattform", Sperkslot);
            case "sprengsatz" -> Items.create(player.getInventory(), Material.STONE_PRESSURE_PLATE, "§3Sprengsatz", Sperkslot);
            case "tauscher" -> Items.create(player.getInventory(), Material.SNOWBALL, "§3Tauscher", Sperkslot);
            case "klospülung" -> Items.create(player.getInventory(), Material.GLASS_BOTTLE, "§3Klospülung", Sperkslot);
            case "wandgenerator" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS_PANE, "§3Wandgenerator", Sperkslot);
            case "woolbombe" -> Items.create(player.getInventory(), Material.TNT, "§3WoolBombe", Sperkslot);
            case "thegrabber" -> Items.create(player.getInventory(), Material.FISHING_ROD, "§3The Grabber", Sperkslot);
        }

    }
}
