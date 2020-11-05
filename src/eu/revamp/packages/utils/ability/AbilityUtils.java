package eu.revamp.packages.utils.ability;

import eu.revamp.packages.utils.ConfigFile;

public class AbilityUtils {

    private static final ConfigFile config = new ConfigFile();

    public static boolean isAbilityEnabled(String name){
        try {
            return config.getBoolean(name + ".ENABLED");
        } catch (Exception ignored) {
        }
        return false;
    }
}
