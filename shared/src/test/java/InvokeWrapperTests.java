import blue.made.angleshared.resolver.InvocationException;
import blue.made.angleshared.resolver.InvokeOnWrapper;
import blue.made.angleshared.resolver.InvokeWrapper;
import com.google.common.reflect.Invokable;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sam Sartor on 12/15/2016.
 */
public class InvokeWrapperTests {
    public static class TestMethods {
        public TestMethods() {
        }

        public TestMethods(Exception e) throws Exception {
            throw e;
        }

        public void methodEx(Exception e) throws Exception {
            throw e;
        }

        public static void staticEx(Exception e) throws Exception {
            throw e;
        }

        public int mul2(int i) {
            return i * 2;
        }

        public static int mul2Static(int i) {
            return i * 2;
        }
    }

    @Test
    public void testConstructorException() {
        Exception ex = new Exception();
        try {
            InvokeWrapper
                    .constructor(TestMethods.class, Exception.class)
                    .invoke(ex);
            assertTrue("Not thrown", false);
        } catch (InvocationException ie) {
            assertSame(ex, ie.getCause());
        }
    }

    @Test
    public void testStaticMethodException() {
        Exception ex = new Exception();
        try {
            InvokeWrapper
                    .staticMethod(TestMethods.class, "staticEx", Exception.class)
                    .invoke(ex);
            assertTrue("Not thrown", false);
        } catch (InvocationException ie) {
            assertSame(ex, ie.getCause());
        }
    }

    @Test
    public void testMethodException() {
        Exception ex = new Exception();
        try {
            InvokeOnWrapper
                    .method(TestMethods.class, "staticEx", Exception.class)
                    .invokeOn(new TestMethods(), ex);
            assertTrue("Not thrown", false);
        } catch (InvocationException ie) {
            assertSame(ex, ie.getCause());
        }
    }

    @Test
    public void testMethod() {
        assertEquals(16, (int) InvokeOnWrapper
                .method(TestMethods.class, "mul2", int.class)
                .invokeOn(new TestMethods(), 8));
    }

    @Test
    public void testStaticMethod() {
        assertEquals(16, (int) InvokeWrapper
                .staticMethod(TestMethods.class, "mul2Static", int.class)
                .invoke(8));
    }

    @Test
    public void testConstructor() {
        assertTrue(InvokeWrapper
                .constructor(TestMethods.class)
                .invoke() instanceof TestMethods);
    }

    @Test
    public void testGuavaMethod() throws NoSuchMethodException {
        assertEquals(16, (int) InvokeOnWrapper
                .from(Invokable.from(
                        TestMethods.class.getMethod("mul2", int.class)
                )).invokeOn(new TestMethods(), 8));
    }

    @Test
    public void testGuavaStaticMethod() throws NoSuchMethodException {
        assertEquals(16, (int) InvokeWrapper
                .from(Invokable.from(
                        TestMethods.class.getMethod("mul2Static", int.class)
                )).invoke(8));
    }

    @Test
    public void testGuavaConstructor() throws NoSuchMethodException {
        assertTrue(InvokeWrapper
                .from(Invokable.from(
                        TestMethods.class.getConstructor()
                )).invoke() instanceof TestMethods);
    }
}
