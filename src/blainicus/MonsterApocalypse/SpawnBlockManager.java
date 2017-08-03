package blainicus.MonsterApocalypse;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class SpawnBlockManager {
    List<Material> blocklist;
    MonsterApocalypse plugin;
    String itemstring;
    Material mat;

    public SpawnBlockManager(MonsterApocalypse instance) {
        plugin = instance;
        blocklist = new ArrayList<Material>();
    }

    @SuppressWarnings("deprecation")
    public void addblock(String m) {
        if (Material.getMaterial(m) != null) {
            try {
                blocklist.add(Material.getMaterial(Integer.parseInt(m)));
            } catch (NumberFormatException e) {
                blocklist.add(Material.getMaterial(m.toUpperCase()));
            }
        }
    }

    public boolean getAllowSpawn(Material m) {
        if (!plugin.checkspawnfeet) {
            return true;
        }
        for (int i = 0; i < blocklist.size(); i++) {
            if (blocklist.get(i) == m) {
                if (plugin.invertspawnfeet) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (plugin.invertspawnfeet) {
            return false;
        }
        return true;
    }
}
