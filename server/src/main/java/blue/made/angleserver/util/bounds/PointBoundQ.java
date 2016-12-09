package blue.made.angleserver.util.bounds;

/**
 * Created by Sam Sartor on 6/2/2016.
 */
public class PointBoundQ extends TransformedBoundQ {
	//TODO modify x, y instead of using TransformedBoundQ
	private int x;
	private int y;
	private boolean found = false;

	public PointBoundQ(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean intersects() {
		return found;
	}

	@Override
	public void reset() {
		super.reset();
		found = false;
	}

	public void reset(int x, int y) {
		this.reset();
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean doBox(int fromx, int fromy, int tox, int toy) {
		if (found) return true;
		if (x >= fromx && y >= fromy && x < tox && y < toy) {
			found = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean doSingle(int x, int y) {
		if (this.x == x && this.y == y) found = true;
		return found;
	}
}
