package blue.made.angleserver.entity.structure.req;

import blue.made.angleserver.Player;
import blue.made.angleshared.util.Location;
import blue.made.bcf.BCFMap;
import blue.made.angleserver.entity.structure.Structure;
import blue.made.angleserver.entity.structure.StructureSpec;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.util.bounds.GridBoundQ;
import blue.made.angleserver.util.bounds.IntersectBoundQ;
import blue.made.angleserver.util.bounds.StoreBoundQ;
import blue.made.angleserver.world.Chunk;
import blue.made.angleserver.world.World;

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
            for (Structure s : c.structures) {
                iq.reset();
                s.getBounds().accept(iq);
                if (iq.intersects() && (predicate == null || predicate.test(s))) {
                    out = false;
                    return true;
                }
            }
            return false;
        }
    }

    public Predicate<Structure> predicate;

    public BuildReqOverlap(Predicate<Structure> predicate) {
        this.predicate = predicate;
    }

    public BuildReqOverlap() {
        this(null);
    }

    @Override
    public boolean check(StructureSpec spec, World w, Player p, Client c, int x, int y, int r,
                         BCFMap other) {
        StoreBoundQ store = new StoreBoundQ();
        store.translate(x, y);
        store.rotate(r);
        spec.getDefaultBounds().accept(store);
        Q q = new Q(w, new IntersectBoundQ(store.getStored()));
        GridBoundQ qer = new GridBoundQ(World.CHUNK_WIDTH, q);
        qer.translate(x, y);
        qer.rotate(r);
        spec.getDefaultBounds().accept(qer);
        return q.out;
    }

}
