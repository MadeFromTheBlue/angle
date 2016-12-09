package blue.made.angleserver.entity;

import blue.made.angleserver.Player;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/16/2016.
 */
public abstract class EntitySpec<E extends Entity> {
    String registeredId;

    public String getRegisteredId() {
        return registeredId;
    }

    public void writeSpec(BCFWriter.Map bcf) throws IOException {
        bcf.writeName("spec");
        bcf.write(getRegisteredId());
    }

    /**
     * Called when a player sends a {@link blue.made.angleserver.action.actions.SpawnEntity} action.
     */
    public abstract E requestSpawn(Player p, BCFMap data);

    public long newUUID() {
        return Util.generateUUID();
    }
}
