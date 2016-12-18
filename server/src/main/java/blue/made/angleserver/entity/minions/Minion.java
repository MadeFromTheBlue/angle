package blue.made.angleserver.entity.minions;

import blue.made.angleserver.entity.Entity;

/**
 * Created by Sumner Evans on 2016/12/16.
 */
public abstract class Minion extends Entity {
    protected int goldReward;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     */
    public Minion(long uuid) {
        super(uuid);
    }
}
