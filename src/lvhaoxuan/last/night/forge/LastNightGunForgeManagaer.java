package lvhaoxuan.last.night.forge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.mail.MailManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class LastNightGunForgeManagaer extends BukkitRunnable {

    public static HashMap<String, List<LastNightGunRecipe>> userMap = new HashMap<>();
    public static HashMap<String, Inventory> userInventoryMap = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry<String, List<LastNightGunRecipe>> entry : userMap.entrySet()) {
            List<LastNightGunRecipe> list = entry.getValue();
            String name = entry.getKey();
            if (!list.isEmpty()) {
                LastNightGunRecipe lngr = list.get(0);
                if (lngr.firstTime == -1) {
                    lngr.firstTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() >= (lngr.firstTime + lngr.forgeTime * 50)) {
                    list.remove(0);
                    Player p = Bukkit.getPlayer(name);
                    MailManager.addItem(name, lngr.getResult().toItemStack0(name));
                    if (p != null) {
                        p.sendMessage(LastNight.FORGE_SUCCESS_MESSAGE);
                    }
                    if (!list.isEmpty()) {
                        lngr = list.get(0);
                        if (lngr.firstTime == -1) {
                            lngr.firstTime = System.currentTimeMillis();
                        }
                    }
                    // 基于 title 的背包判断是不好的，建议更换为基于 InventoryHolder 的判断 —— 754503921
                    if (p != null && p.getOpenInventory() != null && p.getOpenInventory().getTitle() != null && userInventoryMap.get(name) != null && p.getOpenInventory().getTitle().equals(userInventoryMap.get(name).getTitle())) {
                        LastNightGunRecipeMakerInventory.check(userInventoryMap.get(name), name);
                        p.updateInventory();
                    }
                } else {
                    Player p = Bukkit.getPlayer(name);
                    if (p != null && p.getOpenInventory() != null && p.getOpenInventory().getTitle() != null && userInventoryMap.get(name) != null && p.getOpenInventory().getTitle().equals(userInventoryMap.get(name).getTitle())) {
                        LastNightGunRecipeMakerInventory.check(userInventoryMap.get(name), name);
                        p.updateInventory();
                    }
                }
            }
        }

    }
}
