package codes.Elix.Woolbattle.util;

import codes.Elix.Woolbattle.gamestates.GameStateManager;
import codes.Elix.Woolbattle.gamestates.LobbyState;
import codes.Elix.Woolbattle.items.MapVoting;
import codes.Elix.Woolbattle.main.Woolbattle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyTabList {

    private static final Map<Player, Boolean> activePlayers = new HashMap<>();
    private static final Component SEPARATOR = Component.text("━━━━━━━━━━━━━━━━", NamedTextColor.GRAY);

    private static String getMapDisplayName() {
        String fileName = MapVoting.getCurrentWinningMap();
        List<SchematicManager.SchematicInfo> schematics = SchematicManager.getAvailableSchematics();

        for (SchematicManager.SchematicInfo schematic : schematics) {
            if (schematic.getFileName().equals(fileName)) {
                return schematic.getDisplayName();
            }
        }

        return fileName.replace(".schem", "");
    }

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

    public static void remove(Player player) {
        activePlayers.remove(player);
        if (player.isOnline()) {
            player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty());
        }
    }

    private static void updatePlayer(Player player) {
        if (!player.isOnline()) return;
        if (!(GameStateManager.getCurrentGameState() instanceof LobbyState lobbyState)) return;

        Component header = buildHeader();
        Component footer = buildFooter(lobbyState);
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    private static Component buildHeader() {
        return Component.text()
                .append(Component.text("WOOLBATTLE", NamedTextColor.WHITE).decorate(TextDecoration.BOLD))
                .append(Component.newline())
                .append(SEPARATOR)
                .append(Component.newline())
                .append(Component.text("Mini-Game Server", NamedTextColor.AQUA))
                .build();
    }

    private static Component buildFooter(LobbyState lobbyState) {
        return Component.text()
                .append(Component.text("Players: ", NamedTextColor.GRAY))
                .append(Component.text(Woolbattle.players.size(), NamedTextColor.GREEN))
                .append(Component.text("/", NamedTextColor.DARK_GRAY))
                .append(Component.text(LobbyState.MAX_PLAYERS, NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.text("Map: ", NamedTextColor.GRAY))
                .append(Component.text(getMapDisplayName(), NamedTextColor.DARK_AQUA))
                .append(Component.newline())
                .append(Component.text("Starting in: ", NamedTextColor.GRAY))
                .append(Component.text(lobbyState.getCountdown().getSeconds() + "s", NamedTextColor.YELLOW))
                .append(Component.newline())
                .append(SEPARATOR)
                .build();
    }
}
