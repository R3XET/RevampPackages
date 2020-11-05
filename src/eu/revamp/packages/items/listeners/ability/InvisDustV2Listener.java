package eu.revamp.packages.items.listeners.ability;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.CooldownManager;
import eu.revamp.packages.utils.ability.AbilityUtils;
import eu.revamp.packages.utils.time.TimeUtils;
import eu.revamp.packages.utils.ability.InvisDustV2;
import eu.revamp.packages.utils.ability.InvisDustV2_1_7_R4;
import eu.revamp.packages.utils.ability.InvisDustV2_1_8_R3;
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

public class InvisDustV2Listener implements Listener {

    private ArrayList<Player> invplayer;
    private InvisDustV2 invisDustV2;

    public InvisDustV2Listener() {
        this.invplayer = new ArrayList<>();
        if (RevampPackages.getInstance().isOnePointSeven()){
            invisDustV2 = new InvisDustV2_1_7_R4();
        }
        else if (RevampPackages.getInstance().isOnePointEight()){
            invisDustV2 = new InvisDustV2_1_8_R3();
        }
    }

    @EventHandler
    public void invisClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("INVISDUSTV2.MATERIAL")).setName(config.getString("INVISDUSTV2.NAME")).setLore(config.getStringList("INVISDUSTV2.LORE")).toItemStack("ability_invisdustv2");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability) || !AbilityUtils.isAbilityEnabled("INVISDUSTV2")) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if(config.getBoolean("INVISDUSTV2.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("INVISDUSTV2_DELAY", player)) {
                for (String msg : config.getStringList("INVISDUSTV2.COOLDOWN_MESSAGE")){
                    player.sendMessage(msg.replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("INVISDUSTV2_DELAY", player), true)));
                }
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("INVISDUSTV2_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("INVISDUSTV2_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("INVISDUSTV2_DELAY", player, config.getDouble("INVISDUSTV2.COOLDOWN"));
        }
        for (String msg : config.getStringList("INVISDUSTV2.ITEM_USED_MESSAGE")){
            player.sendMessage(msg);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, config.getInt("INVISDUSTV2.SECONDS") * 20, 0));
        this.invisDustV2.hideArmor(player);
        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("INVISDUSTV2.MATERIAL")).setName(config.getString("INVISDUSTV2.NAME")).setLore(config.getStringList("INVISDUSTV2.LORE")).toItemStack("ability_invisdustv2"));
        this.invplayer.add(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                invplayer.remove(player);
            }
        }.runTaskLater(RevampPackages.getInstance(), config.getInt("INVISDUSTV2.SECONDS") * 20);
    }

    @EventHandler
    public void hitByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player defender = (Player) event.getEntity();
                if (this.invplayer.contains(defender)) {
                    if (defender.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        defender.removePotionEffect(PotionEffectType.INVISIBILITY);
                        for (String msg : RevampPackages.getInstance().getConfigFile().getStringList("INVISDUSTV2.PLAYER_WAS_ATTACKED")) {
                            defender.sendMessage(msg);
                        }
                    }
                    this.invisDustV2.showArmor(defender);
                    this.invplayer.remove(defender);
                }
            }
        }
    }




