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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CocaineListener implements Listener {

    @EventHandler
    public void cocaineClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("COCAINE.MATERIAL")).setName(config.getString("COCAINE.NAME")).setLore(config.getStringList("COCAINE.LORE")).toItemStack("ability_cocaine");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("COCAINE")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("COCAINE.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("COCAINE_DELAY", player)) {
                for (String msg : config.getStringList("COCAINE.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("COCAINE_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("COCAINE_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("COCAINE_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("COCAINE_DELAY", player, config.getDouble("COCAINE.COOLDOWN"));
        }
        for (String msg : config.getStringList("COCAINE.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, config.getInt("COCAINE.SECONDS") * 20, config.getInt("COCAINE.AMPLIFIER")));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("COCAINE.MATERIAL")).setName(config.getString("COCAINE.NAME")).setLore(config.getStringList("COCAINE.LORE")).toItemStack("ability_cocaine"));
    }
}
