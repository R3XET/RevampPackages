package eu.revamp.packages.managers;

import eu.revamp.packages.RevampPackages;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class HookManager {

    private static final Map<String, PluginHook<?>> pluginMap = new HashMap<>();

    public HookManager(List<PluginHook<?>> list) {
        for (PluginHook<?> hook : list) {
            pluginMap.put(hook.getName(), (PluginHook<?>) hook.setup());
            if (RevampPackages.getInstance().getServer().getPluginManager().getPlugin(hook.getName()) != null) {
                Bukkit.getLogger().log(Level.INFO, "Successfully hooked " + hook.getName());
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "Could not hook " + hook.getName());
            }
        }
    }

    public static Map<String, PluginHook<?>> getPluginMap() {
        return pluginMap;
    }

}