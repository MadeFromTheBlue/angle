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
    public void testNone() {
        assertNull(res.create("none"));
    }

    @Test
    public void testOne() {
        assertTrue(res.create("foo") instanceof ProvidesOne);
    }

    @Test
    public void testTwo() {
        assertTrue(res.create("bar1") instanceof ProvidesTwo);
        assertTrue(res.create("bar2") instanceof ProvidesTwo);
    }
}
