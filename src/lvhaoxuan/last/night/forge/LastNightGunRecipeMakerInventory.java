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

    Player p;

    public void open(Player p) {
        this.p = p;
        Inventory inv = Bukkit.createInventory(null, 54, LastNight.MAKER_INVENTORY);
        check(inv);
        p.openInventory(inv);
    }

    public void check(Inventory inv) {
        List<LastNightGunRecipe> lngrs = LastNightGunForgeManagaer.userMap.get(p.getName());
        int i = 0;
        for (LastNightGunRecipe lngr : lngrs) {
            if (i == 0) {
                ItemStack item = lngr.getResult().toItemStack0(p.getName());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(meta.getDisplayName() + " " + Commanders.getTime(lngr.firstTime + lngr.forgeTime * 50 - System.currentTimeMillis()));
                item.setItemMeta(meta);
                inv.setItem(i, item);
            } else {
                ItemStack item = lngr.toItemStack();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(meta.getDisplayName() + " " + Commanders.getTime(lngr.forgeTime * 50));
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
            i++;
        }
    }
}
