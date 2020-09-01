package com.github.amejonah1200.xptank.experience;

import ap.amejonah.simplemessagesystem.SimpleMessageSystem;
import com.github.amejonah1200.xptank.XPTankPlugin;
import com.github.amejonah1200.xptank.utils.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class XPTank extends XPHolder {
  
  private boolean onUse;
  
  /**
   * Instantiates a new multiuse XPTank.
   *
   * @param exp the exp
   * @param lvlLimit the lvl limit
   */
  public XPTank(int exp, int lvlLimit) {
    super(exp, lvlLimit);
  }
  
  /**
   * Instantiates a new Xp tank.
   *
   * @param exp the exp
   * @param onUse if one use
   */
  public XPTank(int exp, boolean onUse) {
    super(exp);
    this.onUse = onUse;
  }
  
  /**
   * Is one use.
   *
   * @return if one use
   */
  public boolean isOnUse() {
    return onUse;
  }
  
  /**
   * Sets if it is on use.
   *
   * @param onUse if it's one use
   */
  public void setOnUse(boolean onUse) {
    this.onUse = onUse;
  }
  
  /**
   * Build the default xptank.
   *
   * @return xptank as an itemstack
   */
  @Nonnull
  public ItemStack build() {
    SimpleMessageSystem messageSystem = XPTankPlugin.getPlugin(XPTankPlugin.class).getSimpleMessageSystem();
    return applyXPTankToItemStack(new ItemBuilder(VersionUtils.getXPBottleMaterial()).setName(
        messageSystem.translate("xptank." + (onUse ? "oneuse" : "multiuse") + ".name", getLevel(2), getLvlLimit()))
        .addToLore(messageSystem
            .translate("xptank." + (onUse ? "oneuse" : "multiuse") + ".description", getLevel(2), getExperience(),
                getLvlLimit()).split("\n")).build());
  }
  
  private ItemStack applyXPTankToItemStack(ItemStack itemStack) {
    if (itemStack == null) return null;
    NmsItemModifier tank = new NmsItemModifier(itemStack).setNBTTag("xptank", true, NBTTagger.NBTTagType.BOOLEAN)
        .setNBTTag("xp", xp, NBTTagger.NBTTagType.INT).setNBTTag("oneuse", onUse, NBTTagger.NBTTagType.BOOLEAN);
    if (lvlLimit > 0 && !onUse) tank.setNBTTag("lvllimit", lvlLimit, NBTTagger.NBTTagType.INT);
    return tank.build();
  }
  
  @Override
  public XPTank clone() throws CloneNotSupportedException {
    return (XPTank) super.clone();
  }
  
  /**
   * Parse xp tank xp tank using an itemstack.
   *
   * @param itemStack the item stack
   *
   * @return the xp tank or null if is not a xptank
   */
  @Nullable
  public static XPTank parseXPTank(ItemStack itemStack) {
    if (itemStack == null || itemStack.getType() == Material.AIR || !NBTTagger.hasTag("xptank", itemStack))
      return null;
    NmsItemModifier tankItemModifier = new NmsItemModifier(itemStack);
    final boolean oneUse = tankItemModifier.getNBTTagBoolean("oneuse");
    final int xp = tankItemModifier.getNBTTagInt("xp");
    return oneUse ? new XPTank(xp, true) : new XPTank(xp, tankItemModifier.getNBTTagInt("lvllimit"));
  }
}

