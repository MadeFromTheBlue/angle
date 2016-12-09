package blue.made.angleclient.entity;

import blue.made.bcf.BCFMap;

/**
 * Created by Sam Sartor on 6/17/2016.
 */
public abstract class EntitySpec {
	public String id;
	public String[] actions;

	/**
	 * Called when the server sends an entity's initial data (server: {@code Entity.writeInitialData()})
	 */
	public abstract Entity spawnFromInitial(BCFMap map);

	/**
	 * Load spec metadata from server (server: {@code EntitySpec.writeSpec()})
	 */
	public void readSpec(BCFMap map) {
	}
}
