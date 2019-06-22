package lvhaoxuan.last.night.drop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.last.night.LastNight;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class DropItemGroup {

    List<DropItem> items = new ArrayList<>();
    int maxSize;
    String name;
    String saveFile;

    public DropItemGroup(int maxSize, String name, String saveFile) {
        this.maxSize = maxSize;
        this.name = name;
        this.saveFile = saveFile;
    }

    public void setItems(List<DropItem> items) {
        this.items = items;
    }

    public void addItem(String item, int amount, double probability) {
        DropItem di = new DropItem(item, amount, probability);
        items.add(di);
        save();
    }

    public List<ItemStack> getRandomItems() {
        List<ItemStack> ret = new ArrayList<>();
        int rnd = (int) getRandom(1, maxSize + 1);
        for (int i = 0; i < rnd; i++) {
            DropItem di = getRandomItem();
            if (di != null) {
                ret.add(di.toItemStack());
            }
        }
        return ret;
    }

    public DropItem getRandomItem() {
        double maxProbability = 0;
        for (DropItem p : items) {
            maxProbability += p.probability;
        }
        double randomNumber = getRandom(0, maxProbability);
        double min = 0;
        for (DropItem p : items) {
            if (min + p.probability >= randomNumber && randomNumber >= min) {
                return p;
            }
            min += p.probability;
        }
        return null;
    }

    public static double getRandom(double min, double max) {
        if (min == max) {
            return 0;
        }
        return Math.random() * (max - min + 1) + min;
    }

    public void save() {
        File file1 = new File(LastNight.plugin.getDataFolder(), saveFile);
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            config1.set(name + ".maxSize", maxSize);
            List<String> itemss = new ArrayList<>();
            for (DropItem di : items) {
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
