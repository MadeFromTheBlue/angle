package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleserver.world.World;
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
     * @param player
     * @param config
     */
    public Tower(long uuid, Player player, BCFMap config) {
        super(uuid, config);

        if (config == null)
            throw new InvalidParameterException("Cannot have null configuration");

        this.price = config.get("price").asNumeric().intValue();
        this.x = config.get("x").asNumeric().intValue();
        this.y = config.get("y").asNumeric().intValue();
        this.owner = player;

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
    public boolean canPlace(World world) {
        // TODO: Check that it can be placed

        return false;
    }

    @Override
    public boolean canBuild(World w, Player p) {
        if (!owner.hasFunds(price)) return false;

        // TODO: Do other build checks?
        return true;
    }

    @Override
    protected void onBuild(World w, Player p) {
        p.spendGold(price);
    }

    @Override
    protected void onPlace(World world) {
        world.addToWorld(this);
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
        // TODO: Make this the center
        return new Point(x, y);
    }
}
