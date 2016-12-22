package blue.made.angleserver.action.actions;

import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleserver.entity.Entity;
import blue.made.angleshared.resolver.Provides;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import com.google.common.base.Strings;

import java.security.InvalidParameterException;

/**
 * Created by sumner on 12/8/16.
 */
@Provides("spawn_entity")
public class SpawnEntity extends Action {
    @Override
    public void run(Player player, BCFMap data) {
        BCFItem type = data.get("type");
        if (type == null)
            throw new InvalidParameterException("SpawnEntity requires a type string to be specified");

        String id = type.asString();
        if (Strings.isNullOrEmpty(id))
            throw new InvalidParameterException("SpawnEntity requires a type string to be specified");

        Entity.spawnEntity(id, player, data);
    }
}
