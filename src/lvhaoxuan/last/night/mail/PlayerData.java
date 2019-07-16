package lvhaoxuan.last.night.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lvhaoxuan.last.night.util.ItemSerializerUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

    public String name;
    public List<ItemStack> items;

    public PlayerData(String name, List<ItemStack> items) {
        this.name = name;
        this.items = items;
        File file = new File(MailManager.save, name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PlayerData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file);
        config1.set(name + ".items", ItemSerializerUtils.toBase64(items.toArray(new ItemStack[items.size()])));
        try {
            config1.save(file);
        } catch (IOException ex) {
        }
    }

    public PlayerData(String name) {
        this.name = name;
        File file = new File(MailManager.save, name + ".yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file);
        items = new ArrayList<>(Arrays.asList(ItemSerializerUtils.fromBase64(config1.getString(name + ".items"))));
    }

    public void setItems(ItemStack[] itemss) {
        items.clear();
        for (ItemStack item : itemss) {
            if (item != null) {
                items.add(item);
            }
        }
        File file = new File(MailManager.save, name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PlayerData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file);
        config1.set(name + ".items", ItemSerializerUtils.toBase64(items.toArray(new ItemStack[items.size()])));
        try {
            config1.save(file);
        } catch (IOException ex) {
        }
    }

    public void addItem(ItemStack item) {
        items.add(item);
        File file = new File(MailManager.save, name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PlayerData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file);
        config1.set(name + ".items", ItemSerializerUtils.toBase64(items.toArray(new ItemStack[items.size()])));
        try {
            config1.save(file);
        } catch (IOException ex) {
        }
    }

    public void removeItem(ItemStack item) {
        items.remove(item);
        File file = new File(MailManager.save, name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PlayerData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file);
        config1.set(name + ".items", ItemSerializerUtils.toBase64(items.toArray(new ItemStack[items.size()])));
        try {
            config1.save(file);
        } catch (IOException ex) {
        }
    }
}
