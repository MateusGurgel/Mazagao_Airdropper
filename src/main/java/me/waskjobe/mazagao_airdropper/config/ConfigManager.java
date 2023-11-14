package me.waskjobe.mazagao_airdropper.config;

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
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static ConfigManager instance;
    private FileConfiguration config;

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public void setup(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public List<ItemStack> loadItems(String sectionPath) throws IllegalArgumentException {
        List<ItemStack> items = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection(sectionPath);

        if (section != null) {
            for (String key : section.getKeys(false)) {
                String itemPath = sectionPath + "." + key;

                if (config.isConfigurationSection(itemPath)) {
                    items.add(createItemFromConfig(config, itemPath));
                }
            }
        }

        return items;
    }

    private ItemStack createItemFromConfig(FileConfiguration config, String itemPath) throws IllegalArgumentException {
        String materialName = config.getString(itemPath + ".material");
        int amount = config.getInt(itemPath + ".amount");

        if (materialName == null) {
            throw new IllegalArgumentException("Missing 'material' value for item: " + itemPath);
        }

        Material material = Material.matchMaterial(materialName);

        if (material == null) {
            throw new IllegalArgumentException("Invalid material: " + materialName);
        }

        ItemStack itemStack = new ItemStack(material, amount);
        setItemMetaFromConfig(config, itemPath, itemStack);

        return itemStack;
    }

    private void setItemMetaFromConfig(FileConfiguration config, String itemPath, ItemStack itemStack) {
        ConfigurationSection metaSection = config.getConfigurationSection(itemPath + ".meta");

        if (metaSection != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if(itemMeta == null){
                return;
            }

            setEnchantmentsFromConfig(metaSection.getStringList("enchantments"), itemMeta);
            setPotionEffectsFromConfig(metaSection.getStringList("potionEffects"), (PotionMeta) itemMeta);
            itemMeta.setLore(metaSection.getStringList("lore"));

            itemStack.setItemMeta(itemMeta);
        }
    }

    private void setEnchantmentsFromConfig(List<String> enchantments, ItemMeta itemMeta) {
        enchantments.forEach(enchantmentStr -> {
            String[] enchantmentParts = enchantmentStr.split(":");
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentParts[0]));

            if (enchantment != null) {
                int level = Integer.parseInt(enchantmentParts[1]);
                itemMeta.addEnchant(enchantment, level, true);
            }
        });
    }

    private void setPotionEffectsFromConfig(List<String> potionEffects, PotionMeta potionMeta) {
        potionEffects.forEach(potionEffectStr -> {
            String[] effectParts = potionEffectStr.split(":");
            PotionEffectType effectType = PotionEffectType.getByName(effectParts[0]);

            if (effectType != null) {
                int duration = Integer.parseInt(effectParts[1]);
                int amplifier = Integer.parseInt(effectParts[2]);
                potionMeta.addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
            }
        });
    }
}

