package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleshared.util.Location;
import blue.made.angleshared.util.Point;
import com.google.gson.JsonObject;
import gnu.trove.iterator.TLongObjectIterator;

import java.security.InvalidParameterException;
import java.util.HashSet;

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
    public Tower(long uuid, JsonObject configJson) {
        super(uuid);

        if (configJson == null)
            throw new InvalidParameterException("Cannot have null configuration");

        this.price = configJson.get("price").getAsInt();
        // TODO: how to set owner
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

    protected HashSet<Minion> getMinions() {
        HashSet<Minion> minions = new HashSet<>();
        for (TLongObjectIterator it = Game.INSTANCE.world.entities.iterator(); it.hasNext(); ) {
            it.advance();
            if (it.value() instanceof Minion) minions.add((Minion) it.value());
        }

        return minions;
    }

    protected Location getLocation() {
        return new Location(x, y);
    }

    protected Point getPoint() {
        // TODO: Make this the center
        return new Point(x, y);
    }
}
