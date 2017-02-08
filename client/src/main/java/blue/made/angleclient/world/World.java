package blue.made.angleclient.world;

import blue.made.angleshared.util.Location;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class World {
    public final int xwidth;
    public final int ywidth;
    public final int chunksx;
    public final int chunksy;
    public final int chunkSize;

    public final Chunk[][] chunks;

    public HashSet<Location> towerLocations = new HashSet<>();

    public Tags tags;

    public Consumer<World> onChange = w -> {};

    public World(int xwidth, int ywidth, int chunkSize) {
        this.xwidth = xwidth;
        this.ywidth = ywidth;
        chunksx = ((xwidth - 1) / chunkSize) + 1;
        chunksy = ((ywidth - 1) / chunkSize) + 1;
        chunks = new Chunk[chunksx][chunksy];
        this.chunkSize = chunkSize;
    }

    public boolean isInWorld(int x, int y) {
        return x >= 0 && y >= 0 && x < xwidth && y < ywidth;
    }

    public float getHeight(int x, int y) {
        if (x < 0 || y < 0 || x >= xwidth || y >= ywidth) return 0;
        Chunk c = chunks[x / chunkSize][y / chunkSize];
        if (c == null) return 0;
        return c.height[x % chunkSize + (y % chunkSize) * chunkSize];
    }

    public void onChanged() {
        onChange.accept(this);
    }
}
