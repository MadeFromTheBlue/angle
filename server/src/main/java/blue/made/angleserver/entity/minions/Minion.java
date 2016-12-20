package blue.made.angleserver.entity.minions;

import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.towers.Tower;
import blue.made.angleshared.util.Point;
import com.google.gson.JsonObject;

import java.security.InvalidParameterException;

/**
 * Created by Sumner Evans on 2016/12/16.
 */
public abstract class Minion extends Entity {
    protected int goldReward;
    protected Point point;
    protected int health;
    protected boolean dead = false;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     * @param configJson
     */
    public Minion(long uuid, JsonObject configJson) {
        super(uuid);

        if (configJson == null)
            throw new InvalidParameterException("Cannot have null configuration");

        this.goldReward = configJson.get("gold_reward").getAsInt();
        this.health = configJson.get("health").getAsInt();
        // TODO: figure out how to set the point
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
