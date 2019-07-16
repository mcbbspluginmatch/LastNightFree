package lvhaoxuan.last.night.random;

import lvhaoxuan.last.night.LastNight;
import org.bukkit.inventory.ItemStack;

public class RandomItem {

    double probability;
    String item;
    int amount;

    public RandomItem(String item, int amount, double probability) {
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
