package eu.revamp.packages.utils.firework;

import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.utils.events.FireworkDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtils {
    private final RevampPackages plugin = RevampPackages.getInstance();

    public void fireWork(Location loc) {
        final Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        fm.addEffects(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).withColor(Color.AQUA).withColor(Color.ORANGE).withColor(Color.YELLOW).trail(false).flicker(false).build());
        fm.setPower(0);
        fw.setFireworkMeta(fm);
        FireworkDamageEvent.addFirework(fw);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, fw :: detonate, 2);
    }
}
