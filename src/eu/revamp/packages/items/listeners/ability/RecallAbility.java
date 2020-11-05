package eu.revamp.packages.items.listeners.ability;
/*
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

//TODO CHANGE NAME
public class RecallAbility {
    private HashMap<UUID, Location> locationMap = new HashMap<>();

    @EventHandler
    public void onRecallPlace(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null && item.isSimilar(getItem())){
                if(!isEnabled()){
                    player.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_DISABLED));
                    return;
                }

                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                if(locationMap.containsKey(player.getUniqueId())){
                    player.sendMessage(StringUtil.color("&cYou already have a recall point."));
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                Location location = event.getClickedBlock().getLocation().add(new Vector(0, 1 ,0));
                locationMap.put(player.getUniqueId(), location);
                player.sendMessage(StringUtil.color("&aYou have set your recall point."));
                event.setCancelled(true);
                player.updateInventory();

                location.getBlock().setType(Material.REDSTONE_TORCH_ON);
            }
        }
    }

    @EventHandler
    public void onRecall(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null && item.isSimilar(getItem()) && player.isSneaking()){
                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                if(!locationMap.containsKey(player.getUniqueId())){
                    player.sendMessage(StringUtil.color("&cYou don't have a recall point set."));
                    return;
                }

                Location location = player.getLocation();
                Location check = locationMap.get(player.getUniqueId());

                if(location.getBlockX() > check.getBlockX() + getRange() ||
                        location.getBlockZ() > check.getBlockZ() + getRange() ||
                        location.getBlockY() > check.getBlockY() + getRange() ||
                        location.getBlockX() < check.getBlockX() - getRange() ||
                        location.getBlockZ() < check.getBlockZ() - getRange() ||
                        location.getBlockY() < check.getBlockY() - getRange()){
                    player.sendMessage(StringUtil.color("&cYou must be within " + getRange() + " blocks of your recall point."));
                    return;
                }

                player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 2);

                player.teleport(check);
                player.sendMessage(StringUtil.color("&aYou have recalled to your recall point."));
                perform(player, this);

                if(item.getAmount() > 1){
                    item.setAmount(item.getAmount() - 1);
                }else{
                    player.getInventory().remove(item);
                }

                check.getBlock().setType(Material.AIR);
                locationMap.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        if(locationMap.containsKey(player.getUniqueId())){
            locationMap.get(player.getUniqueId()).getBlock().setType(Material.AIR);
            locationMap.remove(player.getUniqueId());
        }
    }
}*/
