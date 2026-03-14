// This class was created by Elix on 30.08.22


package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.game.HelpClasses.Perk;
import codes.Elix.Woolbattle.game.PerkHelper;
import codes.Elix.Woolbattle.main.Woolbattle;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PerkItems {
    //TODO: richtige Items
    private static FileConfiguration config = Woolbattle.getPlugin().getConfig();

    public static void equip(Player player) {
        /*
        if (!Woolbattle.useDB)
            loadDB();
         */

        Perk perk = PerkHelper.getPerks(player);
        String Fperk = perk.getfirstPerk();
        String Sperk = perk.getsecondPerk();
        String Pperk = perk.getpassivePerk();

        Integer Fperkslot = perk.getfirstPerkSlot();
        Integer Sperkslot = perk.getsecondPerkSlot();

        Perk oldPerk = new Perk(perk.getfirstPerk(), perk.getsecondPerk(), perk.getpassivePerk(), perk.hasParticles(), perk.getfirstPerkSlot(), perk.getsecondPerkSlot());

        /*
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");
        Object Pperk = config.get(player.getName() + ".passive");

        Integer Fperkslot = (Integer) config.get(player.getName() + ".1Perkslot");
        Woolbattle.getPlugin().saveConfig();
         */

        if (Fperk == null) {
            perk.setFirstPerk("booster");
            Pperk = "booster";
        }
        if (Sperk == null) {
            perk.setSecondPerk("enterhaken");
            Sperk = "enterhaken";
        }
        if (Pperk == null) {
            perk.setPassivePerk("aufzug");
            Pperk = "aufzug";
        }
        if (Fperkslot == null) {
            perk.setFirstPerkSlot(6);
            Fperkslot = 6;
        }
        if (Sperkslot == null) {
            perk.setSecondPerkSlot(7);
            Sperkslot = 7;
        }
        PerkHelper.updatePerks(player, oldPerk, perk);

        switch (Objects.requireNonNull(Fperk != null ? Fperk.toString() : null)) {
            case "booster" -> Items.create(player.getInventory(), Material.FIREWORK_ROCKET, "§3Booster", Fperkslot);
            case "blink" -> Items.create(player.getInventory(), Material.TRIPWIRE_HOOK, "§3Blink", Fperkslot);
            case "enterhaken" -> Items.create(player.getInventory(), Material.FISHING_ROD, "§3Enterhaken", Fperkslot);
            case "freezer" -> Items.create(player.getInventory(), Material.PACKED_ICE, "§3Freezer", Fperkslot);
            case "großvatersuhr" -> Items.create(player.getInventory(), Material.CLOCK, "§3Großvaters Uhr", Fperkslot);
            case "linebuilder" -> Items.create(player.getInventory(), Material.STICK, "§3Linebuilder", Fperkslot);
            case "minigun" -> Items.create(player.getInventory(), Material.DIAMOND_HORSE_ARMOR, "§3Minigun", Fperkslot);
            case "pfeilbombe" -> Items.create(player.getInventory(), Material.FIRE_CHARGE, "§3Pfeilbombe", Fperkslot);
            case "portal" -> Items.create(player.getInventory(), Material.ENDER_EYE, "§5Portal", Fperkslot);
            case "rettungskapsel" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS, "§3Rettungskapsel", Fperkslot);
            case "rettungsplattform" -> Items.create(player.getInventory(), Material.BLAZE_ROD, "§3Rettungsplattform", Fperkslot);
            case "rope" -> Items.create(player.getInventory(), Material.VINE, "§3Rope", Fperkslot);
            case "schutzschild" -> Items.create(player.getInventory(), Material.EMERALD, "§3Schuztschild", Fperkslot);
            case "slimeplattform" -> Items.create(player.getInventory(), Material.SLIME_BALL, "§3SlimePlattform", Fperkslot);
            case "sprengsatz", "mine" -> Items.create(player.getInventory(), Material.STONE_PRESSURE_PLATE, "§cMine", Fperkslot);
            case "tauscher" -> Items.create(player.getInventory(), Material.SNOWBALL, "§3Tauscher", Fperkslot);
            case "klospülung" -> Items.create(player.getInventory(), Material.GLASS_BOTTLE, "§3Klospülung", Fperkslot);
            case "wandgenerator" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS_PANE, "§3Wandgenerator", Fperkslot);
            case "woolbombe" -> Items.create(player.getInventory(), Material.TNT, "§3WoolBombe", Fperkslot);
            case "grabber" -> Items.create(player.getInventory(), Material.CARROT_ON_A_STICK, "§3The Grabber", Fperkslot);
        }
        switch (Objects.requireNonNull(Sperk != null ? Sperk.toString() : null)) {
            case "booster" -> Items.create(player.getInventory(), Material.FIREWORK_ROCKET, "§3Booster", Sperkslot);
            case "blink" -> Items.create(player.getInventory(), Material.TRIPWIRE_HOOK, "§3Blink", Sperkslot);
            case "enterhaken" -> Items.create(player.getInventory(), Material.FISHING_ROD, "§3Enterhaken", Sperkslot);
            case "freezer" -> Items.create(player.getInventory(), Material.PACKED_ICE, "§3Freezer", Sperkslot);
            case "großvatersuhr" -> Items.create(player.getInventory(), Material.CLOCK, "§3Großvaters Uhr", Sperkslot);
            case "linebuilder" -> Items.create(player.getInventory(), Material.STICK, "§3Linebuilder", Sperkslot);
            case "minigun" -> Items.create(player.getInventory(), Material.DIAMOND_HORSE_ARMOR, "§3Minigun", Sperkslot);
            case "pfeilbombe" -> Items.create(player.getInventory(), Material.FIRE_CHARGE, "§3Pfeilbombe", Sperkslot);
            case "portal" -> Items.create(player.getInventory(), Material.ENDER_EYE, "§5Portal", Sperkslot);
            case "rettungskapsel" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS, "§3Rettungskapsel", Sperkslot);
            case "rettungsplattform" -> Items.create(player.getInventory(), Material.BLAZE_ROD, "§3Rettungsplattform", Sperkslot);
            case "rope" -> Items.create(player.getInventory(), Material.VINE, "§3Rope", Sperkslot);
            case "schutzschild" -> Items.create(player.getInventory(), Material.EMERALD, "§3Schuztschild", Sperkslot);
            case "slimeplattform" -> Items.create(player.getInventory(), Material.SLIME_BALL, "§3SlimePlattform", Sperkslot);
            case "sprengsatz", "mine" -> Items.create(player.getInventory(), Material.STONE_PRESSURE_PLATE, "§cMine", Sperkslot);
            case "tauscher" -> Items.create(player.getInventory(), Material.SNOWBALL, "§3Tauscher", Sperkslot);
            case "klospülung" -> Items.create(player.getInventory(), Material.GLASS_BOTTLE, "§3Klospülung", Sperkslot);
            case "wandgenerator" -> Items.create(player.getInventory(), Material.RED_STAINED_GLASS_PANE, "§3Wandgenerator", Sperkslot);
            case "woolbombe" -> Items.create(player.getInventory(), Material.TNT, "§3WoolBombe", Sperkslot);
            case "grabber" -> Items.create(player.getInventory(), Material.CARROT_ON_A_STICK, "§3The Grabber", Sperkslot);
        }

    }

    public static void select(Player player, String perkname, String perk) {
        /*
        Object Fperk = config.get(player.getName() + ".1Perk");
        Object Sperk = config.get(player.getName() + ".2Perk");

        if (Objects.equals(Perkslot, ".1Perk"))
            if (Perk == Sperk) return;

        if (Objects.equals(Perkslot, ".2Perk"))
            if (Perk == Fperk) return;

        config.set(player.getName() + Perkslot, Perk);
        Woolbattle.getPlugin().saveConfig();
         */

        Perk playerperk = PerkHelper.getPerks(player);
        Perk oldperk = new Perk(playerperk.getfirstPerk(), playerperk.getsecondPerk(), playerperk.getpassivePerk(), playerperk.hasParticles(), playerperk.getfirstPerkSlot(), playerperk.getsecondPerkSlot());

        if (Objects.equals(perk, ".1Perk")) {
            if (Objects.equals(perkname, playerperk.getfirstPerk())) return;
            if (Objects.equals(playerperk.getsecondPerk(), perkname)) return;
            playerperk.setFirstPerk(perkname);
        }
        if (Objects.equals(perk, ".2Perk")) {
            if (Objects.equals(perkname, playerperk.getsecondPerk())) return;
            if (Objects.equals(playerperk.getfirstPerk(), perkname)) return;
            playerperk.setSecondPerk(perkname);
        }
        PerkHelper.updatePerks(player, oldperk, playerperk);
    }

    public static void selectPassive(Player player, String passive) {
        Perk perk = PerkHelper.getPerks(player);
        Perk oldperk = new Perk(perk.getfirstPerk(), perk.getsecondPerk(), perk.getpassivePerk(), perk.hasParticles(), perk.getfirstPerkSlot(), perk.getsecondPerkSlot());
        perk.setPassivePerk(passive);
        PerkHelper.updatePerks(player, oldperk, perk);
    }
}
