package blue.made.angleserver.entity.minions;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleserver.network.packet.out.O41EntityData;
import blue.made.angleserver.world.Chunk;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.Tags;
import blue.made.angleshared.util.Point;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFMap;

import java.security.InvalidParameterException;

/**
 * Created by Sumner Evans on 2016/12/16.
 */
public abstract class Minion extends Entity {
    public static final float SYNC_MOTION_DIST = 0.2f;

    public final int goldReward;
    private Point pos;
    private Point lastPos;
    private Point lastSync;

    protected int health;
    protected boolean dead = false;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     * @param config
     */
    public Minion(long uuid, BCFMap config) {
        super(uuid, config);

        if (config == null)
            throw new InvalidParameterException("Cannot have null configuration");

        goldReward = config.get("gold_reward").asNumeric().intValue();
        health = config.get("health").asNumeric().intValue();
        pos = new Point(config.get("pos").asMap());
        lastPos = pos;
        lastSync = pos; // TODO: move to first data send
    }

    abstract boolean canBeAttackedBy(Tower tower);

    public boolean attacked(Tower tower, int damage) {
        if (!canBeAttackedBy(tower)) return false;
        decreaseHealth(damage);
        return true;
    }

    private void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) dead = true;
    }

    @Override
    public void tick(World world) {
        Chunk currentChunk = world.getChunkAt(pos);
        Chunk lastChunk = world.getChunkAt(lastPos);
        if (currentChunk != lastChunk) {
            // we have moved chunks, change list
            lastChunk.minions.remove(this);
            currentChunk.minions.add(this);
        }
        if (lastSync.equals(pos, SYNC_MOTION_DIST)) {
            // we have moved enough to send a new update to the clients
            // TODO: send paths and states instead of actual position data
            O41EntityData data = new O41EntityData(this);
            data.add("pos_x", BCF.store(pos.x));
            data.add("pos_y", BCF.store(pos.y));
            Game.INSTANCE.queueToClients(data);
        }
        lastPos = pos;
    }

    public Point getPos() {
        return this.pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    @Override
    public boolean canPlace(World w) {
        return w.getTile(pos.intX(), pos.intY()).isTagged(Tags.path);
    }

    @Override
    public boolean canCreate(World w, Player p) {
        return false;
    }

    @Override
    public void onPlace(World w) {
        w.getChunkAt(pos.intX(), pos.intY()).minions.add(this);
    }

    @Override
    public void onCreate(World w, Player p) {}
}
