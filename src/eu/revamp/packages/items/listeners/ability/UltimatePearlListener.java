package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.CooldownManager;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.time.TimeUtils;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UltimatePearlListener implements Listener {

    @EventHandler
    public void onEnderpearlThrow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            ItemStack i = player.getInventory().getItemInHand();
            ItemMeta im = i.getItemMeta();
            ConfigFile config = RevampPackages.getInstance().getConfigFile();
            ItemStack ability = new ItemBuilder(config.getMaterial("ULTIMATEPEARL.MATERIAL")).setName(config.getString("ULTIMATEPEARL.NAME")).setLore(config.getStringList("ULTIMATEPEARL.LORE")).toItemStack("ability_ultimatepearl");
            if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("ULTIMATEPEARL")) return;
            if (HookManager.getPluginMap().get("WorldGuard") != null) {
                WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                if (wgHook.isPvPBlocked(player)){
                    event.setCancelled(true);
                    return;
                }
            }
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("ULTIMATEPEARL_DELAY", player)) {
                for (String msg : config.getStringList("ULTIMATEPEARL.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ULTIMATEPEARL_DELAY", player), true)));
                }
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("ULTIMATEPEARL_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("ULTIMATEPEARL_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("ULTIMATEPEARL_DELAY", player, config.getDouble("ULTIMATEPEARL.COOLDOWN"));
            for (String cmd : config.getStringList("ULTIMATEPEARL.COMMAND_TO_PERFORM")){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
            }
        }
    }
}