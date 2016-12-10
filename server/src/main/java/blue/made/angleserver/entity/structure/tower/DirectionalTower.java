package blue.made.angleserver.entity.structure.tower;

import blue.made.angleserver.Player;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.EntitySpec;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFMap;

import java.util.function.Consumer;

/**
 * Created by Mobius on 12/10/16.
 */
public abstract class DirectionalTower extends Tower{
    public class DirectionalTowerEntity extends Tower.TowerEntity {
        public DirectionalTowerEntity(long uuid) {
            super(uuid);
        }

        public void setRange(float distance, World world) {
            int rad = (int) (distance + 0.999);
            buildTargets(world, q -> q.box(-rad, -rad, rad, rad));
        }

        public void setAngle(float angle, World world) {
            
        }

        @Override
        public boolean attack(Entity e) {
            // TODO check if in radius
            return ((AOETower) getSpec()).attack(e, this);
        }

        @Override
        public EntitySpec<Entity> getSpec() {
            return DirectionalTower.this;
        }
    }

    @Override
    public Consumer<BoundQ> getDefaultBounds() {
        return q -> q.box(-1, -1, 1, 1);
    }

    @Override
    public Entity requestSpawn(Player p, BCFMap data) {
        return new DirectionalTower.DirectionalTowerEntity(newUUID());
    }

    /**
     * Do the attack.
     */
    protected abstract boolean attack(Entity target, DirectionalTower.DirectionalTowerEntity attacker);
}
