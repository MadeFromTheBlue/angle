package blue.made.angleserver.entity.structure.req;

import blue.made.angleserver.Player;
import blue.made.bcf.BCFMap;
import blue.made.angleserver.entity.structure.StructureSpec;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.world.World;

/**
 * Created by Sam Sartor on 6/9/2016.
 */
public interface BuildReq {
    // TODO send to client

    boolean check(StructureSpec spec, World w, Player p, Client c, int x, int y, int r, BCFMap other);
}
