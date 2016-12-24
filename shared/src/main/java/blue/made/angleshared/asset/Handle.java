package blue.made.angleshared.asset;

/**
 * Created by Sam Sartor on 12/22/2016.
 */
public class Handle<A> {
    A value = null;
    public final String id;
    public final String group;
    public final AssetSource source;

    Handle(String id, String group, AssetSource source) {
        this.id = id;
        this.group = group;
        this.source = source;
    }

    public A pull() {
        return value;
    }

    public boolean isReady() {
        return value != null;
    }
}
