package blue.made.angleshared.asset;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sam Sartor on 12/22/2016.
 */
public abstract class CachingPermanentAssetSource<A> implements AssetSource<A> {
    private boolean ready = false;
    private final List<Handle<A>> waiting = new ArrayList<>();
    private final LoadingCache<CombinedName, A> cache;

    public CachingPermanentAssetSource() {
        cache = CacheBuilder
        .newBuilder()
        .build(new CacheLoader<CombinedName, A>() {
            @Override
            public A load(CombinedName key) throws Exception {
                return CachingPermanentAssetSource.this.load(key.group, key.id);
            }
        });
    }

    protected abstract A load(String group, String id) throws Exception;

    private void loadInto(Handle<A> h, String group, String id) {
        try {
            h.value = cache.get(new CombinedName(group, id));
        } catch (ExecutionException e) {
            new Exception("Could not load asset \"" + group + ":" + id + "\"", e).printStackTrace();
        }
    }

    @Override
    public synchronized Handle<A> get(String group, String id) {
        Handle<A> out = new Handle<>(id, group, this);
        if (isReady()) {
            loadInto(out, group, id);
        } else {
            waiting.add(out);
        }
        return out;
    }

    public boolean isReady() {
        return ready;
    }

    public synchronized void setReady() {
        if (ready) throw new IllegalStateException("The source is already ready");
        ready = true;
        for (Handle<A> h : waiting) {
            loadInto(h, h.group, h.id);
        }
        waiting.clear();
    }
}
