package com.github.amejonah1200.xptank.utils;

import org.bukkit.Material;

public enum GlassColor {
  CLEAR,
  WHITE,
  ORANGE,
  MAGENTA,
  LIGHT_BLUE,
  YELLOW,
  LIME,
  PINK,
  GRAY,
  LIGHT_GRAY,
  CYAN,
  PURPLE,
  BLUE,
  BROWN,
  GREEN,
  RED,
  BLACK;
  
  public Material getMaterial(boolean pane) {
    if (this == CLEAR) return !pane ? Material.GLASS : MCVersion.getVersion().isMajorOlderThan(13) ? Material
        .valueOf("THIN_GLASS") : Material.valueOf("GLASS__PANE");
    String name = MCVersion.getVersion().isMajorOlderThan(13) ? "" : this.toString() + "_";
    return Material.valueOf(name + "STAINED_GLASS" + (pane ? "_PANE" : ""));
  }
  
  public short getSubId() {
    return MCVersion.getVersion().isMajorNewerThan(13) ? 0 : (short) (this == CLEAR ? 0 : ordinal() - 1);
  }
}
