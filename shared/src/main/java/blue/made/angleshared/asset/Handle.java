package blue.made.angleshared.asset;

import java.util.function.Supplier;

/**
 * Created by Sam Sartor on 12/22/2016.
 */
public class Handle<A> {
    A value = null;
    public final String id;
    public final String group;
    public final AssetSource source;
    public final Supplier<Exception> performSync;

    Handle(String id, String group, AssetSource source, Supplier<Exception> performSync) {
        this.id = id;
        this.group = group;
        this.source = source;
        this.performSync = performSync;
    }

    public A pull() {
        return value;
    }

    public void sync() throws Exception {
        Exception ex = performSync.get();
        if (ex != null) throw ex;
    }

    public boolean isReady() {
        return value != null;
    }
}
