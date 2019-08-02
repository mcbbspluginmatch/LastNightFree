package lvhaoxuan.last.night.mail;

import lvhaoxuan.last.night.LastNight;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MailInventory {

    // 这个类没有任何字段，为什么不做成静态方法呢 —— 754503921

    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, LastNight.MAIL_INVENTORY);
        check(inv, p.getName());
        p.openInventory(inv);
    }

    public void check(Inventory inv, String name) {
        for (ItemStack item : MailManager.playerDatas.get(name).items) {
            inv.addItem(item);
        }
    }
}
