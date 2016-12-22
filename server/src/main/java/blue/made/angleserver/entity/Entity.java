package blue.made.angleserver.entity;

import blue.made.angleserver.Player;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public abstract class Entity {
    public final long uuid;
    private boolean spawned = false;

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
        if (!canPlace(w)) return false;
        if (!canCreate(w, p)) return false;
        w.addToWorld(this);
        onPlace(w);
        onCreate(w, p);
        return true;
    }

    public abstract boolean canPlace(World world);
    public abstract boolean canCreate(World world, Player p);

    protected abstract void onPlace(World world);
    protected abstract void onCreate(World world, Player p);

    public abstract void tick(World world);

    /**
     * Writes the data needed by the client to first display this entity.
     */
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        map.writeName("id");
        map.write(uuid);
    }

    public boolean isSpawned() {
        return spawned;
    }
}
