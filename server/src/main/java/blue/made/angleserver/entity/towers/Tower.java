package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.world.World;
import blue.made.angleshared.util.Location;

/**
 * Created by Sumner Evans on 2016/12/15.
 */
public abstract class Tower extends Entity {
    public final int x;
    public final int y;
    protected Player owner;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     * @param location
     */
    public Tower(long uuid, Location location) {
        super(uuid);
        this.x = location.x;
        this.y = location.y;
    }

    @Override
    protected void onSpawn() {

    }

    @Override
    protected boolean checkSpawn() {
        return false;
    }

    @Override
    public void tick(World world) {

    }
}
