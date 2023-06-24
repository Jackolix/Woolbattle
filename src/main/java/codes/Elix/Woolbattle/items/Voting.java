package codes.Elix.Woolbattle.items;

import codes.Elix.Woolbattle.util.Console;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Voting {

    public static HashMap<Player, Integer> voted = new HashMap<>();
    public static ArrayList<Integer> numbers = new ArrayList<>();
    public static List<Integer> six = new ArrayList<>();
    public static List<Integer> twelve = new ArrayList<>();
    public static List<Integer> eighteen = new ArrayList<>();
    public static List<Integer> twentythree = new ArrayList<>();

    public static void vote(Player player, int number) {
        voted.put(player, number);
        update();
    }

    public static int winner() {
        int largest = Collections.max(Arrays.asList(six.size(), twelve.size(), eighteen.size(),
                twentythree.size()));
        if (six.size() == largest) {
            largest = 6;
        } else if (twelve.size() == largest) {
            largest = 12;
        } else if (eighteen.size() == largest) {
            largest = 18;
        } else if (twentythree.size() == largest) {
            largest = 23;
        }

        Console.send("");
        Console.send(Component.text("-------- Life Voting --------", NamedTextColor.GOLD));
        Console.send(Component.text(" 6: " + six.size(), NamedTextColor.WHITE));
        Console.send(Component.text("12: " + twelve.size(), NamedTextColor.WHITE));
        Console.send(Component.text("18: " + eighteen.size(), NamedTextColor.WHITE));
        Console.send(Component.text("23: " + twentythree.size(), NamedTextColor.WHITE));

        return largest;
    }

    public static void update() {
        /* For MultiThreading, but to slow
        Bukkit.getScheduler().runTaskAsynchronously(Woolbattle.getPlugin(), new Runnable() {
            @Override
            public void run() {
                voted.forEach((p, n) -> {
                    numbers.clear();
                    numbers.add(n);
                });

                six.clear();
                twelve.clear();
                eighteen.clear();
                twentythree.clear();

                six = numbers.stream()
                        .filter(item -> item.equals(6))
                        .collect(Collectors.toList());
                twelve = numbers.stream()
                        .filter(item -> item.equals(12))
                        .collect(Collectors.toList());
                eighteen = numbers.stream()
                        .filter(item -> item.equals(18))
                        .collect(Collectors.toList());
                twentythree = numbers.stream()
                        .filter(item -> item.equals(23))
                        .collect(Collectors.toList());
            }
        });
         */

        numbers.clear();
        six.clear();
        twelve.clear();
        eighteen.clear();
        twentythree.clear();

        voted.forEach((p, n) -> {
            if (p.hasPermission("Woolbattle.OP")) {
                numbers.add(n);
                numbers.add(n);
            } else //if (p.hasPermission("Woolbattle.VIP"))
                numbers.add(n);
        });

        six = numbers.stream()
                .filter(item -> item.equals(6))
                .collect(Collectors.toList());
        twelve = numbers.stream()
                .filter(item -> item.equals(12))
                .collect(Collectors.toList());
        eighteen = numbers.stream()
                .filter(item -> item.equals(18))
                .collect(Collectors.toList());
        twentythree = numbers.stream()
                .filter(item -> item.equals(23))
                .collect(Collectors.toList());
    }
}
