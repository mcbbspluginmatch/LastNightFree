package lvhaoxuan.last.night.forge;

import lvhaoxuan.last.night.LastNight;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LastNightGunRecipeReplaceInventory {

    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, LastNight.RECIPE_MAKER_INVENTORY);
        check(inv);
        p.openInventory(inv);
    }

    public void check(Inventory inv) {
        ItemStack bariier = new ItemStack(Material.BARRIER);
        inv.setItem(0, bariier);
        inv.setItem(1, bariier);
        inv.setItem(2, bariier);
        inv.setItem(4, bariier);
        inv.setItem(6, bariier);
        inv.setItem(7, bariier);
        inv.setItem(8, bariier);
        ItemStack item1 = new ItemStack(Material.ANVIL);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("§f随机锻造");
        item1.setItemMeta(meta1);
        inv.setItem(5, item1);
    }
}
