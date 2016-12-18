package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;

/**
 * Created by Sumner Evans on 2016/12/15.
 * <p>
 * <em>Note, towers do not have levels, so they should be created and replaced rather than
 * upgraded.</em>
 * </p>
 */
public abstract class Tower extends Entity {
    protected int x;
    protected int y;
    protected Player owner;
    protected int price;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     */
    public Tower(long uuid) {
        super(uuid);
    }

    @Override
    protected void onSpawn() {
    }

    @Override
    protected boolean checkSpawn() {
        if (!owner.hasFunds(price)) return false;

        // TODO: Check that it can be placed
        return false;
    }
}
