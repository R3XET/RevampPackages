package eu.revamp.packages.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    @Getter public static CooldownManager instance;
    @Getter public static HashMap<String, HashMap<UUID, Long>> cooldowns = new HashMap<>();

    public void deleteCooldowns() {
        CooldownManager.cooldowns.clear();
    }

    public void createCooldown(String cooldown) {
        if (CooldownManager.getCooldowns().containsKey(cooldown)) {
            throw new IllegalArgumentException("Sorry, but cooldown doesn't exists.");
        }
        CooldownManager.getCooldowns().put(cooldown, new HashMap<>());
    }
    public HashMap<UUID, Long> getCooldownMap(String cooldown) {
        if (CooldownManager.cooldowns.containsKey(cooldown)) {
            return CooldownManager.cooldowns.get(cooldown);
        }
        return null;
    }
    public void addCooldown(String cooldown, Player p, double seconds) {
        if (!CooldownManager.cooldowns.containsKey(cooldown)) {
            throw new IllegalArgumentException(cooldown + " doesn't exists.");
        }
        long next = (long) (System.currentTimeMillis() + seconds * 1000L);
        CooldownManager.cooldowns.get(cooldown).put(p.getUniqueId(), next);
    }
    public boolean isOnCooldown(String cooldown, Player p) {
        return CooldownManager.cooldowns.containsKey(cooldown) && CooldownManager.cooldowns.get(cooldown).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= CooldownManager.cooldowns.get(cooldown).get(p.getUniqueId());
    }
    public int getCooldownMillis(String cooldown, Player p) {
        return (int)(CooldownManager.cooldowns.get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis());
    }
    public int getCooldownInt(String cooldown, Player p) {
        return (int)((CooldownManager.cooldowns.get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L);
    }
    public double getCooldownDouble(String cooldown, Player p) {
        return (double) (CooldownManager.cooldowns.get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L;
    }
    public long getCooldownLong(String cooldown, Player p) {
        return CooldownManager.cooldowns.get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis();
    }

    public void removeCooldown(String k, Player p) {
        if (!CooldownManager.cooldowns.containsKey(k)) {
            throw new IllegalArgumentException(k + " doesn't exists.");
        }
        CooldownManager.cooldowns.get(k).remove(p.getUniqueId());
    }

    public void removeCooldown(String k){
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!CooldownManager.cooldowns.containsKey(k)) {
                throw new IllegalArgumentException(k + " doesn't exists.");
            }
            CooldownManager.cooldowns.get(k).remove(online.getUniqueId());
        });
    }
}
