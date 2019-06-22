package lvhaoxuan.last.night.gun;

import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BulletChestInventory {

    public BulletChest bc;
    public Inventory inv;

    public BulletChestInventory(BulletChest bc) {
        this.bc = bc;
        inv = Bukkit.createInventory(null, 9, LastNight.BULLET_CHEST_INVENTORY);
        check();
    }

    public void open(Player p) {
        p.openInventory(inv);
    }

    public void check() {
        ItemStack bariier = new ItemStack(Material.BARRIER);
        NBT nbt = new NBT(bariier);
        nbt.setString("id", bc.as.getUniqueId().toString());
        bariier = nbt.toItemStack();

        ItemStack item0 = new ItemStack(Material.CHEST);
        ItemMeta meta0 = item0.getItemMeta();
        meta0.setDisplayName("§f" + bc.typeName);
        item0.setItemMeta(meta0);
        inv.setItem(0, item0);

        inv.setItem(1, bariier);
        inv.setItem(2, bariier);
        inv.setItem(4, bariier);
        inv.setItem(6, bariier);
        inv.setItem(7, bariier);
        ItemStack item1 = new ItemStack(Material.ANVIL);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("§f一键补弹");
        item1.setItemMeta(meta1);
        inv.setItem(5, item1);

        ItemStack item2 = new ItemStack(Material.ANVIL);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName("§f回收");
        item2.setItemMeta(meta2);
        inv.setItem(8, item2);
    }
}
