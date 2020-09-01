package com.github.amejonah1200.xptank.experience;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * The type Xp holder.
 */
public class XPHolder implements Cloneable {
  
  /**
   * The Xp.
   */
  protected int xp;
  /**
   * The Lvl limit.
   */
  protected int lvlLimit;
  
  /**
   * Instantiates a new XPHolder with exp and level limit.
   *
   * @param xp the xp
   * @param lvlLimit the lvl limit
   */
  public XPHolder(int xp, int lvlLimit) {
    this.xp = xp;
    this.lvlLimit = (lvlLimit = Math.max(0, Math.min(lvlLimit, 21863))) == 0 ? 21863 : lvlLimit;
  }
  
  /**
   * Instantiates a new XPHolder with exp and standard level limit (21863).
   *
   * @param xp the xp
   */
  public XPHolder(int xp) {
    this(xp, -1);
  }
  
  /**
   * Instantiates a new XPHolder using player's exp.
   *
   * @param player the player
   */
  public XPHolder(Player player) {
    this(XPUtils.getExperienceFromPlayer(player));
  }
  
  /**
   * Instantiates a new Xp holder.
   */
  public XPHolder() {
    this(0);
  }
  
  /**
   * Gets level limit.
   *
   * @return the level limit
   */
  public int getLvlLimit() {
    return lvlLimit;
  }
  
  /**
   * Gets experience.
   *
   * @return the experience
   */
  public int getExperience() {
    return xp;
  }
  
  /**
   * Gets levels.
   *
   * @return the level
   */
  public double getLevel() {
    return XPUtils.getLevelFromExp(xp);
  }
  
  /**
   * Gets level with round.
   *
   * @param decimals decimals to show
   *
   * @return the levels
   */
  public double getLevel(int decimals) {
    return ((int) (getLevel() * Math.pow(10, decimals))) / Math.pow(10, decimals);
  }
  
  /**
   * Sets level.
   *
   * @param level the level
   *
   * @return self
   */
  @Nonnull
  public XPHolder setLevel(double level) {
    this.xp = XPUtils.getExperienceFromLevel(level);
    return this;
  }
  
  /**
   * Sets exp.
   *
   * @param exp the exp
   *
   * @return self
   */
  @Nonnull
  public XPHolder setExp(int exp) {
    this.xp = Math.max(exp, 0);
    return this;
  }
  
  /**
   * Sets level limit.
   *
   * @param lvlLimit the level limit
   *
   * @return self
   */
  @Nonnull
  public XPHolder setLvlLimit(int lvlLimit) {
    this.lvlLimit = Math.max(-1, Math.min(lvlLimit, 21863));
    return this;
  }
  
  /**
   * Adds experience points to this holder.
   *
   * @param exp Experience points to add.
   *
   * @return Experience points which couldn't be added to this holder.
   */
  public int addExp(int exp) {
    if (exp == 0) return 0;
    long xp = this.xp;
    xp += exp;
    if (exp < 0) {
      this.xp = xp < 0 ? 0 : (int) xp;
    } else {
      this.xp = (int) Math.min(xp, XPUtils.getExpFromLevel(lvlLimit < 1 ? 21863 : lvlLimit));
    }
    return (int) Math.abs(xp - this.xp);
  }
  
  /**
   * Removes experience points from this holder.
   *
   * @param exp Experience points to remove.
   *
   * @return Experience points which couldn't be removed from this holder.
   */
  public int removeExp(int exp) {
    return addExp(-exp);
  }
  
  /**
   * Adds levels to this holder. It calculates the amount of experience points needed to be add to increase the level amount.
   * Ex. (10 lvls in holder) + (10 lvls given by the parameter) = 20 levels after this.
   *
   * @param level Increase the amount of levels by this amount.
   *
   * @return Experience points which couldn't be added to this holder.
   */
  public int addLevel(double level) {
    return addExp(XPUtils.getExpNeededFromToLevel(getLevel(), getLevel() + level));
  }
  
  /**
   * Apply to player.
   *
   * @param player the player
   */
  public void applyToPlayer(Player player) {
    final int xpToAdd = xp - XPUtils.getExperienceFromPlayer(player);
    if (xpToAdd == 0) return;
    if (xpToAdd > 0) player.giveExp(xpToAdd);
    else XPUtils.setPlayerExp(player, xp);
  }
  
  /**
   * Rounds exp to level and returns rest.
   *
   * @return rest
   */
  public int round() {
    final int xpBefore = xp;
    xp = XPUtils.getExpFromLevel(((int) getLevel()));
    return Math.abs(xpBefore - xp);
  }
  
  public XPHolder clone() throws CloneNotSupportedException {
    super.clone();
    return new XPHolder(xp, lvlLimit);
  }
  
  /**
   * Is full.
   *
   * @return if it's full
   */
  public boolean isFull() {
    return getLevel() >= lvlLimit;
  }
}
