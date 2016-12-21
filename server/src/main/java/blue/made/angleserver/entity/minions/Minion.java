package blue.made.angleserver.entity.minions;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleshared.util.Point;
import blue.made.bcf.BCFMap;

import java.security.InvalidParameterException;

/**
 * Created by Sumner Evans on 2016/12/16.
 */
public abstract class Minion extends Entity {
    public final int goldReward;
    public final Point point;

    protected int health;
    protected boolean dead = false;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     * @param config
     */
    public Minion(long uuid, Player player, BCFMap config) {
        super(uuid, player, config);

        if (config == null)
            throw new InvalidParameterException("Cannot have null configuration");

        goldReward = config.get("gold_reward").asNumeric().intValue();
        health = config.get("health").asNumeric().intValue();
        point = new Point(config.get("point").asMap());
    }

    abstract boolean canBeAttackedBy(Tower tower);

    public boolean attacked(Tower tower, int damage) {
        if (!canBeAttackedBy(tower)) return false;
        decreaseHealth(damage);
        return true;
    }

    private void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) this.dead = true;
    }

    public Point getPoint() {
        return this.point;
    }
}
