package blue.made.angleserver.action.actions;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleserver.entity.Entity;
import blue.made.angleshared.exceptions.AngleException;
import blue.made.angleshared.resolver.Provides;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import com.google.common.base.Strings;

import java.security.InvalidParameterException;
import java.util.HashMap;


/**
 * Created by Sumner Evans on 2016/12/21.
 */
@Provides("upgrade_entity")
public class UpgradeEntity extends Action {
    @Override
    public void run(Player player, BCFMap data) {
        BCFItem uuidItem = data.get("uuid");
        if (uuidItem == null)
            throw new InvalidParameterException("UpgradeEntity requires a uuid");

        BCFItem upgradeToItem = data.get("upgrade_to");
        if (upgradeToItem == null || Strings.isNullOrEmpty(upgradeToItem.asString()))
            throw new AngleException("InvalidConfigurationException", new HashMap<String, String>() {{
                put("message", "UpgradeEntity requires upgrade_to to be specified");
            }});

        long uuid = uuidItem.asNumeric().longValue();
        // TODO: Check if upgrade can happen, etc.
        if (Entity.spawnEntity(upgradeToItem.asString(), data, player))
            Game.INSTANCE.world.removeFromWorld(uuid);
    }
}
