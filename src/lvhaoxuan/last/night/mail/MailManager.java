package lvhaoxuan.last.night.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import lvhaoxuan.last.night.LastNight;
import org.bukkit.inventory.ItemStack;

public class MailManager {

    public static File save = new File(LastNight.plugin.getDataFolder(), "playerDatas");
    public static HashMap<String, PlayerData> playerDatas = new HashMap<>();

    public static void init() {
        if (!save.exists()) {
            save.mkdir();
        }
        for (File file : save.listFiles()) {
            PlayerData pd = new PlayerData(file.getName().split("\\.")[0]);
            playerDatas.put(file.getName().split("\\.")[0], pd);
        }
    }

    public static void addItem(String name, ItemStack item) {
        if (!playerDatas.containsKey(name)) {
            playerDatas.put(name, new PlayerData(name, new ArrayList<>()));
        }
        playerDatas.get(name).addItem(item);
    }

    public static void removeItem(String name, ItemStack item) {
        if (!playerDatas.containsKey(name)) {
            playerDatas.put(name, new PlayerData(name, new ArrayList<>()));
        }
        playerDatas.get(name).removeItem(item);
    }
}
