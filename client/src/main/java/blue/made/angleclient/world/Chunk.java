package blue.made.angleclient.world;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class Chunk {
	public final int x;
	public final int y;
	public short[][] tags;
	public float[] height;

	public Chunk(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
