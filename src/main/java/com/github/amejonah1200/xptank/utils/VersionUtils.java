package com.github.amejonah1200.xptank.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class VersionUtils {
  
  public static boolean isInOffHand(PlayerInteractEvent event) {
    if (MCVersion.getVersion().isMajorOlderThan(12)) return false;
    return event.getHand() == EquipmentSlot.OFF_HAND;
  }
  
  public static Class<?> getNMSClass(String paramString) {
    return getReflectedClass("net.minecraft.server." + getServerVersion() + "." + paramString);
  }
  
  public static Class<?> getOBClass(String className) {
    return getReflectedClass("org.bukkit.craftbukkit." + getServerVersion() + "." + className);
  }
  
  private static Class<?> getReflectedClass(String path) {
    try {
      return Class.forName(path);
    } catch (ClassNotFoundException exception) {
      exception.printStackTrace();
      System.err.println("Unable to find class with reflection: " + path);
    }
    return null;
  }
  
  private static String getServerVersion() {
    return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
  }
  
  public static Material getXPBottleMaterial() {
    // Material.EXP_BOTTLE; 1.8-1.12.2
    // Material.EXPERIENCE_BOTTLE; 1.13-1.15.x
    if (MCVersion.getVersion().isMajorOlderThan(13)) return Material.valueOf("EXP_BOTTLE");
    return Material.valueOf("EXPERIENCE_BOTTLE");
  }
}
