package blue.made.angleserver.entity.towers.req;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.util.bounds.GridBoundQ;
import blue.made.angleserver.util.bounds.IntersectBoundQ;
import blue.made.angleserver.util.bounds.StoreBoundQ;
import blue.made.angleserver.world.Chunk;
import blue.made.angleserver.world.World;
import blue.made.angleshared.util.Location;
import blue.made.bcf.BCFMap;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Sam Sartor on 6/10/2016.
 */
public class BuildReqOverlap implements BuildReq {
    private class Q implements Function<Location, Boolean> {
        public boolean out = true;
        public final World world;
        public IntersectBoundQ iq;

        private Q(World world, IntersectBoundQ iq) {
            this.world = world;
            this.iq = iq;
        }

        @Override
        public Boolean apply(Location cl) {
            if (!out) return true;
            Chunk c = world.getChunk(cl.x, cl.y);
            for (Tower t : c.towers) {
                iq.reset();
                t.getBounds().accept(iq);
                if (iq.intersects() && (predicate == null || predicate.test(t))) {
                    out = false;
                    return true;
                }
            }
            return false;
        }
    }

    public Predicate<Tower> predicate;

    public BuildReqOverlap(Predicate<Tower> predicate) {
        this.predicate = predicate;
    }

    public BuildReqOverlap() {
        this(null);
    }

    @Override
    public boolean check(Tower t, World w, Player p, Client c, int x, int y, int r, BCFMap other) {
        StoreBoundQ store = new StoreBoundQ();
        store.translate(x, y);
        store.rotate(r);
        t.getDefaultBounds().accept(store);
        Q q = new Q(w, new IntersectBoundQ(store.getStored()));
        GridBoundQ qer = new GridBoundQ(World.CHUNK_WIDTH, q);
        qer.translate(x, y);
        qer.rotate(r);
        t.getDefaultBounds().accept(qer);
        return q.out;
    }

}
