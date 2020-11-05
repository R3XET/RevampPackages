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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SwitchereggListener implements Listener {

    @EventHandler @SuppressWarnings("deprecation")
    public void onEggHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Egg) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player)) return;
            if (event.getEntity() instanceof Player) {
                ConfigFile config = RevampPackages.getInstance().getConfigFile();
                Egg egg = (Egg) event.getDamager();
                Player eggshooter = (Player) egg.getShooter();
                Location shooterLoc = eggshooter.getLocation();
                Player eggdamagedplayer = (Player) event.getEntity();
                if (event.isCancelled() || !AbilityUtils.isAbilityEnabled("SWITCHEREGG")) return;
                if (eggshooter.getItemInHand().getItemMeta() != null && eggshooter.getItemInHand().getItemMeta().getDisplayName() != null) {
                    if (eggshooter.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(config.getString("SWITCHEREGG.NAME"))) {
                        if ((int) eggdamagedplayer.getLocation().distance(eggshooter.getLocation()) <= config.getInt("SWITCHEREGG.RANGE_OF_BLOCKS")) {
                            eggshooter.teleport(event.getEntity().getLocation());
                            eggdamagedplayer.teleport(shooterLoc);
                            for (String msg : config.getStringList("SWITCHEREGG.DAMAGEPLAYER-MESSAGE")){
                                eggdamagedplayer.sendMessage(msg.replace("%shooter%", eggshooter.getName()));
                            }
                            for (String msg : config.getStringList("SWITCHEREGG.SHOOTER_MESSAGE")){
                                eggshooter.sendMessage(msg.replace("%damagedplayer%", eggdamagedplayer.getName()));
                            }
                            if (eggshooter.getItemInHand().getAmount() == 1) {
                                eggshooter.setItemInHand(null);
                            }
                            else {
                                eggshooter.getItemInHand().setAmount(eggshooter.getItemInHand().getAmount() - 1);
                            }
                        } else {
                            for (String msg : config.getStringList("SWITCHEREGG.NO_RANGE_MESSAGE")){
                                eggshooter.sendMessage(msg.replace("%damagedplayer%", eggdamagedplayer.getName()));
                            }
                        }
                    }
                }
            }
        }
        else if (event.getDamager() instanceof Snowball) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player)) return;
            if (event.getEntity() instanceof Player) {
                ConfigFile config = RevampPackages.getInstance().getConfigFile();
                Snowball egg = (Snowball) event.getDamager();
                Player eggshooter = (Player) egg.getShooter();
                Location shooterLoc = eggshooter.getLocation();
                Player eggdamagedplayer = (Player) event.getEntity();
                if (event.isCancelled()) return;
                if (eggshooter.getItemInHand().getItemMeta() != null && eggshooter.getItemInHand().getItemMeta().getDisplayName() != null) {
                    if (eggshooter.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(config.getString("SWITCHEREGG.NAME"))) {
                        if ((int) eggdamagedplayer.getLocation().distance(eggshooter.getLocation()) <= config.getInt("SWITCHEREGG.RANGE_OF_BLOCKS")) {
                            eggshooter.teleport(event.getEntity().getLocation());
                            eggdamagedplayer.teleport(shooterLoc);
                            for (String msg : config.getStringList("SWITCHEREGG.DAMAGEPLAYER-MESSAGE")){
                                eggdamagedplayer.sendMessage(msg.replace("%shooter%", eggshooter.getName()));
                            }
                            for (String msg : config.getStringList("SWITCHEREGG.SHOOTER_MESSAGE")){
                                eggshooter.sendMessage(msg.replace("%damagedplayer%", eggdamagedplayer.getName()));
                            }
                            if (eggshooter.getItemInHand().getAmount() == 1) {
                                eggshooter.setItemInHand(null);
                            }
                            else {
                                eggshooter.getItemInHand().setAmount(eggshooter.getItemInHand().getAmount() - 1);
                            }
                        } else {
                            for (String msg : config.getStringList("SWITCHEREGG.NO_RANGE_MESSAGE")){
                                eggshooter.sendMessage(msg.replace("%damagedplayer%", eggdamagedplayer.getName()));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEggThrow(PlayerInteractEvent e) {
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        Player shooter = e.getPlayer();
        ItemStack i = shooter.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ItemStack ability = new ItemBuilder(config.getMaterial("SWITCHEREGG.MATERIAL")).setName(config.getString("SWITCHEREGG.NAME")).setLore(config.getStringList("SWITCHEREGG.LORE")).toItemStack("ability_switcheregg");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("SWITCHEREGG")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(shooter)){
                e.setCancelled(true);
                return;
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (config.getBoolean("SWITCHEREGG.COOLDOWN_ENABLED")){
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("SWITCHEREGG_DELAY", shooter)) {
                    for (String msg : config.getStringList("SWITCHEREGG.COOLDOWN_MESSAGE")){
                        shooter.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("SWITCHEREGG_DELAY", shooter), true)));
                    }
                    e.setCancelled(true);
                    return;
                }
                if (!CooldownManager.getCooldowns().containsKey("SWITCHEREGG_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("SWITCHEREGG_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("SWITCHEREGG_DELAY", shooter, config.getDouble("SWITCHEREGG.COOLDOWN"));
                //e.setCancelled(true);

                //TODO UNCOMMENT
                //shooter.getInventory().addItem(ability);
            }
        }
    }
}