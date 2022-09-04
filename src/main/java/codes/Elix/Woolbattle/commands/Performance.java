// This class was created by Elix on 01.09.22


package codes.Elix.Woolbattle.commands;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.text.DecimalFormat;

public class Performance implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("performance"))
        {
            if (sender instanceof Player && !sender.hasPermission("memcheck.mem"))
            {
                sender.sendMessage("You do not have permission for this command");
                return true;
            }

            final DecimalFormat df1 = new DecimalFormat("#.0");
            final DecimalFormat df0 = new DecimalFormat("#");

            // TPS
            /*
            // Hook in to Essentials
            Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");

            double tps = -1.0;
            ChatColor tpsColor = ChatColor.RED;

            if (essentials != null && essentials.isEnabled())
            {
                IEssentials ess = (IEssentials)essentials;
                EssentialsTimer timer = ess.getTimer();
                if (timer != null)
                {
                    tps = timer.getAverageTPS();

                    if (tps >= 18.0)
                    {
                        tpsColor = ChatColor.GREEN;
                    }
                    else if (tps >= 15.0)
                    {
                        tpsColor = ChatColor.YELLOW;
                    }
                    else
                    {
                        tpsColor = ChatColor.RED;
                    }
                }
            }
                alternative: double[] tps = Bukkit.getServer().getTPS();
             */

            // CPU load

            OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            double cpuLoad = os.getProcessCpuLoad(); // may be < 0.0


            StringBuilder cpu = new StringBuilder();
            // if (tps >= 0.0)
            // {
            //      cpu.append(ChatColor.GOLD + "TPS: " + tpsColor + df1.format(tps) + " ");
            // }
            if (cpuLoad >= 0.0)
            {
                cpu.append(ChatColor.GOLD + "CPU: " + ChatColor.RED + df0.format(cpuLoad*100.0) + "% ");
            }

            // Chunks

            int chunks = 0;
            for (World world : Bukkit.getServer().getWorlds())
            {
                chunks += world.getLoadedChunks().length;
            }
            cpu.append(ChatColor.GOLD + "Chunks: " + ChatColor.RED + chunks);

            // Heap

            long freeMemory  = Runtime.getRuntime().freeMemory();
            long maxMemory   = Runtime.getRuntime().maxMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();

            long used = totalMemory - freeMemory;
            long free = maxMemory - used;

            String heap = ChatColor.GOLD + "Heap Used: " + ChatColor.RED + used/1048576L        + " MB (" + (used*100L)/maxMemory + "%)" +
                    ChatColor.GOLD + " Free: "     + ChatColor.RED + free/1048576L        + " MB" +
                    ChatColor.GOLD + " Alloc: "    + ChatColor.RED + totalMemory/1048576L + " MB";

            // Metaspace

            long usedMetaspace  = 0;
            long allocMetaspace = 0;
            long maxMetaspace   = -1;
            long freeMetaspace  = 0; // max - used

            for (MemoryPoolMXBean memoryMXBean : ManagementFactory.getMemoryPoolMXBeans())
            {
                if ("Metaspace".equals(memoryMXBean.getName()))
                {
                    usedMetaspace = memoryMXBean.getUsage().getUsed();
                    allocMetaspace = memoryMXBean.getUsage().getCommitted();
                    maxMetaspace   = memoryMXBean.getUsage().getMax(); // may be -1
                    break;
                }
            }

            StringBuilder meta = new StringBuilder(ChatColor.GOLD + "Metaspace Used: " + ChatColor.RED + usedMetaspace/1048576L + " MB");
            if (maxMetaspace > 0)
            {
                meta.append(" (" + (usedMetaspace*100L)/maxMetaspace + "%)");
                freeMetaspace = maxMetaspace - usedMetaspace;
                meta.append(ChatColor.GOLD + " Free: " + ChatColor.RED + freeMetaspace/1048576L + " MB");
            }
            meta.append(ChatColor.GOLD + " Alloc: " + ChatColor.RED + allocMetaspace/1048576L + " MB");

            // Output messages

            String[] strArray = new String[] {cpu.toString(), heap, meta.toString()};

            sender.sendMessage(strArray);

            // Normal return
            return true;
        }
        return false;
    }
}
