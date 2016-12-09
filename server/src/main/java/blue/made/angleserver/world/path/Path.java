package blue.made.angleserver.world.path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 12/8/16.
 */
public class Path {
	public abstract class Node {
		public float x, y;
		public Node next;
	}

	public List<Node> starts = new ArrayList<>();
}
