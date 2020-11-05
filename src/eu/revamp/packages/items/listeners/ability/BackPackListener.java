package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ability.AbilityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;

public class BackPackListener implements Listener {

    private File file;
    private YamlConfiguration cfg;

    public BackPackListener() {
        this.file = new File(RevampPackages.getInstance().getDataFolder(), "datas.yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        String bpName = RevampPackages.getInstance().getConfigFile().getString("BACKPACK.NAME");
        if (!i.hasItemMeta() || !im.hasDisplayName() || bpName == null || !im.getDisplayName().equals(bpName) || !AbilityUtils.isAbilityEnabled("BACKPACK")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(p)){
                e.setCancelled(true);
                return;
            }
        }
        e.getPlayer().openInventory(this.loadBackPack(e.getPlayer()));
            p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
        }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        String bpName = RevampPackages.getInstance().getConfigFile().getString("BACKPACK.NAME");
        if (!i.hasItemMeta() || !im.hasDisplayName() || bpName == null || !im.getDisplayName().equalsIgnoreCase(bpName) || !AbilityUtils.isAbilityEnabled("BACKPACK")) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        String bpName = RevampPackages.getInstance().getConfigFile().getString("BACKPACK.NAME");
        if (!i.hasItemMeta() || !im.hasDisplayName() || bpName == null || !im.getDisplayName().equalsIgnoreCase(bpName) || !AbilityUtils.isAbilityEnabled("BACKPACK")) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        ItemStack i = p.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        String bpName = RevampPackages.getInstance().getConfigFile().getString("BACKPACK.NAME");
        if (!i.hasItemMeta() || !im.hasDisplayName() || bpName == null || !im.getDisplayName().equalsIgnoreCase(bpName) || !AbilityUtils.isAbilityEnabled("BACKPACK")) return;
        if (e.getInventory().getName().equalsIgnoreCase("BACKPACK")) {
            this.saveBackPack(p, e.getInventory());
            p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 1.0f, 1.0f);
        }
    }

    /*
    @EventHandler
    public void onBackpackStack(InventoryMoveItemEvent event){
        ItemStack i = event.getItem().clone();
        ItemMeta im = i.getItemMeta();
        if (!i.hasItemMeta() || !im.hasDisplayName() || !im.getDisplayName().equalsIgnoreCase(RevampPackages.getInstance().getConfigFile().getString("BACKPACK.NAME"))) return;
        if (event.getItem().getType().equals())
    }
    */

    private void saveBackPack(Player p, Inventory inv) {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = inv.getItem(i);
            this.cfg.set("BACKPACK." + p.getUniqueId() + "." + i, item);
            try {
                this.cfg.save(this.file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Inventory loadBackPack(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "BACKPACK");
        for (int i = 0; i < 9; ++i) {
            ItemStack item = this.cfg.getItemStack("BACKPACK." + p.getUniqueId() + "." + i);
            inv.setItem(i, item);
        }
        return inv;
    }
    /*

BACKPACK:
  MATERIAL: "CHEST"
  NAME: "&5&lBackPack"
  LORE:
  - "&7Opens a virtual inventory to store all stuff inside"
     */
}
