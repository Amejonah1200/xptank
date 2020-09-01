package com.github.amejonah1200.xptank.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {
  
  protected ItemStack itemStack;
  protected ItemMeta itemMeta;
  private List<String> lore;
  
  public ItemBuilder(ItemStack itemStack) {
    this.itemStack = itemStack.clone();
    this.itemMeta = itemStack.getItemMeta();
    this.lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
  }
  
  public ItemBuilder(Material material) {
    this(new ItemStack(material));
  }
  
  public ItemBuilder(GlassColor color, boolean pane) {
    this(color.getMaterial(pane));
    if (MCVersion.getVersion().isMajorOlderThan(13)) setDamage(color.getSubId());
  }
  
  public ItemBuilder setDamage(short damage) {
    itemStack.setDurability(damage);
    return this;
  }
  
  public ItemBuilder addLineToLore(String lore) {
    if (lore == null) return this;
    this.lore.add(lore);
    return this;
  }
  
  public ItemBuilder addToLore(List<String> lore) {
    if (lore != null && !lore.isEmpty()) this.lore.addAll(lore);
    return this;
  }
  
  public ItemBuilder addToLore(String... lore) {
    if (lore == null || lore.length == 0) return this;
    return addToLore(Arrays.asList(lore));
  }
  
  public ItemBuilder setName(String name) {
    itemMeta.setDisplayName(name);
    return this;
  }
  
  public <T> ItemBuilder setNBTTag(String key, T value, NBTTagger.NBTTagType<T> type) {
    itemStack.setItemMeta(itemMeta);
    try {
      itemMeta = (itemStack = NBTTagger.setNBTTag(key, value, type, itemStack)).getItemMeta();
    } catch (Throwable e) {}
    return this;
  }
  
  public ItemBuilder resetNBTTags() {
    itemStack.setItemMeta(itemMeta);
    try {
      itemMeta = (itemStack = NBTTagger.resetNBTTag(itemStack)).getItemMeta();
    } catch (Throwable e) {}
    return this;
  }
  
  public ItemBuilder removeNBTTag(String key) {
    itemStack.setItemMeta(itemMeta);
    try {
      itemMeta = (itemStack = NBTTagger.removeNBTTag(key, itemStack)).getItemMeta();
    } catch (Throwable e) {}
    itemMeta = itemStack.getItemMeta();
    return this;
  }
  
  public ItemStack build() {
    if (lore == null || lore.isEmpty()) lore = null;
    itemMeta.setLore(lore);
    itemStack.setItemMeta(itemMeta);
    return itemStack.clone();
  }
  
  public ItemBuilder setAmount(int amount) {
    itemStack.setAmount(amount);
    return this;
  }
  
  public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
    itemMeta.addEnchant(enchantment, level, false);
    return this;
  }
  
  public ItemBuilder addFlag(ItemFlag flag) {
    itemMeta.addItemFlags(flag);
    return this;
  }
  
  public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
    itemMeta.addEnchant(enchantment, level, true);
    return this;
  }
}
