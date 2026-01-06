package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.game.HelpClasses.CustomPlayer;
import codes.Elix.Woolbattle.game.LiveSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class IngameTabList {

    private static final Map<Player, Boolean> activePlayers = new HashMap<>();
    private static final Component SEPARATOR = Component.text("━━━━━━━━━━━━━━━━", NamedTextColor.GRAY);

    public static void setup(Player player) {
        if (!player.isOnline()) return;

        activePlayers.put(player, true);
        updatePlayer(player);
    }

    public static void updateAll() {
        for (Player player : activePlayers.keySet()) {
            if (player.isOnline()) {
                updatePlayer(player);
            }
        }
    }

    public static void update(Player player) {
        if (player.isOnline()) {
            updatePlayer(player);
        }
    }

    public static void remove(Player player) {
        activePlayers.remove(player);
        if (player.isOnline()) {
            player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty());
        }
    }

    private static void updatePlayer(Player player) {
        if (!player.isOnline()) return;

        Component header = buildHeader();
        Component footer = buildFooter(player);
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    private static Component buildHeader() {
        return Component.text()
                .append(Component.text("WOOLBATTLE", NamedTextColor.WHITE).decorate(TextDecoration.BOLD))
                .append(Component.newline())
                .append(SEPARATOR)
                .append(Component.newline())
                .append(Component.text("IN GAME", NamedTextColor.RED))
                .build();
    }

    private static Component buildFooter(Player player) {
        TextComponent.Builder footer = Component.text();

        footer.append(Component.text("❤ ", NamedTextColor.RED))
                .append(Component.text("Red: ", NamedTextColor.RED))
                .append(Component.text(LiveSystem.Team.get("red").getLifes(), NamedTextColor.WHITE))
                .append(Component.text("  ", NamedTextColor.GRAY))
                .append(Component.text("❤ ", NamedTextColor.BLUE))
                .append(Component.text("Blue: ", NamedTextColor.BLUE))
                .append(Component.text(LiveSystem.Team.get("blue").getLifes(), NamedTextColor.WHITE));

        if (LiveSystem.Teams >= 3) {
            footer.append(Component.newline())
                    .append(Component.text("❤ ", NamedTextColor.GREEN))
                    .append(Component.text("Green: ", NamedTextColor.GREEN))
                    .append(Component.text(LiveSystem.Team.get("green").getLifes(), NamedTextColor.WHITE))
                    .append(Component.text("  ", NamedTextColor.GRAY));

            if (LiveSystem.Teams >= 4) {
                footer.append(Component.text("❤ ", NamedTextColor.YELLOW))
                        .append(Component.text("Yellow: ", NamedTextColor.YELLOW))
                        .append(Component.text(LiveSystem.Team.get("yellow").getLifes(), NamedTextColor.WHITE));
            }
        } else if (LiveSystem.Teams >= 4) {
            footer.append(Component.newline())
                    .append(Component.text("❤ ", NamedTextColor.YELLOW))
                    .append(Component.text("Yellow: ", NamedTextColor.YELLOW))
                    .append(Component.text(LiveSystem.Team.get("yellow").getLifes(), NamedTextColor.WHITE));
        }

        footer.append(Component.newline())
                .append(Component.text("Your Team: ", NamedTextColor.GRAY))
                .append(getTeamComponent(player))
                .append(Component.newline())
                .append(SEPARATOR);

        return footer.build();
    }

    private static Component getTeamComponent(Player player) {
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);
        if (customPlayer == null || customPlayer.getTeam() == null) {
            return Component.text("nicht ausgewählt", NamedTextColor.GRAY);
        }

        String teamName = customPlayer.getTeam().getName();
        return switch (teamName) {
            case "red" -> Component.text("Rot", NamedTextColor.RED);
            case "blue" -> Component.text("Blau", NamedTextColor.BLUE);
            case "yellow" -> Component.text("Gelb", NamedTextColor.YELLOW);
            case "green" -> Component.text("Grün", NamedTextColor.GREEN);
            default -> Component.text("Spectator", NamedTextColor.GRAY);
        };
    }
}
