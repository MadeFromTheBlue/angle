package blue.made.angleserver.world;

import blue.made.angleserver.world.tags.TileTag;

import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 6/13/2016.
 */
public class Tile {
	private World world;
	private final int i;
	public final int x;
	public final int y;

	Tile(World world, int x, int y) {
		this.world = world;
		this.i = y * world.xwidth + x;
		this.x = x;
		this.y = y;
	}

	private short[] tags() {
		return world.tileTags[i];
	}

	private void tags(short[] to) {
		world.tileTags[i] = to;
	}

	public float getZ() {
		return world.heights[i];
	}

	public float setZ(float z) {
		return world.heights[i] = z;
	}

	public boolean addTag(TileTag tag) {
		short id = tag.getId();
		if (id == -1) {
			throw new IllegalArgumentException("Tag: " + tag + " has not been registered");
		}
		short[] tags = tags();
		short[] newtags = new short[tags.length + 1];
		int j = 0;
		boolean added = false;
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == id) return false;
			if (!added && tags[i] > id) { // if we reach a tag greater, insert before (to keep sorted)
				newtags[j++] = id;
				added = true;
			}
			newtags[j] = tags[i]; // copy over old tag
			j++;
		}
		if (!added) {
			newtags[j] = id; // append to end, is greatest tag so far
		}
		tags(newtags);
		return true;
	}

	public boolean isTaggedLiteral(TileTag tag) {
		short[] tags = tags();
		short id = tag.getId();
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == id) return true;
			if (tags[i] > id) return false;
		}
		return false;
	}

	public boolean isTagged(TileTag as) {
		for (short t : tags()) {
			if (world.tagRegistry.fromId(t).is(as)) return true;
		}
		return false;
	}

	public TileTag getTag(TileTag of) {
		for (short i : tags()) {
			TileTag t = world.tagRegistry.fromId(i);
			if (t.is(of)) return t;
		}
		return null;
	}

	public void foreachTag(Consumer<TileTag> consumer) {
		short[] tags = tags();
		for (int i = 0; i < tags.length; i++) {
			consumer.accept(world.tagRegistry.fromId(tags[i]));
		}
	}
}
