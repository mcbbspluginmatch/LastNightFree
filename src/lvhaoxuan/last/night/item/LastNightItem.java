package lvhaoxuan.last.night.item;

import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.last.night.forge.RecipeItem;
import org.bukkit.inventory.ItemStack;

public class LastNightItem {

    public List<RecipeItem> sources = new ArrayList<>();
    public ItemStack item;

    public ItemStack toItemStack() {
        return item;
    }
}
