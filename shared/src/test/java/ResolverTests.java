import _resolvertest.ProvidesOne;
import _resolvertest.ProvidesTwo;
import _resolvertest.subpack.NestedTest;
import blue.made.angleshared.resolver.Resolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sumner Evans on 2016/12/14.
 */
public class ResolverTests extends TestBase {
    Resolver res = new Resolver();

    @Before
    public void init() {
        res.addPackage("_resolvertest", c -> {
            return !c.getPackage().getName().equals("_resolvertest.duped") &&
                    !c.isAnnotationPresent(Deprecated.class);
        });
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

    // Ensures that it blows up when you have two classes trying to provide the same functionality
    @Test(expected=IllegalArgumentException.class)
    public void testNoDuplicate() {
        res.addPackage("_resolvertest.duped");
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
