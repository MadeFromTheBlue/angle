package blue.made.angleshared.resolver;

/**
 *  Thrown when the method wrapped by {@link InvokeWrapper} or {@link InvokeOnWrapper} throws an exception.
 */
public class InvocationException extends RuntimeException {
    protected InvocationException(Throwable cause) {
        super(cause);
    }
}
