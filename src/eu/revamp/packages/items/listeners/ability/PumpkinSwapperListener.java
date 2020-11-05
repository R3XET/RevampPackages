package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.*;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.events.DisarmerUseEvent;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import eu.revamp.packages.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PumpkinSwapperListener implements Listener {
    @EventHandler
    public void onSwordHit(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Player) {
                if (!e.getEntity().isDead()) {
                    if (e.getEntity() instanceof Player) {
                        Player damager = (Player) e.getDamager();
                        Player player = (Player) e.getEntity();
                        ItemStack i = damager.getInventory().getItemInHand();
                        ItemMeta im = i.getItemMeta();
                        ConfigFile config = RevampPackages.getInstance().getConfigFile();
                        ItemStack ability = new ItemBuilder(config.getMaterial("PUMPKINSWAPPER.MATERIAL")).setName(config.getString("PUMPKINSWAPPER.NAME")).setLore(config.getStringList("PUMPKINSWAPPER.LORE")).toItemStack("ability_pumpkinswapper");
                        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("PUMPKINSWAPPER")) return;
                        if (HookManager.getPluginMap().get("WorldGuard") != null) {
                            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                            if (wgHook.isPvPBlocked(player)){
                                e.setCancelled(true);
                                return;
                            }
                        }
                        if (config.getBoolean("PUMPKINSWAPPER.COOLDOWN_ENABLED")){
                            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("PUMPKINSWAPPER_DELAY", damager)) {
                                for (String msg : config.getStringList("PUMPKINSWAPPER.COOLDOWN_MESSAGE")){
                                    damager.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("PUMPKINSWAPPER_DELAY", damager), true)));
                                }
                                e.setCancelled(true);
                                return;
                            }
                            if (!CooldownManager.getCooldowns().containsKey("PUMPKINSWAPPER_DELAY")) {
                                RevampPackages.getInstance().getCooldownManager().createCooldown("PUMPKINSWAPPER_DELAY");
                            }
                            RevampPackages.getInstance().getCooldownManager().addCooldown("PUMPKINSWAPPER_DELAY", damager, config.getDouble("PUMPKINSWAPPER.COOLDOWN"));
                        }
                        if(Math.random() <= config.getDouble("PUMPKINSWAPPER.SUCCESS_PERCENT")) {
                            if (player.getInventory().getHelmet() != null) {
                                ItemStack armor = player.getEquipment().getHelmet();
                                ItemStack pumpkin = new ItemStack(Material.PUMPKIN, 1);
                                DisarmerUseEvent event = new DisarmerUseEvent(player);
                                Bukkit.getPluginManager().callEvent(event);
                                player.getEquipment().setHelmet(pumpkin);
                                if (player.getInventory().addItem(armor).size() != 0) {
                                    player.getWorld().dropItem(player.getLocation(), armor);
                                }
                                for (String msg : config.getStringList("PUMPKINSWAPPER.SHOOTER_MESSAGE")){
                                    damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
                                }
                                for (String msg : config.getStringList("PUMPKINSWAPPER.DAMAGED_PLAYER_MESSAGE")){
                                    player.sendMessage(msg.replace("%damager%", damager.getName()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}