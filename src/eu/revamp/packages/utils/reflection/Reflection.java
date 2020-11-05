package eu.revamp.packages.utils.reflection;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class Reflection {

    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        return version;
    }

    public static Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getNMSClassWithException(String className) throws Exception {
        String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = Class.forName(fullName);
        return clazz;
    }

    public static Class<?> getOBCClass(String className) {
        String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object getHandle(Object obj) {
        try {
            return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getFieldWithException(Class<?> clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) { return false; }
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
    }

    public static Constructor<?> getConstructor(String className, PackageType packageType, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getConstructor(packageType.getClass(className), parameterTypes);
    }

    public static Object instantiateObject(Class<?> clazz, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }

    public static Object instantiateObject(String className, PackageType packageType, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return instantiateObject(packageType.getClass(className), arguments);
    }


    public enum PackageType
    {
        MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
        CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
        CRAFTBUKKIT_BLOCK(PackageType.CRAFTBUKKIT, "block"),
        CRAFTBUKKIT_CHUNKIO(PackageType.CRAFTBUKKIT, "chunkio"),
        CRAFTBUKKIT_COMMAND(PackageType.CRAFTBUKKIT, "command"),
        CRAFTBUKKIT_CONVERSATIONS(PackageType.CRAFTBUKKIT, "conversations"),
        CRAFTBUKKIT_ENCHANTMENS(PackageType.CRAFTBUKKIT, "enchantments"),
        CRAFTBUKKIT_ENTITY(PackageType.CRAFTBUKKIT, "entity"),
        CRAFTBUKKIT_EVENT(PackageType.CRAFTBUKKIT, "event"),
        CRAFTBUKKIT_GENERATOR(PackageType.CRAFTBUKKIT, "generator"),
        CRAFTBUKKIT_HELP(PackageType.CRAFTBUKKIT, "help"),
        CRAFTBUKKIT_INVENTORY(PackageType.CRAFTBUKKIT, "inventory"),
        CRAFTBUKKIT_MAP(PackageType.CRAFTBUKKIT, "map"),
        CRAFTBUKKIT_METADATA(PackageType.CRAFTBUKKIT, "metadata"),
        CRAFTBUKKIT_POTION(PackageType.CRAFTBUKKIT, "potion"),
        CRAFTBUKKIT_PROJECTILES(PackageType.CRAFTBUKKIT, "projectiles"),
        CRAFTBUKKIT_SCHEDULER(PackageType.CRAFTBUKKIT, "scheduler"),
        CRAFTBUKKIT_SCOREBOARD(PackageType.CRAFTBUKKIT, "scoreboard"),
        CRAFTBUKKIT_UPDATER(PackageType.CRAFTBUKKIT, "updater"),
        CRAFTBUKKIT_UTIL(PackageType.CRAFTBUKKIT, "util");

        @Getter
        private String path;

        PackageType(String path) {
            this.path = path;
        }

        PackageType(PackageType parent, String path) {
            this(parent + "." + path);
        }

        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }

        public Class<?> getClass(String className) throws ClassNotFoundException {
            return Class.forName(this + "." + className);
        }

        @Override
        public String toString() {
            return this.path;
        }
    }

    public enum DataType
    {
        BYTE(Byte.TYPE, Byte.class),
        SHORT(Short.TYPE, Short.class),
        INTEGER(Integer.TYPE, Integer.class),
        LONG(Long.TYPE, Long.class),
        CHARACTER(Character.TYPE, Character.class),
        FLOAT(Float.TYPE, Float.class),
        DOUBLE(Double.TYPE, Double.class),
        BOOLEAN(Boolean.TYPE, Boolean.class);

        private static Map<Class<?>, DataType> CLASS_MAP;
        @Getter
        private Class<?> primitive;
        @Getter
        private Class<?> reference;

        DataType(Class<?> primitive, Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }

        public static DataType fromClass(Class<?> clazz) {
            return DataType.CLASS_MAP.get(clazz);
        }

        public static Class<?> getPrimitive(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return (type == null) ? clazz : type.getPrimitive();
        }

        public static Class<?> getReference(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return (type == null) ? clazz : type.getReference();
        }

        public static Class<?>[] getPrimitive(Class<?>[] classes) {
            int length = (classes == null) ? 0 : classes.length;
            Class<?>[] types = (Class<?>[])new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getPrimitive(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getReference(Class<?>[] classes) {
            int length = (classes == null) ? 0 : classes.length;
            Class<?>[] types = (Class<?>[])new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getReference(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getPrimitive(Object[] objects) {
            int length = (objects == null) ? 0 : objects.length;
            Class<?>[] types = (Class<?>[])new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return types;
        }

        public static Class<?>[] getReference(Object[] objects) {
            int length = (objects == null) ? 0 : objects.length;
            Class<?>[] types = (Class<?>[])new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getReference(objects[index].getClass());
            }
            return types;
        }

        public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }
            for (int index = 0; index < primary.length; ++index) {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass)) {
                    return false;
                }
            }
            return true;
        }

        static {
            CLASS_MAP = new HashMap<>();
            for (DataType type : values()) {
                DataType.CLASS_MAP.put(type.primitive, type);
                DataType.CLASS_MAP.put(type.reference, type);
            }
        }
    }
}
