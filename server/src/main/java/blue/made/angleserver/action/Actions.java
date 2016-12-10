package blue.made.angleserver.action;

import blue.made.angleserver.action.actions.SpawnEntity;

/**
 * Created by Sam Sartor on 6/22/2016.
 */
public class Actions {
    public static void init() {
        ActionRegistry.register("spawn_entity", new SpawnEntity());
    }
}
