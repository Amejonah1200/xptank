package com.github.amejonah1200.xptank;

import ap.amejonah.simplemessagesystem.SimpleMessageSystem;
import com.github.amejonah1200.xptank.command.XPTankCommand;
import com.github.amejonah1200.xptank.listener.XPTankInteractionListener;
import com.github.amejonah1200.xptank.menu.MenuController;
import com.github.johnnyjayjay.compatre.NmsClassLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class XPTankPlugin extends JavaPlugin {
  
  static {
    NmsClassLoader.loadNmsDependents(XPTankPlugin.class);
  }
  
  private MenuController menuController;
  private SimpleMessageSystem simpleMessageSystem;
  
  @Override
  public void onEnable() {
    simpleMessageSystem = new SimpleMessageSystem(this);
    YamlConfiguration messages = YamlConfiguration.loadConfiguration(new File("plugins/APXPTank/messages.yml"));
    simpleMessageSystem.loadCustomMessages(messages);
    if (simpleMessageSystem.saveDefaultMessages(messages, false)) {
      try {
        messages.save("plugins/APXPTank/messages.yml");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    menuController = new MenuController(this);
    Bukkit.getPluginManager().registerEvents(new XPTankInteractionListener(this), this);
    getCommand("xptank").setExecutor(new XPTankCommand(this));
  }
  
  /**
   * Gets simple message system.
   *
   * @return the simple message system
   */
  public SimpleMessageSystem getSimpleMessageSystem() {
    return simpleMessageSystem;
  }
  
  /**
   * Gets menu controller.
   *
   * @return the menu controller
   */
  public MenuController getMenuController() {
    return menuController;
  }
  
  @Override
  public void onDisable() {
    if (menuController != null) menuController.closeAll();
  }
  
  /**
   * Reload messages.yml.
   */
  public void reload() {
    simpleMessageSystem
        .loadCustomMessages(YamlConfiguration.loadConfiguration(new File("plugins/APXPTank/messages.yml")));
    MenuController.updateNames(simpleMessageSystem);
  }
}
