package blue.made.angleserver.util.bounds;

import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 6/10/2016.
 */
public class IntersectBoundQ extends TransformedBoundQ {
	private StoreBoundQ.Combined stored;
	private boolean found = false;

	public IntersectBoundQ(StoreBoundQ.Bounds with) {
		this.stored = (StoreBoundQ.Combined) with;
	}

	public IntersectBoundQ(Consumer<BoundQ> with) {
		if (with instanceof StoreBoundQ.Bounds) this.stored = (StoreBoundQ.Combined) with;
		else {
			StoreBoundQ q = new StoreBoundQ();
			with.accept(q);
			this.stored = (StoreBoundQ.Combined) q.getStored();
		}
	}

	public boolean intersects() {
		return found;
	}

	@Override
	public void reset() {
		super.reset();
		found = false;
	}

	@Override
	public boolean doBox(int fromx, int fromy, int tox, int toy) {
		if (found) return true;
		for (StoreBoundQ.Bounds b : stored.members) {
			if (b instanceof StoreBoundQ.Box) {
				StoreBoundQ.Box box = (StoreBoundQ.Box) b;
				if (fromx < box.tox && tox > box.fromx && fromy < box.toy && toy > box.fromy) {
					found = true;
					return true;
				}
			} else if (b instanceof StoreBoundQ.Single) {
				StoreBoundQ.Single sing = (StoreBoundQ.Single) b;
				if (sing.x >= fromx && sing.y >= fromy && sing.x < tox && sing.y < toy) {
					found = true;
					return true;
				}
			} else {
				throw new IllegalStateException("Stored bounds are non-standard");
			}
		}
		return false;
	}

	@Override
	public boolean doSingle(int x, int y) {
		if (found) return true;
		for (StoreBoundQ.Bounds b : stored.members) {
			if (b instanceof StoreBoundQ.Box) {
				StoreBoundQ.Box box = (StoreBoundQ.Box) b;
				if (x >= box.fromx && y >= box.fromy && x < box.tox && y < box.toy) {
					found = true;
					return true;
				}
			} else if (b instanceof StoreBoundQ.Single) {
				StoreBoundQ.Single sing = (StoreBoundQ.Single) b;
				if (x == sing.x && y == sing.y) {
					found = true;
					return true;
				}
			} else {
				throw new IllegalStateException("Stored bounds are non-standard");
			}
		}
		return false;
	}
}
