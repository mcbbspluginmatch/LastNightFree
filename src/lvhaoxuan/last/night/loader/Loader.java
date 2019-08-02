package lvhaoxuan.last.night.loader;

import java.io.File;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.breakableblock.BreakableBlockListener;
import lvhaoxuan.last.night.random.RandomItemGroup;
import lvhaoxuan.last.night.forge.RecipeItem;
import lvhaoxuan.last.night.gun.BulletChest;
import lvhaoxuan.last.night.gun.BulletChestManager;
import lvhaoxuan.last.night.gun.LastNightItemGun;
import lvhaoxuan.last.night.gun.Range;
import lvhaoxuan.last.night.item.LastNightItem;
import lvhaoxuan.last.night.util.ItemSerializerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Loader {

    public static void loadGuns(Plugin p) {
        File file1 = new File(p.getDataFolder(), "guns.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String key1 : config1.getKeys(false)) {
                LastNightItemGun ibg = new LastNightItemGun(
                        config1.getString(key1 + ".name"),
                        key1,
                        config1.getInt(key1 + ".value"),
                        config1.getDouble(key1 + ".damage"),
                        config1.getDouble(key1 + ".fire"),
                        config1.getDouble(key1 + ".fireSpeed"),
                        config1.getInt(key1 + ".bulletAmount"),
                        config1.getInt(key1 + ".maxBulletAmount"),
                        config1.getInt(key1 + ".solidValue"),
                        config1.getDouble(key1 + ".maxDurability"),
                        config1.getDouble(key1 + ".durability"),
                        Material.valueOf(config1.getString(key1 + ".itemType").split(":")[0]),
                        config1.getString(key1 + ".bulletEntity"),
                        config1.getDouble(key1 + ".bulletSpeed"),
                        config1.getInt(key1 + ".everyBulletAmount"),
                        config1.getDouble(key1 + ".replaceTime"),
                        config1.getString(key1 + ".bulletChest"),
                        config1.getDouble(key1 + ".bulletSpread"),
                        config1.getDouble(key1 + ".explosionDamage"),
                        config1.getDouble(key1 + ".explosionRange"),
                        config1.getDouble(key1 + ".range"),
                        config1.getBoolean(key1 + ".bulletGravity"),
                        new Short(config1.getString(key1 + ".itemType").split(":")[1]),
                        config1.getInt(key1 + ".enlarge"),
                        Double.parseDouble(config1.getString(key1 + ".pitchAdd").split(" ")[0]),
                        Double.parseDouble(config1.getString(key1 + ".pitchAdd").split(" ")[1]),
                        Double.parseDouble(config1.getString(key1 + ".yawAdd").split(" ")[0]),
                        Double.parseDouble(config1.getString(key1 + ".yawAdd").split(" ")[1]),
                        config1.getInt(key1 + ".forgeTime"),
                        config1.getStringList(key1 + ".particle"),
                        config1.getString(key1 + ".sound"));
                for (String key2 : config1.getStringList(key1 + ".randomRanges")) {
                    Range r = new Range(
                            key2.split(" ")[0], // split 了三遍 —— 754503921
                            Double.parseDouble(key2.split(" ")[1]),
                            Double.parseDouble(key2.split(" ")[2]));
                    ibg.ranges.add(r);
                }
                for (String s : config1.getStringList(key1 + ".sources")) {
                    RecipeItem ri = new RecipeItem(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]));
                    ibg.sources.add(ri);
                }
                LastNight.guns.put(key1, ibg);
            }
        } else {
            p.saveResource("guns.yml", true);
            loadGuns(p);
        }
    }

    public static void loadBulletChests(Plugin p) {
        File file1 = new File(p.getDataFolder(), "bulletChests.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String key : config1.getKeys(false)) {
                BulletChest bc = new BulletChest(
                        config1.getInt(key + ".maxBullet"),
                        config1.getInt(key + ".maxBullet"),
                        key
                );
                BulletChestManager.chestsNameMap.put(key, bc);
            }
        } else {
            p.saveResource("bulletChests.yml", true);
            loadBulletChests(p);
        }
    }

    public static void loadItems(Plugin p) {
        File file1 = new File(p.getDataFolder(), "items.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String key : config1.getKeys(false)) {
                LastNightItem lni = new LastNightItem();
                lni.item = ItemSerializerUtils.fromBase64(config1.getString(key + ".item"))[0];
                for (String s : config1.getStringList(key + ".sources")) {
                    RecipeItem ri = new RecipeItem(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]));
                    lni.sources.add(ri);
                }
                LastNight.itemsMap.put(key, lni);
            }
        } else {
            p.saveResource("items.yml", true);
            loadItems(p);
        }
    }

    public static void loadBreakableBlocks(Plugin p) {
        File file1 = new File(p.getDataFolder(), "breakableblock.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String key : config1.getKeys(false)) {
                LastNight.breakableBlockMap.put(Material.valueOf(key), config1.getInt(key));
            }
        } else {
            p.saveResource("breakableblock.yml", true);
            loadBreakableBlocks(p);
        }
    }

    public static void loadConfig(Plugin p) {
        File file1 = new File(p.getDataFolder(), "config.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            BreakableBlockListener.allowBreakWorld = config1.getStringList("allowBreakWorlds");
            LastNight.limitDrop = config1.getBoolean("limitDrop");
            LastNight.clearRecipes = config1.getBoolean("clearRecipes");
        } else {
            p.saveResource("config.yml", true);
            loadConfig(p);
        }
    }
}
