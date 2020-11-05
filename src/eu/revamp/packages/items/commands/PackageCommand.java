package eu.revamp.packages.items.commands;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.utils.CC;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class PackageCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        if (sender.hasPermission("revamppackages.package")) {
            if (args.length == 3 && args[0].equalsIgnoreCase("give") && Bukkit.getServer().getPlayer(args[1]) != null) {
                try {
                    Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return false;
                }
                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("PARTNERPACKAGE.MATERIAL"), Integer.parseInt(args[2])).setName(config.getString("PARTNERPACKAGE.NAME")).setLore(config.getStringList("PARTNERPACKAGE.LORE")).toItemStack("partner_package"));
                return true;
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("giveall")){
                try {
                    Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
                int amount = Integer.parseInt(args[1]);
                Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(new ItemBuilder(config.getMaterial("PARTNERPACKAGE.MATERIAL"), amount).setName(config.getString("PARTNERPACKAGE.NAME")).setLore(config.getStringList("PARTNERPACKAGE.LORE")).toItemStack("partner_package")));
            }
            else {
                for (String str : config.getStringList("REVAMP_PACKAGES.MESSAGE")) {
                    sender.sendMessage(str);
                }
            }
        } else {
            sender.sendMessage(CC.translate("&8&m-------------------------------------------"));
            sender.sendMessage(CC.translate("&6This server is running Revamp Packages " + RevampPackages.getInstance().getDescription().getVersion()));
            sender.sendMessage(CC.translate(" &b» &aAuthor&7: @R3XET / R3XET#0852"));
            sender.sendMessage(CC.translate("&3&nhttps://www.mc-market.org/resources/13513/"));
            sender.sendMessage(CC.translate("&8&m-------------------------------------------"));
        }
        return false;
    }
}