/*
    @EventHandler
    public void invisClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack i = player.getInventory().getItemInHand();
        ItemMeta im = i.getItemMeta();
        ConfigFile config = RevampPackages.getInstance().getConfigFile();
        ItemStack ability = new ItemBuilder(config.getMaterial("INVISDUSTV2.MATERIAL")).setName(config.getString("INVISDUSTV2.NAME")).setLore(config.getStringList("INVISDUSTV2.LORE")).toItemStack("ability_invisdustv2");
        if (!i.hasItemMeta() || !im.hasDisplayName() || !ItemUtils.isSimilar(i, ability)) return;
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
            if (wgHook.isPvPBlocked(player)){
                event.setCancelled(true);
                return;
            }
        }
        if(config.getBoolean("INVISDUSTV2.COOLDOWN_ENABLED")) {
            if (RevampPackages.getInstance().getCooldownManager().isOnCooldown("INVISDUSTV2_DELAY", player)) {
                player.sendMessage(config.getString("INVISDUSTV2.COOLDOWN_MESSAGE").replace("%time%", TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("INVISDUSTV2_DELAY", player), true)));
                event.setCancelled(true);
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("INVISDUSTV2_DELAY")) {
                RevampPackages.getInstance().getCooldownManager().createCooldown("INVISDUSTV2_DELAY");
            }
            RevampPackages.getInstance().getCooldownManager().addCooldown("INVISDUSTV2_DELAY", player, config.getDouble("INVISDUSTV2.COOLDOWN"));
        }
        player.sendMessage(config.getString("INVISDUSTV2.ITEM_USED_MESSAGE"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, config.getInt("INVISDUSTV2.SECONDS") * 20, 0));

        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (o.getUniqueId() != player.getUniqueId()) {
                try {
                    Object c = Reflection.instantiateObject("CraftPlayer", Reflection.PackageType.CRAFTBUKKIT_ENTITY, o);

                    Object item = Reflection.instantiateObject("PacketPlayOutEntityEquipment", Reflection.PackageType.MINECRAFT_SERVER, player.getEntityId(), 0, null);
                    Object helmet = Reflection.instantiateObject("PacketPlayOutEntityEquipment", Reflection.PackageType.MINECRAFT_SERVER, player.getEntityId(), 4, null);
                    Object chestplate = Reflection.instantiateObject("PacketPlayOutEntityEquipment", Reflection.PackageType.MINECRAFT_SERVER, player.getEntityId(), 3, null);
                    Object leggings = Reflection.instantiateObject("PacketPlayOutEntityEquipment", Reflection.PackageType.MINECRAFT_SERVER, player.getEntityId(), 2, null);
                    Object boots = Reflection.instantiateObject("PacketPlayOutEntityEquipment", Reflection.PackageType.MINECRAFT_SERVER, player.getEntityId(), 1, null);


                    c = Reflection.instantiateObject("CraftPlayer", Reflection.PackageType.CRAFTBUKKIT_ENTITY, Reflection.getMethod(Class.forName("CraftPlayer"), "getHandle"));

                    Field dioCane = Reflection.getField((Class<?>) c, "playerConnection");
                    Object dioPorco = Reflection.instantiateObject(Reflection.getMethod((Field)dioCane, "sendPackage", item));

                    c.getHandle().playerConnection.sendPacket(item);
                    c.getHandle().playerConnection.sendPacket(helmet);
                    c.getHandle().playerConnection.sendPacket(chestplate);
                    c.getHandle().playerConnection.sendPacket(leggings);
                    c.getHandle().playerConnection.sendPacket(boots);

                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }


            //CraftPlayer c = (CraftPlayer) o;

            //import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;


                    //PacketPlayOutEntityEquipment item = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, null);
                    //PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
                    //PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
                    //PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
                    //PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
        }

        player.getInventory().removeItem(new ItemBuilder(config.getMaterial("INVISDUSTV2.MATERIAL")).setName(config.getString("INVISDUSTV2.NAME")).setLore(config.getStringList("INVISDUSTV2.LORE")).toItemStack("ability_INVISDUSTV2"));
        this.invplayer.add(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                invplayer.remove(player);
            }
        }.runTaskLater(RevampPackages.getInstance(), config.getInt("INVISDUSTV2.SECONDS") * 20);
    }

    @EventHandler
    public void hitByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player defender = (Player) event.getEntity();
            if (this.invplayer.contains(defender)) {
                if (defender.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    defender.removePotionEffect(PotionEffectType.INVISIBILITY);
                    defender.sendMessage(RevampPackages.getInstance().getConfigFile().getString("INVISDUSTV2.PLAYER_WAS_ATTACKED"));
                }
                for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                    if (o.getUniqueId() != defender.getUniqueId()) {
                        try {
                            CraftPlayer c = (CraftPlayer) o;
                            PacketPlayOutEntityEquipment item = new PacketPlayOutEntityEquipment(defender.getEntityId(), 0, CraftItemStack.asNMSCopy(defender.getItemInHand()));
                            PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(defender.getEntityId(), 4, CraftItemStack.asNMSCopy(defender.getInventory().getHelmet()));
                            PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(defender.getEntityId(), 3, CraftItemStack.asNMSCopy(defender.getInventory().getChestplate()));
                            PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(defender.getEntityId(), 2, CraftItemStack.asNMSCopy(defender.getInventory().getLeggings()));
                            PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(defender.getEntityId(), 1, CraftItemStack.asNMSCopy(defender.getInventory().getBoots()));
                            c.getHandle().playerConnection.sendPacket(item);
                            c.getHandle().playerConnection.sendPacket(helmet);
                            c.getHandle().playerConnection.sendPacket(chestplate);
                            c.getHandle().playerConnection.sendPacket(leggings);
                            c.getHandle().playerConnection.sendPacket(boots);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                this.invplayer.remove(defender);
            }
        }
    }
*/
    /*


INVISDUSTV2:
  MATERIAL: "INK_SACK"
  NAME: "&3&lInvisibility Dust V2"
  LORE:
    - "&7Become invisible until you get hit by someone"
    - "&7Also your armor is invisible!"
  # Seconds that the player is affected by invisibility
  SECONDS: 180
  # Enable cooldown between item uses?
  COOLDOWN_ENABLED: true
  # IN SECONDS # The time that the player must wait before using this item again
  COOLDOWN: 300
  COOLDOWN_MESSAGE:
    - "&7You still have an &3Invisibility Dust &7cooldown for another %time%."
  ITEM_USED_MESSAGE:
    - "&bYou become invisible. If someone hits you, you will lost your invisibility!"
  PLAYER_WAS_ATTACKED:
    - "&bSomeone hit you, you lost your invisibility!"

     */

}
