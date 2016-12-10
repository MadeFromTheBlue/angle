package blue.made.angleserver.entity;

import blue.made.angleserver.entity.structure.tower.AOETower;
import blue.made.angleserver.entity.structure.tower.DirectionalTower;

/**
 * Created by Sam Sartor on 6/22/2016.
 */
public class Entities {
    public static final AOETower flashTower = new AOETower() {
        @Override
        protected boolean attack(Entity target, DirectionalTower.DirectionalTowerEntity attacker) {
            // TODO something
            return true;
        }
    };

    public static void init() {
        EntityRegistry.register("flash_tower", flashTower);
    }


}
