package blue.made.angleserver.entity.towers.req;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFMap;

/**
 * Created by Sam Sartor on 6/9/2016.
 */
public interface BuildReq {
    // TODO send to client

    boolean check(Tower t, World w, Player p, Client c, int x, int y, int r, BCFMap other);
}
