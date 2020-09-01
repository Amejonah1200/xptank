package com.github.amejonah1200.xptank.menu;

import ap.amejonah.simplemessagesystem.SimpleMessageSystem;
import com.github.amejonah1200.xptank.XPTankPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.HashMap;
import java.util.Map;

public final class MenuController implements Listener {
  
  private final XPTankPlugin plugin;
  private final Map<Player, XPTankMenu> menus = new HashMap<>();
  
  public MenuController(XPTankPlugin plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, plugin);
    updateNames(plugin.getSimpleMessageSystem());
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public void open(Player player) {
    menus.putIfAbsent(player, new XPTankMenu(this, player));
    menus.get(player).open();
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public void close(XPTankMenu menu) {
    menus.remove(menu.getViewer());
    menu.setOpened(false);
    menu.getViewer().closeInventory();
  }
  
  /**
   * Internal method, don't use it externally!
   */
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    XPTankMenu menu = menus.get(event.getWhoClicked());
    if (menu == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
    menu.click(event);
  }
  
  /**
   * Internal method, don't use it externally!
   */
  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    menus.remove(event.getPlayer());
  }
  
  /**
   * Internal method, don't use it externally!
   */
  @EventHandler
  public void onSlotChange(PlayerItemHeldEvent event) {
    event.setCancelled(
        event.isCancelled() || menus.containsKey(event.getPlayer()) && menus.get(event.getPlayer()).isOpened());
  }
  
  /**
   * Closes all opened inventories. This method can be used externally.
   */
  public void closeAll() {
    HandlerList.unregisterAll(this);
    menus.keySet().forEach(HumanEntity::closeInventory);
    menus.clear();
    if (plugin.isEnabled()) Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  /**
   * Gets plugin.
   *
   * @return the plugin
   */
  public XPTankPlugin getPlugin() {
    return plugin;
  }
  
  /**
   * Internal method, don't use it externally!
   */
  public static void updateNames(SimpleMessageSystem simpleMessageSystem) {
    XPTankMenu.updateNames(simpleMessageSystem);
  }
}

