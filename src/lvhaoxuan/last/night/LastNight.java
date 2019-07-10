package lvhaoxuan.last.night;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import lvhaoxuan.last.night.breakableblock.BreakableBlockListener;
import lvhaoxuan.last.night.commander.Commanders;
import lvhaoxuan.last.night.drop.DropItemGroup;
import lvhaoxuan.last.night.forge.LastNightGunForgeManagaer;
import lvhaoxuan.last.night.gun.LastNightItemGun;
import lvhaoxuan.last.night.item.LastNightItem;
import lvhaoxuan.last.night.listener.MainListener;
import static lvhaoxuan.last.night.listener.MainListener.enlargeMap;
import lvhaoxuan.last.night.loader.Loader;
import lvhaoxuan.last.night.particle.ParticleManager;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import lvhaoxuan.last.night.util.ItemSerializerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class LastNight extends JavaPlugin {

    public static HashMap<String, LastNightItemGun> guns = new HashMap<>();
    public static Plugin plugin;
    public static LastNightGunForgeManagaer lngfm = new LastNightGunForgeManagaer();
    public static ParticleManager pm = new ParticleManager();
    public static HashMap<String, DropItemGroup> resourceDropsMap = new HashMap<>();
    public static HashMap<String, LastNightItem> itemsMap = new HashMap<>();
    public static HashMap<Material, Integer> breakableBlockMap = new HashMap<>();
    public static boolean limitDrop = true;
    public static final int GUN = 1;
    public static final int BULLET_CHEST = 2;
    public static final int RECIPE = 3;
    public static final int ITEM = 4;
    public static final String TREASURE_CHEST_INVENTORY = "宝藏箱";
    public static final String BULLET_CHEST_INVENTORY = "弹药箱";
    public static final String RECIPE_MAKER_INVENTORY = "配方制作台";
    public static final String MAKER_INVENTORY = "装备制作台(SHIFT+鼠标右键)取消";
    public static final String MAKER = "将物品放入后关闭背包进行锻造";
    public static final String BACK_MESSAGE = "§b物品已经回到你的背包";
    public static final String FORGE_SUCCESS_MESSAGE = "§a锻造成功";
    public static final String FORGE_CANCEL_MESSAGE = "§a锻造取消";
    public static final String MARKET_MAIN = "选择你要交易的物品类型";
    public static final String MARKET_GUN = "枪械";
    public static final String MARKET_ITEM = "物品";

    @Override
    public void onEnable() {
        plugin = this;
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        Loader.loadItems(this);
        Loader.loadGuns(this);
        Loader.loadBulletChests(this);
        Loader.loadConfig(this);
        Loader.loadBreakableBlocks(this);
        Server s = this.getServer();
        s.clearRecipes();
        s.getPluginCommand("ln").setExecutor(new Commanders());
        s.getPluginManager().registerEvents(new MainListener(), this);
        s.getPluginManager().registerEvents(new BreakableBlockListener(), this);
        lngfm.runTaskTimer(this, 1, 1);
        pm.runTaskTimer(this, 1, 1);
    }

    @Override
    public void onDisable() {
        for (UUID uid : enlargeMap.keySet()) {
            if (enlargeMap.get(uid) != null && enlargeMap.get(uid) == true) {
                Player p = plugin.getServer().getPlayer(uid);
                if (p != null) {
                    p.removePotionEffect(PotionEffectType.SLOW);
                }
            }
        }
    }

    public static void saveItems() {
        File file1 = new File(plugin.getDataFolder(), "items.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String key : itemsMap.keySet()) {
                config1.set(key + ".item", ItemSerializerUtils.toBase64(new ItemStack[]{itemsMap.get(key).item}));
                config1.set(key + ".sources", itemsMap.get(key).sources);
            }
            try {
                config1.save(file1);
            } catch (IOException ex) {
            }
        } else {
            plugin.saveResource("items.yml", true);
            saveItems();
        }
    }
}
