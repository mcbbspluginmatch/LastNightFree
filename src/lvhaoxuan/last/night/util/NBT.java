package lvhaoxuan.last.night.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBT {

    public ItemStack item;
    public Object nmsItem;
    public Object compound;
    public static Class<?> NBTTagCompoundClazz = Package.MINECRAFT_SERVER.getClass("NBTTagCompound");
    public static Class<?> NBTBaseClazz = Package.MINECRAFT_SERVER.getClass("NBTBase");
    public static Class<?> NBTTagIntClazz = Package.MINECRAFT_SERVER.getClass("NBTTagInt");
    public static Class<?> NBTTagStringClazz = Package.MINECRAFT_SERVER.getClass("NBTTagString");
    public static Class<?> NBTTagDoubleClazz = Package.MINECRAFT_SERVER.getClass("NBTTagDouble");
    public static Class<?> NBTTagShortClazz = Package.MINECRAFT_SERVER.getClass("NBTTagShort");
    public static Class<?> NBTTagFloatClazz = Package.MINECRAFT_SERVER.getClass("NBTTagFloat");
    public static Class<?> CraftItemStackClazz = Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack");

    public NBT(ItemStack item) {
        this.item = item;
        nmsItem = doStaticMethod(CraftItemStackClazz, "asNMSCopy", new ParamGroup(item, ItemStack.class));
        compound = ((Boolean) doMethod(nmsItem, "hasTag")) ? doMethod(nmsItem, "getTag") : create(NBTTagCompoundClazz);
    }

    public void setInt(String key, Integer value) {
        if (value != null) {
            Object nms = create(NBTTagIntClazz, new ParamGroup(value, int.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setString(String key, String value) {
        if (value != null) {
            Object nms = create(NBTTagStringClazz, new ParamGroup(value));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setDouble(String key, Double value) {
        if (value != null) {
            Object nms = create(NBTTagDoubleClazz, new ParamGroup(value, double.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setShort(String key, Short value) {
        if (value != null) {
            Object nms = create(NBTTagShortClazz, new ParamGroup(value, short.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setFloat(String key, Float value) {
        if (value != null) {
            Object nms = create(NBTTagFloatClazz, new ParamGroup(value, float.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public Integer getInt(String key) {
        return ((Integer) doMethod(compound, "getInt", new ParamGroup(key)));
    }

    public String getString(String key) {
        return ((String) doMethod(compound, "getString", new ParamGroup(key)));
    }

    public Double getDouble(String key) {
        return ((Double) doMethod(compound, "getDouble", new ParamGroup(key)));
    }

    public Short getShort(String key) {
        return ((Short) doMethod(compound, "getShort", new ParamGroup(key)));
    }

    public Float getFloat(String key) {
        return ((Float) doMethod(compound, "getFloat", new ParamGroup(key)));
    }

    public ItemStack toItemStack() {
        doMethod(nmsItem, "setTag", new ParamGroup(compound, NBTTagCompoundClazz));
        return (ItemStack) doStaticMethod(CraftItemStackClazz, "asBukkitCopy", new ParamGroup(nmsItem));
    }

    public static Object create(Class<?> clazz, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Constructor<?> cons = clazz.getConstructor(types);
            return cons.newInstance(objs);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static Object doMethod(Object obj, String methodName, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Method method = obj.getClass().getMethod(methodName, types);
            return method.invoke(obj, objs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static Object doStaticMethod(Class<?> clazz, String methodName, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Method method = clazz.getMethod(methodName, types);
            return method.invoke(null, objs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static enum Package {
        MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
        CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion());

        private String path;

        private Package(String path) {
            this.path = path;
        }

        public Class<?> getClass(String className) {
            try {
                return Class.forName(path + "." + className);
            } catch (ClassNotFoundException ex) {
            }
            return null;
        }

        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }
    }

    public static class ParamGroup {

        public Object obj;
        public Class<?> type;

        public ParamGroup(Object obj, Class<?> type) {
            this.obj = obj;
            this.type = type;
        }

        public ParamGroup(Object obj) {
            this.obj = obj;
            this.type = obj.getClass();
        }
    }
}
