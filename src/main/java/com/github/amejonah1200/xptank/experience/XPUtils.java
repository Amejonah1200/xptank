package com.github.amejonah1200.xptank.experience;

import org.bukkit.entity.Player;

public class XPUtils {
  
  public static final int HIGH_LIMIT = getExpFromLevel(21863);
  
  public static int getExpFromLevel(int level) {
    if (level == 0) return 0;
    boolean positive = level > 0;
    if (!positive) level *= -1;
    if (level > 30) { return (positive ? 1 : -1) * (int) (4.5 * level * level - 162.5 * level + 2220); }
    if (level > 15) { return (positive ? 1 : -1) * (int) (2.5 * level * level - 40.5 * level + 360); }
    return (positive ? 1 : -1) * (level * level + 6 * level);
  }
  
  public static int getExpToNext(int level) {
    if (level > 30) return 9 * level - 158;
    if (level > 15) return 5 * level - 38;
    return 2 * level + 7;
  }
  
  public static double getLevelFromExp(int exp) {
    if (exp <= 0) return 0;
    if (exp > 1395) return (Math.sqrt(72D * exp - 54215) + 325.0D) / 18.0D;
    if (exp > 315) return Math.sqrt(40D * exp - 7839) / 10.0D + 8.1D;
    return Math.sqrt(9D + exp) - 3.0D;
  }
  
  public static int getExperienceFromPlayer(Player p) {
    return getExperienceWithLevelAndProgress(p.getLevel(), p.getExp());
  }
  
  public static int getExperienceWithLevelAndProgress(int level, float progress) {
    return getExpFromLevel(level) + Math.round(getExpToNext(level) * progress);
  }
  
  public static boolean hasLevel(int level, int exp) {
    if (level < 1) { return false; }
    return getExpFromLevel(level) <= exp;
  }
  
  public static int getExpNeededFromToLevel(double from, double to) {
    if (from < 0) from = 0;
    if (to < 0) to = 0;
    if (from == to) return 0;
    return getExperienceFromLevel(to) - getExperienceFromLevel(from);
  }
  
  public static int getExperienceFromLevel(double level) {
    if (level <= 0) return 0;
    return (int) (getExpFromLevel((int) level) + (getExpToNext((int) level) * (level - Math.floor(level))));
  }
  
  public static double getLevel(Player player) {
    return (double) player.getLevel() + (double) player.getExp();
  }
  
  public static void setPlayerExp(Player player, int exp) {
    if (exp < 0) { return; }
    player.setLevel((int) getLevelFromExp(exp));
    player.setExp(0);
    player.setTotalExperience(XPUtils.getExpFromLevel(player.getLevel()));
    player.giveExp(exp - player.getTotalExperience());
  }
  
  public static int roundLevel(Player player) {
    final int xpBefore = getExperienceFromPlayer(player);
    player.setExp(0);
    player.setTotalExperience(getExpFromLevel(player.getLevel()));
    return xpBefore - player.getTotalExperience();
  }
}
