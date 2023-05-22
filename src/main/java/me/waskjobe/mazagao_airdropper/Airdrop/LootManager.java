package me.waskjobe.mazagao_airdropper.Airdrop;

import me.waskjobe.mazagao_airdropper.ConfigManager;
import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LootManager {
    private static List<ItemStack> getItems(FileConfiguration config, String sectionPath) throws IllegalArgumentException {
        List<ItemStack> items = new ArrayList<>();

        ConfigurationSection section = config.getConfigurationSection(sectionPath);
        Set<String> keys = section.getKeys(false);

        for (String key : keys) {
            String itemPath = sectionPath + "." + key;

            if (!config.isConfigurationSection(itemPath)) {
                continue;
            }

            String materialName = config.getString(itemPath + ".material");
            int amount = config.getInt(itemPath + ".amount");

            if (materialName == null) {
                throw new IllegalArgumentException("Missing 'material' value for item: " + key);
            }

            // Create the ItemStack using the material name and amount
            Material material = Material.getMaterial(materialName);
            if (material == null) {
                throw new IllegalArgumentException("Invalid material: " + materialName);
            }
            ItemStack itemStack = new ItemStack(material, amount);

            items.add(itemStack);
        }

        return items;
    }

    public static Inventory generateAirdropChestLoot(){

        //getting the cfg
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig();

        List<ItemStack> commonItems = getItems(config, "settings.drops.common");
        int commonItemsAmount = config.getInt("settings.drops.common_items_amount");
        int commonItemsChance = config.getInt("settings.drops.common_items_chance");

        List<ItemStack> rareItems = getItems(config, "settings.drops.rare");
        int rareItemsAmount = config.getInt("settings.drops.rare_items_amount");
        int rareItemsChance = config.getInt("settings.drops.rare_items_chance");

        List<ItemStack> epicItems = getItems(config, "settings.drops.epic");
        int epicItemsAmount = config.getInt("settings.drops.epic_items_amount");
        int epicItemsChance = config.getInt("settings.drops.epic_items_chance");

        List<ItemStack> legendaryItems = getItems(config, "settings.drops.legendary");
        int legendaryItemsAmount = config.getInt("settings.drops.legendary_items_amount");
        int legendaryItemsChance = config.getInt("settings.drops.legendary_items_chance");

        //Creating the loot inventory
        Inventory loot = Bukkit.createInventory(null, InventoryType.CHEST, "Loot Chest");

        int inventorySize = loot.getSize();

        //Choose Random Common Items
        for (int i = 0; i < commonItemsAmount; i++) {

            if (ProbabilityUtils.getProbability(commonItemsChance)){
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, commonItems.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, commonItems.get(randomItemIndex));
            }
        }

        //Choose Random rare Items
        for (int i = 0; i < rareItemsAmount; i++) {

            if (ProbabilityUtils.getProbability(rareItemsChance)){
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, rareItems.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, rareItems.get(randomItemIndex));
            }
        }

        //Choose Random Epic Items
        for (int i = 0; i < epicItemsAmount; i++) {

            if (ProbabilityUtils.getProbability(epicItemsChance)){
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, epicItems.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, epicItems.get(randomItemIndex));
            }
        }

        //Choose Legendary legendary Items
        for (int i = 0; i < legendaryItemsAmount; i++) {

            if (ProbabilityUtils.getProbability(legendaryItemsChance)){
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, legendaryItems.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, legendaryItems.get(randomItemIndex));
            }
        }

        return loot;
    }

}
