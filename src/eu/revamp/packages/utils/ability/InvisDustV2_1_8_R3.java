package eu.revamp.packages.utils.ability;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class InvisDustV2_1_8_R3 implements InvisDustV2{
    @Override
    public void hideArmor(Player p) {
        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (o.getUniqueId() != p.getUniqueId()) {
                try {
                    CraftPlayer c = (CraftPlayer) o;
                    PacketPlayOutEntityEquipment item = new PacketPlayOutEntityEquipment(p.getEntityId(), 0, null);
                    PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(p.getEntityId(), 4, null);
                    PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(p.getEntityId(), 3, null);
                    PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(p.getEntityId(), 2, null);
                    PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(p.getEntityId(), 1, null);
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
    }

    @Override
    public void showArmor(Player p) {
        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (o.getUniqueId() != p.getUniqueId()) {
                try {
                    CraftPlayer c = (CraftPlayer) o;
                    PacketPlayOutEntityEquipment item = new PacketPlayOutEntityEquipment(p.getEntityId(), 0, CraftItemStack.asNMSCopy(p.getItemInHand()));
                    PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(p.getEntityId(), 4, CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));
                    PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(p.getEntityId(), 3, CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));
                    PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(p.getEntityId(), 2, CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));
                    PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(p.getEntityId(), 1, CraftItemStack.asNMSCopy(p.getInventory().getBoots()));
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
    }
}
