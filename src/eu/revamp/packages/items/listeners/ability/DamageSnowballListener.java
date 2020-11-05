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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageSnowballListener implements Listener {

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player)e.getEntity().getShooter();
            ItemStack i = p.getInventory().getItemInHand();
            ItemMeta im = i.getItemMeta();
            ConfigFile config = RevampPackages.getInstance().getConfigFile();
            ItemStack ability = new ItemBuilder(config.getMaterial("SNOWBALL.MATERIAL")).setName(config.getString("SNOWBALL.NAME")).setLore(config.getStringList("SNOWBALL.LORE")).toItemStack("ability_snowball");
            if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability)|| !AbilityUtils.isAbilityEnabled("SNOWBALL")) return;
            if (e.getEntity() instanceof Snowball && e.getEntity().getType() == EntityType.SNOWBALL) {
                Snowball snowball = (Snowball) e.getEntity();
                snowball.setMetadata("DAMAGE_SNOWBALL", new FixedMetadataValue(RevampPackages.getInstance(), true));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("SNOWBALL.MATERIAL")).setName(config.getString("SNOWBALL.NAME")).setLore(config.getStringList("SNOWBALL.LORE")).toItemStack("ability_snowball");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability)|| !AbilityUtils.isAbilityEnabled("SNOWBALL")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if (event.hasItem() && action.equals(Action.RIGHT_CLICK_AIR)) {
            if (RevampPackages.getInstance().getConfigFile().getBoolean("SNOWBALL.COOLDOWN_ENABLED")) {
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("SNOWBALL_DELAY", player)) {
                    event.setUseItemInHand(Event.Result.DENY);
                    for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("SNOWBALL.COOLDOWN_MESSAGE")){
                        player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("SNOWBALL_DELAY", player), true)));
                    }
                } else if (event.getItem().getType() == Material.SNOW_BALL) {
                    if (!CooldownManager.getCooldowns().containsKey("SNOWBALL_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SNOWBALL_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SNOWBALL_DELAY", player, RevampPackages.getInstance().getConfigFile().getInt("SNOWBALL.COOLDOWN"));
                }
            }
        }
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player target = (Player) e.getEntity();
            if (e.getDamager() instanceof Snowball && e.getDamager().hasMetadata("DAMAGE_SNOWBALL") && (!target.hasPotionEffect(PotionEffectType.BLINDNESS) || !target.hasPotionEffect(PotionEffectType.SLOW))) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RevampPackages.getInstance().getConfigFile().getInt("SNOWBALL.SECONDS") * 20, 0));
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, RevampPackages.getInstance().getConfigFile().getInt("SNOWBALL.SECONDS") * 20, 0));
            }
        }
    }
}
