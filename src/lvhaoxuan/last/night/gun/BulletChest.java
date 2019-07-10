package lvhaoxuan.last.night.gun;

import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BulletChest {

    ArmorStand as;
    int value;
    int maxValue;
    String typeName;

    public BulletChest(ArmorStand as) {
        String name = as.getCustomName();
        String[] args = name.split(" ");
        if (args.length == 4) {
            value = Integer.parseInt(args[1]);
            maxValue = Integer.parseInt(args[3]);
            typeName = args[0];
        }
        this.as = as;
    }

    public BulletChest(int value, int maxValue, String typeName) {
        this.value = value;
        this.maxValue = maxValue;
        this.typeName = typeName;
    }

    public ItemStack toItemStack() {
        as.remove();
        ItemStack item0 = new ItemStack(Material.CHEST);
        NBT nbt = new NBT(item0);
        nbt.setInt("明日之后", LastNight.BULLET_CHEST);
        nbt.setString("弹药箱", typeName + " " + value + " / " + maxValue);
        item0 = nbt.toItemStack();
        ItemMeta meta0 = item0.getItemMeta();
        meta0.setDisplayName("§f" + typeName + " " + value + " / " + maxValue);
        item0.setItemMeta(meta0);
        return item0;
    }

    public void set(Location loc) {
        if (as != null) {
            as.remove();
            BulletChestManager.chestsMap.remove(as.getUniqueId());
        }
        as = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -1.38, 0), EntityType.ARMOR_STAND);
        BulletChestManager.chestsMap.put(as.getUniqueId(), this);
        as.setVisible(false);
        as.setGravity(false);
        as.setHelmet(new ItemStack(Material.CHEST));
        as.setCustomName(typeName + " " + value + " / " + maxValue);
        as.setCustomNameVisible(true);
    }

    public void use(int i) {
        value -= i;
        as.setCustomName(typeName + " " + value + " / " + maxValue);
        if (value <= 0) {
            as.remove();
        }
    }

    public BulletChest clone() {
        return new BulletChest(value, maxValue, typeName);
    }
}
