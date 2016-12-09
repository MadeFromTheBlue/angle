package blue.made.angleserver.entity;

import java.util.HashMap;

/**
 * Created by Sam Sartor on 5/17/2016.
 */
// Populated by Entities.java
public class EntityRegistry {
	public static HashMap<String, EntitySpec> registry = new HashMap<>();

	public static <S extends EntitySpec> S register(String group, String id, S spec) {
		id = group + "." + id;
		spec.registeredId = id;
		registry.put(id, spec);
		return spec;
	}

	public static <S extends EntitySpec> S register(String id, S spec) {
		return register("angle", id, spec);
	}
}
