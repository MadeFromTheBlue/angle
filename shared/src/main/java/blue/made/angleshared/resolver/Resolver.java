package blue.made.angleshared.resolver;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
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

    /**
     * Adds a class that can be instantiated by {@link #creator(String, Class[])}.
     * This function will register the class to the ids listed by one or more {@link Provides}
     * annotations. If the class does not have a @Provides annotation, then
     * this does absolutely nothing.
     */
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

    /**
     * An unfiltered version of {@link #addPackage(String, Predicate)}.
     */
    public void addPackage(String packageName) {
        addPackage(packageName, c -> true);
    }

    /**
     * Search a package (and all subpackages) for classes with one or more {@link Provides} annotations,
     * registering them with {@link #add(Class)} if they pass the {@code filter}.
     *
     * @param packageName The name of the package to search (i.e. {@code blue.made.angleserver.entity})
     * @param only A filter
     */
    public void addPackage(String packageName, Predicate<Class<?>> only) {
        cp.getTopLevelClassesRecursive(packageName)
                .stream()
                .map(ClassPath.ClassInfo::load)
                .filter(only)
                .forEach(this::add);
    }

    /**
     * Provides a functional interface that can be invoked to produce a new instance of the class registered to
     * {@code id}. The interface wraps a constructor that takes arguments of type {@code params} as input.
     *
     * @param id An id (specified by a {@link Provides} annotation)
     * @param params A list of parameter types that the constructor should accept
     * @return An functional interface for creating a new instance or {@code null} if no constructor matching
     * {@code params} exists
     */
    public InvokeWrapper creator(String id, Class<?>... params) {
        Class<?> provider = providedBy.get(id);
        if (provider == null) return null;
        return InvokeWrapper.constructor(provider, params);
    }
}
