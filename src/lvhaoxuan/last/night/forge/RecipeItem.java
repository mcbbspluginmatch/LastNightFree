package lvhaoxuan.last.night.forge;

import lvhaoxuan.last.night.LastNight;
import org.bukkit.inventory.ItemStack;

public class RecipeItem {

    String item;
    int amount;

    public RecipeItem(String item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public ItemStack toItemStack() {
        ItemStack itemm = LastNight.itemsMap.get(item).toItemStack();
        itemm.setAmount(amount);
        return itemm;
    }

    @Override
    public String toString() {
        return item + " * " + amount;
    }
}
