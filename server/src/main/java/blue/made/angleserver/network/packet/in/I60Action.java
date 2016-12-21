package blue.made.angleserver.network.packet.in;

import blue.made.angleserver.Game;
import blue.made.angleserver.action.Action;
import blue.made.angleserver.network.Client;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFReader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/21/2016.
 */
public class I60Action extends IPacket {
    public static class Loader implements IPacket.BCFLoader {
        @Override
        public IPacket create(BCFReader data, Client from) throws IOException {
            return new I60Action(from, BCF.read(data).asMap());
        }
    }

    public BCFMap map;

    public I60Action(Client sender, BCFMap map) {
        super(sender);
        this.map = map;
    }

    private static LoadingCache<String, InvokeWrapper> creatorCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, InvokeWrapper>() {
                @Override
                public InvokeWrapper load(String key) throws Exception {
                    return Game.actionResolver.creator(key);
                }
            });


    @Override
    public void onProcessed() {
        InvokeWrapper creator = creatorCache.getUnchecked(map.get("action").asString());
        Action act = (Action) creator.invoke();
        act.run(sender.player, map);
    }
}
