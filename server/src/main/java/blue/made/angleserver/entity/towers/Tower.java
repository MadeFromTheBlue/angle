package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.Tags;
import blue.made.angleshared.util.Location;
import blue.made.angleshared.util.Point;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFList;
import blue.made.bcf.BCFMap;
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
    public final int x;
    public final int y;
    public final int price;
    public final String[] upgradesTo;

    protected Player owner;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded
     * in the future.
     *
     * @param uuid
     * @param config
     */
    public Tower(long uuid, BCFMap config) {
        super(uuid, config);

        if (config == null)
            throw new InvalidParameterException("Cannot have null configuration");

        this.price = config.get("price").asNumeric().intValue();
        this.x = config.get("x").asNumeric().intValue();
        this.y = config.get("y").asNumeric().intValue();

        BCFItem upgradesToConfig = config.get("upgrades_to");
        if (upgradesToConfig == null) {
            upgradesTo = new String[0];
        } else {
            BCFList arr = upgradesToConfig.asList();
            upgradesTo = new String[arr.size()];

            for (int i = 0; i < arr.size(); i++) {
                BCFItem item = arr.get(i);
                upgradesTo[i] = item.asString();
            }
        }
    }

    @Override
    public boolean canPlace(World w) {
        return w.getTile(x, y).isTagged(Tags.ground);
    }

    @Override
    public boolean canBuild(World w, Player p) {
        return p.hasFunds(price);
    }

    @Override
    public void onPlace(World w) {}

    @Override
    public void onBuild(World w, Player p) {
        owner = p;
    }

    protected HashSet<Minion> getMinions(World world) {
        HashSet<Minion> minions = new HashSet<>();
        for (TLongObjectIterator it = world.entities.iterator(); it.hasNext(); ) {
            it.advance();
            if (it.value() instanceof Minion) minions.add((Minion) it.value());
        }

        return minions;
    }

    protected Location getLocation() {
        return new Location(x, y);
    }

    protected Point getPoint() {
        return new Point(x, y);
    }
}
