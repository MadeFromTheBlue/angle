package blue.made.angleserver.action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam Sartor on 6/14/2016.
 */
public class ActionRegistry {
	public static Map<String, Action> actions = new HashMap<>();

	public static <A extends Action> A register(String id, A action) {
		action.registeredId = id;
		actions.put(id, action);
		return action;
	}
}
