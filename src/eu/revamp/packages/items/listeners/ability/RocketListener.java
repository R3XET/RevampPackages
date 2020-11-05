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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class RocketListener implements Listener {

    private ArrayList<String> rocketused;
    public RocketListener() {
        this.rocketused = new ArrayList<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("ROCKET.MATERIAL")).setName(config.getString("ROCKET.NAME")).setLore(config.getStringList("ROCKET.LORE")).toItemStack("ability_rocket");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("ROCKET")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(p)){
                e.setCancelled(true);
                return;
            }
        }
        if (RevampPackages.getInstance().getConfigFile().getBoolean("ROCKET.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROCKET_DELAY", p)) {
                for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("ROCKET.COOLDOWN_MESSAGE")){
                    p.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ROCKET_DELAY", p), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("ROCKET_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("ROCKET_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("ROCKET_DELAY", p, RevampPackages.getInstance().getConfigFile().getDouble("ROCKET.COOLDOWN"));
        }
        if (!(this.rocketused.contains(p.getName()))) {
            this.rocketused.add(p.getName());
        }
        e.setCancelled(true);
        if (RevampPackages.getInstance().getConfigFile().getBoolean("ROCKET.ONE_TIME_USE")) {
            p.getInventory().removeItem(new ItemBuilder(config.getMaterial("ROCKET.MATERIAL")).setName(config.getString("ROCKET.NAME")).setLore(config.getStringList("ROCKET.LORE")).toItemStack("ability_rocket"));
        }
        Vector vector = p.getEyeLocation().getDirection();
        vector.setY(2);
        p.setVelocity(vector);
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && this.rocketused.contains(p.getName())) {
                this.rocketused.remove(p.getName());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        this.rocketused.remove(p.getName());
    }
}
