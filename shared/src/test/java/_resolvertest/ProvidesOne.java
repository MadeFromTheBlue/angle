package _resolvertest;

import blue.made.angleshared.resolver.Provides;

import java.util.List;

/**
 * Created by Sumner Evans on 2016/12/14.
 */
@Provides("foo")
public class ProvidesOne {
    public final int i;

    public ProvidesOne() {
        this(-1);
    }

    public ProvidesOne(int i) {
        this.i = i;
    }
}
