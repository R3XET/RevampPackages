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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CooldownBowListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR) @SuppressWarnings("deprecation")
    public void DamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player attacker = (Player) arrow.getShooter();
                if (event.getEntity() instanceof Damageable) {
                    Damageable dp = (Damageable) event.getEntity();
                    if (dp instanceof Player) {
                        Player defender = (Player) dp;
                        if (!dp.isDead() && !(event.isCancelled())) {
                            if (attacker.equals(defender)) return;
                            ItemStack i = attacker.getInventory().getItemInHand();
                            ItemMeta im = i.getItemMeta();
                            ConfigFile config = RevampPackages.getInstance().getConfigFile();
                            ItemStack ability = new ItemBuilder(config.getMaterial("COOLDOWNBOW.MATERIAL")).setName(config.getString("COOLDOWNBOW.NAME")).setLore(config.getStringList("COOLDOWNBOW.LORE")).toItemStack("ability_cooldownbow");
                            if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability)|| !AbilityUtils.isAbilityEnabled("COOLDOWNBOW")) return;
                            if (HookManager.getPluginMap().get("WorldGuard") != null) {
                                WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                                if (wgHook.isPvPBlocked(attacker)) {
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            if (config.getBoolean("COOLDOWNBOW.COOLDOWN_ENABLED")) {
                                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("COOLDOWNBOW_DELAY", attacker)) {
                                    for (String msg : config.getStringList("COOLDOWNBOW.COOLDOWN_MESSAGE")) {
                                        attacker.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("COOLDOWNBOW_DELAY", attacker), true)));
                                    }
                                    event.setCancelled(true);
                                    return;
                                }
                                if (!CooldownManager.getCooldowns().containsKey("COOLDOWNBOW_DELAY")) {
                                    RevampPackages.getInstance().getCooldownManager().createCooldown("COOLDOWNBOW_DELAY");
                                }
                                RevampPackages.getInstance().getCooldownManager().addCooldown("COOLDOWNBOW_DELAY", attacker, config.getDouble("COOLDOWNBOW.COOLDOWN"));
                            }
                            for (String msg : config.getStringList("COOLDOWNBOW.ITEM_USED_MESSAGE")) {
                                attacker.sendMessage(msg);
                            }
                            for (String msg : config.getStringList("COOLDOWNBOW.ITEM_USED_MESSAGE_OTHER")) {
                                defender.sendMessage(msg);
                            }
                            for (String cmd : config.getStringList("COOLDOWNBOW.COMMAND_TO_PERFORM")){
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", defender.getName()));
                            }
                        }
                    }
                }
            }
        }
    }
}
