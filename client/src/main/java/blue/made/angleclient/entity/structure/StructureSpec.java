package blue.made.angleclient.entity.structure;

import blue.made.bcf.BCFMap;
import blue.made.angleclient.Game;
import blue.made.angleclient.entity.EntitySpec;

/**
 * Created by Sam Sartor on 6/8/2016.
 */
public class StructureSpec extends EntitySpec {
	public Bounds bounds;

	public Structure spawnFromInitial(BCFMap map) {
		Structure s = new Structure(map.get("id").asNumeric().longValue());
		s.loadInitialData(map);
		Game.INSTANCE.world.structures.put(s.uuid, s);
		Game.INSTANCE.world.onChanged();
		return s;
	}

	@Override
	public void readSpec(BCFMap map) {
		super.readSpec(map);
		bounds = Bounds.load(map.get("bounds"));
	}
}
