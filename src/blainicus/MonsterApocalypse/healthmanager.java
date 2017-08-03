package blainicus.MonsterApocalypse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class healthmanager implements Runnable {
    public Map<LivingEntity, Double> healthmap;
    public Map<LivingEntity, Long> timemap;
    MonsterApocalypse plugin;

    public healthmanager(MonsterApocalypse instance) {
        plugin = instance;
        healthmap = new HashMap<LivingEntity, Double>();
        timemap = new HashMap<LivingEntity, Long>();
    }

    @Override
    public void run() {
        cleanup();
    }

    public void cleanup() {
        Object[] keys = healthmap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            try {
                if (((LivingEntity) keys[i]).isDead()) {
                    healthmap.remove(keys[i]);
                }
            } catch (Exception e) {
                healthmap.remove(keys[i]);
            }
        }
        Object[] keyz = timemap.keySet().toArray();
        for (int i = 0; i < keyz.length; i++) {
            try {
                if (((LivingEntity) keyz[i]).isDead()) {
                    healthmap.remove(keyz[i]);
                }
            } catch (Exception e) {
                healthmap.remove(keyz[i]);
            }
        }
    }

    public void addmob(LivingEntity ent) {
        if (plugin.getMobName(ent) == "" || ent.isDead()) {
            return;
        }
        healthmap.put(ent, (double) plugin.getMobHealth(ent));
        timemap.put(ent, (long) 0);
    }

    public void removemob(Entity ent) {
        if (plugin.getMobName(ent) == "" || ent.isDead()) {
            return;
        }
        healthmap.remove((ent));
        timemap.remove((ent));
    }

    public double damagemob(LivingEntity ent, double amount) {
        if (plugin.getMobName(ent) == "" || ent.isDead()) {
            return 0;
        }
        try {
            double neww = healthmap.get(ent) - amount;
            healthmap.put(ent, neww);
            timemap.put(ent, System.currentTimeMillis());
            return neww;
        } catch (NullPointerException e) {
            addmob(ent);
            double neww = healthmap.get(ent) - amount;
            healthmap.put(ent, neww);
            timemap.put(ent, System.currentTimeMillis());
            return neww;
        }
    }

    public double getmobhp(LivingEntity ent) {
        if (plugin.getMobName(ent) == "" || ent.isDead()) {
            return 0;
        }
        try {
            double neww = healthmap.get(ent);
            return neww;
        } catch (NullPointerException e) {
            addmob(ent);
            double neww = healthmap.get(ent);
            return neww;
        }
    }

    public void setmobhp(LivingEntity ent, double newvalue) {
        if (plugin.getMobName(ent) == "" || ent.isDead()) {
            return;
        }
        try {
            healthmap.put(ent, newvalue);
        } catch (NullPointerException e) {
            addmob(ent);
        }
    }

    public long getLastDamageTime(LivingEntity ent) {
        long li = 0;
        try {
            li = timemap.get(ent);
        } catch (NullPointerException e) {
            li = 0;
        }
        return li;
    }

    protected void addall() {
        for (int i = 0; i < plugin.worldnames.size(); i++) {
            String worldname = plugin.worldnames.get(i);
            List<Entity> L = plugin.getServer().getWorld(worldname).getEntities();
            Iterator<Entity> iter = L.iterator();
            while (iter.hasNext()) {
                Entity entscan = iter.next();
                if (plugin.getMobName(entscan) != "" && !entscan.isDead()) {
                    addmob((LivingEntity) entscan);
                }
            }
        }
    }
}
