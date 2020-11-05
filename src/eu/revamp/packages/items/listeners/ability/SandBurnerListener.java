package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.items.ItemBuilder;
import eu.revamp.packages.utils.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SandBurnerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSandBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("SANDBURNER.MATERIAL")).setName(config.getString("SANDBURNER.NAME")).setLore(config.getStringList("SANDBURNER.LORE")).addEnchant(Enchantment.DIG_SPEED, 5).addEnchant(Enchantment.DURABILITY, 3).toItemStack("ability_sandburner");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || event.isCancelled() || !AbilityUtils.isAbilityEnabled("SANDBURNER")) return;
        if (block.getType() == Material.SAND) {
            event.setCancelled(true);
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GLASS));
        }
    }
}
