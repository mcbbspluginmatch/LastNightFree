package lvhaoxuan.last.night.gun;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.ArmorStand;

public class BulletChestManager {

    public static HashMap<String, BulletChest> chestsNameMap = new HashMap<>();
    public static HashMap<UUID, BulletChest> chestsMap = new HashMap<>();

    public static boolean isBulletChest(ArmorStand as) {
        String name = as.getCustomName();
        String[] args = name.split(" ");
        if (args.length == 4) {
            try {
                Integer.parseInt(args[1]);
                Integer.parseInt(args[3]);
                String typeName = args[0];
                if (!chestsNameMap.containsKey(typeName)) {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }
}
