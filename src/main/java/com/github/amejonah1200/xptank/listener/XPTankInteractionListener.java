package com.github.amejonah1200.xptank.listener;

import com.github.amejonah1200.xptank.XPTankPlugin;
import com.github.amejonah1200.xptank.event.XPTankEvent;
import com.github.amejonah1200.xptank.experience.XPHolder;
import com.github.amejonah1200.xptank.experience.XPTank;
import com.github.amejonah1200.xptank.utils.NBTTagger;
import com.github.amejonah1200.xptank.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class XPTankInteractionListener implements Listener {
  
  public XPTankPlugin plugin;
  
  public XPTankInteractionListener(XPTankPlugin plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  private void onInteract(PlayerInteractEvent event) {
    if (event.getItem() != null && event.getItem().getType() != Material.AIR &&
        NBTTagger.hasTag("xptank", event.getItem())) {
      event.setCancelled(true);
      event.getPlayer().updateInventory();
      if (!VersionUtils.isInOffHand(event)) {
        if (event.getItem().getAmount() > 1) {
          event.getPlayer().sendMessage(plugin.getSimpleMessageSystem().translate("messages.error.multiple_tanks"));
        } else {
          XPTank xpTank = XPTank.parseXPTank(event.getItem());
          if (xpTank == null) return;
          if ((xpTank.isOnUse() && !event.getPlayer().hasPermission("apxptank.xptank.oneuse")) ||
              (!xpTank.isOnUse() && !event.getPlayer().hasPermission("apxptank.xptank.multiuse"))) {
            event.getPlayer().sendMessage(plugin.getSimpleMessageSystem().translate("messages.error.noperm.use_tank"));
            return;
          }
          if (!xpTank.isOnUse() && event.getAction().toString().startsWith("LEFT")) {
            plugin.getMenuController().open(event.getPlayer());
          } else if (event.getAction().toString().startsWith("RIGHT")) {
            XPHolder playerXpHolder = new XPHolder(event.getPlayer());
            if (playerXpHolder.isFull()) {
              return;
            }
            int xp = xpTank.getExperience();
            XPTankEvent xpTankEvent = new XPTankEvent(event.getPlayer(), xpTank, XPTankEvent.XPTankAction.EXP_REMOVE,
                xp);
            Bukkit.getPluginManager().callEvent(xpTankEvent);
            if (xpTankEvent.isCancelled()) {
              return;
            }
            xpTank.setExp(0);
            xpTank.addExp(playerXpHolder.addExp(xp));
            if (xpTank.isOnUse() && xpTank.getExperience() == 0) event.getPlayer().setItemInHand(null);
            else event.getPlayer().setItemInHand(xpTank.build());
            playerXpHolder.applyToPlayer(event.getPlayer());
          }
        }
      }
    }
  }
  
  @EventHandler
  private void onSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    if (!event.getPlayer().isSneaking()) return;
    XPTank xpTank = XPTank.parseXPTank(player.getItemInHand());
    if (xpTank == null) return;
    if (player.getItemInHand().getAmount() > 1) {
      event.getPlayer().sendMessage(plugin.getSimpleMessageSystem().translate("messages.error.multiple_tanks"));
      return;
    }
    if (!xpTank.isOnUse()) {
      if (!player.hasPermission("apxptank.xptank.multiuse")) {
        event.getPlayer().sendMessage(plugin.getSimpleMessageSystem().translate("messages.error.noperm.use_tank"));
        return;
      } else if (xpTank.isFull()) return;
      XPHolder playerXpHolder = new XPHolder(event.getPlayer());
      int xp = playerXpHolder.getExperience();
      XPTankEvent xpTankEvent = new XPTankEvent(player, xpTank, XPTankEvent.XPTankAction.EXP_ADD, xp);
      Bukkit.getPluginManager().callEvent(xpTankEvent);
      if (xpTankEvent.isCancelled()) return;
      playerXpHolder.setExp(0);
      playerXpHolder.addExp(xpTank.addExp(xp));
      player.setItemInHand(xpTank.build());
      playerXpHolder.applyToPlayer(player);
    }
  }
}
