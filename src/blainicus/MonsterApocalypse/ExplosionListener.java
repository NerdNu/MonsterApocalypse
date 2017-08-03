package blainicus.MonsterApocalypse;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ExplosionListener implements Listener {
    MonsterApocalypse plugin;
    double x, y, z, xdis, ydis, zdis;
    boolean xflag, yflag, zflag;

    public ExplosionListener(MonsterApocalypse instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (plugin.changeexplosions) {
            for (int i = 0; i < plugin.worldnames.size(); i++) {
                String worldname = plugin.worldnames.get(i);
                if (event.getEntity().getWorld().getName().equals(worldname))
                    break;
                if (i == plugin.worldnames.size() - 1)
                    return;
            }
            Entity exploder = event.getEntity();
            if (plugin.wgexplosions && !plugin.getWGExplosion(exploder)) {
                event.setCancelled(true);
            }
            if (exploder instanceof Creeper) {
                event.setRadius((float) plugin.getMobRadius("Creeper"));
                event.setFire(plugin.getMobFireExplosion("Creeper"));
            }
            if (exploder instanceof Fireball) {
                event.setRadius((float) plugin.getMobRadius("Ghast"));
                event.setFire(plugin.getMobFireExplosion("Ghast"));
                if (plugin.wgexplosions && !plugin.getWGExplosion(exploder)) {
                    event.setCancelled(true);
                    exploder.remove();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplosion(EntityExplodeEvent event) {
        try {
            for (int i = 0; i < plugin.worldnames.size(); i++) {
                String worldname = plugin.worldnames.get(i);
                if (event.getEntity().getWorld().getName().equals(worldname))
                    break;
                if (i == plugin.worldnames.size() - 1)
                    return;
            }
            Entity exploder = event.getEntity();
            if (plugin.wgexplosions && !plugin.getWGExplosion(exploder.getLocation())) {
                event.setCancelled(true);
            }
            if (exploder instanceof EnderDragon) {
                if (!plugin.enderdamage) {
                    event.setCancelled(true);
                }
            }
        } catch (NullPointerException e) {

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityPickup(EntityChangeBlockEvent event) {
        for (int i = 0; i < plugin.worldnames.size(); i++) {
            String worldname = plugin.worldnames.get(i);
            if (event.getEntity().getWorld().getName().equals(worldname))
                break;
            if (i == plugin.worldnames.size() - 1)
                return;
        }
        Entity exploder = event.getEntity();
        if (exploder instanceof Enderman) {
            if (!plugin.enderblockdamage) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent e) {
        if (plugin.changeadvanced) {
            for (int i = 0; i < plugin.worldnames.size(); i++) {
                String worldname = plugin.worldnames.get(i);
                if (e.getEntity().getWorld().getName().equals(worldname))
                    break;
                if (i == plugin.worldnames.size() - 1)
                    return;
            }
            Entity ent = e.getEntity();
            if (ent instanceof Arrow) {
                if (((Arrow) ent).getShooter() instanceof Skeleton) {
                    if (plugin.setoggle) {
                        if (plugin.spawnrand.nextDouble() * 100d <= plugin.sechance) {
                            if (plugin.skeletonradius <= 0 || plugin.getnearestplayerLEycount(ent, plugin.skeletonradius) <= 0) {
                                if (plugin.WGexplode(ent.getLocation(), (float) plugin.serad, plugin.sefire)) {
                                    ent.remove();
                                    return;
                                }
                            }
                        }
                    }
                    // TODO: THIS IS WHERE IT REALLY GOES
                    // blockbreaking(ent);

                }
                // player damage
                // blockbreaking(ent);
            }
        }
    }
}
