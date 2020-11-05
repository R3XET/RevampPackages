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
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StormBreakerListener implements Listener {

    @Getter private List<UUID> affectedUuids;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            if (event.getDamager() instanceof Player) {
                if (!event.getEntity().isDead()) {
                    if (event.getEntity() instanceof Player) {
                        Player attacker = (Player) event.getDamager();
                        Player defender = (Player) event.getEntity();
                        ItemStack i = attacker.getInventory().getItemInHand();
                        ItemMeta im = i.getItemMeta();
                        ConfigFile config = RevampPackages.getInstance().getConfigFile();
                        ItemStack ability = new ItemBuilder(config.getMaterial("STORMBREAKER.MATERIAL")).setName(config.getString("STORMBREAKER.NAME")).setLore(config.getStringList("STORMBREAKER.LORE")).toItemStack("ability_stormbreaker");
                        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("STORMBREAKER")) return;
                        if (HookManager.getPluginMap().get("WorldGuard") != null) {
                            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                            if (wgHook.isPvPBlocked(attacker)){
                                event.setCancelled(true);
                                return;
                            }
                        }
                        if (config.getBoolean("STORMBREAKER.COOLDOWN_ENABLED")) {
                            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("STORMBREAKER_DELAY", attacker)) {
                                for (String msg : config.getStringList("STORMBREAKER.COOLDOWN_MESSAGE")){
                                    attacker.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("STORMBREAKER_DELAY", attacker), true)));
                                }
                                event.setCancelled(true);
                                return;
                            }
                            if (!CooldownManager.getCooldowns().containsKey("STORMBREAKER_DELAY")) {
                                RevampPackages.getInstance().getCooldownManager().createCooldown("STORMBREAKER_DELAY");
                            }
                            RevampPackages.getInstance().getCooldownManager().addCooldown("STORMBREAKER_DELAY", attacker, config.getDouble("STORMBREAKER.COOLDOWN"));
                        }
                        for (String msg : config.getStringList("STORMBREAKER.ITEM_USED_MESSAGE")){
                            attacker.sendMessage(msg);
                        }
                        this.affectedUuids = findAffectedUuids(attacker, config.getInt("STORMBREAKER.RADIUS"));
                        /*StormBreakerUseEvent thorHammerEvent = new StormBreakerUseEvent(attacker, affectedUuids);
                        thorHammerEvent.call();
                        if (thorHammerEvent.isCancelled())
                            return;*/
                        event.setDamage(0.0D);
                        attacker.getWorld().strikeLightningEffect(attacker.getLocation());
                        for (Player target : getAffectedPlayers()) {
                            target.damage(config.getDouble("STORMBREAKER.DAMAGE"));
                            target.getWorld().strikeLightning(target.getLocation());
                        }
                        //attacker.getItemInHand().setDurability((short) (attacker.getItemInHand().getDurability() + 8));
                    }
                }
            }
        }
    }

    public List<UUID> findAffectedUuids(Player player, int radius) {
        List<UUID> toReturn = new ArrayList<>();
        if (player.getNearbyEntities(radius, radius, radius) != null) {
            for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                if (!(entity instanceof Player))
                    continue;
                toReturn.add(entity.getUniqueId());
                entity.getWorld().strikeLightning(entity.getLocation());
            }
        }
        return toReturn;
    }

    public List<Player> getAffectedPlayers() {
        return getAffectedUuids().stream().map(Bukkit::getPlayer).collect(Collectors.toList());
    }

    public void setAffectedPlayers(List<Player> affectedPlayers) {
        this.affectedUuids = affectedPlayers.stream().map(Entity::getUniqueId).collect(Collectors.toList());
    }
}
