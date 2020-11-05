package eu.revamp.packages.managers;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import eu.revamp.packages.RevampPackages;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardHook implements PluginHook<WorldGuardHook> {

    private static boolean instantiated = false;
    private WorldGuardPlugin worldGuardPlugin;

    public WorldGuardHook() {
        super();
    }

    @Override
    public WorldGuardHook setup() {
        this.worldGuardPlugin = WorldGuardPlugin.inst();
        instantiated = true;
        return this;
    }

    public boolean isPvPBlocked(Player player){
        if (!instantiated) {
            return false;
        }
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        Location loc = player.getLocation();
        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (RevampPackages.getInstance().isItemBlockedByWG() && !query.testState(loc, localPlayer, DefaultFlag.PVP)){
            player.sendMessage(RevampPackages.getInstance().getConfigFile().getString("CANNOT_USE_HERE_WORLDGUARD"));
            return true;
        }
        return false;
    }


    public boolean canBuild(Player player, Block block) {
        if (!instantiated) {
            return true;
        }
        return worldGuardPlugin.canBuild(player, block);
    }


    @Override
    public String getName() {
        return "WorldGuard";
    }
}