package blue.made.angleserver.entity;

import blue.made.angleserver.world.World;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public abstract class Entity {
    public final long uuid;
    private boolean spawned = false;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     */
    public Entity(long uuid) {
        this.uuid = uuid;
    }

    /**
     * Called by {@link #spawn()}
     */
    protected abstract void onSpawn();

    /**
     * Called by {@link #spawn()}
     */
    protected abstract boolean checkSpawn();

    public abstract void tick(World world);

    public final boolean spawn() {
        if (!checkSpawn()) return false;
        onSpawn();
        spawned = true;
        return true;
    }

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
