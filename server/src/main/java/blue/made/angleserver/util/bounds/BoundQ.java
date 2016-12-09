package blue.made.angleserver.util.bounds;

import blue.made.angleshared.util.Location;

/**
 * Created by Sam Sartor on 6/2/2016.
 */
public interface BoundQ {
    public void translate(int byX, int byY);

    public void rotate(int quarters);

    public void flipX();

    public void flipY();

    public boolean box(int fromx, int fromy, int tox, int toy);

    public default boolean boxAt(int x, int y, int w, int h) {
        return box(x, y, x + w, y + h);
    }

    public default boolean boxAt(Location min, int w, int h) {
        return boxAt(min.x, min.y, w, h);
    }

    public default boolean boxAt(int x, int y, int up, int down, int left, int right) {
        return box(x - left, y - up, x + right + 1, y + down + 1);
    }

    public default boolean boxAt(Location from, int up, int down, int left, int right) {
        return boxAt(from.x, from.y, up, down, left, right);
    }

    public boolean single(int x, int y);

    public default boolean single(Location l) {
        return single(l.x, l.y);
    }
}
