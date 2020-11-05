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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class GrapplingHookListener implements Listener {

    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        Fish h = e.getHook();
        ItemStack i = p.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("GRAPPLINGHOOK.MATERIAL")).setName(config.getString("GRAPPLINGHOOK.NAME")).setLore(config.getStringList("GRAPPLINGHOOK.LORE")).toItemStack("ability_grapplinghook");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("GRAPPLINGHOOK")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(p)){
                e.setCancelled(true);
                return;
            }
        }
        if (RevampPackages.getInstance().getConfigFile().getBoolean("GRAPPLINGHOOK.COOLDOWN_ENABLED")){
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("GRAPPLINGHOOK_DELAY", p)) {
                for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("GRAPPLINGHOOK.COOLDOWN_MESSAGE")){
                    p.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("GRAPPLINGHOOK_DELAY", p), true)));
                }
                e.setCancelled(true);
                return;
            }
        }
        if ((e.getState().equals(PlayerFishEvent.State.IN_GROUND) || e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) || e.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)) && Bukkit.getWorld(e.getPlayer().getWorld().getName()).getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ()).getType() != Material.AIR && Bukkit.getWorld(e.getPlayer().getWorld().getName()).getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ()).getType() != Material.STATIONARY_WATER) {
            Location lc = p.getLocation();
            Location to = e.getHook().getLocation();
            lc.setY(lc.getY() + 0.8);
            p.teleport(lc);
            double t = to.distance(lc);
            double v_x = (1.0 + 0.07 * t) * (to.getX() - lc.getX()) / t;
            double v_y = (1.0 + 0.03 * t) * (to.getY() - lc.getY()) / t - -0.04 * t;
            double v_z = (1.0 + 0.07 * t) * (to.getZ() - lc.getZ()) / t;
            Vector v = p.getVelocity();
            v.setX(v_x);
            v.setY(v_y);
            v.setZ(v_z);
            p.setVelocity(v);
            if (RevampPackages.getInstance().getConfigFile().getBoolean("GRAPPLINGHOOK.COOLDOWN_ENABLED")){
                if (!CooldownManager.getCooldowns().containsKey("GRAPPLINGHOOK_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("GRAPPLINGHOOK_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("GRAPPLINGHOOK_DELAY", p, RevampPackages.getInstance().getConfigFile().getDouble("GRAPPLINGHOOK.COOLDOWN"));
            }
        }
    }
}