package blue.made.angleserver.entity;

import blue.made.angleserver.world.World;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public abstract class Entity {
    public final long uuid;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     */
    public Entity(long uuid) {
        this.uuid = uuid;
    }

    /**
     * Called by {@link World#addToWorld(Entity)}.
     */
    public abstract void postSpawn();

    /**
     * @return the {@link EntitySpec} responsible for this entity's creation & properties.
     */
    public abstract EntitySpec<Entity> getSpec();

    /**
     * Writes the data needed by the client to first display this entity.
     */
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        map.writeName("id");
        map.write(uuid);
    }

    public abstract void tick(World world);
}
