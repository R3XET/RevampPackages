package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.CooldownManager;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.time.TimeUtils;
import eu.revamp.packages.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class ThrowableWebListener implements Listener {

    /*private HashMap<Player, Entity> web;
    public ThrowableWebListener() {
        this.web = new HashMap<>();
    }*/

    /*
    @EventHandler
    public void onWebClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK) || player.isSneaking() || event.getClickedBlock() != null || !event.isCancelled()) {
            Block block = event.getClickedBlock();
            if (block != null) {
                if (block.getType().equals(Material.WEB) && block.hasMetadata("THROWWEB") && RevampPackages.getInstance().getConfigFile().getBoolean("THROWWEB.BREAK_ON_SHIFT_CLICK")) {
                    if (HookManager.getPluginMap().get("WorldGuard") != null) {
                        WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                        if (wgHook.isPvPBlocked(player)){
                            event.setCancelled(true);
                            return;
                        }
                    }
                    block.setType(Material.AIR);
                    Location loc = block.getLocation();
                    loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.WEB));
                }
            }
        }
    }*/

    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            Player p = e.getPlayer();
            ItemStack i = p.getInventory().getItemInHand();
            ItemMeta im = i.getItemMeta();
            ConfigFile config = RevampPackages.getInstance().getConfigFile();
            String webName = config.getString("THROWWEB.NAME");
            if (!i.hasItemMeta() || !im.hasDisplayName() || webName == null || !im.getDisplayName().equals(webName) || !AbilityUtils.isAbilityEnabled("THROWWEB")) return;
            if (HookManager.getPluginMap().get("WorldGuard") != null) {
                WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                if (wgHook.isPvPBlocked(p)){
                    e.setCancelled(true);
                    return;
                }
            }
            if (p.getItemInHand().getType() == Material.WEB) {
                if (config.getBoolean("THROWWEB.COOLDOWN_ENABLED")) {
                    if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("THROWWEB_DELAY", p)) {
                        for (String msg : config.getStringList("THROWWEB.COOLDOWN_MESSAGE")){
                            p.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("THROWWEB_DELAY", p), true)));
                        }
                        e.setCancelled(true);
                        return;
                    }
                    if (!CooldownManager.getCooldowns().containsKey("THROWWEB_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("THROWWEB_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("THROWWEB_DELAY", p, config.getDouble("THROWWEB.COOLDOWN"));
                }
                FallingBlock wobweb = p.getWorld().spawnFallingBlock(p.getEyeLocation(), Material.WEB, (byte) 0);
                wobweb.setMetadata("THROWWEB", new FixedMetadataValue(RevampPackages.getInstance(), true));
                wobweb.setVelocity(p.getLocation().getDirection().multiply(0.8D));
                //this.web.put(p, wobweb);
                p.getInventory().remove(new ItemBuilder(config.getMaterial("THROWWEB.MATERIAL")).setName(config.getString("THROWWEB.NAME")).setLore(config.getStringList("THROWWEB.LORE")).toItemStack("ability_throwweb"));
                /*
                if (p.getItemInHand().getAmount() == 1) {
                    int slot = p.getInventory().getHeldItemSlot();
                    p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                } else {
                    p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                }*/

                //RevampPackages.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RevampPackages.getInstance(), () -> this.web.remove(p), 60L);
            }
        }
    }

   /* @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            if (this.web.containsValue(event.getEntity())) {
                for (Player player : this.web.keySet()) {
                    Entity e = this.web.get(player);
                    if (e.equals(event.getEntity())) {
                        this.web.remove(player);
                    }
                }
            }
        }
    }*/
/*
    @EventHandler
    public void onWebDrop(ITEMSpawnEvent event) {
        if (event.getEntity().getItemStack().getType().equals(Material.WEB)) {
            event.getEntity().getItemStack().setType(Material.STRING);
        }
    }
    */
}