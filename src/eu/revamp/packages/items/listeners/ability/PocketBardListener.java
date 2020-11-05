package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.CC;
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

public class PocketBardListener implements Listener {
    @EventHandler
    public void onPocketBardClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("POCKET_BARD.MATERIAL")).setName(config.getString("POCKET_BARD.NAME")).setLore(config.getStringList("POCKET_BARD.LORE")).toItemStack("ability_pocketbard");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("POCKET_BARD")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("POCKET_BARD.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("POCKET_BARD_DELAY", player)) {
                for (String msg : config.getStringList("POCKET_BARD.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("POCKET_BARD_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("POCKET_BARD_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("POCKET_BARD_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("POCKET_BARD_DELAY", player, config.getDouble("POCKET_BARD.COOLDOWN"));
        }
        for (String msg: config.getStringList("POCKET_BARD.ITEM_USED_MESSAGE")) {
            player.sendMessage(CC.translate(msg));
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, config.getInt("POCKET_BARD.STRENGTH_SECONDS") * 20, config.getInt("POCKET_BARD.STRENGTH_AMPLIFIER")));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, config.getInt("POCKET_BARD.RESISTANCE_SECONDS") * 20, config.getInt("POCKET_BARD.RESISTANCE_AMPLIFIER")));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, config.getInt("POCKET_BARD.REGENERATION_SECONDS") * 20, config.getInt("POCKET_BARD.REGENERATION_AMPLIFIER")));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("POCKET_BARD.MATERIAL")).setName(config.getString("POCKET_BARD.NAME")).setLore(config.getStringList("POCKET_BARD.LORE")).toItemStack("ability_pocketbard"));
    }
}
