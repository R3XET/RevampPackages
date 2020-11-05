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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuardianAngelListener implements Listener {

    @EventHandler
    public void guardianAngelClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("GUARDIAN_ANGEL.MATERIAL")).setName(config.getString("GUARDIAN_ANGEL.NAME")).setLore(config.getStringList("GUARDIAN_ANGEL.LORE")).toItemStack("ability_guardianangel");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("GUARDIAN_ANGEL")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("GUARDIAN_ANGEL.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("GUARDIAN_ANGEL_DELAY", player)) {
                for (String msg : config.getStringList("GUARDIAN_ANGEL.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("GUARDIAN_ANGEL_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("GUARDIAN_ANGEL_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("GUARDIAN_ANGEL_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("GUARDIAN_ANGEL_DELAY", player, config.getDouble("GUARDIAN_ANGEL.COOLDOWN"));
        }
        for (String msg : config.getStringList("GUARDIAN_ANGEL.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        if (!CooldownManager.getCooldowns().containsKey("GUARDIAN_ANGEL_USE_DELAY")) {
            RevampPackages.getInstance().getCooldownManager().createCooldown("GUARDIAN_ANGEL_USE_DELAY");
        }
        RevampPackages.getInstance().getCooldownManager().addCooldown("GUARDIAN_ANGEL_USE_DELAY", player, config.getDouble("GUARDIAN_ANGEL.MAX_SECONDS"));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("GUARDIAN_ANGEL.MATERIAL")).setName(config.getString("GUARDIAN_ANGEL.NAME")).setLore(config.getStringList("GUARDIAN_ANGEL.LORE")).toItemStack("ability_guardianangel"));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (!event.isCancelled()) {
            if (event.getDamager() instanceof Player) {
                if (!event.getEntity().isDead()) {
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("GUARDIAN_ANGEL_USE_DELAY", player)) {
                            if (player.getHealth() <= RevampPackages.getInstance().getConfigFile().getDouble("GUARDIAN_ANGEL.MAX_DROP_HEALTH")){
                                player.setHealth(20.0);
                                for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("GUARDIAN_ANGEL.HEAL_MESSAGE")){
                                    player.sendMessage(msg);
                                }
                                RevampPackages.getInstance().getCooldownManager().removeCooldown("GUARDIAN_ANGEL_USE_DELAY", player);
                            }
                        }
                    }
                }
            }
        }
    }


    /*
    *

REFILLCHEST:
  MATERIAL: "BREWING_STAND_ITEM"
  NAME: "&6&lRefill Chest"
  LORE:
    - "&7Place this item to spawn"
    - "&7a double chest full of pots"
  PLACE_MESSAGE: "&aYou have placed the &6Refill Chest &aitem!"
  BLOCKS_NEAR: "&cThere are blocks nearby, place the item in another place!"


GUARDIAN_ANGEL:
  MATERIAL: "GHAST_TEAR"
  NAME: "&d&lGuardian Angel"
  LORE:
    - "&7Heal yourself if you drop to 2 hearts!"
    - "&7This only works within 30 seconds of item use!"
  # Enable cooldown between item uses?
  COOLDOWN_ENABLED: true
  # IN SECONDS # The time that the player must wait before using this item again
  COOLDOWN: 300
  # IN SECONDS # The maximum time that this item works
  MAX_SECONDS: 30
  COOLDOWN_MESSAGE: "&7You still have a &dGuardian Angel &7cooldown for another %time%."
  HEAL_MESSAGE: "&5Your &dGuardian Angel &5 ability has healed you!"
  ITEM_USED_MESSAGE: "&5You have used your &dGuardian Angel &5 ability! If you drop to 2 hearths within 30 seconds you will be healed"

FLAMECHARGE:
  MATERIAL: "DIAMOND_HOE"
  NAME: "&2&lFlame Charge"
  LORE:
    - "&7Wither a player with fire charge projectile"
    - "&7You have 3 chances to hit a player"
  # Seconds that the player is affected by wither
  SECONDS: 6
  # Level of the wither effect - 0 equals to wither 1 | 1 equals to wither 2, etc.
  AMPLIFIER: 1
  # Number of fireballs the player can fail before be on cooldown
  CHANCES: 3
  # Enable cooldown between item uses?
  COOLDOWN_ENABLED: true
  # IN SECONDS # The time that the player must wait before using this item again
  COOLDOWN: 60
  COOLDOWN_MESSAGE: "&7You still have a &fFreeze Gun &7cooldown for another %time%."
    *
     */
}

