package me.waskjobe.mazagao_airdropper.Airdrop;

import me.waskjobe.mazagao_airdropper.ConfigManager;
import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
import me.waskjobe.mazagao_airdropper.bombers.factory.BomberFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LootManager {

    private final BomberFactory bomberFactory;

    public LootManager(Plugin plugin) {
        this.bomberFactory = new BomberFactory(plugin);
    }

    public void chooseRandomItems(Inventory loot, int ItemsAmount, int itemsChance, List<ItemStack> items) {

        int inventorySize = loot.getSize();

        for (int i = 0; i < ItemsAmount; i++) {
            if (ProbabilityUtils.getProbability(itemsChance)) {
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, items.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, items.get(randomItemIndex));
            }
        }
    }

    public Inventory generateAirdropChestLoot(){

        //getting the cfg
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig();

        List<ItemStack> commonItems = configManager.getItems(config, "settings.drops.common");
        int commonItemsAmount = config.getInt("settings.drops.common_items_amount");
        int commonItemsChance = config.getInt("settings.drops.common_items_chance");

        List<ItemStack> rareItems = configManager.getItems(config, "settings.drops.rare");
        int rareItemsAmount = config.getInt("settings.drops.rare_items_amount");
        int rareItemsChance = config.getInt("settings.drops.rare_items_chance");

        List<ItemStack> epicItems = configManager.getItems(config, "settings.drops.epic");
        int epicItemsAmount = config.getInt("settings.drops.epic_items_amount");
        int epicItemsChance = config.getInt("settings.drops.epic_items_chance");

        List<ItemStack> legendaryItems = configManager.getItems(config, "settings.drops.legendary");
        int legendaryItemsAmount = config.getInt("settings.drops.legendary_items_amount");
        int legendaryItemsChance = config.getInt("settings.drops.legendary_items_chance");

        int godlyItemsAmount = config.getInt("settings.drops.godly_items_amount");
        int godlyItemsChance = config.getInt("settings.drops.godly_items_chance");

        //Creating the loot inventory
        Inventory loot = Bukkit.createInventory(null, InventoryType.CHEST, "Loot Chest");

        chooseRandomItems(loot, commonItemsAmount, commonItemsChance, commonItems);
        chooseRandomItems(loot, rareItemsAmount, rareItemsChance, rareItems);
        chooseRandomItems(loot, epicItemsAmount, epicItemsChance, epicItems);
        chooseRandomItems(loot, legendaryItemsAmount, legendaryItemsChance, legendaryItems);
        chooseRandomItems(loot, godlyItemsAmount, godlyItemsChance, bomberFactory.getAllBombers());

        return loot;
    }

}
