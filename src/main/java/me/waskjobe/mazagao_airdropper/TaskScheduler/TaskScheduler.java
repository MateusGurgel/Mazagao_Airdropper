package me.waskjobe.mazagao_airdropper;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Server;

@FunctionalInterface
interface VoidFunction {
    void execute();
}

public class TaskScheduler implements TaskSchedulerInterface {
    private final Plugin plugin;
    private final Server server;

    public TaskScheduler(Plugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    public BukkitTask scheduleTask(long delay, long period, VoidFunction task) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.execute();
            }
        }.runTaskTimer(plugin, delay, period);
    }
}
