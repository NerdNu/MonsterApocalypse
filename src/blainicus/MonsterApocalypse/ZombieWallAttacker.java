package blainicus.MonsterApocalypse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class ZombieWallAttacker implements Runnable {
    List<BlockPair> blocklist;
    List<RealBlockPair> resetmap;
    public Map<LivingEntity, Integer> timemap, timebelowmap;
    public Map<LivingEntity, Location> locationmap;
    public Map<Block, Integer> destructionmap;
    MonsterApocalypse plugin;
    String blockstring;
    Material mat;
    int seconds, secondindex;
    public static int maxtime = 12000;

    public ZombieWallAttacker(MonsterApocalypse instance) {
        plugin = instance;
        blocklist = new ArrayList<BlockPair>();
        timemap = new HashMap<LivingEntity, Integer>();
        timebelowmap = new HashMap<LivingEntity, Integer>();
        locationmap = new HashMap<LivingEntity, Location>();
        // if(plugin.newzombiemethod){
        destructionmap = new HashMap<Block, Integer>();
        resetmap = new ArrayList<RealBlockPair>();
        // }
    }

    @Override
    public void run() {
        cleanup();
    }

    public void cleanup() {
        Object[] keys = timemap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            try {
                if (((LivingEntity) keys[i]).isDead()) {
                    timemap.remove(keys[i]);
                }
            } catch (Exception e) {
                timemap.remove(keys[i]);
            }
        }
        keys = null;
        keys = timebelowmap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            try {
                if (((LivingEntity) keys[i]).isDead()) {
                    timebelowmap.remove(keys[i]);
                }
            } catch (Exception e) {
                timebelowmap.remove(keys[i]);
            }
        }
        keys = null;
        keys = locationmap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            try {
                if (((LivingEntity) keys[i]).isDead()) {
                    locationmap.remove(keys[i]);
                }
            } catch (Exception e) {
                locationmap.remove(keys[i]);
            }
        }
        keys = null;
    }

    public int getmobstill(LivingEntity ent) {
        try {
            return timemap.get(ent);
        } catch (NullPointerException e) {
            addmob(ent);
            return timemap.get(ent);
        }
    }

    public int getmobbelow(LivingEntity ent) {
        try {
            return timebelowmap.get(ent);
        } catch (NullPointerException e) {
            addmob(ent);
            return timebelowmap.get(ent);
        }
    }

    public void addmob(LivingEntity ent) {
        timemap.put(ent, 0);
        if (timebelowmap.get(ent) == null) {
            timebelowmap.put(ent, 0);
        }
        setmobloc(ent);
    }

    public void resetmobstill(LivingEntity ent) {
        timemap.remove(ent);
        locationmap.remove(ent);
    }

    public void resetmobbelow(LivingEntity ent) {
        timebelowmap.remove(ent);
    }

    public void increasestilltime(LivingEntity ent) {
        if (timemap.get(ent) == null) {
            timemap.put(ent, 4);
        } else {
            int time2 = timemap.get(ent);
            timemap.put(ent, time2 + 4);
        }
        if (timebelowmap.get(ent) == null) {
            timebelowmap.put(ent, 1);
        } else {
            int time2 = timebelowmap.get(ent);
            timebelowmap.put(ent, time2 + 1);
        }
    }

    public Location getmobloc(LivingEntity ent) {
        return locationmap.get(ent);
    }

    public void setmobloc(LivingEntity ent) {
        Location roundedloc = new Location(ent.getWorld(), 0, 0, 0);
        Location entloc = ent.getLocation();
        roundedloc.setX(Math.round(entloc.getBlockX() + 0));
        roundedloc.setY(Math.round(entloc.getBlockY() + 0));
        roundedloc.setZ(Math.round(entloc.getBlockZ() + 0));
        locationmap.put(ent, roundedloc);
    }

    public boolean sameloc(LivingEntity ent) {
        Location roundedloc = new Location(ent.getWorld(), 0, 0, 0);
        Location entloc = ent.getLocation();
        roundedloc.setX(entloc.getBlockX());
        roundedloc.setY(entloc.getBlockY());
        roundedloc.setZ(entloc.getBlockZ());
        if (roundedloc.getBlockX() == locationmap.get(ent).getBlockX()
            &&
            roundedloc.getBlockY() == locationmap.get(ent).getBlockY()
            &&
            roundedloc.getBlockZ() == locationmap.get(ent).getBlockZ()) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public void stringadd(String s) {
        try {
            if (s.startsWith("block"))
                return;
            secondindex = s.indexOf(":") + 1;
            try {
                mat = Material.getMaterial(Integer.parseInt(s.substring(0, secondindex - 1)));
            } catch (NumberFormatException e) {
                mat = Material.getMaterial(s.substring(0, secondindex - 1).toUpperCase());
            }
            seconds = Integer.parseInt(s.substring(secondindex));
            addblock(mat, seconds);
        } catch (Exception e) {
            System.out.println("Monster Apocalypse: Invalid time setting " + blockstring + ". Please use a valid CraftBukkit enum.");
            return;
        }
    }

    public void addblock(Material m, int s) {
        // will bug please test
        blocklist.add(new BlockPair(m, s));
    }

    public int getblockseconds(Material b) {
        for (int n = 0; n < blocklist.size(); n++) {
            if (blocklist.get(n).getblock() == b) {
                return blocklist.get(n).getseconds();
            }
        }
        return -1;
    }

    public void checkblocks() {
        for (int n = 0; n < blocklist.size(); n++) {
            try {
                (blocklist.get(n)).getseconds();
            } catch (NullPointerException e) {
                System.out.println("Monster Apocalypse: Invalid time setting " + blockstring + ". Please use a valid CraftBukkit enum.");
                return;
            }

        }
    }

    public int getblockstate(Block b, int time) {
        Material oldmat = b.getType();
        if (oldmat == Material.AIR || oldmat == Material.LAVA || oldmat == Material.WATER)
            return 0;
        int test = getblockseconds(oldmat);
        if (test < 0)
            return 0;
        int timeoffset = 0;
        try {
            timeoffset += destructionmap.get(b);
        } catch (Exception e) {
        }
        destructionmap.put(b, time + timeoffset);
        update(b, 0);
        if (time + timeoffset >= test) {
            destructionmap.remove(b);
            removeblock(b);
            return 2;
        }
        return 1;

    }

    private void update(Block b, int time) {
        removeblock(b);
        resetmap.add(new RealBlockPair(b, time));
    }

    private void removeblock(Block b) {
        for (int i = 0; i < resetmap.size(); i++) {
            if (resetmap.get(i).getblock().equals(b)) {
                resetmap.remove(i);
                return;
            }
        }
    }

    public void resettimes() {
        // int maxtime=plugin.breakreset;
        for (int i = 0; i < resetmap.size(); i++) {
            resetmap.get(i).addseconds(4);
            if (resetmap.get(i).getseconds() > maxtime) {
                destructionmap.remove(resetmap.get(i).getblock());
                resetmap.remove(i);
            }
        }

    }

    public class BlockPair {
        Material block;
        int seconds;

        public BlockPair(Material m, int s) {
            block = m;
            seconds = s;
        }

        public int getseconds() {
            return seconds;
        }

        public Material getblock() {
            return block;
        }
    }

    public class RealBlockPair {
        Block block;
        int seconds;

        public RealBlockPair(Block m, int s) {
            block = m;
            seconds = s;
        }

        public int getseconds() {
            return seconds;
        }

        public Block getblock() {
            return block;
        }

        public void addseconds(int amount) {
            seconds += amount;
        }
    }
}
