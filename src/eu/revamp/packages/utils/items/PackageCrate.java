package eu.revamp.packages.utils.items;

import eu.revamp.packages.RevampPackages;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ToString
public class PackageCrate {
    @Getter @Setter private static List<PackageCrate> packages = new ArrayList<>();
    @Getter @Setter private List<ItemStack> items;
    @Getter @Setter private List<String> commands;
    @Getter @Setter private String name;
    @Getter @Setter private String displayname;

    private PackageCrate(String name) {
        this.items = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.name = name;
    }

    public static void loadPackages() {
        ConfigurationSection section = RevampPackages.getInstance().getConfigFile().getConfigurationSection("packages");
        PackageCrate[] p = new PackageCrate[1];
        section.getKeys(false).forEach(key -> {
            p[0] = new PackageCrate(key);
            p[0].setDisplayname(RevampPackages.getInstance().getConfigFile().getString("packages." + key + ".DISPLAY_NAME"));
            RevampPackages.getInstance().getConfigFile().getStringList("packages." + key + ".ITEMS").forEach(str -> p[0].getItems().add(ItemUtils.deserialize(str)));
            RevampPackages.getInstance().getConfigFile().getStringList("packages." + key + ".COMMANDS").forEach(str -> p[0].getCommands().add(str));
            PackageCrate.packages.add(p[0]);
        });
    }

    public static PackageCrate getRandom() {
        if (PackageCrate.getPackages().size() == 1) {
            return PackageCrate.getPackages().get(0);
        }
        return PackageCrate.getPackages().get(new Random().nextInt(PackageCrate.getPackages().size()));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PackageCrate)) {
            return false;
        }
        PackageCrate other = (PackageCrate) o;
        if (!other.canEqual(this)) {
            return false;
        }
        ITEMSNull: {
            if (this.items == null) {
                if (other.items == null) {
                    break ITEMSNull;
                }
            }
            else if (this.items.equals(other.items)) {
                break ITEMSNull;
            }
            return false;
        }
        COMMANDSNull: {
            if (this.commands == null) {
                if (other.commands == null) {
                    break COMMANDSNull;
                }
            }
            else if (this.commands.equals(other.commands)) {
                break COMMANDSNull;
            }
            return false;
        }
        nameNull: {
            if (this.name == null) {
                if (other.name == null) {
                    break nameNull;
                }
            }
            else if (this.name.equals(other.name)) {
                break nameNull;
            }
            return false;
        }
        if (this.displayname == null) {
            return other.displayname == null;
        }
        else return this.displayname.equals(other.displayname);
    }

    private boolean canEqual(Object other) {
        return other instanceof PackageCrate;
    }

    @Override
    public int hashCode() {
        int prime = 59;
        int result = 1;
        result = result * prime + ((items == null) ? 43 : items.hashCode());
        result = result * prime + ((commands == null) ? 43 : commands.hashCode());
        result = result * prime + ((name == null) ? 43 : name.hashCode());
        result = result * prime + ((displayname == null) ? 43 : displayname.hashCode());
        return result;
    }
}