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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SecondChanceListener implements Listener {

    @EventHandler
    public void onFeatherClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("SECONDCHANCE.MATERIAL")).setName(config.getString("SECONDCHANCE.NAME")).setLore(config.getStringList("SECONDCHANCE.LORE")).toItemStack("ability_secondchance");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("SECONDCHANCE")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("SECONDCHANCE.COOLDOWN_ENABLED")){
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("SECONDCHANCE_DELAY", player)) {
                for (String msg : config.getStringList("SECONDCHANCE.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("SECONDCHANCE_DELAY", player), true)));
                }
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("SECONDCHANCE_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("SECONDCHANCE_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("SECONDCHANCE_DELAY", player, config.getDouble("SECONDCHANCE.COOLDOWN"));
        }
        for (String msg : config.getStringList("SECONDCHANCE.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("SECONDCHANCE.MATERIAL")).setName(config.getString("SECONDCHANCE.NAME")).setLore(config.getStringList("SECONDCHANCE.LORE")).toItemStack("ability_secondchance"));
        for (String cmd : config.getStringList("SECONDCHANCE.COMMAND_TO_PERFORM")){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }
    }
}
