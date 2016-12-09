import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Created by sumner on 11/29/16.
 */
public class TestBase {
    public static <T> boolean assertContains(T[] array, T item) {
        return assertContains(array, el -> el.equals(item));
    }

    public static <T> boolean assertContains(T[] array, Predicate<T> comparator) {
        return Arrays.stream(array).anyMatch(comparator);
    }
}
