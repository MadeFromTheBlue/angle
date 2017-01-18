package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.util.bounds.GridBoundQ;
import blue.made.angleserver.util.bounds.ToBCFBoundQ;
import blue.made.angleserver.world.Chunk;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.Tags;
import blue.made.angleshared.util.Location;
import blue.made.angleshared.util.Point;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFList;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;
import gnu.trove.iterator.TLongObjectIterator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
    public final byte rotation = 0;
    public final int price;
    public final String[] upgradesTo;

    public Chunk[] toAttack = new Chunk[0];

    protected Player owner;

    // Constructors and Initialisers
    // ============================================================================================

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

    /**
     * TODO: UPDATE COMMENT
     * Call this function to specify the bounds of the tower's attack range. This is just an
     * optimization, further checks will be needed in {@link #attack(Entity)}.
     * <br>
     * MAKE SURE TO CALL THIS AT LEAST ONCE, PROBABLY IN {@link #onPlace(World)}
     */
    public void buildTargets(World world, Consumer<BoundQ> range) {
        HashSet<Chunk> locs = new HashSet<>();
        BoundQ q = new GridBoundQ(World.CHUNK_WIDTH, l -> {
            locs.add(world.getChunk(l.x, l.y));
            return false;
        });
        toAttack = new Chunk[locs.size()];
        locs.toArray(toAttack);
    }

    // Abstract Methods
    // ============================================================================================
    public abstract Consumer<BoundQ> getDefaultBounds();

    /**
     * Checks if an entity is is range and attacks it
     *
     * @return true if the entity can be (and was) attacked
     */
    public abstract boolean attack(Minion e);

    // Entity Overrides
    // ============================================================================================
    @Override
    public boolean canBuild(World w, Player p) {
        return p.hasFunds(price);
    }

    @Override
    public boolean canPlace(World w) {
        // TODO: Tower must not overlap other structures and must be on ground
        // new BuildReqAnd(new BuildReqOverlap(), new BuildReqTilesAre(Tags.ground));

        return w.getTile(x, y).isTagged(Tags.ground);
    }

    @Override
    protected void onPlace(World world) {
        Set<Location> chunks = new HashSet<>();
        this.getBounds().accept(new GridBoundQ(World.CHUNK_WIDTH, l -> {
            chunks.add(l);
            return false;
        }));
        chunks.forEach(l -> Game.INSTANCE.world.getChunk(l.x, l.y).towers.add(this));

        world.addToWorld(this);
    }

    @Override
    public void onBuild(World w, Player p) {
        owner = p;
        p.spendGold(price);
    }

    @Override
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        super.writeInitialData(map);
        map.put("x", x);
        map.put("y", y);
        // TODO: probably send information about where to fire projectiles from
        map.put("rotation", rotation);

        // TODO: Make this better
        map.writeName("bounds");
        ToBCFBoundQ q = new ToBCFBoundQ(map);
        this.getBounds().accept(q);
        q.finish();
    }

    @Override
    public void tick(World world) {
        for (Chunk c : toAttack) {
            for (Minion m : c.minions) {
                attack(m);
            }
        }
    }

    // Helper Methods
    // ============================================================================================
    public Consumer<BoundQ> getBounds() {
        return q -> {
            q.translate(x, y);
            q.rotate(rotation); // TODO: Actually load the rotation out of the BCFMap config
            getDefaultBounds().accept(q);
        };
    }

    protected HashSet<Minion> getMinions(World world) {
        HashSet<Minion> minions = new HashSet<>();
        for (TLongObjectIterator it = world.entities.iterator(); it.hasNext(); ) {
            it.advance();
            if (it.value() instanceof Minion) minions.add((Minion) it.value());
        }

        return minions;
    }

    // Getters and Setters
    // ============================================================================================
    protected Location getLocation() {
        return new Location(x, y);
    }

    protected Point getPoint() {
        return new Point(x, y);
    }
}
