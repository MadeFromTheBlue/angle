package blue.made.angleserver.entity;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.world.World;
import blue.made.angleshared.exceptions.InvalidConfigurationException;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public abstract class Entity {
    public final long uuid;
    private boolean spawned = false;

    private static LoadingCache<String, InvokeWrapper> creatorCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, InvokeWrapper>() {
                @Override
                public InvokeWrapper load(String key) throws Exception {
                    return Game.entityResolver.creator(key, long.class, Player.class, BCFMap.class);
                }
            });

    private static LoadingCache<String, BCFMap> configCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, BCFMap>() {
                @Override
                public BCFMap load(String id) throws Exception {
                    return (BCFMap) BCF.fromJson(Util.findConfigJson(id));
                }
            });

    public static boolean spawnEntity(String id, Player player, BCFMap config) {
        // Splice the config onto the data
        config.addAll(configCache.getUnchecked(id));

        // Determine provider
        BCFItem providedByItem = config.get("provided_by");
        if (providedByItem == null)
            throw new InvalidConfigurationException("Configuration must provide a provided_by");
        String providedBy = providedByItem.asString();

        // Spawn the actual entity with the configuration JSON
        InvokeWrapper creator = creatorCache.getUnchecked(providedBy);
        Entity entity = (Entity) creator.invoke(Util.generateUUID(), player, config);
        return Game.INSTANCE.world.addToWorld(entity);
    }

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded
     * in the future.
     */
    public Entity(long uuid, Player player, BCFMap config) {
        this.uuid = uuid;
    }

    /**
     * Called by {@link #spawn()}
     */
    protected abstract void onSpawn();

    /**
     * Called by {@link #spawn()}
     */
    protected abstract boolean checkSpawn();

    public abstract void tick(World world);

    public final boolean spawn() {
        if (!checkSpawn()) return false;
        onSpawn();
        spawned = true;
        return true;
    }

    /**
     * Writes the data needed by the client to first display this entity.
     */
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        map.writeName("id");
        map.write(uuid);
    }

    public boolean isSpawned() {
        return spawned;
    }
}
