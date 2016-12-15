package blue.made.angleshared.resolver;

import com.google.common.reflect.ClassPath;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * Created by Sumner Evans on 2016/12/14.
 */
public class Resolver {
    private Map<String, Class> providedBy = new HashMap<>();
    private static ClassPath cp;

    static {
        try {
            cp = ClassPath.from(Resolver.class.getClassLoader());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void add(Class<?> c) {

        for (Annotation a : c.getAnnotations()) {
            if (a instanceof ProvidesMany) {
                // Find all types that this class provides and puts them into the providedBy map
                ProvidesMany provides = (ProvidesMany) a;
                Stream.of(provides.value())
                        .map(Provides::value)
                        .forEach(s -> providedBy.put(s, c));
                break;
            } else if (a instanceof Provides) {
                // This is the case when there is only one annotation, in which case we need to
                providedBy.put(((Provides) a).value(), c);
                break;
            }
        }
    }

    public void addPackage(String packageName) {
        cp.getTopLevelClassesRecursive(packageName).forEach(c -> add(c.load()));
    }

    public InvokeWrapper creator(String type, Class<?>... params) throws NoSuchMethodException {
        Class<?> provider = providedBy.get(type);
        Constructor<?> c = provider.getConstructor(params);
        return o -> {
            try {
                return c.newInstance(o);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    /**
     * This is a quick and dirty method for creating instances. It is deprecated because:
     * <ul>
     *     <li>It is slow to do this every time</li>
     *     <li>IT WILL NOT WORK ON CONSTRUCTORS THAT ACCEPT PRIMITIVES (int, float, boolean, etc.)</li>
     * </ul>
     * Use {@link #creator(String, Class[])} instead.
     */
    @Deprecated
    public Object create(String type, Object... params) throws NoSuchMethodException {
        Class<?> provider = providedBy.get(type);
        if (provider == null) return null;

        Class<?>[] paramClasses = Stream.of(params).map(Object::getClass).toArray(Class[]::new);

        // TODO: Determine if we want prettier error messages for this
        try {
            return provider.getConstructor(paramClasses).newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
