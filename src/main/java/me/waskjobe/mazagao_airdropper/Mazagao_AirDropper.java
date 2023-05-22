package me.waskjobe.mazagao_airdropper;

import me.waskjobe.mazagao_airdropper.Airdrop.Airdrop;
import me.waskjobe.mazagao_airdropper.TaskScheduler.TaskScheduler;
import me.waskjobe.mazagao_airdropper.TaskScheduler.TaskSchedulerInterface;
import me.waskjobe.mazagao_airdropper.TaskScheduler.VoidFunction;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Mazagao_AirDropper extends JavaPlugin {

    private BukkitTask airdropTask;

    @Override
    public void onEnable() {

        //Plugin startup logic
        System.out.println("Setuping");

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.setup(this);
        //Getting OverWorld
        World overworld = Bukkit.getWorld("world");

        //getting airdrop period
        FileConfiguration config = configManager.getConfig();
        int airdropPeriod = config.getInt("settings.airdrop_period");

        //Scheduling Tasks
        System.out.println("Scheduling Tasks");

        TaskSchedulerInterface taskScheduler = new TaskScheduler(this, getServer());

        airdropTask = taskScheduler.scheduleTask(airdropPeriod, airdropPeriod, () -> {
            Airdrop.task(overworld);
        });

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
