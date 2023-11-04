package me.waskjobe.mazagao_airdropper.Airdrop;

import me.waskjobe.mazagao_airdropper.ConfigManager;
import me.waskjobe.mazagao_airdropper.GodlyItems.AnvilBomber;
import me.waskjobe.mazagao_airdropper.GodlyItems.Bomber;
import me.waskjobe.mazagao_airdropper.GodlyItems.Chamoy;
import me.waskjobe.mazagao_airdropper.GodlyItems.PenetrationBomber;
import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
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
            Material material = Material.matchMaterial(materialName);
            if (material == null) {
                throw new IllegalArgumentException("Invalid material: " + materialName);
            }
            ItemStack itemStack = new ItemStack(material, amount);

            // Set item metadata (e.g., enchantments, lore, potion effects)
            ConfigurationSection metaSection = config.getConfigurationSection(itemPath + ".meta");
            if (metaSection != null) {
                ItemMeta itemMeta = itemStack.getItemMeta();

                // Set enchantments
                List<String> enchantments = metaSection.getStringList("enchantments");
                for (String enchantmentStr : enchantments) {
                    String[] enchantmentParts = enchantmentStr.split(":");
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentParts[0]));
                    if (enchantment != null) {
                        int level = Integer.parseInt(enchantmentParts[1]);
                        itemMeta.addEnchant(enchantment, level, true);
                    }
                }

                // Set lore
                List<String> lore = metaSection.getStringList("lore");
                itemMeta.setLore(lore);

                // Check if it's a potion
                if (itemMeta instanceof PotionMeta) {
                    PotionMeta potionMeta = (PotionMeta) itemMeta;

                    // Set potion effects
                    List<String> potionEffects = metaSection.getStringList("potionEffects");
                    for (String potionEffectStr : potionEffects) {
                        String[] effectParts = potionEffectStr.split(":");
                        PotionEffectType effectType = PotionEffectType.getByName(effectParts[0]);
                        if (effectType != null) {
                            int duration = Integer.parseInt(effectParts[1]);
                            int amplifier = Integer.parseInt(effectParts[2]);
                            potionMeta.addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
                        }
                    }
                }

                itemStack.setItemMeta(itemMeta);
            }

            items.add(itemStack);
        }

        return items;
    }


    public static Inventory generateAirdropChestLoot(Plugin plugin){

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

        List<ItemStack> godlyItems = new ArrayList<>();
        Bomber bomber = new Bomber(plugin);
        Chamoy chamoy = new Chamoy(plugin);
        PenetrationBomber penetrationBomber = new PenetrationBomber(plugin);
        AnvilBomber anvilBomber = new AnvilBomber(plugin);

        godlyItems.add(bomber.getBombingCaller());
        godlyItems.add(chamoy.getBombingCaller());
        godlyItems.add(penetrationBomber.getBombingCaller());
        godlyItems.add(anvilBomber.getBombingCaller());
        int godlyItemsAmount = config.getInt("settings.drops.godly_items_amount");
        int godlyItemsChance = config.getInt("settings.drops.godly_items_chance");


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

        //Choose legendary Items
        for (int i = 0; i < legendaryItemsAmount; i++) {

            if (ProbabilityUtils.getProbability(legendaryItemsChance)){
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, legendaryItems.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, legendaryItems.get(randomItemIndex));
            }
        }

        //Choose godly Items
        for (int i = 0; i < godlyItemsAmount; i++) {

            if (ProbabilityUtils.getProbability(godlyItemsChance)){
                int randomItemIndex = ProbabilityUtils.getRandomInt(0, godlyItems.size());
                int randomSlot = ProbabilityUtils.getRandomInt(0, inventorySize);
                loot.setItem(randomSlot, godlyItems.get(randomItemIndex));
            }
        }

        return loot;
    }

}
