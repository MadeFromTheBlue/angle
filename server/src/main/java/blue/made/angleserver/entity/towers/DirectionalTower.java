package blue.made.angleserver.entity.towers;

import blue.made.angleserver.Game;
import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.world.World;
import blue.made.angleshared.resolver.Provides;
import blue.made.angleshared.util.Point;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by Sumner Evans on 2016/12/16.
 * <p>
 * These towers fire at a given angle
 */
@Provides("directional_tower")
public class DirectionalTower extends Tower {
    // Configured
    public final float fireRate;
    public final float dtheta;
    public final int damage;
    public final float range;
    public final boolean isAreaOfEffect;

    private float angle;
    private int isFiring = 0;
    Instant lastFireTime;

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded in the future.
     *
     * @param uuid
     * @param config
     */
    public DirectionalTower(long uuid, BCFMap config) {
        super(uuid, config);

        // Randomize the angle by default, it can be overridden in loadActionData
        this.setAngle((float) (new Random().nextFloat() * 2 * Math.PI - Math.PI));

        // By default, set to not AOE
        BCFItem isAOE = config.get("is_area_of_effect");
        isAreaOfEffect = isAOE != null && isAOE.asBoolean();

        dtheta = config.get("dtheta").asNumeric().floatValue();
        fireRate = config.get("fire_rate").asNumeric().intValue();
        damage = config.get("damage").asNumeric().intValue();
        range = config.get("range").asNumeric().intValue();
    }

    @Override
    protected void loadActionData(BCFMap data) {
        super.loadActionData(data);

        BCFItem angle = data.get("angle");
        if (angle != null) {
            this.setAngle(angle.asNumeric().floatValue());
        }
    }

    @Override
    public void onPlace(World w) {
        int rad = (int) Math.ceil(range);
        buildTargets(w, q -> q.box(-rad, -rad, rad, rad));
    }

    @Override
    public Consumer<BoundQ> getDefaultBounds() {
        return q -> q.box(-1, -1, 1, 1);
    }

    // TODO: Figure out who should control tick (Tower or DirectionalTower) and call attack
    // accordingly
    @Override
    public boolean attack(Minion e) {
        return false;
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

        // Attack minions
        for (Minion m : this.getMinions(world)) {
            Point towerPt = this.getPoint();
            Point minionPoint = m.getPos();

            boolean angleInAngleRange = Util.angleInRange(this.angle, Point.angle(towerPt, minionPoint), dtheta);
            boolean angleInRange = Point.dist(towerPt, minionPoint) < this.range;

            if (angleInAngleRange && angleInRange) {
                boolean minionHit = m.attacked(this, this.damage);
                hit |= minionHit;
                if (minionHit) {
                    this.isFiring = 5;
                    if (!isAreaOfEffect) break;
                }
            }
        }

        this.lastFireTime = now;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
