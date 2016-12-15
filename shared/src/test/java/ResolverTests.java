import _resolvertest.*;
import _resolvertest.subpack.NestedTest;
import blue.made.angleshared.resolver.InvocationException;
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
        res.addPackage("_resolvertest", c -> !c.isAnnotationPresent(Deprecated.class));
    }

    @Test
    public void testPackageFilter() {
        assertNull(res.creator("depr"));
    }


    @Test
    public void testNone() {
        assertNull(res.creator("none"));
    }

    @Test
    public void testNested() {
        assertTrue(res.creator("nested").invoke() instanceof NestedTest);
    }

    @Test
    public void testOne() {
        assertTrue(res.creator("foo").invoke() instanceof ProvidesOne);
    }

    @Test
    public void testTwo() {
        assertTrue(res.creator("bar1").invoke() instanceof ProvidesTwo);
        assertTrue(res.creator("bar2").invoke() instanceof ProvidesTwo);
    }
    @Test
    public void testParams() {
        assertEquals(((ProvidesOne) res.creator("foo").invoke()).i, -1);
        assertEquals(((ProvidesOne) res.creator("foo", int.class).invoke(10)).i, 10);
    }

    @Test
    public void testBadParams() {
        assertNull(res.creator("foo", Object.class));
    }
}
