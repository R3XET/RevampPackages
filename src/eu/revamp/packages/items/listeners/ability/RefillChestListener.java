package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class RefillChestListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRefillChestPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("REFILLCHEST.MATERIAL")).setName(config.getString("REFILLCHEST.NAME")).setLore(config.getStringList("REFILLCHEST.LORE")).toItemStack("ability_refillchest");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || event.isCancelled() || !AbilityUtils.isAbilityEnabled("REFILLCHEST")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)) {
                event.setCancelled(true);
                return;
            }
        }
        //event.setUseItemInHand(Event.Result.DENY);
        Location location = event.getBlock().getLocation();
        Location location1 = new Location(location.getWorld(), location.getX(), location.getY() + 0, location.getZ());
        Location location2 = new Location(location.getWorld(), location.getX() - 1, location.getY() + 0, location.getZ());
        Location location3 = new Location(location.getWorld(), location.getX() - 2, location.getY() + 0, location.getZ());
        Location location4 = new Location(location.getWorld(), location.getX() + 1, location.getY() + 0, location.getZ());
        Location location5 = new Location(location.getWorld(), location.getX() + 2, location.getY() + 0, location.getZ());
        Location location6 = new Location(location.getWorld(), location.getX(), location.getY() + 0, location.getZ() - 1);
        Location location7 = new Location(location.getWorld(), location.getX(), location.getY() + 0, location.getZ() - 2);
        Location location8 = new Location(location.getWorld(), location.getX(), location.getY() + 0, location.getZ() + 2);
        Location location9 = new Location(location.getWorld(), location.getX(), location.getY() + 0, location.getZ() + 1);

        if ((location1.getBlock().getType() == Material.BREWING_STAND || location1.getBlock().getType() == null) && (location2.getBlock().getType() == Material.AIR || location2.getBlock().getType() == null) && (location3.getBlock().getType() == Material.AIR || location3.getBlock().getType() == null) && (location4.getBlock().getType() == Material.AIR || location4.getBlock().getType() == null) && (location5.getBlock().getType() == Material.AIR || location5.getBlock().getType() == null) && (location6.getBlock().getType() == Material.AIR || location6.getBlock().getType() == null) && (location7.getBlock().getType() == Material.AIR || location7.getBlock().getType() == null) && (location8.getBlock().getType() == Material.AIR || location8.getBlock().getType() == null) && (location9.getBlock().getType() == Material.AIR || location9.getBlock().getType() == null)) {
            //location1.getBlock().setType(Material.AIR);
            location2.getBlock().setType(Material.AIR);
            new BukkitRunnable() {
                @Override
                public void run() {
                    location1.getBlock().setType(Material.CHEST);
                    location2.getBlock().setType(Material.CHEST);
                }
            }.runTaskLater(RevampPackages.getInstance(), 5L);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Chest chest1 = (Chest) location1.getBlock().getState();
                    Inventory inv1 = chest1.getInventory();
                    for (int x = 0; x < 54; x++) {
                        inv1.setItem(x, new ItemStack(Material.POTION, 1, (short) 16421));
                    }
                }
            }.runTaskLater(RevampPackages.getInstance(), 10L);

        } else {
            for (String msg : config.getStringList("REFILLCHEST.BLOCKS_NEAR")) {
                player.sendMessage(msg);
            }
            event.setCancelled(true);
            return;
        }
        for (String msg : config.getStringList("REFILLCHEST.PLACE_MESSAGE")) {
            player.sendMessage(msg);
        }
    }
}
