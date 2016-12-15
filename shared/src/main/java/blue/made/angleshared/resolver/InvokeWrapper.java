package blue.made.angleshared.resolver;

import com.google.common.reflect.Invokable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Sam Sartor on 12/15/2016.
 */
@FunctionalInterface
public interface InvokeWrapper {
    /**
     * @throws InvocationException If the wrapped method/constructor/whatever throws an exception
     */
    public Object invoke(Object... params);

    public static InvokeWrapper constructor(Class<?> clas, Class<?>... params) {
        try {
            Constructor<?> constructor = clas.getConstructor(params);
            constructor.setAccessible(true);
            return from(constructor);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static InvokeWrapper staticMethod(Class<?> clas, String name, Class<?>... params) {
        try {
            Method method = clas.getMethod(name, params);
            if (!Modifier.isStatic(method.getModifiers())) return null;
            method.setAccessible(true);
            return from(null, method);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static InvokeWrapper from(Constructor<?> constructor) {
        return p -> {
            try {
                return constructor.newInstance(p);
            } catch (InvocationTargetException e) {
                throw new InvocationException(e.getCause());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }

    public static InvokeWrapper from(Object on, Method method) {
        return p -> {
            try {
                return method.invoke(on, p);
            } catch (InvocationTargetException e) {
                throw new InvocationException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static InvokeWrapper from(Object on, Invokable method) {
        return p -> {
            try {
                return method.invoke(on, p);
            } catch (InvocationTargetException e) {
                throw new InvocationException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static InvokeWrapper from(Invokable method) {
        return p -> {
            try {
                return method.invoke(null, p);
            } catch (InvocationTargetException e) {
                throw new InvocationException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }
}
