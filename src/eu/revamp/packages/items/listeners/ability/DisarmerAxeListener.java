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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DisarmerAxeListener implements Listener {

    @EventHandler
    public void onAxeHit(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Player) {
                if (!e.getEntity().isDead()) {
                    if (e.getEntity() instanceof Player) {
                        Player damager = (Player) e.getDamager();
                        Player player = (Player) e.getEntity();
                        ItemStack i = damager.getInventory().getItemInHand();
                        ItemMeta im = i.getItemMeta();
                        ConfigFile config = RevampPackages.getInstance().getConfigFile();
                        ItemStack ability = new ItemBuilder(config.getMaterial("DISARMERAXE.MATERIAL")).setName(config.getString("DISARMERAXE.NAME")).setLore(config.getStringList("DISARMERAXE.LORE")).toItemStack("ability_disarmeraxe");
                        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability)|| !AbilityUtils.isAbilityEnabled("DISARMERAXE")) return;
                        if (HookManager.getPluginMap().get("WorldGuard") != null) {
                            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                            if (wgHook.isPvPBlocked(damager)) {
                                e.setCancelled(true);
                                return;
                            }
                        }
                        if (config.getBoolean("DISARMERAXE.COOLDOWN_ENABLED")) {
                            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("DISARMERAXE_DELAY", damager)) {
                                for (String msg : config.getStringList("DISARMERAXE.COOLDOWN_MESSAGE")){
                                    damager.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("DISARMERAXE_DELAY", damager), true)));
                                }
                                e.setCancelled(true);
                                return;
                            }
                            if (!CooldownManager.getCooldowns().containsKey("DISARMERAXE_DELAY")) {
                                RevampPackages.getInstance().getCooldownManager().createCooldown("DISARMERAXE_DELAY");
                            }
                            RevampPackages.getInstance().getCooldownManager().addCooldown("DISARMERAXE_DELAY", damager, RevampPackages.getInstance().getConfigFile().getDouble("DISARMERAXE.COOLDOWN"));
                        }
                        int slot = ItemUtils.getRandomSlot(4, 1);
                        if (config.getBoolean("DISARMERAXE.ONLY_HELMET")) {
                            slot = 1;
                        }
                        switch (slot) {
                            case 1:
                                if (player.getInventory().getBoots() != null) {
                                    if (player.getInventory().getHelmet().getType().equals(Material.DIAMOND_HELMET)) {
                                        ItemStack armor = player.getEquipment().getHelmet();
                                        DisarmerUseEvent event = new DisarmerUseEvent(player);
                                        Bukkit.getPluginManager().callEvent(event);
                                        player.getEquipment().setHelmet(null);
                                        if (player.getInventory().addItem(armor).size() != 0) {
                                            addEffectsInvFull(damager, player);
                                            player.getEquipment().setHelmet(armor);
                                            return;
                                        }
                                        for (String msg : config.getStringList("DISARMERAXE.ITEM_SUCCESSFULLY_USED")){
                                            damager.sendMessage(msg.replace("%item%", "helmet").replace("%damagedplayer%", player.getName()));
                                        }
                                    } else {
                                        addEffects(damager, player);
                                    }
                                }
                                break;
                            case 2:
                                if (player.getInventory().getChestplate() != null) {
                                    if (player.getInventory().getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE)) {
                                        ItemStack armor = player.getEquipment().getChestplate();
                                        DisarmerUseEvent event = new DisarmerUseEvent(player);
                                        Bukkit.getPluginManager().callEvent(event);
                                        player.getEquipment().setChestplate(null);
                                        if (player.getInventory().addItem(armor).size() != 0) {
                                            addEffectsInvFull(damager, player);
                                            player.getEquipment().setChestplate(armor);
                                            return;
                                        }
                                        for (String msg : config.getStringList("DISARMERAXE.ITEM_SUCCESSFULLY_USED")){
                                            damager.sendMessage(msg.replace("%item%", "chestplate").replace("%damagedplayer%", player.getName()));
                                        }
                                    } else {
                                        addEffects(damager, player);
                                    }
                                }
                                break;
                            case 3:
                                if (player.getInventory().getLeggings() != null) {
                                    if (player.getInventory().getLeggings().getType().equals(Material.DIAMOND_LEGGINGS)) {
                                        ItemStack armor = player.getEquipment().getLeggings();
                                        DisarmerUseEvent event = new DisarmerUseEvent(player);
                                        Bukkit.getPluginManager().callEvent(event);
                                        player.getEquipment().setLeggings(null);
                                        if (player.getInventory().addItem(armor).size() != 0) {
                                            addEffectsInvFull(damager, player);
                                            player.getEquipment().setLeggings(armor);
                                            return;
                                        }
                                        for (String msg : config.getStringList("DISARMERAXE.ITEM_SUCCESSFULLY_USED")){
                                            damager.sendMessage(msg.replace("%item%", "leggings").replace("%damagedplayer%", player.getName()));
                                        }
                                    } else {
                                        addEffects(damager, player);
                                    }
                                }
                                break;
                            case 4:
                                if (player.getInventory().getBoots() != null) {
                                    if (player.getInventory().getBoots().getType().equals(Material.DIAMOND_BOOTS)) {
                                        ItemStack armor = player.getEquipment().getBoots();
                                        DisarmerUseEvent event = new DisarmerUseEvent(player);
                                        Bukkit.getPluginManager().callEvent(event);
                                        player.getEquipment().setBoots(null);
                                        if (player.getInventory().addItem(armor).size() != 0) {
                                            addEffectsInvFull(damager, player);
                                            player.getEquipment().setBoots(armor);
                                            return;
                                        }
                                        for (String msg : config.getStringList("DISARMERAXE.ITEM_SUCCESSFULLY_USED")){
                                            damager.sendMessage(msg.replace("%item%", "boots").replace("%damagedplayer%", player.getName()));
                                        }
                                    } else {
                                        addEffects(damager, player);
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private void addEffects(Player damager, Player player){
        for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("DISARMERAXE.POTION_EFFECT_MESSAGE")){
            damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RevampPackages.getInstance().getConfigFile().getInt("DISARMERAXE.POTION_EFFECT_TIME") * 20, 3));
    }
    private void addEffectsInvFull(Player damager, Player player){
        for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("DISARMERAXE.POTION_EFFECT_MESSAGE_INVENTORY_FULL")){
            damager.sendMessage(msg.replace("%damagedplayer%", player.getName()));
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RevampPackages.getInstance().getConfigFile().getInt("DISARMERAXE.POTION_EFFECT_TIME") * 20, 3));
    }
}
