package blue.made.angleshared.asset;

/**
 * Created by Sam Sartor on 12/23/2016.
 */
public abstract class UncachingAssetSource<A> implements AssetSource<A> {
    protected abstract A load(String group, String id) throws Exception;

    @Override
    public synchronized Handle<A> get(String group, String id) {
        Handle<A> out = new Handle<>(id, group, this);
        try {
            out.value = load(group, id);
        } catch (Exception e) {
            new Exception("Could not load asset \"" + group + ":" + id + "\"", e).printStackTrace();
        }
        return out;
    }
}
