package me.waskjobe.mazagao_airdropper.GodlyItems;

import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static me.waskjobe.mazagao_airdropper.ProbabilityUtils.getRandomInt;

public class AnvilBomber extends Bomber implements Listener {

    public AnvilBomber(Plugin plugin) {
        super(plugin);
        setItemName("Anvil Bomber Caller");
        setKey("AnvilCaller");
    }

    public void applySlownessEffect(Location centerLocation, double radius, int size, int yOffset) {
        World world = centerLocation.getWorld();
        double radiusSquared = radius * radius;

        for (Entity entity : world.getEntities()) {
            if (entity instanceof Player player) {
                Location entityLocation = entity.getLocation();
                if (centerLocation.distanceSquared(entityLocation) <= radiusSquared) {

                    Integer height = world.getHighestBlockYAt(centerLocation) + yOffset;

                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {

                            PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOW, 200, 16, false);
                            player.addPotionEffect(slownessEffect);

                            Location anvilLocation = entityLocation.clone();
                            anvilLocation.add(i - (int) ((double) size /2), 0, j - (int) ((double) size /2));
                            anvilLocation.setY(height);

                            Block anvil = anvilLocation.getBlock();
                            anvil.setType(Material.DAMAGED_ANVIL);
                        }

                    }


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

                applySlownessEffect(location, range, 21, yOffset);
            }
        };

        bombardmentTask.runTaskTimer(plugin, 0, 20);
    }


}
