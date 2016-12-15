package blue.made.angleclient.entity;

import blue.made.angleclient.Game;
import blue.made.bcf.BCFMap;

import java.awt.*;
import java.util.Random;

/**
 * Created by Sam Sartor on 6/21/2016.
 */
public abstract class Entity {
    public final long uuid;
    public Color color;

    public Entity(long uuid) {
        Random rand = new Random(uuid);
        this.uuid = uuid;
        this.color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }

    /**
     * Update with data from the server
     */
    public void dataRec(BCFMap map) {
        Game.INSTANCE.world.onChanged();
    }
}
