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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WebBombListener implements Listener {

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onPlayerInteract(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("WEBBOMB.MATERIAL")).setName(config.getString("WEBBOMB.NAME")).setLore(config.getStringList("WEBBOMB.LORE")).toItemStack("ability_webbomb");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("WEBBOMB")) return;
        if (event.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if (config.getBoolean("WEBBOMB.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("WEBBOMB_DELAY", player)) {
                for (String msg : config.getStringList("WEBBOMB.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("WEBBOMB_DELAY", player), true)));
                }
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("WEBBOMB_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("WEBBOMB_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("WEBBOMB_DELAY", player, config.getDouble("WEBBOMB.COOLDOWN"));
        }
        this.generateWebs(event.getBlock().getLocation(), config.getInt("WEBBOMB.RADIUS"));
    }

    public void generateWebs(Location location, int radius) {
        for (int x = 0; x < radius; x++) {
            for (int y = 0; y < radius; y++) {
                for (int z = 0; z < radius; z++) {
                    Location newWeb = location.clone();
                    newWeb.setX(location.getX() + x);
                    newWeb.setY(location.getY() + y);
                    newWeb.setZ(location.getZ() + z);
                    if (newWeb.getBlock().getType() == Material.AIR){
                        newWeb.getBlock().setType(Material.WEB);
                    }
                }
            }
        }
    }
}
