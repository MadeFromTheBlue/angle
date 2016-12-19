package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Game;
import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleserver.world.World;
import blue.made.angleshared.resolver.Provides;
import blue.made.angleshared.util.Point;
import blue.made.angleshared.util.Util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Created by Sumner Evans on 2016/12/16.
 * <p>
 * These towers fire at a given angle
 */
@Provides("directional_tower")
public class DirectionalTower extends Tower {
    // Configured
    private float angle;
    private float dtheata;
    private float fireRate;
    private int damage;
    private int range;

    private int isFiring = 0;
    Instant lastFireTime;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     */
    public DirectionalTower(long uuid) {
        super(uuid);
    }

    @Override
    public void tick(World world) {
        this.isFiring--;

        Instant now = Game.INSTANCE.getNow();

        if (this.lastFireTime == null) {
            this.lastFireTime = now;
            return;
        }

        // Don't fire unless enough time has elapsed since the last fire time
        float timeUntilFire = this.lastFireTime.until(now, ChronoUnit.MILLIS) / 1000f;
        if (timeUntilFire < 1 / this.fireRate) return;

        boolean hit = false;

        // TODO: Make this work
        for (Minion m : this.getMinions()) {
            Point towerPt = this.getPoint();
            Point minionPoint = m.getPoint();

            boolean angleInAngleRange = Util.angleInRange(this.angle, Point.angle(towerPt, minionPoint), dtheata);
            boolean angleInRange = Point.dist(towerPt, minionPoint) < this.range;

            if (angleInAngleRange && angleInRange) {
                boolean minionHit = m.attacked(this, this.damage);
                hit |= minionHit;
                if (minionHit) {
                    this.isFiring = 5;
                    //if (!isAreaOfEffect()) break;
                }
            }
        }

        this.lastFireTime = now;
    }
}
