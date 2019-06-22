package lvhaoxuan.last.night.drop;

import lvhaoxuan.last.night.LastNight;
import org.bukkit.inventory.ItemStack;

public class DropItem {

    double probability;
    String item;
    int amount;

    public DropItem(String item, int amount, double probability) {
        this.item = item;
        this.amount = amount;
        this.probability = probability;
    }

    public ItemStack toItemStack() {
        ItemStack itemm = LastNight.itemsMap.get(item).toItemStack();
        itemm.setAmount(amount);
        return itemm;
    }

    @Override
    public String toString() {
        return item + " " + amount + " " + probability;
    }
}
