package blue.made.angleshared.asset;

/**
 * Created by Sam Sartor on 12/23/2016.
 *
 */
public interface AssetSource<A> {
    public Handle<A> get(String group, String id);

    public static <A> CachingPermanentAssetSource<A> cachingPermanent(LoadFunc<A, CachingPermanentAssetSource<A>> load) {
        return new CachingPermanentAssetSource<A>() {
            @Override
            protected A load(String group, String id) throws Exception {
                return load.load(this, group, id);
            }
        };
    }
}
