package lvhaoxuan.last.night.particle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleManager extends BukkitRunnable {

    public static HashMap<Entity, List<Particle>> entities = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry<Entity, List<Particle>> entry : entities.entrySet()) {
            Entity en = entry.getKey();
            if (!en.isDead()) {
                for (Particle pa : entry.getValue()) {
                    en.getWorld().spawnParticle(pa, en.getLocation(), 0);
                }
            }
        }
    }
}
