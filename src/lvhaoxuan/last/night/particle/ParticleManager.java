package lvhaoxuan.last.night.particle;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleManager extends BukkitRunnable {

    public static HashMap<Entity, Particle> entities = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry<Entity, Particle> entry : entities.entrySet()) {
            Entity en = entry.getKey();
            if (!en.isDead()) {
                Particle pa = entry.getValue();
                en.getWorld().spawnParticle(pa, en.getLocation(), 0);
            }
        }
    }
}
