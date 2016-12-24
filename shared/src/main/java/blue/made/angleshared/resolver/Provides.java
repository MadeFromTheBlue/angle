package blue.made.angleshared.resolver;

import java.lang.annotation.*;

/**
 * Created by Sumner Evans on 2016/12/14.
 */
@Repeatable(ProvidesMany.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Provides {
    public String value();
}
