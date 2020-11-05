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
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PyroBallListener implements Listener
{

    @EventHandler @SuppressWarnings("deprecation")
    void onLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player)e.getEntity().getShooter();
            ItemStack i = p.getInventory().getItemInHand();
            ItemMeta im = i.getItemMeta();
            ConfigFile config = RevampPackages.getInstance().getConfigFile();
            ItemStack ability = new ItemBuilder(config.getMaterial("PYROBALL.MATERIAL")).setName(config.getString("PYROBALL.NAME")).setLore(config.getStringList("PYROBALL.LORE")).toItemStack("ability_pyroball");
            if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("PYROBALL")) return;
            if (HookManager.getPluginMap().get("WorldGuard") != null) {
                WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                if (wgHook.isPvPBlocked(p)){
                    e.setCancelled(true);
                    return;
                }
            }
            if (e.getEntity() instanceof Snowball && e.getEntity().getType() == EntityType.SNOWBALL) {
                if (p.getVehicle() != null) {
                    p.getVehicle().remove();
                    p.eject();
                }
                e.setCancelled(true);
                p.setVelocity(p.getLocation().getDirection().normalize().setY(0.8f));
                p.setVelocity(p.getLocation().getDirection().normalize().multiply(4.4f));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("PYROBALL.MATERIAL")).setName(config.getString("PYROBALL.NAME")).setLore(config.getStringList("PYROBALL.LORE")).toItemStack("ability_pyroball");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("PYROBALL")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)) {
                event.setCancelled(true);
                return;
            }
        }
        if (RevampPackages.getInstance().getConfigFile().getBoolean("PYROBALL.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("PYROBALL_DELAY", player)) {
                event.setUseItemInHand(Event.Result.DENY);
                for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("PYROBALL.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("PYROBALL_DELAY", player), true)));
                }
            } else if (event.getItem().getType() == Material.SNOW_BALL) {
                if (!CooldownManager.getCooldowns().containsKey("PYROBALL_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("PYROBALL_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("PYROBALL_DELAY", player, RevampPackages.getInstance().getConfigFile().getInt("PYROBALL.COOLDOWN"));
            }
        }
    }
}
