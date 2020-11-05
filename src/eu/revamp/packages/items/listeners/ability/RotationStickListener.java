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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RotationStickListener implements Listener {
    @EventHandler
    public void onStickHit(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Player) {
                if (!e.getEntity().isDead()) {
                    if (e.getEntity() instanceof Player) {
                        Player damager = (Player) e.getDamager();
                        Player player = (Player) e.getEntity();
                        ItemStack i = damager.getInventory().getItemInHand();
                        ItemMeta im = i.getItemMeta();
                        ConfigFile config = RevampPackages.getInstance().getConfigFile();
                        ItemStack ability = new ItemBuilder(config.getMaterial("ROTATIONSTICK.MATERIAL")).setName(config.getString("ROTATIONSTICK.NAME")).setLore(config.getStringList("ROTATIONSTICK.LORE")).toItemStack("ability_rotationstick");
                        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("ROTATIONSTICK")) return;
                        if (HookManager.getPluginMap().get("WorldGuard") != null) {
                            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                            if (wgHook.isPvPBlocked(player)){
                                e.setCancelled(true);
                                return;
                            }
                        }
                        if (config.getBoolean("ROTATIONSTICK.COOLDOWN_ENABLED")) {
                            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROTATIONSTICK_DELAY", damager)) {
                                for (String msg : config.getStringList("ROTATIONSTICK.COOLDOWN_MESSAGE")){
                                    damager.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ROTATIONSTICK_DELAY", damager), true)));
                                }
                                e.setCancelled(true);
                                return;
                            }
                            if (!CooldownManager.getCooldowns().containsKey("ROTATIONSTICK_DELAY")) {
                                RevampPackages.getInstance().getCooldownManager().createCooldown("ROTATIONSTICK_DELAY");
                            }
                            RevampPackages.getInstance().getCooldownManager().addCooldown("ROTATIONSTICK_DELAY", damager, config.getDouble("ROTATIONSTICK.COOLDOWN"));
                        }
                        Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw() + 180, player.getLocation().getPitch());
                        player.teleport(loc);
                        for (String msg : config.getStringList("ROTATIONSTICK.SHOOTER_MESSAGE")){
                            damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
                        }
                        for (String msg : config.getStringList("ROTATIONSTICK.DAMAGED_PLAYER_MESSAGE")){
                            player.sendMessage(msg.replace("%damager%", damager.getName()));
                        }
                    }
                }
            }
        }
    }
}
