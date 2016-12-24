package blue.made.angleserver.action.actions;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.util.Util;
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

        // TODO: Make this actually work
        InvokeWrapper creator = Game.resolver.creator(id, long.class);
        creator.invoke(Util.generateUUID());

        // TODO: Spawn an actual entity (pull from config)
    }
}
