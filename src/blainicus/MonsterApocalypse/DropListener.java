package blainicus.MonsterApocalypse;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DropListener implements Listener {
    MonsterApocalypse plugin;

    public DropListener(MonsterApocalypse instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent e) {
        for (int i = 0; i < plugin.worldnames.size(); i++) {
            String worldname = plugin.worldnames.get(i);
            if (e.getEntity().getWorld().getName().equals(worldname))
                break;
            if (i == plugin.worldnames.size() - 1)
                return;
        }
        Entity ent = e.getEntity();
        if (plugin.getMobDrops(ent) == null)
            return;
        if (plugin.getMobDropOverwrite(ent))
            e.getDrops().clear();
        plugin.rand = new Random(System.currentTimeMillis());
        plugin.dropper.addDrops(ent, e.getDrops());
    }

}
