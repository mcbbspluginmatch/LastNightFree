package lvhaoxuan.last.night.gun;

import java.text.DecimalFormat;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.forge.LastNightGunRecipe;
import lvhaoxuan.last.night.item.LastNightItem;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.ItemMeta;

public class LastNightItemGun extends LastNightItem {

    public String name;
    public String type;
    public int value;
    public double damage;
    public double fire;
    public double fireSpeed;
    public int bulletAmount;
    public int hasBulletAmount;
    public int maxBulletAmount;
    public int hasMaxBulletAmount;
    public int solidValue;
    public double maxDurability;
    public double durability;
    public Material itemType;
    public String maker;
    public String bulletEntity;
    public double bulletSpeed;
    public int everyBulletAmount;
    public double replaceTime;
    public String bulletChest;
    public double bulletSpread;
    public double explosionDamage;
    public double explosionRange;
    public double range;
    public boolean bulletGravity;
    public short shortValue;
    public int amount = 1;
    public int enlarge;
    public float pitchAddMax;
    public float pitchAddMin;
    public float yawAddMax;
    public float yawAddMin;
    public List<Range> ranges = new ArrayList<>();
    public int forgeTime;
    public String particle;

    public LastNightItemGun() {
    }

    public LastNightItemGun(String name, String type, int value, double damage, double fire, double fireSpeed, int bulletAmount, int maxBulletAmount, int solidValue, double maxDurability, double durability, Material itemType, EntityType bulletEntity, double bulletSpeed, int everyBulletAmount, double replaceTime, String bulletChest, double bulletSpread, double explosionDamage, double explosionRange, double range, boolean bulletGravity, short shortValue, int enlarge, float pitchAddMax, float pitchAddMin, float yawAddMax, float yawAddMin, int forgeTime, String particle) {
        this(name, type, value, damage, fire, fireSpeed, bulletAmount, maxBulletAmount, solidValue, maxDurability, durability, itemType, bulletEntity.name(), bulletSpeed, everyBulletAmount, replaceTime, bulletChest, bulletSpread, explosionDamage, explosionRange, range, bulletGravity, shortValue, enlarge, pitchAddMax, pitchAddMin, yawAddMax, yawAddMin, forgeTime, particle);
    }

