package blue.made.angleshared.asset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam Sartor on 12/23/2016.
 */
public abstract class UncachedAssetSource<A> implements WaitingAssetSource<A> {
    private boolean ready = false;
    private final List<Handle<A>> waiting = new ArrayList<>();

    protected abstract A load(String group, String id) throws Exception;

    private void loadInto(Handle<A> h, String group, String id) {
        try {
            h.value = load(group, id);
        } catch (Exception e) {
            new Exception("Could not load asset \"" + group + ":" + id + "\"", e).printStackTrace();
        }
    }

    @Override
    public synchronized Handle<A> get(String group, String id) {
        Handle<A> out = new Handle<>(id, group, this, performSync);
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
