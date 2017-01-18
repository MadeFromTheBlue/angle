package blue.made.angleserver.entity.towers.req;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Sam Sartor on 6/10/2016.
 */
public class BuildReqAnd implements BuildReq {
    public Collection<BuildReq> all;

    public BuildReqAnd() {
        this.all = Collections.EMPTY_LIST;
    }

    public BuildReqAnd(BuildReq... all) {
        this.all = Arrays.asList(all);
    }

    @Override
    public boolean check(Tower t, World w, Player n, Client c, int x, int y, int r, BCFMap other) {
        for (BuildReq req : all) {
            if (!req.check(t, w, n, c, x, y, r, other)) return false;
        }
        return true;
    }
}
