package com.github.amejonah1200.xptank.utils;

import com.github.johnnyjayjay.compatre.NmsDependent;
import org.bukkit.inventory.ItemStack;

@NmsDependent
public class NmsItemModifier {
  
  protected net.minecraft.server.v1_16_R1.ItemStack itemStack;
  protected net.minecraft.server.v1_16_R1.NBTTagCompound compound;
  
  public NmsItemModifier(ItemStack itemStack) {
    this.itemStack = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(itemStack);
    this.compound = (this.itemStack.hasTag()) ? this.itemStack
        .getTag() : new net.minecraft.server.v1_16_R1.NBTTagCompound();
  }
  
  public <T> NmsItemModifier setNBTTag(String key, T o, NBTTagger.NBTTagType<T> type) {
    switch (type.toString()) {
      case "BOOLEAN":
        compound.setBoolean(key, (boolean) o);
        break;
      case "BYTE":
        compound.setByte(key, (byte) o);
        break;
      case "BYTEARRAY":
        compound.setByteArray(key, (byte[]) o);
        break;
      case "DOUBLE":
        compound.setDouble(key, (double) o);
        break;
      case "FLOAT":
        compound.setFloat(key, (long) o);
        break;
      case "INT":
        compound.setInt(key, (int) o);
        break;
      case "INTARRAY":
        compound.setIntArray(key, (int[]) o);
        break;
      case "LONG":
        compound.setLong(key, (long) o);
        break;
      case "NBTBASE":
        compound.set(key, (net.minecraft.server.v1_16_R1.NBTBase) o);
        break;
      case "SHORT":
        compound.setShort(key, (short) o);
        break;
      case "STRING":
        compound.setString(key, (String) o);
      default:
    }
    return this;
  }
  
  public net.minecraft.server.v1_16_R1.NBTBase getNBTBase(String key) {
    return compound.get(key);
  }
  
  public String getNBTTagString(String key) {
    return compound.getString(key);
  }
  
  public int getNBTTagInt(String key) {
    return compound.getInt(key);
  }
  
  public NmsItemModifier removeNBTTag(String key) {
    compound.remove(key);
    return this;
  }
  
  public Object getNBTTagList(String key, ItemStack itemStack, int type) {
    return compound.getList(key, type);
  }
  
  public boolean getNBTTagBoolean(String key) {
    return compound.getBoolean(key);
  }
  
  public boolean hasTag(String key) {
    return compound.hasKey(key);
  }
  
  public float getNBTTagFloat(String key) {
    return compound.getFloat(key);
  }
  
  public long getNBTTagLong(String key) {
    return compound.getLong(key);
  }
  
  public NmsItemModifier resetNBTTag() {
    itemStack.setTag(compound = new net.minecraft.server.v1_16_R1.NBTTagCompound());
    return this;
  }
  
  public NmsItemModifier setNBTTag(String tag) throws Throwable {
    itemStack.setTag(compound = net.minecraft.server.v1_16_R1.MojangsonParser.parse(tag));
    return this;
  }
  
  public ItemStack build() {
    itemStack.setTag(compound);
    return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(itemStack);
  }
}
