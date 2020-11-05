package eu.revamp.packages.items.listeners.ability;
/*
import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.CooldownManager;
import eu.revamp.packages.utils.time.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
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

import java.util.HashMap;

public class FlameChargeListener implements Listener {

    private HashMap<Player, Integer> uses = new HashMap<>();

    @EventHandler @SuppressWarnings("deprecation")
    public void itemUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        String gunName = config.getString("FLAMECHARGE.NAME");
        if (!i.hasItemMeta() || !im.hasDisplayName() || gunName == null || !im.getDisplayName().equalsIgnoreCase(gunName)) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if(config.getBoolean("FLAMECHARGE.COOLDOWN_ENABLED")){
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("FLAMECHARGE_DELAY", player)) {
                    player.sendMessage(config.getString("FLAMECHARGE.COOLDOWN_MESSAGE").replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("FLAMECHARGE_DELAY", player), true)));
                    e.setCancelled(true);
                    return;
                }
                if (!CooldownManager.getCooldowns().containsKey("FLAMECHARGE_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("FLAMECHARGE_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("FLAMECHARGE_DELAY", player, RevampPackages.getInstance().getConfigFile().getDouble("FLAMECHARGE.COOLDOWN"));
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().getItemInHand().getType().equals(Material.GOLD_HOE)) {

                (player.launchProjectile(Fireball.class)).setVelocity(player.getVelocity());
                //Fireball fireball = e.getPlayer().throwSnowball();
                //fireball.setVelocity(fireball.getVelocity().multiply(10));
                fireball.setMetadata("FLAME_CHARGE-BALL", new FixedMetadataValue(RevampPackages.getInstance(), true));
            }
        }
    }

    @EventHandler
    public void onChargeHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
            Player target = (Player)e.getEntity();
            Entity damager = e.getDamager();
            if (damager.hasMetadata("FLAME_CHARGE-BALL")) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, RevampPackages.getInstance().getConfigFile().getInt("FLAMECHARGE.SECONDS") * 20, 3));
            }
        }
    }


}


 */