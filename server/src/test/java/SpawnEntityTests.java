import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleserver.action.actions.SpawnEntity;
import blue.made.angleserver.entity.towers.DirectionalTower;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sumner Evans on 2016/12/20.
 */
public class SpawnEntityTests {
    @Before
    public void before() {
        Game._reset();
        Game.INSTANCE.start();
    }

    @Test
    public void testAirTowerSpawn() {
        // Set up some fake data like what will be sent from the client
        BCFMap data = new BCFMap();
        data.put("type", "towers.air");
        data.put("x", 5);
        data.put("y", 1);

        Player player = new Player("test player");

        Action spawnAction = new SpawnEntity();
        spawnAction.run(player, data);
        long entityUuid = Game.INSTANCE.world.entities.keys()[0]; // There's only one entity
        DirectionalTower tower = (DirectionalTower) Game.INSTANCE.world.entities.get(entityUuid);

        assertEquals(0.02777777777777777, tower.dtheta, Util.FLOAT_TOLERANCE);
        assertEquals(5, tower.x);
        assertEquals(1, tower.y);
        assertEquals(10, tower.damage);
        assertEquals(30, tower.price);
        assertEquals(2f, tower.fireRate);
        assertEquals(5, tower.range);
        assertTrue(Arrays.deepEquals(new String[]{"towers.air2"}, tower.upgradesTo));
    }
}
