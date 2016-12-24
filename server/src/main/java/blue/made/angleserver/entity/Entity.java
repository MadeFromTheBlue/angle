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
                    return Game.entityResolver.creator(key, long.class, BCFMap.class);
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

    private static Entity constructEntity(String id, BCFMap config) {
        // Splice the config onto the data
        config.addAll(configCache.getUnchecked(id));

        // Determine provider
        BCFItem providedByItem = config.get("provided_by");
        if (providedByItem == null)
            throw new InvalidConfigurationException("Configuration must provide a provided_by");
        String providedBy = providedByItem.asString();

        // Spawn the actual entity with the configuration JSON
        InvokeWrapper creator = creatorCache.getUnchecked(providedBy);
        return (Entity) creator.invoke(Util.generateUUID(), config);
    }

    public static boolean spawnEntity(String id, BCFMap config) {
        return constructEntity(id, config).spawn(Game.INSTANCE.world);
    }

    public static boolean spawnEntity(String id, BCFMap config, Player builder) {
        return constructEntity(id, config).spawn(Game.INSTANCE.world, builder);
    }

    /**
     * Takes a UUID instead of generating a new one so that games towers can be saved and reloaded
     * in the future.
     */
    public Entity(long uuid, BCFMap config) {
        this.uuid = uuid;
    }

    public final boolean spawn(World w) {
        if (!canPlace(w)) return false;
        w.addToWorld(this);
        onPlace(w);
        return true;
    }

    public final boolean spawn(World w, Player p) {
        if (!canBuild(w, p)) return false;
        if (!spawn(w)) return false;
        onBuild(w, p);
        return true;
    }

    public abstract boolean canPlace(World world);

    public abstract boolean canBuild(World world, Player p);

    protected abstract void onPlace(World world);

    protected abstract void onBuild(World world, Player p);

    public abstract void tick(World world);

    /**
     * Writes the data needed by the client to first display this entity.
     */
    public void writeInitialData(BCFWriter.Map map) throws IOException {
        map.put("id", uuid);
    }

    public boolean isSpawned() {
        return spawned;
    }
}
