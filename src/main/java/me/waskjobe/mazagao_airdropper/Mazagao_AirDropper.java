package me.waskjobe.mazagao_airdropper;

import me.waskjobe.mazagao_airdropper.TaskScheduler.TaskSchedulerInterface;
import me.waskjobe.mazagao_airdropper.TaskScheduler.TaskScheduler;
import me.waskjobe.mazagao_airdropper.bombers.factory.BomberFactory;
import me.waskjobe.mazagao_airdropper.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import me.waskjobe.mazagao_airdropper.Airdrop.Airdrop;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public final class Mazagao_AirDropper extends JavaPlugin {

    private final List<BukkitTask> tasks = new ArrayList<>();

    @Override
    public void onEnable() {

        //Plugin startup logic
        System.out.println("setting up configManager");

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.setup(this);

        System.out.println("Registering Events");

        BomberFactory bomberFactory = new BomberFactory(this);
        bomberFactory.registerBombers();

        //getting airdrop period
        FileConfiguration config = configManager.getConfig();
        int airdropPeriod = config.getInt("settings.airdrop_period");

        //Scheduling airdrop tasks
        System.out.println("Scheduling airdrop task");

        TaskSchedulerInterface taskScheduler = new TaskScheduler(this, getServer());
        World overworld = Bukkit.getWorld("world");
        Airdrop airdropManager = new Airdrop(this);
        BukkitTask airdropTask = taskScheduler.scheduleTask(airdropPeriod, airdropPeriod, () -> airdropManager.task(overworld));
        tasks.add(airdropTask);

        System.out.println("Mazagão Dropper is enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Canceling all the services");

        for (BukkitTask task:tasks) {
            task.cancel();
        }

        System.out.println("Mazagão Dropper is disabled");
    }

}
