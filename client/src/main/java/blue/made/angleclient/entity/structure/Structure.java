package blue.made.angleclient.entity.structure;

import blue.made.angleclient.entity.Entity;
import blue.made.bcf.BCFMap;

/**
 * Created by Sam Sartor on 6/8/2016.
 */
public class Structure extends Entity {
    public Bounds bounds;

    public Structure(long uuid) {
        super(uuid);
    }

    /**
     * Called by {@link StructureSpec#spawnFromInitial(BCFMap)}
     */
    public void loadInitialData(BCFMap map) {
        this.bounds = Bounds.load(map.get("bounds"));
    }
}
