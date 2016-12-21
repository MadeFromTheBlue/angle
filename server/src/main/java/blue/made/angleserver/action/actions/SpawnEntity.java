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

/**
 * Created by sumner on 12/8/16.
 */
public class SpawnEntity extends Action {
    @Override
    public void run(Player player, BCFMap data) {
        String id = data.get("type").asString();

        // TODO: better error handling
        if (id == null) return;

        // Splice the data onto the config
        BCFMap config = (BCFMap) BCF.fromJson(Util.findConfigJson(id));
        config.addAll(data);

        // Determine provider
        BCFItem providedByItem = config.get("provided_by");
        if (providedByItem == null)
            throw new InvalidConfigurationException("Configuration must provide a provided_by");
        String providedBy = providedByItem.asString();

        // Spawn the actual entity with the configuration JSON
        InvokeWrapper creator = Game.resolver.creator(providedBy, long.class, Player.class, BCFMap.class);
        Entity entity = (Entity) creator.invoke(Util.generateUUID(), player, config);
        Game.INSTANCE.world.addToWorld(entity);
    }
}
