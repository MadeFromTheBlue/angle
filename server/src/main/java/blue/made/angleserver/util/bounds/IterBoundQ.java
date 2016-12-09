package blue.made.angleserver.util.bounds;

import blue.made.angleshared.util.Location;

import java.util.function.Function;

/**
 * Created by Sam Sartor on 6/2/2016.
 */
public class IterBoundQ extends TransformedBoundQ {
    private boolean finished = false;
    public Function<Location, Boolean> alert;

    public IterBoundQ(Function<Location, Boolean> alert) {
        this.alert = alert;
    }

    @Override
    public boolean doBox(int fromx, int fromy, int tox, int toy) {
        for (int x = fromx; x < tox; x++) {
            for (int y = fromy; y < toy; y++) {
                if (finished) return true;
                finished |= alert.apply(new Location(x, y));
            }
        }

        return finished;
    }

    @Override
    public boolean doSingle(int x, int y) {
        finished |= alert.apply(new Location(x, y));
        return finished;
    }
}
