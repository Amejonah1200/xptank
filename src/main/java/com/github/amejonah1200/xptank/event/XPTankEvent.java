package com.github.amejonah1200.xptank.event;

import com.github.amejonah1200.xptank.experience.XPTank;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

/**
 * Fired when a player tries to open the inventory or tries to sneak to store exp
 * (xptank will be multiuse n this cases) or when right click to remove exp from tank.
 */
public class XPTankEvent extends Event implements Cancellable {
  
  private static final HandlerList HANDLERS = new HandlerList();
  private final Player player;
  private final XPTank xpTank;
  private final XPTankAction action;
  private final int movingExp;
  private boolean cancelled;
  
  public XPTankEvent(Player player, XPTank xpTank, XPTankAction action, int movingExp) {
    this.player = player;
    this.xpTank = xpTank;
    this.action = action;
    this.movingExp = movingExp;
  }
  
  /**
   * Gets player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }
  
  /**
   * Gets xp tank.
   *
   * @return the xp tank
   */
  public XPTank getXpTank() {
    return xpTank;
  }
  
  /**
   * Gets action.
   *
   * @return the action
   */
  public XPTankAction getAction() {
    return action;
  }
  
  /**
   * Gets moving exp.
   *
   * @return the moving exp
   */
  public int getMovingExp() {
    return movingExp;
  }
  
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
  
  /**
   * Gets handler list.
   *
   * @return the handler list
   */
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
  
  @Override
  public boolean isCancelled() {
    return cancelled;
  }
  
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
  
  /**
   * Actions when interacted with the xptank.
   */
  public enum XPTankAction {
    /**
     * Action when Exp will be added to the Tank.
     */
    EXP_ADD,
    /**
     * Action when Exp will be removed from the Tank.
     */
    EXP_REMOVE,
    /**
     * Action when the GUI tries to open.
     */
    GUI_OPEN
  }
}
