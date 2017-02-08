package blue.made.angleserver.entity;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.world.World;
import blue.made.angleshared.ConfigMerge;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public abstract class Entity {
    public final long uuid;
    private boolean spawned = false;

    private static Entity constructEntity(String id, BCFMap config) {
        // Spawn the actual entity with the configuration JSON
        ConfigMerge.TowerType towerType = Game.configMerger.towerTypes.get(id);
        return (Entity) towerType.create(Util.generateUUID());
    }

    public static boolean spawnEntity(String id, BCFMap config) {
        return constructEntity(id, config).spawn(Game.INSTANCE.world);
    }

    public static boolean spawnEntity(String id, BCFMap config, Player builder) {
        return constructEntity(id, config).spawn(Game.INSTANCE.world, builder);
    }

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded
     * in the future.
     */
    public Entity(long uuid, BCFMap config) {
        this.uuid = uuid;
    }

    public final boolean spawn(World w) {
        if (!canPlace(w)) return false;
        w.addToWorld(this);
        onPlace(w);
        return true;
    }

    public final boolean spawn(World w, Player p) {
        if (!canBuild(w, p)) return false;
        if (!spawn(w)) return false;
        onBuild(w, p);
        return true;
    }

    public abstract boolean canPlace(World world);

    public abstract boolean canBuild(World world, Player p);

    protected abstract void onPlace(World world);

    protected abstract void onBuild(World world, Player p);

    public abstract void tick(World world);

    /**
     * Writes the data needed by the client to first display this entity.
     */
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        map.put("id", uuid);
    }

    public boolean isSpawned() {
        return spawned;
    }
}
