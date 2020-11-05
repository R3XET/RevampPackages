package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.CooldownManager;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.time.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class MalignantEggListener implements Listener
{
    private boolean isMalEgg(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        String meggName = RevampPackages.getInstance().getConfigFile().getString("MALIGNANTEGG.NAME");
        return im.getDisplayName() != null && im.getDisplayName().equals(meggName);
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void setEffects(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if (damager instanceof Egg && damager.getType().getName() != null && damager.getType().getName().equals(RevampPackages.getInstance().getConfigFile().getString("MALIGNANTEGG.NAME")) && damaged instanceof Player) {
            Player player = (Player)damaged;
            Egg Egg = (Egg)damager;
            ProjectileSource source = Egg.getShooter();
            Player shooter = (Player)source;
            if (isMalEgg(shooter.getItemInHand())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 240, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 240, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 240, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 160, 1));
                this.sendDamageByEggMessage(shooter, player);
            }
        }
    }

    public void sendDamageByEggMessage(Player shooter, Player player) {
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("MALIGNANTEGG.SHOOTER_INVIS_MESSAGE"))
            shooter.sendMessage(msg);
        }
        else {
            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("MALIGNANTEGG.SHOOTER_MESSAGE")){
                shooter.sendMessage(msg.replace("%player%", player.getName()));
            }
        }
        if (shooter.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("MALIGNANTEGG.DAMAGER_INVIS_MESSAGE")){
                player.sendMessage(msg);
            }
        }
        else {
            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("MALIGNANTEGG.DAMAGER_MESSAGE")){
                player.sendMessage(msg.replace("%player%", shooter.getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (event.hasItem() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            ItemStack i = player.getInventory().getItemInHand();
            ItemMeta im = i.getItemMeta();
            String meggName = RevampPackages.getInstance().getConfigFile().getString("MALIGNANTEGG.NAME");
            if (!i.hasItemMeta() || !im.hasDisplayName() || meggName == null || !im.getDisplayName().equals(meggName) || event.isCancelled() || !AbilityUtils.isAbilityEnabled("MALIGNANTEGG")) return;
            if (HookManager.getPluginMap().get("WorldGuard") != null) {
                WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                if (wgHook.isPvPBlocked(player)){
                    event.setCancelled(true);
                    return;
                }
            }
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("MALIGNANTEGG_DELAY", player)) {
                event.setUseItemInHand(Event.Result.DENY);
                for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("MALIGNANTEGG.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("MALIGNANTEGG_DELAY", player), true) + "."));
                }
            }
            else if (event.getItem().getType() == Material.EGG && isMalEgg(player.getItemInHand())) {
                if (!CooldownManager.getCooldowns().containsKey("MALIGNANTEGG_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("MALIGNANTEGG_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("MALIGNANTEGG_DELAY", player, RevampPackages.getInstance().getConfigFile().getInt("COOLDOWNS.MALIGNANTEGG"));
            }
        }
    }
}
/*

MALIGNANTEGG:
  MATERIAL: "EGG"
  NAME: "&c&lMalignant Egg"
  LORE:
    - "&7Damage the player you hit"
    - "&7You will receive %time%s of cooldown"
  # Enable cooldown between item uses?
  COOLDOWN_ENABLED: true
  # IN SECONDS # The time that the player must wait before using this item again
  COOLDOWN: 120
  COOLDOWN_MESSAGE: "&7You still have a &cMalignant Egg &7cooldown for another %time%."
  SHOOTER_MESSAGE: "&cYou have damaged &e%player%&c with a &c&lMalignant Egg&c."
  SHOOTER_INVIS_MESSAGE: "&cYou have damaged &e???&c with a &c&lMalignant Egg&c."
  DAMAGER_MESSAGE: "&cYou have been damaged by &e%player%&c with a &c&lMalignant Egg&c."
  DAMAGER_INVIS_MESSAGE: "&cYou have been damaged by &e???&c with a &c&lMalignant Egg&c."

    - "   - &c&lMalignant Egg"

 */