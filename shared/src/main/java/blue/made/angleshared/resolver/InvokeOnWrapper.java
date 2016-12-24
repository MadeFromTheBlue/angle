package blue.made.angleshared.resolver;

import com.google.common.reflect.Invokable;
import jdk.nashorn.internal.runtime.regexp.joni.Warnings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sam Sartor on 12/15/2016.
 */
@FunctionalInterface
public interface InvokeOnWrapper {
    /**
     * @throws InvocationException If the wrapped method/whatever throws an exception
     */
    public Object invokeOn(Object on, Object... params);

    public static InvokeOnWrapper method(Class<?> clas, String name, Class<?>... params) {
        try {
            Method method = clas.getMethod(name, params);
            method.setAccessible(true);
            return from(method);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static InvokeOnWrapper from(Method method) {
        return (o, p) -> {
            try {
                return method.invoke(o, p);
            } catch (InvocationTargetException e) {
                throw new InvocationException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static InvokeOnWrapper from(Invokable method) {
        return (o, p) -> {
            try {
                return method.invoke(o, p);
            } catch (InvocationTargetException e) {
                throw new InvocationException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }
}
