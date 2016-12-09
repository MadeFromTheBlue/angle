package blue.made.angleserver.world.tags;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public class TileTag {
	short id = 0;
	String name;

	public List<TileTag> parents = new LinkedList<>();

	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public TileTag addParents(TileTag... parents) {
		this.parents.addAll(Arrays.asList(parents));
		return this;
	}

	public TileTag addChildren(TileTag... children) {
		for (TileTag child : children) {
			child.parents.add(this);
		}
		return this;
	}

	public boolean is(TileTag tag) {
		if (this == tag) return true;
		for (TileTag p : parents) {
			if (p.is(tag)) return true; // TODO Make breadth-first
		}
		return false;
	}
}
