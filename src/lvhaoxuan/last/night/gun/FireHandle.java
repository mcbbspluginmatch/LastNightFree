package lvhaoxuan.last.night.gun;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.particle.ParticleManager;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.util.Vector;
import org.bukkit.metadata.FixedMetadataValue;

public class FireHandle {

    public static HashMap<String, Integer> lastReplaceMap = new HashMap<>();
    public static HashMap<String, Long> lastFireTimeMap = new HashMap<>();

    public static boolean fire(Player p, ItemStack item) {
        LastNightItemGun ibg = new LastNightItemGun(item);
        Long lastFireTime = lastFireTimeMap.get(p.getName());
        Long fireTime = System.currentTimeMillis();
        if ((lastFireTime == null || fireTime - lastFireTime > (60.0 / ibg.fireSpeed) * 1000) && ibg.hasBulletAmount > 0) {
            lastFireTimeMap.put(p.getName(), fireTime);
            for (int i = 0; i < ibg.everyBulletAmount * ibg.fireSpeed / 300; i++) {
                Projectile pr = (Projectile) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.valueOf(ibg.bulletEntity));
                for (Particle pa : Particle.values()) {
                    if (pa.name().equals(ibg.particle)) {
                        ParticleManager.entities.put(pr, pa);
                        break;
                    }
                }
                pr.setMetadata("lastnight_damage", new FixedMetadataValue(LastNight.plugin, ibg.damage * ibg.fire));
                pr.setMetadata("lastnight_damageType", new FixedMetadataValue(LastNight.plugin, ibg.type));
                pr.setMetadata("lastnight_explosionDamage", new FixedMetadataValue(LastNight.plugin, ibg.explosionDamage));
                pr.setMetadata("lastnight_explosionRange", new FixedMetadataValue(LastNight.plugin, ibg.explosionRange));
                pr.setMetadata("lastnight_range", new FixedMetadataValue(LastNight.plugin, ibg.range));
                pr.setGravity(ibg.bulletGravity);
                pr.setShooter(p);
                pr.setVelocity(p.getLocation().getDirection().normalize().multiply(ibg.bulletSpeed).add(Vector.getRandom().multiply(getRandom(-ibg.bulletSpread, ibg.bulletSpread))));
            }
            ibg.use();
            sendPacketPlayOutPosition(p, (float) getRandom(ibg.yawAddMin, ibg.yawAddMax), (float) getRandom(ibg.pitchAddMin, ibg.pitchAddMax));
            p.getInventory().setItemInMainHand(ibg.toItemStack());
            return true;
        } else {
            if (ibg.hasBulletAmount == 0 && ibg.hasMaxBulletAmount >= 0) {
                if (lastReplaceMap.get(p.getName()) == null) {
                    int thread = Bukkit.getScheduler().scheduleSyncDelayedTask(LastNight.plugin, () -> {
                        if (ibg.hasBulletAmount == 0 && ibg.hasMaxBulletAmount >= 0) {
                            if (ibg.hasMaxBulletAmount - ibg.bulletAmount >= 0) {
                                ibg.hasBulletAmount = ibg.bulletAmount;
                                ibg.hasMaxBulletAmount = ibg.hasMaxBulletAmount - ibg.bulletAmount;
                            } else {
                                ibg.hasBulletAmount = ibg.hasMaxBulletAmount;
                                ibg.hasMaxBulletAmount = 0;
                            }
                            if (p.getInventory().getItemInMainHand().equals(item)) {
                                p.getInventory().setItemInMainHand(ibg.toItemStack());
                            }
                            lastReplaceMap.put(p.getName(), null);
                        }
                    }, (long) ibg.replaceTime * 20);
                    lastReplaceMap.put(p.getName(), thread);
                }
            }
            return false;
        }
    }

    public static void sendPacketPlayOutPosition(Player player, float yaw, float pitch) {
        try {
            Class<?> enumPlayerTeleportFlagsClass = NBT.Package.MINECRAFT_SERVER.getClass("PacketPlayOutPosition$EnumPlayerTeleportFlags");
            Class<?> packetPlayOutPositionClass = NBT.Package.MINECRAFT_SERVER.getClass("PacketPlayOutPosition");
            Class<?> packetClass = NBT.Package.MINECRAFT_SERVER.getClass("Packet");
            Set<Object> teleportFlags = new HashSet<>();
            teleportFlags.add(enumPlayerTeleportFlagsClass.getField("X").get(null));
            teleportFlags.add(enumPlayerTeleportFlagsClass.getField("Y").get(null));
            teleportFlags.add(enumPlayerTeleportFlagsClass.getField("Z").get(null));
            teleportFlags.add(enumPlayerTeleportFlagsClass.getField("Y_ROT").get(null));
            teleportFlags.add(enumPlayerTeleportFlagsClass.getField("X_ROT").get(null));
            Object packet = NBT.create(packetPlayOutPositionClass,
                    new NBT.ParamGroup(0.0, double.class),
                    new NBT.ParamGroup(0.0, double.class),
                    new NBT.ParamGroup(0.0, double.class),
                    new NBT.ParamGroup(yaw, float.class),
                    new NBT.ParamGroup(pitch, float.class),
                    new NBT.ParamGroup(teleportFlags, Set.class),
                    new NBT.ParamGroup(0, int.class));
            Object nmsPlayer = NBT.doMethod(player, "getHandle");
            Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            NBT.doMethod(playerConnection, "sendPacket", new NBT.ParamGroup(packet, packetClass));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
        }
    }

    public static double getRandom(double paramMin, double paramMax) {
        double min = Math.min(paramMin, paramMax);
        double max = Math.max(paramMin, paramMax);
        if (min == max) {
            return min;
        }
        return Math.random() * (max - min) + min;
    }
}
