package blue.made.angleserver.entity.towers.req;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.util.bounds.IterBoundQ;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.TileTag;
import blue.made.angleshared.util.Location;
import blue.made.bcf.BCFMap;

import java.util.function.Function;

/**
 * Created by Sam Sartor on 6/10/2016.
 */
public class BuildReqTilesAre implements BuildReq {
    private class Q implements Function<Location, Boolean> {
        public boolean out = true;
        public final World world;

        private Q(World world) {
            this.world = world;
        }

        @Override
        public Boolean apply(Location location) {
            if (!out || world.getTile(location).isTagged(tag)) return !out;
            out = false;
            return true;
        }
    }

    public TileTag tag;

    public BuildReqTilesAre(TileTag tag) {
        this.tag = tag;
    }

    @Override
    public boolean check(Tower t, World w, Player p, Client c, int x, int y, int r, BCFMap other) {
        Q q = new Q(w);
        BoundQ qer = new IterBoundQ(q);
        qer.translate(x, y);
        qer.rotate(r);
        t.getDefaultBounds().accept(qer);
        return q.out;
    }

}
