package me.waskjobe.mazagao_airdropper;
import org.bukkit.Location;
import org.bukkit.World;

public class Airdrop {

    private static Location generateAirDropCords(World world){
        Location location = new Location(world, 0, 0, 0);

        final int randomX = 1;
        final int randomY = 1;

        location.setX(randomX);
        location.setZ(randomY);

        location.setY(world.getHighestBlockYAt(randomX, location.getBlockY()) + 1);

        return location;
    }

    public static void call(World world){

        //Get Location
        Location location = generateAirDropCords(world);

        //Get Loot


        //hit that cord with thunders

        //place the chest

        //place the loot on the Chest

        //Place the Chest

        //Send Message
    }

}
