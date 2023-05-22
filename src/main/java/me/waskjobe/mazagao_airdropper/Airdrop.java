package me.waskjobe.mazagao_airdropper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

public class Airdrop {

    private static int getRandomInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private static Location generateAirDropLocation(World world){
        Location location = new Location(world, 0, 0, 0);

        //getting the cfg
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig();

        int airdropRange = config.getInt("settings.airdrop_range");
        int airdropBaseLocationX = config.getInt("settings.airdrop_base_location.x");
        int airdropBaseLocationZ = config.getInt("settings.airdrop_base_location.z");

        int xRange = getRandomInt(-airdropRange,airdropRange);
        int zRange = getRandomInt(-airdropRange,airdropRange);

        int randomX = xRange + airdropBaseLocationX;
        int randomZ = zRange + airdropBaseLocationZ;

        location.setX(randomX);
        location.setZ(randomZ);

        location.setY(world.getHighestBlockYAt(randomX, location.getBlockY()) + 1);

        return location;
    }

    public static void call(World world){

        //Get Location
        Location location = generateAirDropLocation(world);

        System.out.println(location.toString());

        //Get Loot


        //hit that cord with thunders

        //place the chest

        //place the loot on the Chest

        //Place the Chest

        //Send Message
    }

}
