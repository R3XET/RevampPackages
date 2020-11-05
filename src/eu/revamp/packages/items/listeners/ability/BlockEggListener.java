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
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockEggListener implements Listener {

    @EventHandler
    public void onEggHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Egg) {
            Projectile projectile = (Projectile) event.getDamager();
            if (event.getEntity() instanceof Player && projectile.getShooter() instanceof Player) {
                Egg egg = (Egg) event.getDamager();
                Player eggshooter = (Player) egg.getShooter();
                Player eggdamagedplayer = (Player) event.getEntity();
                ConfigFile config = RevampPackages.getInstance().getConfigFile();
                ItemStack i = eggshooter.getInventory().getItemInHand();
                ItemMeta im = i.getItemMeta();
                ItemStack ability = new ItemBuilder(config.getMaterial("BLOCKEGG.MATERIAL")).setName(config.getString("BLOCKEGG.NAME")).setLore(config.getStringList("BLOCKEGG.LORE")).toItemStack("ability_blockegg");
                if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("BLOCKEGG")) return;
                if ((int) eggdamagedplayer.getLocation().distance(eggshooter.getLocation()) <= config.getInt("BLOCKEGG.RANGE_OF_BLOCKS")) {
                    if (!CooldownManager.getCooldowns().containsKey("BLOCKEGGDEFENDER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("BLOCKEGGDEFENDER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("BLOCKEGGDEFENDER_DELAY", eggdamagedplayer, RevampPackages.getInstance().getConfigFile().getInt("BLOCKEGG.COOLDOWN_BREAK_PLACE"));
                    if (eggshooter.getItemInHand().getAmount() == 1) {
                        eggshooter.setItemInHand(null);
                    }
                    else {
                        eggshooter.getItemInHand().setAmount(eggshooter.getItemInHand().getAmount() - 1);
                    }
                } else {
                    for (String msg : config.getStringList("BLOCKEGG.NO_RANGE_MESSAGE")){
                        eggshooter.sendMessage(msg.replace("%damagedplayer%", eggdamagedplayer.getName()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEggThrow(PlayerInteractEvent event) {
        Player shooter = event.getPlayer();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack i = shooter.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ItemStack ability = new ItemBuilder(config.getMaterial("BLOCKEGG.MATERIAL")).setName(config.getString("BLOCKEGG.NAME")).setLore(config.getStringList("BLOCKEGG.LORE")).toItemStack("ability_blockegg");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("BLOCKEGG")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
           if (wgHook.isPvPBlocked(shooter)){
               event.setCancelled(true);
               return;
           }
        }
        if (config.getBoolean("BLOCKEGG.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGSHOOTER_DELAY", shooter) && shooter.getItemInHand().getType() == Material.EGG) {
                for (String msg : config.getStringList("BLOCKEGG.COOLDOWN_MESSAGE")){
                    shooter.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BLOCKEGGSHOOTER_DELAY", shooter), true)));
                }
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("BLOCKEGGSHOOTER_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("BLOCKEGGSHOOTER_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("BLOCKEGGSHOOTER_DELAY", shooter, RevampPackages.getInstance().getConfigFile().getDouble("BLOCKEGG.COOLDOWN"));
            //event.setCancelled(true);

            //TODO UNCOMMENT
            //shooter.getInventory().addItem(ability);
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        if (e.getPlayer() == null) return;
        Player player = e.getPlayer();
        if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGDEFENDER_DELAY", player) || !AbilityUtils.isAbilityEnabled("BLOCKEGG")) {
            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("BLOCKEGG.NO_BREAK_PLACE_MESSAGE")){
                player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BLOCKEGGDEFENDER_DELAY", player), true)));
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        if (e.getPlayer() == null) return;
        Player player = e.getPlayer();
        if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGDEFENDER_DELAY", player) || !AbilityUtils.isAbilityEnabled("BLOCKEGG")) {
            for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("BLOCKEGG.NO_BREAK_PLACE_MESSAGE")){
                player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BLOCKEGGDEFENDER_DELAY", player), true)));
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChickenSpawn(CreatureSpawnEvent event){
        if (event.getEntity() instanceof Chicken && event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG  && RevampPackages.getInstance().getConfigFile().getBoolean("DISABLE_CHICKEN_SPAWN")){
            event.setCancelled(true);
        }
    }
}