    public LastNightItemGun(String name, String type, int value, double damage, double fire, double fireSpeed, int bulletAmount, int maxBulletAmount, int solidValue, double maxDurability, double durability, Material itemType, String bulletEntity, double bulletSpeed, int everyBulletAmount, double replaceTime, String bulletChest, double bulletSpread, double explosionDamage, double explosionRange, double range, boolean bulletGravity, short shortValue, int enlarge, float pitchAddMax, float pitchAddMin, float yawAddMax, float yawAddMin, int forgeTime, String particle) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.damage = damage;
        this.fire = fire;
        this.fireSpeed = fireSpeed;
        this.bulletAmount = bulletAmount;
        this.hasBulletAmount = bulletAmount;
        this.maxBulletAmount = maxBulletAmount;
        this.hasMaxBulletAmount = maxBulletAmount;
        this.solidValue = solidValue;
        this.maxDurability = maxDurability;
        this.durability = durability;
        this.itemType = itemType;
        this.bulletEntity = bulletEntity;
        this.bulletSpeed = bulletSpeed;
        this.everyBulletAmount = everyBulletAmount;
        this.replaceTime = replaceTime;
        this.bulletChest = bulletChest;
        this.bulletSpread = bulletSpread;
        this.explosionDamage = explosionDamage;
        this.explosionRange = explosionRange;
        this.range = range;
        this.bulletGravity = bulletGravity;
        this.shortValue = shortValue;
        this.enlarge = enlarge;
        this.pitchAddMax = pitchAddMax;
        this.pitchAddMin = pitchAddMin;
        this.yawAddMax = yawAddMax;
        this.yawAddMin = yawAddMin;
        this.forgeTime = forgeTime;
        this.particle = particle;
    }

    public LastNightItemGun(ItemStack item) {
        NBT nbt = new NBT(item);
        if (nbt.getInt("明日之后") == LastNight.GUN) {
            this.name = nbt.getString("显示名");
            this.type = nbt.getString("武器种类");
            this.value = nbt.getInt("评分");
            this.damage = nbt.getDouble("攻击力");
            this.fire = nbt.getDouble("武器火力");
            this.fireSpeed = nbt.getDouble("开火速率");
            this.solidValue = nbt.getInt("坚固值");
            this.durability = nbt.getDouble("耐久值");
            this.maxDurability = nbt.getDouble("最大耐久值");
            this.itemType = item.getType();
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
            this.pitchAddMax = nbt.getFloat("枪口上扬最大");
            this.pitchAddMin = nbt.getFloat("枪口上扬最小");
            this.yawAddMax = nbt.getFloat("枪口偏移最大");
            this.yawAddMin = nbt.getFloat("枪口偏移最小");
            this.forgeTime = nbt.getInt("锻造时间");
            this.particle = nbt.getString("粒子效果");
        }
    }

    public ItemStack toItemStack() {
        return toItemStack0(this.maker);
    }

    public ItemStack toItemStack0(String maker) {
        this.maker = maker;
        ItemStack item = new ItemStack(itemType, amount, shortValue);
        NBT nbt = new NBT(item);
        nbt.setInt("明日之后", LastNight.GUN);
        nbt.setString("显示名", name);
        nbt.setString("武器种类", type);
        nbt.setInt("评分", value);
        nbt.setDouble("攻击力", damage);
        nbt.setDouble("武器火力", fire);
        nbt.setDouble("开火速率", fireSpeed);
        nbt.setInt("坚固值", solidValue);
        nbt.setDouble("耐久值", durability);
        nbt.setDouble("最大耐久值", maxDurability);
        nbt.setString("锻造者", maker);
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
        nbt.setFloat("枪口上扬最大", pitchAddMax);
        nbt.setFloat("枪口上扬最小", pitchAddMin);
        nbt.setFloat("枪口偏移最大", yawAddMax);
        nbt.setFloat("枪口偏移最小", yawAddMin);
        nbt.setString("物品类型", itemType.name());
        nbt.setShort("附加值", shortValue);
        nbt.setInt("锻造时间", forgeTime);
        nbt.setString("粒子效果", particle);
        item = nbt.toItemStack();
        item = calibration(item);
        return item;
    }

    public void use() {
        if (hasBulletAmount > 0) {
            durability -= 0.2;
            hasBulletAmount -= 1;
        }
        if (durability <= 0) {
            this.itemType = Material.AIR;
        }
    }

    public ItemStack calibration(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name + "  " + hasBulletAmount + " / " + hasMaxBulletAmount);
        DecimalFormat df = new DecimalFormat("0.0");
        List<String> firstLore = new ArrayList<>();
        firstLore.add("§f★评分: " + value);
        firstLore.add("§e攻击力: " + df.format(damage));
        firstLore.add("§e武器火力: " + df.format(fire));
        firstLore.add("§f开火速率(子弹/分钟): " + df.format(fireSpeed));
        firstLore.add("§f弹夹容量: " + bulletAmount);
        firstLore.add("§f坚固值: " + solidValue);
        firstLore.add("§f耐久值: " + ((int) durability) + "/" + ((int) maxDurability));
        firstLore.add("§f锻造者: " + maker);
        meta.setLore(firstLore);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public void repalce(BulletChest rbc) {
        if (rbc.typeName.equals(bulletChest)) {
            int use = bulletAmount - hasBulletAmount;
            if (rbc.value >= use) {
                rbc.use(use);
                hasBulletAmount = bulletAmount;
            } else {
                rbc.use(rbc.value);
                hasBulletAmount = hasBulletAmount + rbc.value;
            }
            use = maxBulletAmount - hasMaxBulletAmount;
            if (rbc.value >= use) {
                rbc.use(use);
                hasMaxBulletAmount = maxBulletAmount;
            } else {
                rbc.use(rbc.value);
                hasMaxBulletAmount = hasMaxBulletAmount + rbc.value;
            }
        }
    }

    public LastNightGunRecipe getLastNightGunRecipe(int maxUse) {
        return new LastNightGunRecipe(this, maxUse);
    }

    public LastNightItemGun clone() {
        LastNightItemGun lnig = new LastNightItemGun(name, type, value, damage, fire, fireSpeed, bulletAmount, maxBulletAmount, solidValue, maxDurability, durability, itemType, bulletEntity, bulletSpeed, everyBulletAmount, replaceTime, bulletChest, bulletSpread, explosionDamage, explosionRange, range, bulletGravity, shortValue, enlarge, pitchAddMax, pitchAddMin, yawAddMax, yawAddMin, forgeTime, particle);
        lnig.ranges = ranges;
        lnig.sources = sources;
        return lnig;
    }

    public static int getInt(boolean flag) {
        if (flag) {
            return 1;
        }
        return 0;
    }

    public static boolean getBoolean(int a) {
        return a == 1;
    }
}
