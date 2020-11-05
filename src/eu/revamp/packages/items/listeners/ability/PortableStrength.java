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

public class PortableStrength implements Listener {

    @EventHandler
    public void onStrengthClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("PORTABLESTRENGTH.MATERIAL")).setName(config.getString("PORTABLESTRENGTH.NAME")).setLore(config.getStringList("PORTABLESTRENGTH.LORE")).toItemStack("ability_portablestrength");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("PORTABLESTRENGTH")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("PORTABLESTRENGTH.COOLDOWN_ENABLED")){
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("PORTABLESTRENGTH_DELAY", player)) {
                for (String msg : config.getStringList("PORTABLESTRENGTH.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("PORTABLESTRENGTH_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("PORTABLESTRENGTH_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("PORTABLESTRENGTH_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("PORTABLESTRENGTH_DELAY", player, config.getDouble("PORTABLESTRENGTH.COOLDOWN"));
        }
        for (String msg : config.getStringList("PORTABLESTRENGTH.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, config.getInt("PORTABLESTRENGTH.SECONDS") * 20, config.getInt("PORTABLESTRENGTH.AMPLIFIER")));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("PORTABLESTRENGTH.MATERIAL")).setName(config.getString("PORTABLESTRENGTH.NAME")).setLore(config.getStringList("PORTABLESTRENGTH.LORE")).toItemStack("ability_portablestrength"));
    }
}
