package me.waskjobe.mazagao_airdropper.bombers.factory;

import me.waskjobe.mazagao_airdropper.ProbabilityUtils;
import me.waskjobe.mazagao_airdropper.bombers.AnvilBomber;
import me.waskjobe.mazagao_airdropper.bombers.Bomber;
import me.waskjobe.mazagao_airdropper.bombers.Chamoy;
import me.waskjobe.mazagao_airdropper.bombers.PenetrationBomber;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BomberFactory {

    private final List<Bomber> bombers = new ArrayList<>();
    private final Plugin plugin;

    public BomberFactory(Plugin plugin) {
        this.plugin = plugin;

        bombers.add(new Bomber(plugin));
        bombers.add(new Chamoy(plugin));
        bombers.add(new PenetrationBomber(plugin));
        bombers.add(new AnvilBomber(plugin));
    }

    public void registerBombers() {
        for (Bomber bomber:bombers) {
            plugin.getServer().getPluginManager().registerEvents( bomber, plugin );
        }
    }

    public ItemStack getRandomBomber() {
        int randomItemIndex = ProbabilityUtils.getRandomInt(0, 128) % bombers.size();
        return bombers.get(randomItemIndex).getBombingCaller();
    }

    public List<ItemStack> getAllBombers(){
        List<ItemStack> getAllBombers =  new ArrayList<>();

        for (Bomber bomber: bombers) {
            getAllBombers.add(bomber.getBombingCaller());
        }

        return getAllBombers;
    }

}
