import blue.made.angleshared.util.Util;
import com.google.gson.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sumner Evans on 2016/12/20.
 */
public class UtilTests {
    @Test
    public void testBoundAngle() {
        assertEquals(0f, Util.boundAngle(Util.floatPI * 4), Util.FLOAT_TOLERANCE);
        assertEquals(0f, Util.boundAngle(0f), Util.FLOAT_TOLERANCE);
        assertEquals(-Util.floatPI, Util.boundAngle(Util.floatPI * 5), Util.FLOAT_TOLERANCE);
        assertEquals(Util.floatPI / 2, Util.boundAngle(Util.floatPI * 5 / 2), Util.FLOAT_TOLERANCE);
        assertEquals(1f, 1f, Util.FLOAT_TOLERANCE);
    }

    @Test
    public void testAngleInRange() {
        // Test angles that are in range
        assertTrue(Util.angleInRange(0, Util.floatPI, Util.floatPI));
        assertTrue(Util.angleInRange(Util.floatPI / 20, 0, Util.floatPI / 16));
        assertTrue(Util.angleInRange(0, Util.floatPI / 20, Util.floatPI / 16));

        // Test angles that are not in range
        assertFalse(Util.angleInRange(0, Util.floatPI, Util.floatPI / 2));
        assertFalse(Util.angleInRange(0, Util.floatPI / 16, Util.floatPI / 20));
        assertFalse(Util.angleInRange(Util.floatPI / 16, 0, Util.floatPI / 20));
    }

    @Test
    public void testGetConfigJson() {
        JsonObject config = Util.findConfigJson("config");
        JsonObject nestedConfig = Util.findConfigJson("nested.nested_config");

        assertEquals("bar", config.get("foo").getAsString());
        assertEquals("nested", nestedConfig.get("type").getAsString());
    }
}
