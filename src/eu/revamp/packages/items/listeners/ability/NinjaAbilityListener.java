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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class NinjaAbilityListener implements Listener {

    private Map<Player, Location> lastDamage = new HashMap<>();
    private Map<Player, Player> lastAttacker = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerHits(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        Player defender = (Player) e.getEntity();
        getPositions(attacker, defender);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerShoot(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player attacker = (Player) arrow.getShooter();
                if (event.getEntity() instanceof Damageable) {
                    Damageable dp = (Damageable) event.getEntity();
                    if (dp instanceof Player) {
                        Player defender = (Player) dp;
                        if (!dp.isDead() && !(event.isCancelled())) {
                            if (attacker.equals(defender)) return;
                            getPositions(attacker, defender);
                        }
                    }
                }
            }
        }
    }

    private void getPositions(Player attacker, Player defender) {
        Location location = attacker.getLocation();
        lastAttacker.put(defender, attacker);
        lastDamage.put(defender, location);
        new BukkitRunnable() {
            @Override
            public void run() {
                lastDamage.remove(defender, location);
                lastAttacker.remove(defender, attacker);
            }
        }.runTaskLater(RevampPackages.getInstance(), 30 * 20);
    }


    @EventHandler
    public void ninjaClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("NINJA.MATERIAL")).setName(config.getString("NINJA.NAME")).setLore(config.getStringList("NINJA.LORE")).toItemStack("ability_ninja");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("NINJA")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                e.setCancelled(true);
                return;
            }
        }
        if (lastDamage.containsKey(player)) {
            Location location = lastDamage.get(player);
            if (config.getBoolean("NINJA.COOLDOWN_ENABLED")) {
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("NINJA_DELAY", player)) {
                    for (String msg : config.getStringList("NINJA.COOLDOWN_MESSAGE")){
                        player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("NINJA_DELAY", player), true)));
                    }
                    e.setCancelled(true);
                    return;
                }
                if (!CooldownManager.getCooldowns().containsKey("NINJA_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("NINJA_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("NINJA_DELAY", player, config.getDouble("NINJA.COOLDOWN"));
            }
            if (config.getBoolean("NINJA.ONE_TIME_USE")) {
                player.getInventory().removeItem(new ItemBuilder(config.getMaterial("NINJA.MATERIAL")).setName(config.getString("NINJA.NAME")).setLore(config.getStringList("NINJA.LORE")).toItemStack("ability_ninja"));
            }
            for (String msg : config.getStringList("NINJA.ITEM_USED_MESSAGE")){
                player.sendMessage(msg.replace("%time%", config.getString("NINJA.TP_DELAY")));
            }
            if (config.getBoolean("NINJA.TELEPORTED_OTHER_ENABLED")){
                for (String msg : config.getStringList("NINJA.TELEPORTED_OTHER_MESSAGE")){
                    lastAttacker.get(player).sendMessage(msg.replace("%time%", config.getString("NINJA.TP_DELAY")).replace("%player%", player.getName()));
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String msg : config.getStringList("NINJA.SUCCESSFULLY_TELEPORTED_MESSAGE")){
                        player.sendMessage(msg);
                    }
                    player.teleport(location);
                }
            }.runTaskLater(RevampPackages.getInstance(), config.getInt("NINJA.TP_DELAY") * 20);
        }
    }
}
/*
    private HashMap<Entity, Entity> lastDamage = new HashMap<>();
    private Location lastLocation;

    @EventHandler
    public void playerHits(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        Player defender = (Player) e.getEntity();
        lastDamage.put(defender, attacker);
        Entity player = lastDamage.get(defender);
        if ((player.getLocation() != null) && (attacker != null) && (defender != null)) {
            lastLocation = player.getLocation();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                lastDamage.remove(defender, attacker);
            }
        }.runTaskLater(RevampPackages.getInstance(), 30 * 20);
    }

    @EventHandler
    public void NinjaClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        String starName = config.getString("NINJA.NAME");
        if (!i.hasItemMeta() || !im.hasDisplayName() || starName == null || !im.getDisplayName().equalsIgnoreCase(starName))) return;
        if (lastDamage.containsKey(player) && lastLocation != null) {
            if (config.getBoolean("NINJA.COOLDOWN_ENABLED")) {
                if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("NINJA_DELAY", player)) {
                    player.sendMessage(config.getString("NINJA.COOLDOWN_MESSAGE")).replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("NINJA_DELAY", player), true)));
                    e.setCancelled(true);
                    return;
                }
                if (!CooldownManager.getCooldowns().containsKey("NINJA_DELAY")) {
                    RevampPackages.getInstance().getCooldownManager().createCooldown("NINJA_DELAY");
                }
                RevampPackages.getInstance().getCooldownManager().addCooldown("NINJA_DELAY", player, config.getDouble("NINJA.COOLDOWN"));
            }
            if (config.getBoolean("NINJA.ONE_TIME_USE")) {
                player.getInventory().removeItem(ItemBuilder.createItem(config.getMaterial("NINJA.MATERIAL"), config.getString("NINJA.NAME"), 1, 0, config.getStringList("NINJA.LORE")));
            }
            player.sendMessage(config.getString("NINJA.ITEM_USED_MESSAGE").replace("%time%", config.getString("NINJA.TP_DELAY"))));
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(config.getString("NINJA.SUCCESSFULLY_TELEPORTED_MESSAGE")));
                    player.teleport(lastLocation);
                }
            }.runTaskLater(RevampPackages.getInstance(), config.getInt("NINJA.TP_DELAY") * 20);
        }
    }
 */
