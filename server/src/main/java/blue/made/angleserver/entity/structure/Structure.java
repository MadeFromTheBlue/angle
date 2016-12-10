package blue.made.angleserver.entity.structure;

import blue.made.angleserver.Game;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.util.bounds.GridBoundQ;
import blue.made.angleserver.util.bounds.ToBCFBoundQ;
import blue.made.angleserver.world.World;
import blue.made.angleshared.util.Location;
import blue.made.bcf.BCFWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 5/16/2016.
 */
public abstract class Structure extends Entity {
    public int centerx;
    public int centery;
    public byte rotation = 0;

    public Structure(long uuid) {
        super(uuid);
    }

    public Consumer<BoundQ> getBounds() {
        return q -> {
            q.translate(centerx, centery);
            q.rotate(rotation);
            ((StructureSpec) getSpec()).getDefaultBounds().accept(q);
        };
    }

    @Override
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        super.writeInitialData(map);
        map.writeName("centerx");
        map.write(this.centerx);
        map.writeName("centery");
        map.write(this.centery);
        map.writeName("rotation");
        map.write(this.rotation);
        map.writeName("bounds");
        ToBCFBoundQ q = new ToBCFBoundQ(map);
        this.getBounds().accept(q);
        q.finish();
    }

    /**
     * Called by {@link World#spawnInWorld(Entity)}.
     * <p>
     * Responsible for adding the structure to its corresponding chunks.
     */
    @Override
    public void postSpawn() {
        Set<Location> chunks = new HashSet<>();
        this.getBounds().accept(new GridBoundQ(World.CHUNK_WIDTH, l -> {
            chunks.add(l);
            return false;
        }));
        chunks.forEach(l -> Game.INSTANCE.world.getChunk(l.x, l.y).structures.add(this));
    }
}
