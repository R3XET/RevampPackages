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
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeGunListener implements Listener {

    @EventHandler @SuppressWarnings("deprecation")
    public void itemUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("FREEZEGUN.MATERIAL")).setName(config.getString("FREEZEGUN.NAME")).setLore(config.getStringList("FREEZEGUN.LORE")).toItemStack("ability_freezegun");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("FREEZEGUN")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if(config.getBoolean("FREEZEGUN.COOLDOWN_ENABLED")){
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("FREEZEGUN_DELAY", player)) {
                    for (String msg : config.getStringList("FREEZEGUN.COOLDOWN_MESSAGE")){
                        player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("FREEZEGUN_DELAY", player), true)));
                    }
                    e.setCancelled(true);
                    return;
                }
                if (!CooldownManager.getCooldowns().containsKey("FREEZEGUN_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("FREEZEGUN_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("FREEZEGUN_DELAY", player, RevampPackages.getInstance().getConfigFile().getDouble("FREEZEGUN.COOLDOWN"));
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().getItemInHand().getType().equals(Material.GOLD_HOE)) {
                Snowball snowball = e.getPlayer().throwSnowball();
                snowball.setVelocity(snowball.getVelocity().multiply(10));
                snowball.setMetadata("FREEZEGUN-SNOWBALL", new FixedMetadataValue(RevampPackages.getInstance(), true));
            }
        }
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
            Player target = (Player)e.getEntity();
            Entity damager = e.getDamager();
            if (damager.hasMetadata("FREEZEGUN-SNOWBALL")) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RevampPackages.getInstance().getConfigFile().getInt("FREEZEGUN.SECONDS") * 20, 3));
            }
        }
    }
}
