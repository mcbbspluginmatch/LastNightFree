package lvhaoxuan.last.night.forge;

import java.util.List;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.commander.Commanders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LastNightGunRecipeMakerInventory {

    Player p; // 这个变量有什么用吗 —— 754503921

    public void open(Player p) {
        this.p = p;
        Inventory inv = Bukkit.createInventory(null, 54, LastNight.MAKER_INVENTORY);
        check(inv, p.getName());
        p.openInventory(inv);
    }

    public static void check(Inventory inv, String name) {
        inv.clear();
        List<LastNightGunRecipe> lngrs = LastNightGunForgeManagaer.userMap.get(name);
        LastNightGunForgeManagaer.userInventoryMap.put(name, inv);
        int i = 0;
        for (LastNightGunRecipe lngr : lngrs) {
            if (i == 0) {
                ItemStack item = lngr.getResult().toItemStack0(name);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(meta.getDisplayName() + " §a" + Commanders.getTime(lngr.firstTime + lngr.forgeTime * 50 - System.currentTimeMillis()));
                item.setItemMeta(meta);
                inv.setItem(i, item);
            } else {
                ItemStack item = lngr.toItemStack();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(meta.getDisplayName() + " §a" + Commanders.getTime(lngr.forgeTime * 50));
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
            i++;
        }
    }
}
