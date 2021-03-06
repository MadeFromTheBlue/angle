package blue.made.angleserver.util.bounds;

import blue.made.bcf.BCFWriter;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 6/3/2016.
 */
public class ToBCFBoundQ extends TransformedBoundQ {
    private BCFWriter.List out;
    private BCFWriter bcf;

    public ToBCFBoundQ(BCFWriter bcf) throws IOException {
        this.bcf = bcf;
        out = bcf.startList();
    }

    public void finish() throws IOException {
        out.end();
    }

    /**
     * Not supported
     */
    @Override
    @Deprecated
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean doBox(int fromx, int fromy, int tox, int toy) {
        try {
            BCFWriter.Map map = out.startMap();
            map.writeName("fromx");
            map.write(fromx);
            map.writeName("fromy");
            map.write(fromy);
            map.writeName("tox");
            map.write(tox);
            map.writeName("toy");
            map.write(toy);
            map.end();
        } catch (IOException e) {
            throw new IllegalStateException("Could not write a bounds feature", e);
        }
        return false;
    }

    @Override
    public boolean doSingle(int x, int y) {
        try {
            BCFWriter.Map map = out.startMap();
            map.writeName("x");
            map.write(x);
            map.writeName("y");
            map.write(y);
            map.end();
        } catch (IOException e) {
            throw new IllegalStateException("Could not write a bounds feature", e);
        }
        return false;
    }

    public static void write(BCFWriter writer, Consumer<BoundQ> def) throws IOException {
        ToBCFBoundQ to = new ToBCFBoundQ(writer);
        def.accept(to);
        to.finish();
    }
}
