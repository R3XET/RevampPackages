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
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LumberJackListener implements Listener {

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR) @SuppressWarnings("deprecation")
    public void onLogBreak(BlockBreakEvent e) {
        World tw = e.getPlayer().getWorld();
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        int x = e.getBlock().getX();
        int y = e.getBlock().getY();
        int z = e.getBlock().getZ();
        ItemStack ability = new ItemBuilder(config.getMaterial("LUMBERJACK.MATERIAL")).setName(config.getString("LUMBERJACK.NAME")).setLore(config.getStringList("LUMBERJACK.LORE")).toItemStack("ability_lumberjack");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("LUMBERJACK")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (e.isCancelled()){
            e.setCancelled(true);
            return;
        }
        if (e.getBlock().getTypeId() == 17 || e.getBlock().getTypeId() == 162) {
            if (config.getBoolean("LUMBERJACK.COOLDOWN_ENABLED")) {
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("LUMBERJACK_DELAY", player)) {
                    for (String msg : config.getStringList("LUMBERJACK.COOLDOWN_MESSAGE")){
                        player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("LUMBERJACK_DELAY", player), true)));
                    }
                    e.setCancelled(true);
                    return;
                } else {
                    if (!CooldownManager.getCooldowns().containsKey("LUMBERJACK_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("LUMBERJACK_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("LUMBERJACK_DELAY", player, config.getDouble("LUMBERJACK.COOLDOWN"));
                }
            }
            for (String msg : config.getStringList("LUMBERJACK.ITEM_USED_MESSAGE")){
                player.sendMessage(msg);
            }
            breakChain(tw, x, y, z);
        }
    }

    @SuppressWarnings("deprecation")
    private int gb(World world, int x, int y, int z) {
        return world.getBlockAt(x, y, z).getTypeId();
    }

    public void breakChain(World w, int x, int y, int z) {
        w.getBlockAt(x, y, z).breakNaturally();
        if (this.gb(w, x, y + 1, z) == 17 || this.gb(w, x, y + 1, z) == 162) {
            this.breakChain(w, x, y + 1, z);
        }
        if (this.gb(w, x, y + 1, z) == 18 || this.gb(w, x, y + 1, z) == 161) {
            this.breakChain(w, x, y + 1, z);
        }
        if (this.gb(w, x, y - 1, z) == 18 || this.gb(w, x, y - 1, z) == 161) {
            this.breakChain(w, x, y - 1, z);
        }
        if (this.gb(w, x + 1, y, z) == 18 || this.gb(w, x + 1, y, z) == 161) {
            this.breakChain(w, x + 1, y, z);
        }
        if (this.gb(w, x - 1, y, z) == 18 || this.gb(w, x - 1, y, z) == 161) {
            this.breakChain(w, x - 1, y, z);
        }
        if (this.gb(w, x, y, z + 1) == 18 || this.gb(w, x, y, z + 1) == 161) {
            this.breakChain(w, x, y, z + 1);
        }
        if (this.gb(w, x, y, z - 1) == 18 || this.gb(w, x, y, z - 1) == 161) {
            this.breakChain(w, x, y, z - 1);
        }
        if (this.gb(w, x, y - 1, z) == 17 || this.gb(w, x, y - 1, z) == 162) {
            this.breakChain(w, x, y - 1, z);
        }
        if (this.gb(w, x + 1, y, z) == 17 || this.gb(w, x + 1, y, z) == 162) {
            this.breakChain(w, x + 1, y, z);
        }
        if (this.gb(w, x - 1, y, z) == 17 || this.gb(w, x - 1, y, z) == 162) {
            this.breakChain(w, x - 1, y, z);
        }
        if (this.gb(w, x, y, z + 1) == 17 || this.gb(w, x, y, z + 1) == 162) {
            this.breakChain(w, x, y, z + 1);
        }
        if (this.gb(w, x, y, z - 1) == 17 || this.gb(w, x, y, z - 1) == 162) {
            this.breakChain(w, x, y, z - 1);
        }
    }
}