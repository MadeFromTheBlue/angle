package blue.made.angleserver.world;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.world.tags.TagRegistry;
import blue.made.angleserver.world.tags.Tags;
import blue.made.angleshared.util.Location;
import blue.made.angleshared.util.Point;
import gnu.trove.map.hash.TLongObjectHashMap;

/**
 * Created by Sam Sartor on 5/9/2016.
 */
public class World {

    public static final int CHUNK_WIDTH_BITS = 4;
    public static final int CHUNK_WIDTH = 1 << CHUNK_WIDTH_BITS;

    public TLongObjectHashMap<Entity> entities = new TLongObjectHashMap<>();

    public int xwidth = -1;
    public int ywidth = -1;

    // Tile data
    public float[] heights;
    public short[][] tileTags;

    public Chunk[] chunks;
    public int xchunks;
    public int ychunks;

    public TagRegistry tagRegistry;

    public World(TagRegistry tagRegistry) {
        this.tagRegistry = tagRegistry;
    }

    public void tick() { // TODO change tick system
        entities.forEachValue(e -> {
            e.tick(this);
            return true;
        });
    }

    public void addToWorld(Entity e) {
        this.entities.put(e.uuid, e);
    }

    private int roundUp(int n, int m) {
        int r = n % m;
        if (r != 0) n += m - r;
        return n;
    }

    public void buildInitial(int width, int height) {
        this.xwidth = roundUp(width, CHUNK_WIDTH);
        this.ywidth = roundUp(height, CHUNK_WIDTH);
        xchunks = ((xwidth - 1) >> CHUNK_WIDTH_BITS) + 1;
        ychunks = ((ywidth - 1) >> CHUNK_WIDTH_BITS) + 1;

        int len = xwidth * ywidth;
        heights = new float[len];
        tileTags = new short[len][0];
        chunks = new Chunk[xchunks * ychunks];

        for (int i = 0; i < chunks.length; i++) chunks[i] = new Chunk();
        Tags.register(tagRegistry);
    }

    public boolean isInWorld(int x, int y) {
        return x >= 0 && y >= 0 && x < xwidth && y < ywidth;
    }

    public boolean isChunkInWorld(int x, int y) {
        return x >= 0 && y >= 0 && x < xchunks && y < ychunks;
    }

    public Tile getTile(int x, int y) {
        if (isInWorld(x, y)) return new Tile(this, x, y);
        return null;
    }

    public Tile getTile(Location location) {
        return getTile(location.x, location.y);
    }

    public Chunk getChunkAt(Point p) {
        return getChunkAt(p.intX(), p.intY());
    }

    public Chunk getChunkAt(Location l) {
        return getChunkAt(l.x, l.y);
    }

    public Chunk getChunkAt(int x, int y) {
        if (isInWorld(x, y)) {
            int ci = (x >> CHUNK_WIDTH_BITS) + xchunks * (y >> CHUNK_WIDTH_BITS);
            return chunks[ci];
        }
        return null;
    }

    public Chunk getChunk(int x, int y) {
        if (isChunkInWorld(x, y)) {
            int ci = x + xchunks * y;
            return chunks[ci];
        }
        return null;
    }
}
