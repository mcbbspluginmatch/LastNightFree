package lvhaoxuan.last.night.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemSerializerUtils {

    private static Method WRITE_NBT;
    private static Method READ_NBT;

    public static String toBase64(ItemStack[] items) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
        Object localNBTTagList = null;
        try {
            localNBTTagList = NBT.Package.MINECRAFT_SERVER.getClass("NBTTagList").getConstructor().newInstance();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println("错误: " + e.getMessage());
        }
        try {
            for (ItemStack item : items) {
                Object localCraftItemStack = NBT.Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack")
                        .getMethod("asCraftCopy", ItemStack.class).invoke(item, item);
                Object localNBTTagCompound = NBT.Package.MINECRAFT_SERVER.getClass("NBTTagCompound").getConstructor().newInstance();
                if (localCraftItemStack != null) {
                    try {
                        Object nmsItem = new NBT(item).nmsItem;
                        nmsItem.getClass().getMethod("save", NBT.Package.MINECRAFT_SERVER.getClass("NBTTagCompound")).invoke(nmsItem,
                                localNBTTagCompound);
                    } catch (NullPointerException localNullPointerException) {
                        System.out.println("错误: " + localNullPointerException.getMessage());
                    }
                }
                localNBTTagList.getClass().getMethod("add", NBT.Package.MINECRAFT_SERVER.getClass("NBTBase")).invoke(localNBTTagList,
                        localNBTTagCompound);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println("错误: " + e.getMessage());
        }

        if (WRITE_NBT == null) {
            try {
                WRITE_NBT = NBT.Package.MINECRAFT_SERVER.getClass("NBTCompressedStreamTools").getDeclaredMethod("a",
                        new Class[]{NBT.Package.MINECRAFT_SERVER.getClass("NBTBase"), DataOutput.class});
                WRITE_NBT.setAccessible(true);
            } catch (NoSuchMethodException | SecurityException localException1) {
                throw new IllegalStateException("未找到写入方法", localException1);
            }
        }
        try {
            WRITE_NBT.invoke(null, new Object[]{localNBTTagList, localDataOutputStream});
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException localException2) {
            throw new IllegalArgumentException("无法写入" + localNBTTagList + "至" + localDataOutputStream, localException2);
        }
        return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
    }

    public static ItemStack[] fromBase64(String paramString) {
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
        Object localNBTTagList = readNbt(new DataInputStream(localByteArrayInputStream)); // NBTTagList
        ItemStack[] arrayOfItemStack;
        try {
            arrayOfItemStack = new ItemStack[(int) localNBTTagList.getClass().getMethod("size").invoke(localNBTTagList)];

            for (int i = 0; i < arrayOfItemStack.length; i++) {
                Object localNBTTagCompound = localNBTTagList.getClass().getMethod("get", Integer.TYPE).invoke(localNBTTagList, i);
                if (!(boolean) localNBTTagCompound.getClass().getMethod("isEmpty").invoke(localNBTTagCompound)) {
                    String version = NBT.Package.getServerVersion();
                    int subVersion = Integer.valueOf(version.split("_")[1]);
                    if (subVersion >= 11) {
                        Constructor<?> constructor = NBT.Package.MINECRAFT_SERVER.getClass("ItemStack").getConstructor(NBT.Package.MINECRAFT_SERVER.getClass("NBTTagCompound"));
                        Object nmsItem = constructor.newInstance(localNBTTagCompound);
                        arrayOfItemStack[i] = (ItemStack) NBT.Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack").getMethod("asCraftMirror", NBT.Package.MINECRAFT_SERVER.getClass("ItemStack")).invoke(nmsItem, nmsItem);
                    } else {
                        arrayOfItemStack[i] = (ItemStack) NBT.Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack")
                                .getMethod("asCraftMirror", NBT.Package.MINECRAFT_SERVER.getClass("ItemStack"))
                                .invoke(localNBTTagCompound, NBT.Package.MINECRAFT_SERVER.getClass("ItemStack")
                                        .getMethod("createStack", NBT.Package.MINECRAFT_SERVER.getClass("NBTTagCompound"))
                                        .invoke(localNBTTagCompound, localNBTTagCompound));
                    }
                }
            }

            return arrayOfItemStack;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println("错误: " + e.getMessage());
        }
        return null;
    }

    private static Object readNbt(DataInput paramDataInput) {
        if (READ_NBT == null) {
            try {
                READ_NBT = NBT.Package.MINECRAFT_SERVER.getClass("NBTCompressedStreamTools").getDeclaredMethod("a",
                        new Class[]{DataInput.class, Integer.TYPE, NBT.Package.MINECRAFT_SERVER.getClass("NBTReadLimiter")});
                READ_NBT.setAccessible(true);
            } catch (NoSuchMethodException | SecurityException localException1) {
                throw new IllegalStateException("未找到方法.", localException1);
            }
        }
        try {
            Object limiter = NBT.Package.MINECRAFT_SERVER.getClass("NBTReadLimiter").getConstructor(Long.TYPE)
                    .newInstance(9223372036854775807L);
            return (Object) READ_NBT.invoke(null, new Object[]{paramDataInput, 0, limiter});
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException localException2) {
            throw new IllegalArgumentException("无法从该位置读取数据" + paramDataInput, localException2);
        }
    }
}
