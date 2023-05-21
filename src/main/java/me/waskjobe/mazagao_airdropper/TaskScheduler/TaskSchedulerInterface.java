package me.waskjobe.mazagao_airdropper;
import org.bukkit.scheduler.BukkitTask;

@FunctionalInterface
public interface TaskSchedulerInterface {
    BukkitTask scheduleTask(long delay, long period, VoidFunction task);
}
