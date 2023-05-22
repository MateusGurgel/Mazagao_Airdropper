package me.waskjobe.mazagao_airdropper;

import me.waskjobe.mazagao_airdropper.TaskScheduler.TaskScheduler;
import me.waskjobe.mazagao_airdropper.TaskScheduler.TaskSchedulerInterface;
import me.waskjobe.mazagao_airdropper.TaskScheduler.VoidFunction;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Mazagao_AirDropper extends JavaPlugin {

    private BukkitTask airdropTask;

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("Initializing...");

        System.out.println("Scheduling Tasks...");

        VoidFunction airdropLogic = () -> {
            System.out.println("Teste");
        };

        TaskSchedulerInterface taskScheduler = new TaskScheduler(this, getServer());

        airdropTask = taskScheduler.scheduleTask(0, 24000, airdropLogic);

        System.out.println("Mazagão Dropper is enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Canceling all the services");

        airdropTask.cancel();

        System.out.println("Mazagão Dropper is disabled");
    }

}
