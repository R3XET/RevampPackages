package eu.revamp.packages.items.listeners;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.firework.FireworkUtils;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import eu.revamp.packages.utils.items.PackageCrate;
import eu.revamp.packages.utils.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PackageListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ConfigFile config = RevampPackages.getInstance().getConfigFile();
            ItemStack i = player.getInventory().getItemInHand();
            ItemMeta im = i.getItemMeta();
            ItemStack ability = new ItemBuilder(config.getMaterial("PARTNERPACKAGE.MATERIAL")).setName(config.getString("PARTNERPACKAGE.NAME")).setLore(config.getStringList("PARTNERPACKAGE.LORE")).toItemStack("partner_package");
            if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability)) return;
            if (HookManager.getPluginMap().get("WorldGuard") != null) {
                WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                if (wgHook.isPvPBlocked(player)){
                    e.setCancelled(true);
                    return;
                }
            }
            if (config.getBoolean("PARTNERPACKAGE.BROADCAST_ENABLED")) {
                config.getStringList("PARTNERPACKAGE.BROADCAST_MESSAGE").forEach(str -> Bukkit.broadcastMessage(str.replaceAll("%player%", player.getName())));
            }
            if (player.getItemInHand().getAmount() == 1) {
                player.setItemInHand(null);
            } else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }
            ParticleEffect effect = ParticleEffect.fromName(config.getString("PARTNERPACKAGE.PARTICLE_EFFECT.NAME"));
            if (effect != null){
                effect.display(config.getFloat("PARTNERPACKAGE.PARTICLE_EFFECT.OFFSET.X"), config.getFloat("PARTNERPACKAGE.PARTICLE_EFFECT.OFFSET.Y"), config.getFloat("PARTNERPACKAGE.PARTICLE_EFFECT.OFFSET.Z"), config.getFloat("PARTNERPACKAGE.PARTICLE_EFFECT.SPEED"), config.getInt("PARTNERPACKAGE.PARTICLE_EFFECT.AMOUNT"), player.getLocation()/*.add(0, 1, 0)*/, config.getInt("PARTNERPACKAGE.PARTICLE_EFFECT.RANGE"));
            }
            if (config.getBoolean("PARTNERPACKAGE.FIREWORK_ENABLED")){
                FireworkUtils fireworkUtils = new FireworkUtils();
                fireworkUtils.fireWork(player.getLocation().add(0, 1, 0));
            }

/*
            if (player.getItemInHand().getAmount() == 1) {
                player.setItemInHand(null);
            }
            else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }*/
            PackageCrate packageCrate = PackageCrate.getRandom();
            if (packageCrate.getItems().size() != 0){
                packageCrate.getItems().forEach(ItemStack -> {
                    if (ItemStack.getType() != Material.STRING) {
                        if (player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(ItemStack);
                        } else {
                            player.getWorld().dropItemNaturally(player.getLocation(), ItemStack);
                        }
                    }
                });
            }
            if (packageCrate.getCommands().size() != 0){
                String[] command = new String[1];
                packageCrate.getCommands().forEach(str -> {
                    command[0] = str.replaceAll("%player%", player.getName());
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command[0]);
                });
            }

            player.updateInventory();
            e.setCancelled(true);
        }
    }
}
