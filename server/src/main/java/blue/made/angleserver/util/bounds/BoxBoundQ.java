package blue.made.angleserver.util.bounds;

/**
 * Created by Sam Sartor on 6/6/2016.
 */
public class BoxBoundQ extends TransformedBoundQ {
	private int minx;
	private int miny;
	private int maxx;
	private int maxy;
	private boolean found = false;

	public BoxBoundQ(int minx, int miny, int maxx, int maxy) {
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
	}

	@Override
	public boolean doBox(int fromx, int fromy, int tox, int toy) {
		if (found) return true;
		if (minx < tox && maxx > fromx && miny < toy && maxy > fromy) {
			found = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean doSingle(int x, int y) {
		if (x >= minx && y >= miny && x < maxx && y < maxy) found = true;
		return found;
	}
}
