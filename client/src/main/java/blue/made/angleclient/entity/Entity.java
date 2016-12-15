package blue.made.angleclient.entity;

import blue.made.angleclient.Game;
import blue.made.bcf.BCFMap;

/**
 * Created by Sam Sartor on 6/21/2016.
 */
public abstract class Entity {
    public final long uuid;

    public Entity(long uuid) {
        this.uuid = uuid;
    }

    /**
     * Update with data from the server
     */
    public void dataRec(BCFMap map) {
        Game.INSTANCE.world.onChanged();
    }
}
