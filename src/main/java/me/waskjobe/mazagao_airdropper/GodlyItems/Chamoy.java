package me.waskjobe.mazagao_airdropper.GodlyItems;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Chamoy extends Bomber implements Listener {

    public Chamoy(Plugin plugin) {
        super(plugin);
        setItemName("Chamoy Caller");
        setKey("ChamoyCaller");
    }

    public void applySlownessEffect(Location centerLocation, double radius) {
        World world = centerLocation.getWorld();
        double radiusSquared = radius * radius;

        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity) {
                Location entityLocation = entity.getLocation();
                if (centerLocation.distanceSquared(entityLocation) <= radiusSquared) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOW, 600, 3, false);
                    livingEntity.addPotionEffect(slownessEffect);
                }
            }
        }
    }

    @Override
     void bombardment(Location location) {

        World world = location.getWorld();
        Integer loops = 30;
        Integer amountOfTntPerLoop = 3;
        Integer effectRange = 40;

        applySlownessEffect(location, effectRange);

        BukkitRunnable bombardmentTask = new BukkitRunnable() {
            private Location superiorEpicenter = location.clone();
            private Location inferiorEpicenter = location.clone();
            private int count = 0;

            @Override
            public void run() {

                count++;

                if (count >= loops) {
                    cancel();
                }

                for (double angle = 0; angle < 360; angle += 40) {

                    double radians =  Math.toRadians(angle + ((count % 2) * 45));

                    Vector direction = new Vector(Math.cos(radians), 0, Math.sin(radians));


                    TNTPrimed tnt = (TNTPrimed) world.spawnEntity(superiorEpicenter, EntityType.PRIMED_TNT);
                    tnt.setVelocity(direction);
                }

                for (int i = 0; i < amountOfTntPerLoop; i++){
                    world.spawnEntity(inferiorEpicenter, EntityType.PRIMED_TNT);
                    ExplosiveMinecart tntCart = (ExplosiveMinecart) world.spawnEntity(inferiorEpicenter, EntityType.MINECART_TNT);
                    tntCart.setFuseTicks(10);
                }


                inferiorEpicenter.add(0,-0.1,0);
                superiorEpicenter.add(0, 0.8, 0);
            }
        };

        bombardmentTask.runTaskTimer(plugin, 2, 0);


    }


}
