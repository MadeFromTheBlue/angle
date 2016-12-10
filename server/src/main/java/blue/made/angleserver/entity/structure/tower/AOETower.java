package blue.made.angleserver.entity.structure.tower;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.EntitySpec;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFMap;

import java.util.function.Consumer;

/**
 * Created by sam on 12/8/16.
 */
public abstract class AOETower extends Tower {
    public class AOETowerEntity extends Tower.TowerEntity {
        public AOETowerEntity(long uuid) {
            super(uuid);
        }

        public void setRange(float distance, World world) {
            int rad = (int) (distance + 0.999);
            buildTargets(world, q -> q.box(-rad, -rad, rad, rad));
        }

        @Override
        public boolean attack(Entity e) {
            // TODO check if in radius
            return ((AOETower) getSpec()).attack(e, this);
        }

        @Override
        public EntitySpec<Entity> getSpec() {
            return AOETower.this;
        }
    }

    @Override
    public Consumer<BoundQ> getDefaultBounds() {
        return q -> q.box(-1, -1, 1, 1);
    }

    @Override
    public Entity requestSpawn(Player p, BCFMap data) {
        return new AOETowerEntity(newUUID());
    }

    /**
     * Do the attack.
     */
    protected abstract boolean attack(Entity target, AOETowerEntity attacker);

}
