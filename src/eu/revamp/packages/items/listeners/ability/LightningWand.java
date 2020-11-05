package eu.revamp.packages.items.listeners.ability;
/*
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class LightningWand implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(event.getRightClicked() instanceof Player){
            Player clicked = (Player) event.getRightClicked();

            if(item != null && item.isSimilar(getItem().clone())){
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

                if(player.getItemInHand().getAmount() > 1){
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                }else{
                    player.getInventory().remove(player.getItemInHand());
                }

                clicked.getWorld().strikeLightning(clicked.getLocation());
                event.setCancelled(true);
                player.updateInventory();

                player.sendMessage(StringUtil.color("&aYou have smited " + clicked.getName() + " with your Lighting Rod."));
                clicked.sendMessage(StringUtil.color("&cYou have been smited by " + player.getName() + "'s Lighting Rod."));
                perform(player, this);
            }
        }
    }
}
*/