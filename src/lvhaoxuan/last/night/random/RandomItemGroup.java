package lvhaoxuan.last.night.random;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.last.night.LastNight;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class RandomItemGroup {

    List<RandomItem> items = new ArrayList<>();
    int maxSize;
    String name;
    String saveFile;

    public RandomItemGroup(int maxSize, String name, String saveFile) {
        this.maxSize = maxSize;
        this.name = name;
        this.saveFile = saveFile;
    }

    public void setItems(List<RandomItem> items) {
        this.items = items;
    }

    public void addItem(String item, int amount, double probability) {
        RandomItem di = new RandomItem(item, amount, probability);
        items.add(di);
        save();
    }

    public List<ItemStack> getRandomItems() {
        List<ItemStack> ret = new ArrayList<>();
        int rnd = (int) getRandom(1, maxSize + 1);
        for (int i = 0; i < rnd; i++) {
            RandomItem di = getRandomItem();
            if (di != null) {
                ret.add(di.toItemStack());
            }
        }
        return ret;
    }

    public RandomItem getRandomItem() {
        double maxProbability = 0;
        for (RandomItem p : items) {
            maxProbability += p.probability;
        }
        double randomNumber = getRandom(0, maxProbability);
        double min = 0;
        for (RandomItem p : items) {
            if (min + p.probability >= randomNumber && randomNumber >= min) {
                return p;
            }
            min += p.probability;
        }
        return null;
    }

    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        }
        return Math.random() * (max - min) + min;
    }

    public void save() {
        File file1 = new File(LastNight.plugin.getDataFolder(), saveFile);
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            config1.set(name + ".maxSize", maxSize);
            List<String> itemss = new ArrayList<>();
            for (RandomItem di : items) {
                itemss.add(di.toString());
            }
            config1.set(name + ".items", itemss);
            try {
                config1.save(file1);
            } catch (IOException ex) {
            }
        } else {
            LastNight.plugin.saveResource(saveFile, true);
            save();
        }
    }
}
