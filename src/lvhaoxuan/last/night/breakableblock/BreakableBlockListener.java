package lvhaoxuan.last.night.breakableblock;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Bukkit;

public class BreakableBlockListener implements Listener {
    
    public static HashMap<Location, Integer> blockMap = new HashMap<>();
    public static List<String> allowBreakWorld = new ArrayList<>();
    public static int id = 0;
    public static HashMap<Location, Integer> blockIDMap = new HashMap<>();
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerInteractEvent(PlayerInteractEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if (e.hasBlock() && LastNight.breakableBlockMap.containsKey(e.getClickedBlock().getType()) && allowBreakWorld.contains(e.getClickedBlock().getWorld().getName())) {
            if (!blockMap.containsKey(e.getClickedBlock().getLocation())) {
                blockMap.put(e.getClickedBlock().getLocation(), LastNight.breakableBlockMap.get(e.getClickedBlock().getType()));
            }
            blockMap.put(e.getClickedBlock().getLocation(), blockMap.get(e.getClickedBlock().getLocation()) - 1);
        }
    }
    
    @EventHandler
    public void ProjectileHitEvent(ProjectileHitEvent e) {
        Projectile p = (Projectile) e.getEntity();
        if (p.getShooter() instanceof LivingEntity) {
            if (e.getHitBlock() != null) {
                if (!p.getMetadata("lastnight_damage").isEmpty() && p.getMetadata("lastnight_damage").get(0) != null && LastNight.breakableBlockMap.containsKey(e.getHitBlock().getType()) && allowBreakWorld.contains(e.getHitBlock().getWorld().getName())) {
                    if (!p.getMetadata("lastnight_range").isEmpty() && p.getMetadata("lastnight_range").get(0).asDouble() >= e.getEntity().getLocation().distance(((LivingEntity) p.getShooter()).getLocation())) {
                        if (!blockMap.containsKey(e.getHitBlock().getLocation())) {
                            blockMap.put(e.getHitBlock().getLocation(), LastNight.breakableBlockMap.get(e.getHitBlock().getType()));
                        }
                        blockMap.put(e.getHitBlock().getLocation(), blockMap.get(e.getHitBlock().getLocation()) - p.getMetadata("lastnight_damage").get(0).asInt());
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            sendPacketPlayOutBlockBreakAnimation(player, e.getHitBlock().getLocation(), 10 * (LastNight.breakableBlockMap.get(e.getHitBlock().getType()) - blockMap.get(e.getHitBlock().getLocation())) / LastNight.breakableBlockMap.get(e.getHitBlock().getType()));
                        }
                    } else {
                        if (!blockMap.containsKey(e.getHitBlock().getLocation())) {
                            blockMap.put(e.getHitBlock().getLocation(), LastNight.breakableBlockMap.get(e.getHitBlock().getType()));
                        }
                        blockMap.put(e.getHitBlock().getLocation(), blockMap.get(e.getHitBlock().getLocation()) - 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            sendPacketPlayOutBlockBreakAnimation(player, e.getHitBlock().getLocation(), 10 * (LastNight.breakableBlockMap.get(e.getHitBlock().getType()) - blockMap.get(e.getHitBlock().getLocation())) / LastNight.breakableBlockMap.get(e.getHitBlock().getType()));
                        }
                    }
                    if (blockMap.get(e.getHitBlock().getLocation()) < 0) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            sendPacketPlayOutBlockBreakAnimation(player, e.getHitBlock().getLocation(), 10);
                        }
                        sendPacketPlayOutTitle((Player) p.getShooter(), e.getHitBlock().getType() + " §6" + 0 + "§e/§a" + LastNight.breakableBlockMap.get(e.getHitBlock().getType()), 10, 30, 10);
                        e.getHitBlock().setType(Material.AIR);
                        blockMap.remove(e.getHitBlock().getLocation());
                    } else {
                        sendPacketPlayOutTitle((Player) p.getShooter(), e.getHitBlock().getType() + " §6" + blockMap.get(e.getHitBlock().getLocation()) + "§e/§a" + LastNight.breakableBlockMap.get(e.getHitBlock().getType()), 10, 30, 10);
                    }
                }
            }
        }
    }
    
    public static void sendPacketPlayOutBlockBreakAnimation(Player player, Location loc, int value) {
        if (!blockIDMap.containsKey(loc)) {
            blockIDMap.put(loc, id++);
        }
        try {
            Class<?> packetPlayOutBlockBreakAnimationClass = NBT.Package.MINECRAFT_SERVER.getClass("PacketPlayOutBlockBreakAnimation");
            Class<?> blockPositionClass = NBT.Package.MINECRAFT_SERVER.getClass("BlockPosition");
            Class<?> packetClass = NBT.Package.MINECRAFT_SERVER.getClass("Packet");
            Object packet = NBT.create(packetPlayOutBlockBreakAnimationClass,
                    new NBT.ParamGroup(blockIDMap.get(loc), int.class),
                    new NBT.ParamGroup(NBT.create(blockPositionClass, new NBT.ParamGroup(loc.getBlockX(), int.class), new NBT.ParamGroup(loc.getBlockY(), int.class), new NBT.ParamGroup(loc.getBlockZ(), int.class)), blockPositionClass),
                    new NBT.ParamGroup(value, int.class));
            Object nmsPlayer = NBT.doMethod(player, "getHandle");
            Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            NBT.doMethod(playerConnection, "sendPacket", new NBT.ParamGroup(packet, packetClass));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
        }
    }
    
    public static void sendPacketPlayOutTitle(Player player, String str, int a, int b, int c) {
        try {
            Class<?> packetPlayOutTitleClass = NBT.Package.MINECRAFT_SERVER.getClass("PacketPlayOutTitle");
            Class<?> enumTitleActionClass = NBT.Package.MINECRAFT_SERVER.getClass("PacketPlayOutTitle$EnumTitleAction");
            Class<?> chatComponentTextClass = NBT.Package.MINECRAFT_SERVER.getClass("ChatComponentText");
            Class<?> iChatBaseComponentClass = NBT.Package.MINECRAFT_SERVER.getClass("IChatBaseComponent");
            Class<?> packetClass = NBT.Package.MINECRAFT_SERVER.getClass("Packet");
            Object packet = NBT.create(packetPlayOutTitleClass,
                    new NBT.ParamGroup(enumTitleActionClass.getField("ACTIONBAR").get(null), enumTitleActionClass),
                    new NBT.ParamGroup(NBT.create(chatComponentTextClass, new NBT.ParamGroup(str, String.class)), iChatBaseComponentClass),
                    new NBT.ParamGroup(a, int.class),
                    new NBT.ParamGroup(b, int.class),
                    new NBT.ParamGroup(c, int.class)
            );
            Object nmsPlayer = NBT.doMethod(player, "getHandle");
            Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            NBT.doMethod(playerConnection, "sendPacket", new NBT.ParamGroup(packet, packetClass));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
        }
    }
}
