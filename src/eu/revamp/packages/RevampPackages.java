package eu.revamp.packages;

import eu.revamp.packages.items.commands.PackageCommand;
import eu.revamp.packages.items.commands.RevampAbilityCommand;
import eu.revamp.packages.items.listeners.*;
import eu.revamp.packages.items.listeners.ability.*;
import eu.revamp.packages.license.RevampLicense;
import eu.revamp.packages.managers.HookManager;
import eu.revamp.packages.managers.PluginHook;
import eu.revamp.packages.managers.WorldGuardHook;
import eu.revamp.packages.utils.CC;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.packages.utils.CooldownManager;
import eu.revamp.packages.utils.reflection.Reflection;
import eu.revamp.packages.utils.items.PackageCrate;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RevampPackages extends JavaPlugin {

    private static final String userid = "%%__USER__%%";
    @Getter @Setter private static RevampPackages instance;
    private boolean valid;
    private boolean itemBlockedByWG;
    private ConfigFile configFile;
    private CooldownManager cooldownManager;

    public void onEnable() {
        setInstance(this);
        setValid(false);
        setConfigFile(new ConfigFile());
        this.checkLicense();
        if (!(isValid())) {
            try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
        } else {
            getCommand("package").setExecutor(new PackageCommand());
            getCommand("revampability").setTabCompleter(new RevampAbilityCommand());
            getCommand("revampability").setExecutor(new RevampAbilityCommand());
            //new AsyncCheckLicense().asyncCheck();
            PackageCrate.loadPackages();
            setItemBlockedByWG(getConfigFile().getBoolean("DENY_ABILITY_USE_IN_NO_PVP_REGIONS"));


            List<PluginHook<?>> hooks = new ArrayList<>();
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                hooks.add(new WorldGuardHook());
            }
            new HookManager(hooks);

            PluginManager pm = getServer().getPluginManager();

            pm.registerEvents(new PackageListener(), this);
            pm.registerEvents(new SwitchereggListener(), this);
            pm.registerEvents(new DamageSnowballListener(), this);
            pm.registerEvents(new ThrowableWebListener(), this);
            pm.registerEvents(new BlockEggListener(), this);
            pm.registerEvents(new DisarmerAxeListener(), this);
            pm.registerEvents(new NinjaAbilityListener(), this);
            pm.registerEvents(new FreezeGunListener(), this);
            pm.registerEvents(new InvisDustListener(), this);
            pm.registerEvents(new SecondChanceListener(), this);
            //pm.registerEvents(new BackPackListener(), this);
            pm.registerEvents(new GrapplingHookListener(), this);
            pm.registerEvents(new RocketListener(), this);
            pm.registerEvents(new PumpkinSwapperListener(), this);
            pm.registerEvents(new CocaineListener(), this);
            pm.registerEvents(new CooldownBowListener(), this);
            pm.registerEvents(new PortableStrength(), this);
            pm.registerEvents(new RotationStickListener(), this);
            pm.registerEvents(new RandomizerListener(), this);
            pm.registerEvents(new LumberJackListener(), this);
            pm.registerEvents(new PyroBallListener(), this);

            //pm.registerEvents(new MalignantEggListener(), this);
            pm.registerEvents(new UltimatePearlListener(), this);
            pm.registerEvents(new RefillChestListener(), this);
            pm.registerEvents(new GuardianAngelListener(), this);

            pm.registerEvents(new IcaroFeatherListener(), this);
            pm.registerEvents(new BedBombListener(), this);
            pm.registerEvents(new PocketBardListener(), this);

            pm.registerEvents(new InvisDustV2Listener(), this);
            pm.registerEvents(new WebBombListener(), this);
            pm.registerEvents(new StormBreakerListener(), this);
            pm.registerEvents(new SandBurnerListener(), this);


            setCooldownManager(new CooldownManager());
        }
    }

    public void onDisable() {
        try {
            Bukkit.getScheduler().cancelTasks(this);
            setInstance(null);
        } catch (Exception ignored) { }
    }

    public boolean getWorldGuard() {
        try {
            return getServer().getPluginManager().getPlugin("WorldGuard").isEnabled();
        } catch (NoClassDefFoundError  e) {
            //Bukkit.Bukkit.getConsoleSender().sendMessage("WorldGuard not found");
        }
        return false;
    }


    public boolean isOnePointSeven() {
        return Reflection.getVersion().contains("1_7");
    }

    public boolean isOnePointEight() {
        return Reflection.getVersion().contains("1_8");
    }

    /*
    private static String getUserSpigot(String userid) {
        if(RevampPackages.userid.equals("%%__USER__%%")) {
            return "Robot";
        }
        try {
            URL url = new URL("https://www.spigotmc.org/members/" + userid);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder code = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                code.append(line);
            }
            return code.toString().split("<title>")[1].split("</title>")[0].split(" | ")[0];
        }
        catch (IOException ignored) { }
        return null;
    }*/

/*
if (HookManager.getPluginMap().get("WorldGuard") != null) {
            WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
           if (wgHook.isPvPBlocked(shooter)){
               event.setCancelled(true);
               return;
           }
        }
 */

    private void checkLicense() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &e            This plugin is protected using a licence system! &b "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &e            Checking Licence... Please Wait...&b                "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
        String license_key = getConfigFile().getString("LICENSE");
        if (license_key.equals("XXXX-XXXX-XXXX-XXXX")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Go to config.yml to put your license key!");
            setValid(false);
            try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
        } else {
            RevampLicense.ValidationType vt = new RevampLicense(license_key, "https://api.revampdev.tk/verify.php", this).isValid();
            String clientName = new RevampLicense(license_key, "https://api.revampdev.tk/verify.php", this).clientName();
            switch (vt) {
                case VALID:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &a     License check successfully passed! &b                      "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &a     Thanks to " + clientName + " for purchasing! &b            "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &a     If you have issues please contact the author R3XET#0852&b  "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    setValid(true);
                    break;
                case INVALID_PLUGIN:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     This license is for another plugin! &b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Please contact the author R3XET#0852&b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
                    setValid(false);
                    break;
                case URL_ERROR:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     HTTP request error during License verification! &b         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Please contact the author R3XET#0852&b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
                    setValid(false);
                    break;
                case WRONG_RESPONSE:
                case PAGE_ERROR:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     The License system has an error! &b                        "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Please contact the author R3XET#0852&b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
                    setValid(false);
                    break;
                case KEY_NOT_FOUND:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     This license key doesn't exists! &b                        "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Please contact the author R3XET#0852&b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
                    setValid(false);
                    break;
                case KEY_OUTDATED:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     The license key has been expired! &b                       "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Please contact the author R3XET#0852&b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
                    setValid(false);
                    break;
                case NOT_VALID_IP:
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Your server's IP is invalid! &b                            "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4     Please contact the author R3XET#0852&b                     "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                    try { Bukkit.getPluginManager().disablePlugin(this); } catch (Exception ignored) { }
                    setValid(false);
            }
        }
    }
}
