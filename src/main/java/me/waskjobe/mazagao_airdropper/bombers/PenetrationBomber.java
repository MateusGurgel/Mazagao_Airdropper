package me.waskjobe.mazagao_airdropper.bombers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PenetrationBomber extends Bomber {

    public PenetrationBomber(Plugin plugin) {
        super(plugin);
        setItemName("Penetration Bomber Caller");
        setKey("PenetrationBomberCaller");
    }

    @Override
    void bombardment(Location location) {

        World world = location.getWorld();
        Integer amountOfTnt = 100;

        BukkitRunnable bombardmentTask = new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {

                count++;

                if (count >= amountOfTnt) {
                    cancel();
                }

                world.createExplosion(location, 10);
                location.add(0,-1.3,0);
            }
        };

        bombardmentTask.runTaskTimer(plugin, 0, 0);
    }


}
