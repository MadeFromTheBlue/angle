package blue.made.angleclient.action;

import blue.made.bcf.BCFWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam Sartor on 6/18/2016.
 */
public class ActionRegistry {
	public static Map<String, Action> registry = new HashMap<>();

	public static Action create(String id, String uitype) {
		switch (uitype) {
			case "click":
				return new ClickAction(id);
			default:
				return new Action(id);
		}
	}
}
