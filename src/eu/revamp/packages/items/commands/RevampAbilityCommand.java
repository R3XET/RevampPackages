package eu.revamp.packages.items.commands;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.utils.CC;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RevampAbilityCommand implements TabExecutor {

    private final String[] COMMANDS = {"snowball", "blockegg", "switcheregg", "grapplinghook", "throwweb", "disarmeraxe", "ninja", "freezegun", "invisdust", "secondchance", /*"backpack", */"rocket", "pumpkinswapper", "cocaine", "cooldownbow", "randomizer", "portablestrength", "rotationstick", "lumberjack", "pyroball", "ultimatepearl", "refillchest", "guardianangel", "icarofeather", "bedbomb", "pocketbard", "webbomb", "invisdustv2", "stormbreaker", "sandburner", /*"malignantegg"*/};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        if (sender.hasPermission("revamppackages.admin")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("list") || (args[0].equalsIgnoreCase("ITEMS"))) {
                    for (String str : config.getStringList("REVAMP_PACKAGES.LIST")) {
                        sender.sendMessage(str);
                    }
                } else if (args.length == 4 && args[0].equalsIgnoreCase("give") && Bukkit.getServer().getPlayer(args[1]) != null) {
                    if (ItemUtils.isNumber(args[3])) {
                        int amount = Integer.parseInt(args[3]);
                        switch (args[2]) {
                            case "snowball":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("SNOWBALL.MATERIAL"), amount).setName(config.getString("SNOWBALL.NAME")).setLore(config.getStringList("SNOWBALL.LORE")).toItemStack("ability_snowball"));
                                break;
                            case "blockegg":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("BLOCKEGG.MATERIAL"), amount).setName(config.getString("BLOCKEGG.NAME")).setLore(config.getStringList("BLOCKEGG.LORE")).toItemStack("ability_blockegg"));
                                break;
                            case "switcheregg":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("SWITCHEREGG.MATERIAL"), amount).setName(config.getString("SWITCHEREGG.NAME")).setLore(config.getStringList("SWITCHEREGG.LORE")).toItemStack("ability_switcheregg"));
                                break;
                            case "grapplinghook":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("GRAPPLINGHOOK.MATERIAL"), amount).setName(config.getString("GRAPPLINGHOOK.NAME")).setLore(config.getStringList("GRAPPLINGHOOK.LORE")).toItemStack("ability_grapplinghook"));
                                break;
                            case "throwweb":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("THROWWEB.MATERIAL"), amount).setName(config.getString("THROWWEB.NAME")).setLore(config.getStringList("THROWWEB.LORE")).toItemStack("ability_throwweb"));
                                break;
                            case "disarmeraxe":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("DISARMERAXE.MATERIAL"), amount).setName(config.getString("DISARMERAXE.NAME")).setLore(config.getStringList("DISARMERAXE.LORE")).toItemStack("ability_disarmeraxe"));
                                break;
                            case "ninja":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("NINJA.MATERIAL"), amount).setName(config.getString("NINJA.NAME")).setLore(config.getStringList("NINJA.LORE")).toItemStack("ability_ninja"));
                                break;
                            case "freezegun":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("FREEZEGUN.MATERIAL"), amount).setName(config.getString("FREEZEGUN.NAME")).setLore(config.getStringList("FREEZEGUN.LORE")).toItemStack("ability_freezegun"));
                                break;
                            case "invisdust":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("INVISDUST.MATERIAL"), amount).setName(config.getString("INVISDUST.NAME")).setLore(config.getStringList("INVISDUST.LORE")).toItemStack("ability_invisdust"));
                                break;
                            case "invisdustv2":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("INVISDUSTV2.MATERIAL"), amount).setName(config.getString("INVISDUSTV2.NAME")).setLore(config.getStringList("INVISDUSTV2.LORE")).toItemStack("ability_invisdustv2"));
                                break;
                            case "secondchance":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("SECONDCHANCE.MATERIAL"), amount).setName(config.getString("SECONDCHANCE.NAME")).setLore(config.getStringList("SECONDCHANCE.LORE")).toItemStack("ability_secondchance"));
                                break;
                            /*case "backpack":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(ItemBuilder.createItem(config.getMaterial("BACKPACK.MATERIAL"), config.getString("BACKPACK.NAME"), amount, 0, config.getStringList("BACKPACK.LORE")));
                                break;*/
                            case "rocket":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("ROCKET.MATERIAL"), amount).setName(config.getString("ROCKET.NAME")).setLore(config.getStringList("ROCKET.LORE")).toItemStack("ability_rocket"));
                                break;
                            case "pumpkinswapper":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("PUMPKINSWAPPER.MATERIAL"), amount).setName(config.getString("PUMPKINSWAPPER.NAME")).setLore(config.getStringList("PUMPKINSWAPPER.LORE")).toItemStack("ability_pumpkinswapper"));
                                break;
                            case "cocaine":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("COCAINE.MATERIAL"), amount).setName(config.getString("COCAINE.NAME")).setLore(config.getStringList("COCAINE.LORE")).toItemStack("ability_cocaine"));
                                break;
                            case "cooldownbow":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("COOLDOWNBOW.MATERIAL"), amount).setName(config.getString("COOLDOWNBOW.NAME")).setLore(config.getStringList("COOLDOWNBOW.LORE")).toItemStack("ability_cooldownbow"));
                                break;
                            case "randomizer":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("RANDOMIZER.MATERIAL"), amount).setName(config.getString("RANDOMIZER.NAME")).setLore(config.getStringList("RANDOMIZER.LORE")).toItemStack("ability_randomizer"));
                                break;
                            case "portablestrength":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("PORTABLESTRENGTH.MATERIAL"), amount).setName(config.getString("PORTABLESTRENGTH.NAME")).setLore(config.getStringList("PORTABLESTRENGTH.LORE")).toItemStack("ability_portablestrength"));
                                break;
                            case "pyroball":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("PYROBALL.MATERIAL"), amount).setName(config.getString("PYROBALL.NAME")).setLore(config.getStringList("PYROBALL.LORE")).toItemStack("ability_pyroball"));
                                break;
                            case "rotationstick":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("ROTATIONSTICK.MATERIAL"), amount).setName(config.getString("ROTATIONSTICK.NAME")).setLore(config.getStringList("ROTATIONSTICK.LORE")).toItemStack("ability_rotationstick"));
                                break;
                            case "lumberjack":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("LUMBERJACK.MATERIAL"), amount).setName(config.getString("LUMBERJACK.NAME")).setLore(config.getStringList("LUMBERJACK.LORE")).toItemStack("ability_lumberjack"));
                                break;
                                /*
                            case "malignantegg":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(ItemBuilder.createItem(config.getMaterial("MALIGNANTEGG.MATERIAL"), config.getString("MALIGNANTEGG.NAME"), amount, 0, config.getStringList("MALIGNANTEGG.LORE")));
                                break;
                                 */
                            case "ultimatepearl":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("ULTIMATEPEARL.MATERIAL"), amount).setName(config.getString("ULTIMATEPEARL.NAME")).setLore(config.getStringList("ULTIMATEPEARL.LORE")).toItemStack("ability_ultimatepearl"));
                                break;
                            case "refillchest":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("REFILLCHEST.MATERIAL"), amount).setName(config.getString("REFILLCHEST.NAME")).setLore(config.getStringList("REFILLCHEST.LORE")).toItemStack("ability_refillchest"));
                                break;
                            case "guardianangel":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("GUARDIAN_ANGEL.MATERIAL"), amount).setName(config.getString("GUARDIAN_ANGEL.NAME")).setLore(config.getStringList("GUARDIAN_ANGEL.LORE")).toItemStack("ability_guardianangel"));
                                break;
                            case "icarofeather":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("ICARO_FEATHER.MATERIAL"), amount).setName(config.getString("ICARO_FEATHER.NAME")).setLore(config.getStringList("ICARO_FEATHER.LORE")).toItemStack("ability_icarofeather"));
                                break;
                            case "bedbomb":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("BEDBOMB.MATERIAL"), amount).setName(config.getString("BEDBOMB.NAME")).setLore(config.getStringList("BEDBOMB.LORE")).toItemStack("ability_bedbomb"));
                                break;
                            case "pocketbard":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("POCKET_BARD.MATERIAL"), amount).setName(config.getString("POCKET_BARD.NAME")).setLore(config.getStringList("POCKET_BARD.LORE")).toItemStack("ability_pocketbard"));
                                break;
                            case "webbomb":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("WEBBOMB.MATERIAL"), amount).setName(config.getString("WEBBOMB.NAME")).setLore(config.getStringList("WEBBOMB.LORE")).toItemStack("ability_webbomb"));
                                break;
                            case "stormbreaker":
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("STORMBREAKER.MATERIAL"), amount).setName(config.getString("STORMBREAKER.NAME")).setLore(config.getStringList("STORMBREAKER.LORE")).toItemStack("ability_stormbreaker"));
                                break;
                            case "sandburner":
                                /*int level = 5;
                                String enchant = null;
                                for (String enchantments : config.getStringList("SANDBURNER.ENCHANTS")){
                                    String[] line = enchantments.split(":");
                                    enchant = line[0];
                                    level = Integer.parseInt(line[1]);
                                }
                                ItemStack sandBurner = ;*/
                                Bukkit.getPlayer(args[1]).getInventory().addItem(new ItemBuilder(config.getMaterial("SANDBURNER.MATERIAL"), amount).setName(config.getString("SANDBURNER.NAME")).setLore(config.getStringList("SANDBURNER.LORE")).addEnchant(Enchantment.DIG_SPEED, 5).addEnchant(Enchantment.DURABILITY, 3).toItemStack("ability_sandburner"));
                                break;
                        }
                    }
                }
            } else {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 3) {
            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[2], Arrays.asList(COMMANDS), completions);
            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}
