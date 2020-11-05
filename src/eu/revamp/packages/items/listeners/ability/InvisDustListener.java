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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class InvisDustListener implements Listener {

    private ArrayList<Player> invplayer;

    public InvisDustListener() {
        this.invplayer = new ArrayList<>();
    }

    @EventHandler
    public void invisClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("INVISDUST.MATERIAL")).setName(config.getString("INVISDUST.NAME")).setLore(config.getStringList("INVISDUST.LORE")).toItemStack("ability_invisdust");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("INVISDUST")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if(config.getBoolean("INVISDUST.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("INVISDUST_DELAY", player)) {
                for (String msg : config.getStringList("INVISDUST.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("INVISDUST_DELAY", player), true)));
                }
                e.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("INVISDUST_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("INVISDUST_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("INVISDUST_DELAY", player, config.getDouble("INVISDUST.COOLDOWN"));
        }
        for (String msg : config.getStringList("INVISDUST.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, config.getInt("INVISDUST.SECONDS") * 20, 0));
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("INVISDUST.MATERIAL")).setName(config.getString("INVISDUST.NAME")).setLore(config.getStringList("INVISDUST.LORE")).toItemStack("ability_invisdust"));
        this.invplayer.add(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                invplayer.remove(player);
            }
        }.runTaskLater(RevampPackages.getInstance(), config.getInt("INVISDUST.SECONDS") * 20);
    }

    @EventHandler
    public void hitByPlayer(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player) {
                Player defender = (Player) e.getEntity();
                if (this.invplayer.contains(defender)) {
                    if (defender.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        defender.removePotionEffect(PotionEffectType.INVISIBILITY);
                        for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("INVISDUST.PLAYER_WAS_ATTACKED")) {
                            defender.sendMessage(msg);
                        }
                    }
                    this.invplayer.remove(defender);
                }
            }
        }
    }
}
