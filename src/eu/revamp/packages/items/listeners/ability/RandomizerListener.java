package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.*;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.events.DisarmerUseEvent;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import eu.revamp.packages.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class RandomizerListener implements Listener {

    private HashMap<Player, ItemStack> getInHand = new HashMap<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Player) {
                if (!e.getEntity().isDead()) {
                    if (e.getEntity() instanceof Player) {
                        Player damager = (Player) e.getDamager();
                        Player player = (Player) e.getEntity();
                        ItemStack i = damager.getInventory().getItemInHand();
                        ItemMeta im = i.getItemMeta();
                        ConfigFile config = RevampPackages.getInstance().getConfigFile();
                        ItemStack ability = new ItemBuilder(config.getMaterial("RANDOMIZER.MATERIAL")).setName(config.getString("RANDOMIZER.NAME")).setLore(config.getStringList("RANDOMIZER.LORE")).toItemStack("ability_randomizer");
                        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("RANDOMIZER")) return;
                        if (HookManager.getPluginMap().get("WorldGuard") != null) {
                            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                            if (wgHook.isPvPBlocked(player)) {
                                e.setCancelled(true);
                                return;
                            }
                        }
                        if (config.getBoolean("RANDOMIZER.COOLDOWN_ENABLED")) {
                            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("RANDOMIZER_DELAY", damager)) {
                                for (String msg : config.getStringList("RANDOMIZER.COOLDOWN_MESSAGE")) {
                                    damager.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("RANDOMIZER_DELAY", damager), true)));
                                }
                                e.setCancelled(true);
                                return;
                            }
                        }
                        ItemStack itemHand = player.getItemInHand();
                        getInHand.put(player, itemHand);
                        DisarmerUseEvent event = new DisarmerUseEvent(player);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!hasHotbarEmpty(player)) {
                            int slot = ItemUtils.getRandomSlot(8, 0);
                            ItemStack randomItem = new ItemStack(Material.AIR);
                            if (player.getInventory() == null) {
                                return;
                            }
                            if (player.getItemInHand().equals(new ItemStack(Material.AIR))) {
                                for (String msg : config.getStringList("RANDOMIZER.NO-HAND-ITEMS")) {
                                    damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
                                }
                            }
                            if (player.getInventory().getItem(slot) == null) {
                                player.getInventory().addItem(getInHand.get(player));
                            } else {
                                randomItem = player.getInventory().getItem(slot);
                                player.getInventory().setItem(slot, getInHand.get(player));
                            }
                            if (randomItem.equals(new ItemStack(Material.AIR))) { //CHECKARE
                                player.setItemInHand(new ItemStack(Material.AIR));
                            } else {
                                player.setItemInHand(randomItem);
                                //player.getInventory().removeItem(randomItem); //It should works
                            }

                            //TODO fix item being deleted after swapper (not the item in the hand, but the other)
                            for (String msg : config.getStringList("RANDOMIZER.SHOOTER_MESSAGE")) {
                                damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
                            }
                            for (String msg : config.getStringList("RANDOMIZER.DAMAGED_PLAYER_MESSAGE")) {
                                player.sendMessage(msg.replace("%damager%", damager.getName()));
                            }
                            if (!CooldownManager.getCooldowns().containsKey("RANDOMIZER_DELAY")) {
                                RevampPackages.getInstance().getCooldownManager().createCooldown("RANDOMIZER_DELAY");
                            }
                            RevampPackages.getInstance().getCooldownManager().addCooldown("RANDOMIZER_DELAY", damager, config.getDouble("RANDOMIZER.COOLDOWN"));
                            return;
                        }
                        for (String msg : config.getStringList("RANDOMIZER.NO-HOTBAR-ITEMS")) {
                            damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
                        }
                    }
                }
            }
        }
    }

    private boolean hasHotbarEmpty(Player player){
        for (int slot = 0; slot < 8; slot++) {
            if (player.getInventory().getItem(slot)!= null && player.getInventory().getItem(slot).getType() != Material.AIR){
                return false;
            }
        }
        return true;
    }
}
