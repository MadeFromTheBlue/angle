package blue.made.angleshared.util;

/**
 * Created by Sam Sartor on 6/8/2016.
 */
public class Point implements Cloneable {
    public final float x, y;

    /**
     * Represents a point on the board. Not to be confused with Location, Point stores exact
     * floating-point coordinates rather than the integer locations which Location uses.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets a Location object that corresponds to this point.
     *
     * @return the Location corresponding to the point
     */
    public Location asLocation() {
        return new Location(this.intX(), this.intY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point other = (Point) o;
        return this.equals(other.x, other.y);
    }

    public boolean equals(float x, float y) {
        return Math.abs(this.x - x) < Util.FLOAT_TOLERANCE && Math.abs(this.y - y) < Util.FLOAT_TOLERANCE;
    }

    /**
     * Calculate the distance between the x coordinates squared added to the distance between the y
     * coordinates squared.
     *
     * @param a point 1
     * @param b point 2
     * @return (x2 - x1)^2 + (y2 - y1)^2
     */
    public static float distSq(Point a, Point b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return dx * dx + dy * dy;
    }

    /**
     * Calculate the Euclidean distance between two Points
     *
     * @param a point 1
     * @param b point 2
     * @return the Euclidean distance between the given Locations
     */
    public static float dist(Point a, Point b) {
        return (float) Math.sqrt(distSq(a, b));
    }

    /**
     * Linearly interpolate between two points by the given value
     *
     * @param a point 1
     * @param b point 2
     * @param d the distance between the two points to linearly interpolate
     * @return the linearly interpolated point
     */
    public static Point lerp(Point a, Point b, float d) {
        float x = a.x * (1 - d) + b.x * d;
        float y = a.y * (1 - d) + b.y * d;
        return new Point(x, y);
    }

    // Get the point coordinates as integers
    public int intX() {
        return (int) this.x;
    }

    public int intY() {
        return (int) this.y;
    }

    // Point is immutable so provide some helper methods to create a new point with the
    // data from this one.
    public Point withY(float y) {
        return new Point(this.x, y);
    }

    public Point withX(float x) {
        return new Point(x, this.y);
    }

    public Point floor() {
        return new Point(this.intX(), this.intY());
    }

    /**
     * Returns the angle between two locations
     *
     * @param a point 1
     * @param b point 2
     * @return the angle between them in radians
     */
    public static float angle(Point a, Point b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return (float) Math.atan2(dy, dx);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point: (%d.3f, %d.3f)", x, y);
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}

