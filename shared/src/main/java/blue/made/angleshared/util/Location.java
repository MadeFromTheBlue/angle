package blue.made.angleshared.util;

/**
 * Created by Sumner Evans on 2016/12/09.
 */
public class Location {
    public final int x, y;

    /**
     * Represents a location on the board. Not to be confused with Point, Location only stores
     * integer locations (useful for Square coordinates) rather than exact floating-point
     * coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the Point corresponding to this location
     *
     * @return the point
     */
    public Point asPoint() {
        return new Point(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (x != location.x) return false;
        return y == location.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Location: (%d.3f, %d.3f)", x, y);
    }
}
