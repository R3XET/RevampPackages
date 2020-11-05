package eu.revamp.packages.license;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncCheckLicense {

    private String license_key;

    public AsyncCheckLicense() {
        this.license_key = RevampPackages.getInstance().getConfigFile().getString("LICENSE");
    }

    public void asyncCheck(){
        new BukkitRunnable() {
            @Override
            public void run() {
                RevampLicense.ValidationType vt = new RevampLicense(license_key, "https://api.revampdev.tk/verify.php", RevampPackages.getInstance()).isValid();
                switch (vt) {
                    case VALID:
                        break;
                    case INVALID_PLUGIN:
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            This license is for another plugin! &b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Please contact the author R3XET#0852&b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getPluginManager().disablePlugin(RevampPackages.getInstance());
                        break;
                    case URL_ERROR:
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            HTTP request error during License verification! &b  "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Please contact the author R3XET#0852&b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getPluginManager().disablePlugin(RevampPackages.getInstance());
                        break;
                    case WRONG_RESPONSE:
                    case PAGE_ERROR:
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            The License system has an error! &b                 "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Please contact the author R3XET#0852&b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getPluginManager().disablePlugin(RevampPackages.getInstance());
                        break;
                    case KEY_NOT_FOUND:
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            This license key doesn't exists! &b                 "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Please contact the author R3XET#0852&b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getPluginManager().disablePlugin(RevampPackages.getInstance());
                        break;
                    case KEY_OUTDATED:
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            The license key has been expired! &b                "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Please contact the author R3XET#0852&b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getPluginManager().disablePlugin(RevampPackages.getInstance());
                        break;
                    case NOT_VALID_IP:
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Your server's IP is invalid! &b                     "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|       &4            Please contact the author R3XET#0852&b              "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("  #|                                                                         "));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&b&m#+++______------______------______------______------______------______+++"));
                        Bukkit.getPluginManager().disablePlugin(RevampPackages.getInstance());
                        break;
                }
            }
        }.runTaskTimerAsynchronously((RevampPackages.getInstance()), 36000L, 36000L);
    }
}
