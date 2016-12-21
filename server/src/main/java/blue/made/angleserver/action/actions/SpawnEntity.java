package blue.made.angleserver.action.actions;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleserver.entity.Entity;
import blue.made.angleshared.exceptions.InvalidConfigurationException;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.security.InvalidParameterException;

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
                    return Game.resolver.creator(key, long.class, BCFMap.class);
                }
            });

    // And this stuff too, come on
    private static LoadingCache<String, BCFMap> configCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, BCFMap>() {
                @Override
                public BCFMap load(String id) throws Exception {
                    return (BCFMap) BCF.fromJson(Util.findConfigJson(id));
                }
            });

    @Override
    public void run(Player player, BCFMap data) {
        BCFItem type = data.get("type");
        if (type == null)
            throw new InvalidParameterException("SpawnEntity requires a type string to be specified");

        String id = type.asString();
        if (Strings.isNullOrEmpty(id))
            throw new InvalidParameterException("SpawnEntity requires a type string to be specified");

        // Splice the data onto the config
        BCFMap config = configCache.getUnchecked(id);
        config.addAll(data);

        // Determine provider
        BCFItem providedByItem = config.get("provided_by");
        if (providedByItem == null)
            throw new InvalidConfigurationException("Configuration must provide a provided_by");
        String providedBy = providedByItem.asString();

        // Spawn the actual entity with the configuration JSON
        InvokeWrapper creator = creatorCache.getUnchecked(providedBy);
        Entity entity = (Entity) creator.invoke(Util.generateUUID(), player, config);
        Game.INSTANCE.world.addToWorld(entity);
    }
}
