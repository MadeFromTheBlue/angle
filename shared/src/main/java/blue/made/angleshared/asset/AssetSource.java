package blue.made.angleshared.asset;

/**
 * Created by Sam Sartor on 12/23/2016.
 *
 */
public interface AssetSource<A> {
    public Handle<A> get(String group, String id);

    public static <A> CachedPermanentAssetSource<A> cachedPermanent(LoadFunc<A, CachedPermanentAssetSource<A>> load) {
        return new CachedPermanentAssetSource<A>() {
            @Override
            protected A load(String group, String id) throws Exception {
                return load.load(this, group, id);
            }
        };
    }

    public static <A> UncachedAssetSource<A> uncached(LoadFunc<A, UncachedAssetSource<A>> load) {
        return new UncachedAssetSource<A>() {
            @Override
            protected A load(String group, String id) throws Exception {
                return load.load(this, group, id);
            }
        };
    }
}
