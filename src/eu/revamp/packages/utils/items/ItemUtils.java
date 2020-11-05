package eu.revamp.packages.utils.items;

import eu.revamp.packages.multisupport.nbttagapi.NBTItem;
import eu.revamp.packages.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemUtils {

    public boolean isAbility(ItemStack is, String nbtTag, ItemStack itemUsed) {
        NBTItem nbti = new NBTItem(is);
        nbti.setString("abilityType", nbtTag);
        is = nbti.getItem();
        return is.isSimilar(itemUsed);
    }

    public boolean isListerAbility() {

        return true;
    }

    private static Random random = new Random();

    public static String serialize(ItemStack item) {
        StringBuilder builder = new StringBuilder();
        builder.append(item.getType().toString());
        if (item.getDurability() != 0) {
            builder.append(":").append(item.getDurability());
        }
        builder.append(" ").append(item.getAmount());
        for (Enchantment enchant : item.getEnchantments().keySet()) {
            builder.append(" ").append(enchant.getName()).append(":").append(item.getEnchantments().get(enchant));
        }
        String name = getName(item);
        if (name != null) {
            builder.append(" name:").append(name);
        }
        String lore = getLore(item);
        if (lore != null) {
            builder.append(" lore:").append(lore);
        }
        org.bukkit.Color color = getArmorColor(item);
        if (color != null) {
            builder.append(" rgb:").append(color.getRed()).append("|").append(color.getGreen()).append("|").append(color.getBlue());
        }
        String owner = getOwner(item);
        if (owner != null) {
            builder.append(" owner:").append(owner);
        }
        return builder.toString();
    }

    public static ItemStack deserialize(String serializedItem) {
        String[] strings = serializedItem.split(" ");
        Map<Enchantment, Integer> enchants = new HashMap<>();
        ItemStack item = new ItemStack(Material.AIR);
        int length = strings.length;
        int i = 0;
        while (i < length) {
            String str = strings[i];
            String[] args = str.split(":");
            if (Material.matchMaterial(args[0]) != null && item.getType() == Material.AIR) {
                item.setType(Material.matchMaterial(args[0]));
                if (args.length == 2) {
                    item.setDurability(Short.parseShort(args[1]));
                    break;
                }
                break;
            } else {
                ++i;
            }
        }
        if (item.getType() == Material.AIR) {
            Bukkit.getLogger().info("Could not find a valid material for the item in \"" + serializedItem + "\"");
            return null;
        }
        String[] array2 = strings;
        for (int length2 = array2.length, j = 0; j < length2; ++j) {
            String str = array2[j];
            String[] args = str.split(":", 2);
            if (isNumber(args[0])) {
                item.setAmount(Integer.parseInt(args[0]));
            }
            if (args.length != 1) {
                if (args[0].equalsIgnoreCase("name")) {
                    setName(item, CC.translate(args[1]));
                } else if (args[0].equalsIgnoreCase("lore")) {
                    setLore(item, CC.translate(args[1]));
                } else if (args[0].equalsIgnoreCase("rgb")) {
                    setArmorColor(item, args[1]);
                } else if (args[0].equalsIgnoreCase("owner")) {
                    setOwner(item, args[1]);
                } else if (Enchantment.getByName(args[0].toUpperCase()) != null) {
                    enchants.put(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
                }
            }
        }
        item.addUnsafeEnchantments(enchants);
        return item.getType().equals(Material.AIR) ? null : item;
    }

    private static String getOwner(ItemStack item) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            return null;
        }
        return ((SkullMeta) item.getItemMeta()).getOwner();
    }

    private static void setOwner(ItemStack item, String owner) {
        try {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(owner);
            item.setItemMeta(meta);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static String getName(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return null;
        }
        return item.getItemMeta().getDisplayName().replace(" ", "_").replace('§', '&');
    }

    private static void setName(ItemStack item, String name) {
        name = name.replace("_", " ");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    private static String getLore(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        if (!item.getItemMeta().hasLore()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        List<String> lore = item.getItemMeta().getLore();
        for (int ind = 0; ind < lore.size(); ++ind) {
            builder.append(((ind > 0) ? "|" : "") + lore.get(ind).replace(" ", "_").replace('§', '&'));
        }
        return builder.toString();
    }

    private static void setLore(ItemStack item, String lore) {
        lore = lore.replace("_", " ");
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore.split("\\|")));
        item.setItemMeta(meta);
    }

    private static org.bukkit.Color getArmorColor(ItemStack item) {
        if (!(item.getItemMeta() instanceof LeatherArmorMeta)) {
            return null;
        }
        return ((LeatherArmorMeta) item.getItemMeta()).getColor();
    }

    private static void setArmorColor(ItemStack item, String str) {
        try {
            String[] colors = str.split("\\|");
            int red = Integer.parseInt(colors[0]);
            int green = Integer.parseInt(colors[1]);
            int blue = Integer.parseInt(colors[2]);
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(org.bukkit.Color.fromRGB(red, green, blue));
            item.setItemMeta(meta);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    public static int getRandomSlot(int max, int min) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false;
        } else if (item1 == item2) {
            return true;
        } else {
            return item2.getTypeId() == item1.getTypeId() && item2.hasItemMeta() == item1.hasItemMeta() && (!item2.hasItemMeta() || Bukkit.getItemFactory().equals(item2.getItemMeta(), item1.getItemMeta()));
        }
    }

    public static boolean isSimilarFull(ItemStack item1, ItemStack item2) {
        if (item2.getType() == item1.getType() && item2.getDurability() == item1.getDurability()) {
            ItemMeta item1Meta = item1.getItemMeta();
            ItemMeta item2Meta = item2.getItemMeta();
            if (item1Meta.hasDisplayName() != item2Meta.hasDisplayName()) {
                return false;
            }
            if (item1Meta.hasDisplayName()) {
                if (!item1Meta.getDisplayName().equals(item2Meta.getDisplayName())) {
                    return false;
                }
            }
            if (item1Meta.hasLore() != item2Meta.hasLore()) {
                return false;
            }
            if (item1Meta.hasLore()) {
                if (item1Meta.getLore().size() != item2Meta.getLore().size()) {
                    return false;
                }
                for (int index = 0; index < item1Meta.getLore().size(); index++) {
                    if (item1Meta.getLore().get(index).equals(item2Meta.getLore().get(index))) {
                        return false;
                    }
                }
            }
            if (item1Meta.hasEnchants() != item2Meta.hasEnchants()) {
                return false;
            }
            if (item1Meta.hasEnchants()) {
                if (item1Meta.getEnchants().size() != item2Meta.getEnchants().size()) {
                    return false;
                }
                for (Map.Entry<Enchantment, Integer> enchantInfo : item1Meta.getEnchants().entrySet()) {
                    if (item1Meta.getEnchantLevel(enchantInfo.getKey()) != item2Meta.getEnchantLevel(enchantInfo.getKey())) {
                        return false;
                    }
                }
            }
            if (item1Meta.getItemFlags().size() != item2Meta.getItemFlags().size()) {
                return false;
            }
            for (ItemFlag flag : item1Meta.getItemFlags()) {
                if (!item2Meta.hasItemFlag(flag)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
