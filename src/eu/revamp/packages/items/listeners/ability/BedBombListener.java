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
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BedBombListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("BEDBOMB.MATERIAL")).setName(config.getString("BEDBOMB.NAME")).setLore(config.getStringList("BEDBOMB.LORE")).toItemStack("ability_bedbomb");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("BEDBOMB")) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)) {
                e.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("BEDBOMB.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("BEDBOMB_DELAY", player)) {
                for (String msg : config.getStringList("BEDBOMB.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BEDBOMB_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("BEDBOMB_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("BEDBOMB_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("BEDBOMB_DELAY", player, config.getDouble("BEDBOMB.COOLDOWN"));
        }
        for (String msg : config.getStringList("BEDBOMB.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        if (!CooldownManager.getCooldowns().containsKey("BEDBOMB_USE_DELAY")) {
            RevampPackages.getInstance().getCooldownManager().createCooldown("BEDBOMB_USE_DELAY");
        }
        RevampPackages.getInstance().getCooldownManager().addCooldown("BEDBOMB_USE_DELAY", player, config.getDouble("BEDBOMB.MAX_SECONDS"));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("BEDBOMB.MATERIAL")).setName(config.getString("BEDBOMB.NAME")).setLore(config.getStringList("BEDBOMB.LORE")).toItemStack("ability_bedbomb"));
        List<Player> toEffect = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.RANGE_BLOCKS"), RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.RANGE_BLOCKS"), RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.RANGE_BLOCKS"))) {
            if (!(entity instanceof Player)) continue;
            if (entity.equals(player)) continue;
            toEffect.add((Player) entity);
        }
        toEffect.forEach(online -> online.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.POTION_EFFECT_TIME") * 20, RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.AMPLIFIER"))));
        toEffect.forEach(online -> online.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.POTION_EFFECT_TIME") * 20, RevampPackages.getInstance().getConfigFile().getInt("BEDBOMB.AMPLIFIER"))));
        toEffect.clear();
        if (e.getItem().getAmount() > 1)
            e.getItem().setAmount(e.getItem().getAmount() - 1);
        else
            player.getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
    }

    @EventHandler
    public void onBedBombPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("BEDBOMB.MATERIAL")).setName(config.getString("BEDBOMB.NAME")).setLore(config.getStringList("BEDBOMB.LORE")).toItemStack("ability_bedbomb");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("BEDBOMB")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)) {
                event.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("BEDBOMB.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("BEDBOMB_DELAY", player)) {
                for (String msg : config.getStringList("BEDBOMB.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BEDBOMB_DELAY", player), true)));
                }
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("BEDBOMB_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("BEDBOMB_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("BEDBOMB_DELAY", player, config.getDouble("BEDBOMB.COOLDOWN"));
        }
    }
}
