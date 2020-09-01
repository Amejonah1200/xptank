package com.github.amejonah1200.xptank.menu;

import ap.amejonah.simplemessagesystem.SimpleMessageSystem;
import com.github.amejonah1200.xptank.event.XPTankEvent;
import com.github.amejonah1200.xptank.experience.*;
import com.github.amejonah1200.xptank.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

final class XPTankMenu {
  
  private static final ItemStack[] INVENTORY_TEMPLATE = new ItemStack[45];
  
  static {
    final ItemStack BLACK_GLASS = new ItemBuilder(GlassColor.BLACK, true).setName("§a").build();
    for (int i = 0; i < 45; i++) {
      INVENTORY_TEMPLATE[i] = BLACK_GLASS;
    }
    for (int row = 0; row < 2; row++) {
      for (int column = 0; column < 7; column++) {
        INVENTORY_TEMPLATE[19 + (row * 9) + column] = new NmsItemModifier(
            new ItemBuilder(getColorFromColumn(column), true).build())
            .setNBTTag("amount", (int) ((column - 3 < 0 ? -1 : 1) * Math.pow(10, Math.abs(column - 3) - 1)),
                NBTTagger.NBTTagType.INT).setNBTTag("player", row == 1, NBTTagger.NBTTagType.BOOLEAN)
            .setNBTTag("button", true, NBTTagger.NBTTagType.BOOLEAN).build();
      }
    }
  }
  
  private static GlassColor getColorFromColumn(int column) {
    switch (column) {
      case 0:
        return GlassColor.RED;
      case 1:
      case 2:
        return GlassColor.ORANGE;
      case 3:
        return GlassColor.YELLOW;
      case 4:
      case 5:
        return GlassColor.LIME;
      case 6:
        return GlassColor.GREEN;
      default:
        return GlassColor.CLEAR;
    }
  }
  
  private final MenuController menuController;
  private final Player viewer;
  private boolean opened;
  
  public XPTankMenu(MenuController menuController, Player viewer) {
    this.menuController = menuController;
    this.viewer = viewer;
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public void open() {
    Inventory inv;
    XPTank xpTank = XPTank.parseXPTank(viewer.getItemInHand());
    if (xpTank == null) {
      close();
      return;
    }
    if (opened) {
      inv = viewer.getOpenInventory().getTopInventory();
      inv.setItem(4, new ItemBuilder(VersionUtils.getXPBottleMaterial())
          .setName(messageSystem().translate("gui.preview", xpTank.getLevel(2), xpTank.getLvlLimit())).build());
    } else {
      XPTankEvent xpTankEvent = new XPTankEvent(viewer, xpTank, XPTankEvent.XPTankAction.GUI_OPEN, 0);
      Bukkit.getPluginManager().callEvent(xpTankEvent);
      if (xpTankEvent.isCancelled()) {
        close();
        return;
      }
      String title = messageSystem().translate("gui.title", xpTank.getLevel(2));
      if (title.length() > 32) title = title.substring(0, 32);
      inv = Bukkit.createInventory(null, 45, title);
      inv.setContents(INVENTORY_TEMPLATE);
      inv.setItem(4, new ItemBuilder(VersionUtils.getXPBottleMaterial())
          .setName(messageSystem().translate("gui.preview", xpTank.getLevel(2), xpTank.getLvlLimit())).build());
      viewer.openInventory(inv);
      opened = true;
    }
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public Player getViewer() {
    return viewer;
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public boolean isOpened() {
    return opened;
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public void setOpened(boolean opened) {
    this.opened = opened;
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public void click(InventoryClickEvent event) {
    event.setCancelled(true);
    if (!event.getInventory().equals(event.getClickedInventory()) ||
        !NBTTagger.hasTag("button", event.getCurrentItem())) return;
    XPTank xpTank = XPTank.parseXPTank(event.getWhoClicked().getItemInHand());
    if (xpTank == null || !viewer.hasPermission("apxptank.xptank.multiuse") || xpTank.isOnUse()) {
      close();
      return;
    }
    XPHolder playerExp = new XPHolder((Player) event.getWhoClicked());
    final int amount = NBTTagger.getNBTTagInt("amount", event.getCurrentItem());
    boolean isPlayer = NBTTagger.getNBTTagBoolean("player", event.getCurrentItem());
    playerExp.addLevel(0);
    XPHolder[] holders = isPlayer ? new XPHolder[] { playerExp, xpTank } : new XPHolder[] { xpTank, playerExp };
    if (holders[amount > 0 ? 0 : 1].isFull()) return;
    int movingExp = amount == 0 ? holders[0].round() : XPUtils
        .getExpNeededFromToLevel(holders[0].getLevel(), holders[0].getLevel() + amount);
    if (movingExp == 0) return;
    if (amount == 0) {
      if (holders[1].addExp(movingExp) != 0) return;
      if (isPlayer) XPUtils.roundLevel(viewer);
      else playerExp.applyToPlayer(viewer);
    } else {
      if (amount > 0) {
        movingExp = Math.min(holders[1].getExperience(), movingExp);
        holders[1].removeExp(movingExp);
        holders[1].addExp(holders[0].addExp(movingExp));
      } else {
        movingExp += holders[0].addExp(movingExp);
        holders[0].addExp(holders[1].removeExp(movingExp));
      }
      playerExp.applyToPlayer(viewer);
    }
    event.getWhoClicked().setItemInHand(xpTank.build());
    open();
  }
  
  /**
   * Gets the messagesytsem.
   * @return the messagesystem
   */
  private SimpleMessageSystem messageSystem() {
    return menuController.getPlugin().getSimpleMessageSystem();
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public void close() {
    menuController.close(this);
  }
  /**
   * Internal method, don't use it externally!
   */
  public static void updateNames(SimpleMessageSystem simpleMessageSystem) {
    for (int row = 0; row < 2; row++) {
      for (int column = 0; column < 7; column++) {
        INVENTORY_TEMPLATE[19 + (row * 9) + column] = new ItemBuilder(INVENTORY_TEMPLATE[19 + (row * 9) + column])
            .setName(simpleMessageSystem.translate(getNameForColumn(column) + (row == 0 ? "xptank" : "player"),
                (int) ((column - 3 < 0 ? -1 : 1) * Math.pow(10, Math.abs(column - 3) - 1)))).build();
      }
    }
  }
  
  private static String getNameForColumn(int column) {
    if (column == 3) return "gui.round.";
    if (column < 3) return "gui.remove.";
    else return "gui.add.";
  }
}
