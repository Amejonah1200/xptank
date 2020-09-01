package com.github.amejonah1200.xptank.utils;

import com.github.johnnyjayjay.compatre.NmsDependent;
import org.bukkit.inventory.ItemStack;

@NmsDependent
public class NBTTagger {
  
  public static <T> ItemStack setNBTTag(String key, T o, NBTTagType<T> type, ItemStack itemStack) {
    net.minecraft.server.v1_16_R1.ItemStack nmsItem = (net.minecraft.server.v1_16_R1.ItemStack) getNMSItem(
        itemStack);
    net.minecraft.server.v1_16_R1.NBTTagCompound comp = (net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(
        nmsItem);
    switch (type.toString()) {
      case "BOOLEAN":
        comp.setBoolean(key, (boolean) o);
        break;
      case "BYTE":
        comp.setByte(key, (byte) o);
        break;
      case "BYTEARRAY":
        comp.setByteArray(key, (byte[]) o);
        break;
      case "DOUBLE":
        comp.setDouble(key, (double) o);
        break;
      case "FLOAT":
        comp.setFloat(key, (long) o);
        break;
      case "INT":
        comp.setInt(key, (int) o);
        break;
      case "INTARRAY":
        comp.setIntArray(key, (int[]) o);
        break;
      case "LONG":
        comp.setLong(key, (long) o);
        break;
      case "NBTBASE":
        comp.set(key, (net.minecraft.server.v1_16_R1.NBTBase) o);
        break;
      case "SHORT":
        comp.setShort(key, (short) o);
        break;
      case "STRING":
        comp.setString(key, (String) o);
      default:
    }
    nmsItem.setTag(comp);
    return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(nmsItem);
  }
  
  public static net.minecraft.server.v1_16_R1.NBTBase getNBTTag(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).get(key);
  }
  
  public static String getNBTTagString(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).getString(key);
  }
  
  public static int getNBTTagInt(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).getInt(key);
  }
  
  public static ItemStack removeNBTTag(String key, ItemStack itemStack) {
    net.minecraft.server.v1_16_R1.ItemStack nmsItem = (net.minecraft.server.v1_16_R1.ItemStack) getNMSItem(
        itemStack);
    net.minecraft.server.v1_16_R1.NBTTagCompound comp = (net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(
        nmsItem);
    comp.remove(key);
    nmsItem.setTag(comp);
    return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(nmsItem);
  }
  
  public static Object getNBTTagList(String key, ItemStack itemStack, int type) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack)))
        .getList(key, type);
  }
  
  public static boolean getNBTTagBoolean(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).getBoolean(key);
  }
  
  public static boolean hasTag(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).hasKey(key);
  }
  
  public static float getNBTTagFloat(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).getFloat(key);
  }
  
  public static long getNBTTagLong(String key, ItemStack itemStack) {
    return ((net.minecraft.server.v1_16_R1.NBTTagCompound) getNMSCompound(getNMSItem(itemStack))).getLong(key);
  }
  
  public static Object getNMSItem(ItemStack itemStack) {
    return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(itemStack);
  }
  
  public static Object getNMSCompound(Object nmsItem) {
    return (((net.minecraft.server.v1_16_R1.ItemStack) nmsItem)
                .hasTag()) ? ((net.minecraft.server.v1_16_R1.ItemStack) nmsItem)
        .getTag() : new net.minecraft.server.v1_16_R1.NBTTagCompound();
  }
  
  public static ItemStack resetNBTTag(ItemStack itemStack) {
    net.minecraft.server.v1_16_R1.ItemStack nms = (net.minecraft.server.v1_16_R1.ItemStack) getNMSItem(itemStack);
    nms.setTag(new net.minecraft.server.v1_16_R1.NBTTagCompound());
    return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(nms);
  }
  
  public static ItemStack setNBTTag(String tag, ItemStack itemStack) throws Throwable {
    net.minecraft.server.v1_16_R1.ItemStack nms = (net.minecraft.server.v1_16_R1.ItemStack) getNMSItem(itemStack);
    nms.setTag(net.minecraft.server.v1_16_R1.MojangsonParser.parse(tag));
    return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(nms);
  }
  
  public static final class NBTTagType<T> {
    
    public static NBTTagType<?> NBTBASE = new NBTTagType<>("NBTBase", VersionUtils.getNMSClass("NBTBase"));
    public static NBTTagType<String> STRING = new NBTTagType<>("String", String.class);
    public static NBTTagType<Double> DOUBLE = new NBTTagType<>(double.class);
    public static NBTTagType<Integer> INT = new NBTTagType<>(int.class);
    public static NBTTagType<Long> LONG = new NBTTagType<>(long.class);
    public static NBTTagType<Float> FLOAT = new NBTTagType<>(float.class);
    public static NBTTagType<Boolean> BOOLEAN = new NBTTagType<>(boolean.class);
    public static NBTTagType<Byte> BYTE = new NBTTagType<>(byte.class);
    public static NBTTagType<Short> SHORT = new NBTTagType<>(short.class);
    public static NBTTagType<byte[]> BYTEARRAY = new NBTTagType<>("ByteArray", byte[].class);
    public static NBTTagType<int[]> INTARRAY = new NBTTagType<>("IntArray", int[].class);
    private final String reflectionName;
    private final Class<T> clazz;
    
    NBTTagType(String reflectionName, Class<T> clazz) {
      this.reflectionName = reflectionName;
      this.clazz = clazz;
    }
    
    NBTTagType(Class<T> clazz) {
      this(clazz.getName(), clazz);
    }
    
    public Class<T> getClazz() {
      return clazz;
    }
    
    public String getReflectionName() {
      return reflectionName;
    }
    
    @Override
    public String toString() {
      return getReflectionName().toUpperCase();
    }
  }
}
