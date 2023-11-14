package me.waskjobe.mazagao_airdropper.Airdrop;
import me.waskjobe.mazagao_airdropper.config.ConfigManager;
import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class Airdrop {

    private final LootManager lootManager;

    public Airdrop(Plugin plugin) {
        this.lootManager = new LootManager(plugin);
    }

    private static final FileConfiguration config = ConfigManager.getInstance().getConfig();

    private Location generateAirDropLocation(World world){
        Location location = new Location(world, 0, 0, 0);

        int airdropRange = config.getInt("settings.airdrop_range");
        int airdropBaseLocationX = config.getInt("settings.airdrop_base_location.x");
        int airdropBaseLocationZ = config.getInt("settings.airdrop_base_location.z");

        int xRange = ProbabilityUtils.getRandomInt(-airdropRange,airdropRange);
        int zRange = ProbabilityUtils.getRandomInt(-airdropRange,airdropRange);

        int randomX = xRange + airdropBaseLocationX;
        int randomZ = zRange + airdropBaseLocationZ;

        location.setX(randomX);
        location.setZ(randomZ);

        location.setY(world.getHighestBlockYAt(randomX, randomZ) + 1);

        return location;
    }

    public void task(World world){
        int airdropChance = config.getInt("settings.airdrop_chance");
        int minAmountOfPlayers = config.getInt("settings.min_amount_of_players");
        int connectedPlayers = Bukkit.getOnlinePlayers().size();

        if (!ProbabilityUtils.getProbability(airdropChance) || connectedPlayers < minAmountOfPlayers){
            return;
        }

        call(world);
    }

    public void call(World world){

        //Get Location
        Location location = generateAirDropLocation(world);

        //hit that cord with thunders
        world.strikeLightningEffect(location);

        //place the chest
        Block chestBlock = location.getBlock();
        chestBlock.setType(Material.CHEST);

        //get chest inventory
        BlockState chestState = chestBlock.getState();
        Chest chest = (Chest) chestState;
        Inventory ChestInventory = chest.getInventory();

        //place the loot on the Chest
        Inventory loot = lootManager.generateAirdropChestLoot();
        ChestInventory.setContents(loot.getContents());

        //Send Message
        String airdropMessage = config.getString("settings.airdrop_message");

        Bukkit.broadcastMessage(ChatColor.YELLOW
                + airdropMessage + ": X: "
                + location.getBlockX() + " Z: "
                + location.getBlockZ());
    }

}
