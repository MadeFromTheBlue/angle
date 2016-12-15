package blue.made.angleshared.resolver;

/**
 * Created by Sam Sartor on 12/15/2016.
 */
@FunctionalInterface
public interface InvokeWrapper {
    public Object invoke(Object... params);
}
