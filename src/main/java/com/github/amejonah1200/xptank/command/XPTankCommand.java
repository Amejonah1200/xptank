package com.github.amejonah1200.xptank.command;

import com.github.amejonah1200.xptank.XPTankPlugin;
import com.github.amejonah1200.xptank.experience.XPTank;
import com.github.amejonah1200.xptank.experience.XPUtils;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class XPTankCommand implements CommandExecutor {
  
  private final XPTankPlugin plugin;
  
  public XPTankCommand(XPTankPlugin plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission("apxptank.admin")) {
      sender.sendMessage(plugin.getSimpleMessageSystem().translate("messages.error.noperm.command"));
      return true;
    }
    if (args.length == 0) {
      sendHelp(sender);
      return true;
    } else if ("reload".equalsIgnoreCase(args[0])) {
      plugin.reload();
      sender.sendMessage("§aReloaded messages.");
    } else if ("give".equalsIgnoreCase(args[0])) {
      switch (args.length) {
        case 1:
        case 2:
          sender.sendMessage("§cSyntax:");
          sender.sendMessage("§c/xptank give <playername|me|all> <lvl> [true/false]");
          sender.sendMessage("§c/xptank give <playername|me|all> <lvl> <0<lvllimit<=21863>");
          break;
        case 3:
        case 4:
          Integer lvl;
          lvl = Ints.tryParse(args[2]);
          if (lvl == null || 0 > lvl || lvl > 21863) {
            sender.sendMessage("§c<lvl> must be an integer between 0 and 21863 (bounds inclusive).");
            return true;
          }
          XPTank xpTank;
          if (args.length == 4) {
            Integer lvllimit;
            lvllimit = Ints.tryParse(args[3]);
            if (lvllimit == null) {
              switch (args[3].toLowerCase()) {
                case "true":
                case "false":
                  xpTank = new XPTank(XPUtils.getExpFromLevel(lvl), Boolean.parseBoolean(args[3]));
                  break;
                default:
                  sender.sendMessage("§cSyntax:");
                  sender.sendMessage("§c/xptank give <playername|me|all> <lvl> [true/false]");
                  return true;
              }
            } else if (1 > lvllimit || lvllimit > 21863) {
              sender.sendMessage("§c<lvllimit> must be an integer between 1 and 21863 (bounds inclusive).");
              return true;
            } else xpTank = new XPTank(XPUtils.getExpFromLevel(lvl), lvllimit);
          } else xpTank = new XPTank(XPUtils.getExpFromLevel(lvl), false);
          final ItemStack tankItem = xpTank.build();
          if ("me".equalsIgnoreCase(args[1])) {
            if (sender instanceof Player) ((Player) sender).getInventory().addItem(tankItem);
            else {
              sender.sendMessage("§cThe console is not a Player.");
              return true;
            }
          } else if ("all".equalsIgnoreCase(args[1])) {
            for (Player p : Bukkit.getOnlinePlayers())
              p.getInventory().addItem(tankItem.clone());
            sender.sendMessage("§aAll players received the xptank.");
          } else {
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null) {
              sender.sendMessage("§aPlayer \"" + player.getName() + "\" received the xptank.");
              player.getInventory().addItem(tankItem);
            } else {
              sender.sendMessage("§cPlayer \"" + args[1] + "\" not found.");
            }
          }
      }
    } else sendHelp(sender);
    return true;
  }
  
  private void sendHelp(CommandSender sender) {
    sender.sendMessage("§7------§6Help§7------");
    sender.sendMessage("§7/xptank reload");
    sender.sendMessage("§7/xptank give <playername|me|all> <lvl> [true/false]");
    sender.sendMessage("§7 --- §6Gives a xptank with <lvl> levels. [true/false] - oneUse, default: false");
    sender.sendMessage("§7/xptank give <playername|me|all> <lvl> <0<lvllimit<=21863>");
    sender.sendMessage("§7 --- §6Gives a xptank with <lvl> levels with given lvllimit, multiuse.");
    sender.sendMessage("§6<> required §7- §6[] optional");
    sender.sendMessage("§7------§6Help§7------");
  }
}
