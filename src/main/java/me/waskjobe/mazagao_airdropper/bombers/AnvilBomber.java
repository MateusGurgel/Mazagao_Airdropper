package me.waskjobe.mazagao_airdropper.bombers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilBomber extends Bomber {

    public AnvilBomber(Plugin plugin) {
        super(plugin);
        setItemName("Anvil Bomber Caller");
        setKey("AnvilCaller");
    }

    public void placeAnvilRoof(Location centerLocation, double radius, int size, int yOffset) {
        World world = centerLocation.getWorld();
        double radiusSquared = radius * radius;

        int height = world.getHighestBlockYAt(centerLocation) + yOffset;

        for (Entity entity : world.getEntities()) {

            if (!(entity instanceof Player player)) {
                continue;
            }

            Location entityLocation = entity.getLocation();
            if (centerLocation.distanceSquared(entityLocation) > radiusSquared){
                continue;
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {

                    PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOW, 200, 16, false);
                    player.addPotionEffect(slownessEffect);

                    Location anvilLocation = entityLocation.clone();

                    int halfSize = size / 2;
                    anvilLocation.add(i - halfSize, 0, j - halfSize);

                    anvilLocation.setY(height);

                    Block anvil = anvilLocation.getBlock();
                    anvil.setType(Material.DAMAGED_ANVIL);
                }

            }
        }
    }

    @Override
    void bombardment(Location location) {

        Integer yOffset = 40;
        Integer amountOfTnt = 3;
        Integer range = 40;

        BukkitRunnable bombardmentTask = new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {

                count++;

                if (count > amountOfTnt) {
                    cancel();
                }

                placeAnvilRoof(location, range, 21, yOffset);
            }
        };

        bombardmentTask.runTaskTimer(plugin, 0, 20);
    }


}
