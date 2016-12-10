package blue.made.angleclient.entity.structure;

import blue.made.angleshared.util.Location;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 6/8/2016.
 */
public interface Bounds {
    public class Box implements Bounds {
        public final int fromx;
        public final int fromy;
        public final int tox;
        public final int toy;

        public Box(int fromx, int fromy, int tox, int toy) {
            this.fromx = fromx;
            this.fromy = fromy;
            this.tox = tox;
            this.toy = toy;
        }

        @Override
        public void forEach(Consumer<Location> consumer) {
            for (int x = fromx; x < tox; x++) {
                for (int y = fromy; y < toy; y++) {
                    consumer.accept(new Location(x, y));
                }
            }
        }

        @Override
        public boolean in(int x, int y) {
            return x >= fromx && y >= fromy && x < tox && y < toy;
        }

        @Override
        public String toString() {
            return String.format("(%d -> %d, %d -> %d)", fromx, tox, fromy, toy);
        }
    }

    public class Single implements Bounds {
        public final int x;
        public final int y;

        public Single(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void forEach(Consumer<Location> consumer) {
            consumer.accept(new Location(x, y));
        }

        @Override
        public boolean in(int x, int y) {
            return this.x == x && this.y == y;
        }
    }

    public class List implements Bounds {
        public final Bounds[] list;

        public List(Bounds... list) {
            this.list = list;
        }

        public List(java.util.List<Bounds> list) {
            this.list = new Bounds[list.size()];
            list.toArray(this.list);
        }

        @Override
        public void forEach(Consumer<Location> consumer) {
            for (Bounds b : list) b.forEach(consumer);
        }

        @Override
        public boolean in(int x, int y) {
            for (Bounds b : list) {
                if (b.in(x, y)) return true;
            }
            return false;
        }

        @Override
        public String toString() {
            String s = "";
            for (Bounds b : list) {
                if (!s.isEmpty()) s += ", ";
                s += b;
            }
            return s;
        }
    }

    public static Bounds load(BCFReader reader) throws IOException {
        return load(BCF.read(reader));
    }

    public static Bounds load(BCFItem item) {
        if (item.isMap()) {
            BCFMap map = item.asMap();
            if (map.containsKey("fromx")) {
                int fromx = map.get("fromx").asNumeric().intValue();
                int fromy = map.get("fromy").asNumeric().intValue();
                int tox = map.get("tox").asNumeric().intValue();
                int toy = map.get("toy").asNumeric().intValue();
                return new Box(fromx, fromy, tox, toy);
            } else {
                return new Single(map.get("x").asNumeric().intValue(), map.get("y").asNumeric().intValue());
            }
        } else if (item.isCollection()) {
            ArrayList<Bounds> is = new ArrayList<>();
            for (BCFItem i : item.asCollection()) {
                is.add(load(i));
            }
            return new List(is);
        }
        return null;
    }

    public void forEach(Consumer<Location> consumer);

    public boolean in(int x, int y);
}
