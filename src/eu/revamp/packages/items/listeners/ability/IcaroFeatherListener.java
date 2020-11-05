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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IcaroFeatherListener implements Listener {

    @EventHandler
    public void onFeatherClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("ICARO_FEATHER.MATERIAL")).setName(config.getString("ICARO_FEATHER.NAME")).setLore(config.getStringList("ICARO_FEATHER.LORE")).toItemStack("ability_icarofeather");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("ICARO_FEATHER")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)) {
                e.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("ICARO_FEATHER.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("ICARO_FEATHER_DELAY", player)) {
                for (String msg : config.getStringList("ICARO_FEATHER.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ICARO_FEATHER_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("ICARO_FEATHER_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("ICARO_FEATHER_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("ICARO_FEATHER_DELAY", player, config.getDouble("ICARO_FEATHER.COOLDOWN"));
        }
        for (String msg : config.getStringList("ICARO_FEATHER.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        if (!CooldownManager.getCooldowns().containsKey("ICARO_FEATHER_USE_DELAY")) {
            RevampPackages.getInstance().getCooldownManager().createCooldown("ICARO_FEATHER_USE_DELAY");
        }
        RevampPackages.getInstance().getCooldownManager().addCooldown("ICARO_FEATHER_USE_DELAY", player, config.getDouble("ICARO_FEATHER.MAX_SECONDS"));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("ICARO_FEATHER.MATERIAL")).setName(config.getString("ICARO_FEATHER.NAME")).setLore(config.getStringList("ICARO_FEATHER.LORE")).toItemStack("ability_icarofeather"));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!event.isCancelled()) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (!event.getEntity().isDead()) {
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("ICARO_FEATHER_USE_DELAY", player)) {
                            event.setCancelled(true);
                            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("ICARO_FEATHER.USE_MESSAGE")){
                                player.sendMessage(msg);
                            }
                            RevampPackages.getInstance().getCooldownManager().removeCooldown("ICARO_FEATHER_USE_DELAY", player);
                        }
                    }
                }
            }
        }
    }
}
