package lvhaoxuan.last.night.forge;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lvhaoxuan.last.night.LastNight;
import static lvhaoxuan.last.night.LastNight.RECIPE;
import lvhaoxuan.last.night.gun.LastNightItemGun;
import static lvhaoxuan.last.night.gun.LastNightItemGun.getBoolean;
import lvhaoxuan.last.night.gun.Range;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LastNightGunRecipe extends LastNightItemGun {

    public HashMap<String, Double> values = new HashMap<>();
    public int maxUse = 0;
    public int use = 0;
    public Material material = Material.PAPER;
    public long firstTime = -1;

    public LastNightGunRecipe(LastNightItemGun lnig, int maxUse) {
        super(lnig.name, lnig.type, lnig.value, lnig.damage, lnig.fire, lnig.fireSpeed, lnig.bulletAmount, lnig.maxBulletAmount, lnig.solidValue, lnig.maxDurability, lnig.durability, lnig.itemType, lnig.bulletEntity, lnig.bulletSpeed, lnig.everyBulletAmount, lnig.replaceTime, lnig.bulletChest, lnig.bulletSpread, lnig.explosionDamage, lnig.explosionRange, lnig.range, lnig.bulletGravity, lnig.shortValue, lnig.enlarge, lnig.pitchAddMax, lnig.pitchAddMin, lnig.yawAddMax, lnig.yawAddMin, lnig.forgeTime, lnig.particle);
        this.maxUse = maxUse;
        this.use = maxUse;
        for (Range key : lnig.ranges) {
            try {
                setValue(this, key.name, getValue(lnig, key.name) + getRandom(key.min, key.max, 3));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            }
        }
        calibrationValue(lnig);
        this.sources = LastNight.guns.get(type).sources;
    }

    public void calibrationValue(LastNightItemGun base) {
        double value = 0;
        value += (this.damage - base.damage) * 5;
        value += (this.fire - base.fire) * 10;
        value += (this.fireSpeed - base.fireSpeed) * 3;
        value += (this.solidValue - base.solidValue) * 2;
        value += (this.maxDurability - base.maxDurability) * 2;
        value += (this.bulletSpeed - base.bulletSpeed) * 4;
        value += (this.maxBulletAmount - base.maxBulletAmount) * 2;
        value += (this.hasMaxBulletAmount - base.hasMaxBulletAmount) * 2;
        value += (this.everyBulletAmount - base.everyBulletAmount) * 7;
        value += (this.replaceTime - base.replaceTime) * 5;
        value -= (this.bulletSpread - base.bulletSpread) * 5;
        value += (this.explosionDamage - base.explosionDamage) * 5;
        value += (this.explosionRange - base.explosionRange) * 7;
        value += (this.range - base.range) * 10;
        value -= (this.pitchAddMax - base.pitchAddMax) * 2;
        value -= (this.pitchAddMin - base.pitchAddMin) * 2;
        value -= (this.yawAddMax - base.yawAddMax) * 2;
        value -= (this.yawAddMin - base.yawAddMin) * 2;
        this.value += (int) value;
    }

    public LastNightGunRecipe(ItemStack item) {
        NBT nbt = new NBT(item);
        if (nbt.getInt("明日之后") == LastNight.RECIPE) {
            this.name = nbt.getString("显示名");
            this.type = nbt.getString("武器种类");
            this.value = nbt.getInt("评分");
            this.damage = nbt.getDouble("攻击力");
            this.fire = nbt.getDouble("武器火力");
            this.fireSpeed = nbt.getDouble("开火速率");
            this.solidValue = nbt.getInt("坚固值");
            this.durability = nbt.getDouble("耐久值");
            this.maxDurability = nbt.getDouble("最大耐久值");
            this.itemType = Material.valueOf(nbt.getString("物品类型"));
            this.shortValue = nbt.getShort("附加值");
            this.maker = nbt.getString("锻造者");
            this.bulletEntity = nbt.getString("子弹实体");
            this.bulletSpeed = nbt.getDouble("子弹速度");
            this.bulletAmount = nbt.getInt("弹夹容量");
            this.hasBulletAmount = nbt.getInt("弹夹剩余子弹数");
            this.maxBulletAmount = nbt.getInt("总子弹容量");
            this.hasMaxBulletAmount = nbt.getInt("总子弹数");
            this.everyBulletAmount = nbt.getInt("每发子弹数");
            this.replaceTime = nbt.getDouble("换弹时间");
            this.bulletChest = nbt.getString("弹药箱类型");
            this.bulletSpread = nbt.getDouble("子弹散布");
            this.explosionDamage = nbt.getDouble("爆炸伤害");
            this.explosionRange = nbt.getDouble("爆炸范围");
            this.range = nbt.getDouble("射程");
            this.bulletGravity = getBoolean(nbt.getInt("子弹重力"));
            this.shortValue = item.getData().getData();
            this.amount = item.getAmount();
            this.enlarge = nbt.getInt("放大");
            this.pitchAddMax = nbt.getDouble("枪口上扬最大");
            this.pitchAddMin = nbt.getDouble("枪口上扬最小");
            this.yawAddMax = nbt.getDouble("枪口偏移最大");
            this.yawAddMin = nbt.getDouble("枪口偏移最小");
            this.maxUse = nbt.getInt("最大使用次数");
            this.use = nbt.getInt("剩余使用次数");
            this.forgeTime = nbt.getInt("锻造时间");
            this.particle = nbt.getString("粒子效果");
            this.sources = LastNight.guns.get(type).sources;
        }
    }

    public LastNightItemGun getResult() {
        return this.clone();
    }

    public void useRecipe() {
        if (use != -1) {
            use -= 1;
            if (use == 0) {
                this.material = Material.AIR;
            }
        }
    }

    public ItemStack toItemStack() {
        return toItemStack0();
    }

    public ItemStack toItemStack0() {
        ItemStack item = new ItemStack(material);
        if (item.getType() != Material.AIR) {
            NBT nbt = new NBT(item);
            nbt.setInt("明日之后", RECIPE);
            nbt.setString("显示名", name);
            nbt.setString("武器种类", type);
            nbt.setInt("评分", value);
            nbt.setDouble("攻击力", damage);
            nbt.setDouble("武器火力", fire);
            nbt.setDouble("开火速率", fireSpeed);
            nbt.setInt("坚固值", solidValue);
            nbt.setDouble("耐久值", durability);
            nbt.setDouble("最大耐久值", maxDurability);
            nbt.setString("子弹实体", bulletEntity);
            nbt.setDouble("子弹速度", bulletSpeed);
            nbt.setInt("弹夹容量", bulletAmount);
            nbt.setInt("弹夹剩余子弹数", hasBulletAmount);
            nbt.setInt("总子弹容量", maxBulletAmount);
            nbt.setInt("总子弹数", hasMaxBulletAmount);
            nbt.setInt("每发子弹数", everyBulletAmount);
            nbt.setDouble("换弹时间", replaceTime);
            nbt.setString("弹药箱类型", bulletChest);
            nbt.setDouble("子弹散布", bulletSpread);
            nbt.setDouble("爆炸伤害", explosionDamage);
            nbt.setDouble("爆炸范围", explosionRange);
            nbt.setDouble("射程", range);
            nbt.setInt("子弹重力", getInt(bulletGravity));
            nbt.setInt("放大", enlarge);
            nbt.setDouble("枪口上扬最大", pitchAddMax);
            nbt.setDouble("枪口上扬最小", pitchAddMin);
            nbt.setDouble("枪口偏移最大", yawAddMax);
            nbt.setDouble("枪口偏移最小", yawAddMin);
            nbt.setString("物品类型", itemType.name());
            nbt.setShort("附加值", shortValue);
            nbt.setInt("最大使用次数", maxUse);
            nbt.setInt("剩余使用次数", use);
            nbt.setInt("锻造时间", forgeTime);
            nbt.setString("粒子效果", particle);
            item = nbt.toItemStack();
            item = calibration(item);
        }
        return item;
    }

    public ItemStack calibration(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name + "图纸");
        DecimalFormat df = new DecimalFormat("0.0");
        List<String> firstLore = new ArrayList<>();
        if (use == -1) {
            firstLore.add("§e剩余使用次数: §a无限");
        } else {
            firstLore.add("§e剩余使用次数: §a" + use);
        }
        firstLore.add("§f★评分: " + value);
        firstLore.add("§e攻击力: " + df.format(damage));
        firstLore.add("§e武器火力: " + df.format(fire));
        firstLore.add("§f开火速率(子弹/分钟): " + df.format(fireSpeed));
        firstLore.add("§f弹夹容量: " + bulletAmount);
        firstLore.add("§f坚固值: " + solidValue);
        firstLore.add("§f耐久值: " + ((int) maxDurability));
        firstLore.add("§f材料: ");
        for (RecipeItem ri : sources) {
            firstLore.add("§f - " + ri.toString());
        }
        meta.setLore(firstLore);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public boolean canForge(Inventory inv) {
        boolean ret = true;
        for (RecipeItem ri : sources) {
            if (!inventoryHasItem(inv, ri)) {
                ret = false;
            }
        }
        if (ret) {
            for (RecipeItem ri : sources) {
                inventoryRemoveItem(inv, ri);
            }

        }
        return ret;
    }

    public boolean inventoryHasItem(Inventory inv, RecipeItem ri) {
        int num = 0;
        for (ItemStack itemm : inv) {
            if (itemm != null && itemm.getType() != Material.AIR) {
                if (itemm.isSimilar(ri.toItemStack())) {
                    num += itemm.getAmount();
                }
                if (num == ri.amount) {
                    return true;
                }
            }
        }
        return num >= ri.amount;
    }

    public void inventoryRemoveItem(Inventory inv, RecipeItem ri) {
        int num = ri.amount;
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
                if (inv.getItem(i).isSimilar(ri.toItemStack())) {
                    if (num > 0) {
                        int num2 = inv.getItem(i).getAmount();
                        if (inv.getItem(i).getAmount() - num > 0) {
                            inv.getItem(i).setAmount(inv.getItem(i).getAmount() - num);
                        } else {
                            inv.setItem(i, null);
                        }
                        num -= num2;
                    }
                }
            }
        }
    }

    public static void setValue(Object instance, String fileName, Object value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = instance.getClass().getField(fileName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static double getValue(Object instance, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = instance.getClass().getField(fieldName);
        field.setAccessible(true);
        return field.getDouble(instance);
    }

    public static double getRandom(double min, double max, int i) {
        if (min == max) {
            return min;
        }
        double ret = Double.MAX_VALUE;
        for (int j = 0; j < i; j++) {
            double d = Math.random() * (max - min) + min;
            ret = Math.min(ret, d);
        }
        return ret;
    }
}
