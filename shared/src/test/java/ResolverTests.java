import _resolvertest.*;
import blue.made.angleshared.resolver.Resolver;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Sumner Evans on 2016/12/14.
 */
public class ResolverTests {
    Resolver res = new Resolver();

    @Before
    public void init() {
        res.addPackage("_resolvertest");
    }

    @Test
    public void testNone() throws NoSuchMethodException {
        assertNull(res.create("none"));
    }

    @Test
    public void testOne() throws NoSuchMethodException {
        assertTrue(res.create("foo") instanceof ProvidesOne);
    }

    @Test
    public void testTwo() throws NoSuchMethodException {
        assertTrue(res.create("bar1") instanceof ProvidesTwo);
        assertTrue(res.create("bar2") instanceof ProvidesTwo);
    }

    /*
    @Test
    public void testParams() throws NoSuchMethodException {
        assertEquals(((ProvidesOne) res.create("foo")).i, -1);
        assertEquals(((ProvidesOne) res.create("foo", 10)).i, 10);
    }
    */

    @Test
    public void testParamsDelayed() throws NoSuchMethodException {
        assertEquals(((ProvidesOne) res.creator("foo").invoke()).i, -1);
        assertEquals(((ProvidesOne) res.creator("foo", int.class).invoke(10)).i, 10);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testBadParams() throws NoSuchMethodException {
        res.create("foo", Object.class, int.class);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testBadParamsDelayed() throws NoSuchMethodException {
        res.creator("foo", Object.class, int.class).invoke(new Object(), 10);
    }
}
