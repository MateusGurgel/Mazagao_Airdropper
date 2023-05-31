package me.waskjobe.mazagao_airdropper.GodlyItems;

import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static me.waskjobe.mazagao_airdropper.ProbabilityUtils.getRandomInt;

public class Bomber implements Listener {

    public Plugin plugin;
    private NamespacedKey key;
    private String itemName;


    NamespacedKey getKey(){
        return key;
    }
    void setKey(String keyName){
        this.key = new NamespacedKey(plugin, keyName);
    }
    String getItemName(){
        return this.itemName;
    }
    void setItemName(String itemName){
        this.itemName = itemName;
    }

    public Bomber(Plugin plugin){
        this.plugin = plugin;

        setItemName("Bombing Caller");
        setKey("BombingCaller");
    }

    public ItemStack getBombingCaller() {

        ItemStack bombCallerItem = new ItemStack(Material.SNOWBALL);
        ItemMeta bombingCallerMeta = bombCallerItem.getItemMeta();

        bombingCallerMeta.setDisplayName(getItemName());

        PersistentDataContainer data = bombingCallerMeta.getPersistentDataContainer();
        data.set(getKey(), PersistentDataType.STRING, "true");

        bombCallerItem.setItemMeta(bombingCallerMeta);

        return bombCallerItem;
    }

     void bombardment(Location location) {

        World world = location.getWorld();
        Integer yOffset = 50;
        Integer amountOfTnt = 400;
        Integer range = 40;

        BukkitRunnable bombardmentTask = new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {

                count++;

                if (count >= amountOfTnt) {
                    cancel();
                }

                int xRange = ProbabilityUtils.getRandomInt(-range,range);
                int zRange = ProbabilityUtils.getRandomInt(-range,range);

                Location tntLocation = location.clone();
                Integer height = world.getHighestBlockYAt(tntLocation) + yOffset;
                tntLocation.add(xRange, 0, zRange);
                tntLocation.setY(height);

                TNTPrimed tnt = (TNTPrimed) world.spawnEntity(tntLocation,EntityType.PRIMED_TNT);
                tnt.setFuseTicks(getRandomInt(20,37));
                tnt.setGlowing(true);
                tnt.setIsIncendiary(true);
                tnt.setVelocity(new Vector(0,-2,0));
            }
        };

        bombardmentTask.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e){
        if(e.getEntity().getType() == EntityType.SNOWBALL){

            Snowball bombCaller = (Snowball) e.getEntity();
            ItemStack item = bombCaller.getItem();
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();

            if (data.has(getKey(), PersistentDataType.STRING)) {
                bombardment(bombCaller.getLocation());
            }
        }
    }


}
