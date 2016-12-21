package blue.made.angleserver.action.actions;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleserver.config.JSONConfig;
import blue.made.angleserver.entity.Entity;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFMap;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by sumner on 12/8/16.
 */
public class SpawnEntity extends Action {
    // Gosh Sumner, cache that stuff
    private static LoadingCache<String, InvokeWrapper> creatorCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, InvokeWrapper>() {
                @Override
                public InvokeWrapper load(String key) throws Exception {
                    return Game.resolver.creator(key, long.class, JsonObject.class);
                }
            });

    // And this stuff too, come on
    private static LoadingCache<String, JsonObject> configCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, JsonObject>() {
                @Override
                public JsonObject load(String id) throws Exception {
                    return findConfigJson(id);
                }
            });

    @Override
    public void run(Player player, BCFMap data) {
        String id = data.get("type").asString();

        // TODO: better error handling
        if (id == null) return;

        // Spawn the actual entity with the configuration JSON
        InvokeWrapper creator = creatorCache.getUnchecked(id);
        Entity e = (Entity) creator.invoke(Util.generateUUID(), configCache.getUnchecked(id));
        Game.INSTANCE.world.addToWorld(e);
    }

    private static JsonObject findConfigJson(String id) {
        // Find the Configuration JSON
        InputStream inputStream;
        try {
            inputStream = Util.newFileStream(String.format("configs/%s.json", id));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            // There is no config for this object, just return null
            return null;
        }

        // Read and parse the JSON
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);

        return new JsonParser().parse(reader).getAsJsonObject();
    }
}
