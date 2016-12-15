package blue.made.angleshared.resolver;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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
            } else if (a instanceof Provides) {
                // This is the case when there is only one annotation, in which case we need to
                providedBy.put(((Provides) a).value(), c);
            }
        }
    }

    public void addPackage(String packageName) {
        cp.getTopLevelClassesRecursive(packageName).forEach(c -> add(c.load()));
    }

    public Object create(String type, Object... params) {
        Class<?> provider = providedBy.get(type);
        if (provider == null) return null;

        // TODO: Determine if we want prettier error messages for this
        // TODO: Handle multiple constructors
        // TODO: Probably use Guava?
        // Note: This just calls the first constructor, that's no bueno
        try {
            return provider.getConstructors()[0].newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
