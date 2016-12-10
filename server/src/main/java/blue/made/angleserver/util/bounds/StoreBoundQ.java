package blue.made.angleserver.util.bounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 6/10/2016.
 */
public class StoreBoundQ extends TransformedBoundQ {
    public static interface Bounds extends Consumer<BoundQ> {
        public boolean apply(BoundQ q);

        @Override
        public default void accept(BoundQ q) {
            apply(q);
        }
    }

    public static class Combined implements Bounds {
        public Collection<Bounds> members;

        public Combined(Collection<Bounds> members) {
            this.members = members;
        }

        public Combined(Bounds... members) {
            this(Arrays.asList(members));
        }

        public Combined() {
            this(new ArrayList<>());
        }

        @Override
        public boolean apply(BoundQ q) {
            for (Bounds b : members) {
                if (b.apply(q)) return true;
            }
            return false;
        }
    }

    public static class Box implements Bounds {
        public int fromx;
        public int fromy;
        public int tox;
        public int toy;

        public Box(int fromx, int fromy, int tox, int toy) {
            this.fromx = fromx;
            this.fromy = fromy;
            this.tox = tox;
            this.toy = toy;
        }

        @Override
        public boolean apply(BoundQ q) {
            return q.box(fromx, fromy, tox, toy);
        }
    }

    public static class Single implements Bounds {
        public int x;
        public int y;

        public Single(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean apply(BoundQ q) {
            return q.single(x, y);
        }
    }

    private Combined current = new Combined();

    @Override
    public void reset() {
        super.reset();
        current = new Combined();
    }

    @Override
    public boolean doBox(int fromx, int fromy, int tox, int toy) {
        current.members.add(new Box(fromx, fromy, tox, toy));
        return false;
    }

    @Override
    public boolean doSingle(int x, int y) {
        current.members.add(new Single(x, y));
        return false;
    }

    public Bounds getStored() {
        return current;
    }
}
