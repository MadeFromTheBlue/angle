package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.world.World;
import blue.made.angleshared.util.Location;

/**
 * Created by Sumner Evans on 2016/12/15.
 * <p>
 * <em>Note, towers do not have levels, so they should be created and replaced rather than
 * upgraded.</em>
 * </p>
 */
public abstract class Tower extends Entity {
    public final int x;
    public final int y;
    protected Player owner;
    protected final int price;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     * @param location
     * @param price
     */
    public Tower(long uuid, Player owner, Location location, int price) {
        super(uuid);
        this.x = location.x;
        this.y = location.y;
        this.owner = owner;
        this.price = price;
    }

    @Override
    protected void onSpawn() {
    }

    @Override
    protected boolean checkSpawn() {
        // TODO: Check that it can be placed
        return false;
    }

}
