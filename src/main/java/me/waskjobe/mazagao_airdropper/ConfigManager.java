package me.waskjobe.mazagao_airdropper;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//TODO refactor that >:(

public class ConfigManager {
    private static ConfigManager instance;
    private File configFile;
    private FileConfiguration config;

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public void setup(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public List<ItemStack> getItems(FileConfiguration config, String sectionPath) throws IllegalArgumentException {

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

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

