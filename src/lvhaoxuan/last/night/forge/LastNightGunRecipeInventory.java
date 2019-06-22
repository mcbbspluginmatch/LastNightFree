package lvhaoxuan.last.night.forge;

import lvhaoxuan.last.night.LastNight;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LastNightGunRecipeInventory {

    public LastNightGunRecipe lngr;
    public Inventory inv;

    public LastNightGunRecipeInventory(LastNightGunRecipe lngr) {
        this.lngr = lngr;
        inv = Bukkit.createInventory(null, 54, LastNight.MAKER);
        check();
    }

    public void open(Player p) {
        p.openInventory(inv);
    }

    public void check() {
    }
}
