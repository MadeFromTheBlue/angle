import blue.made.angleshared.util.Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    public void testValueFromJsonOrDefault() {
        String json = "{\"exists\":\"custom\"}";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        assertEquals("default", Util.valueFromJsonOrDefault(jsonObject, "dne", "default"));
        assertEquals("custom", Util.valueFromJsonOrDefault(jsonObject, "exists", "default"));
        assertEquals(1, (int) Util.valueFromJsonOrDefault(jsonObject, "dne", 1));
        assertEquals(true, Util.valueFromJsonOrDefault(jsonObject, "dne", true));
        assertNull(Util.valueFromJsonOrDefault(jsonObject, "dne", null));
    }
}
