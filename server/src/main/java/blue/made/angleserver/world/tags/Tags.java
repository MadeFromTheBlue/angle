package blue.made.angleserver.world.tags;

/**
 * Created by Sam Sartor on 5/13/2016.
 */
public class Tags {
	public static void register(TagRegistry registry) {
		registry.register("ground", ground);
		registry.register("path", path);
	}

	public static final TileTag ground = new TileTag();
	public static final TileTag path = new TileTag();
}
