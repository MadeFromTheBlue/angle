package blue.made.angleshared.asset;

/**
 * Created by Sam Sartor on 12/23/2016.
 */
public class CombinedName {
    public final String group;
    public final String id;

    public CombinedName(String group, String id) {
        this.group = group;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CombinedName that = (CombinedName) o;

        if (!group.equals(that.group)) return false;
        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return group + ":" + id;
    }
}
