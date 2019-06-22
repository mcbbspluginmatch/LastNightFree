package lvhaoxuan.last.night.forge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lvhaoxuan.last.night.LastNight;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;

public class LastNightGunForgeManagaer extends BukkitRunnable {

    public static HashMap<String, List<LastNightGunRecipe>> userMap = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry<String, List<LastNightGunRecipe>> entry : userMap.entrySet()) {
            List<LastNightGunRecipe> list = entry.getValue();
            String name = entry.getKey();
            if (!list.isEmpty()) {
                LastNightGunRecipe lngr = list.get(0);
                if (lngr.firstTime == -1) {
                    lngr.firstTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() > (lngr.firstTime + lngr.forgeTime * 50)) {
                    userMap.get(name).remove(0);
                    Player p = Bukkit.getPlayer(name);
                    p.getInventory().addItem(lngr.getResult().toItemStack0(name));
                    p.sendMessage(LastNight.FORGE_SUCCESS_MESSAGE);
                }
            }
        }
    }

}
