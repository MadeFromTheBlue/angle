package blue.made.angleserver.entity.structure;

import blue.made.angleserver.entity.EntitySpec;
import blue.made.angleserver.entity.structure.req.BuildReq;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.util.bounds.ToBCFBoundQ;
import blue.made.bcf.BCFWriter;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 5/16/2016.
 */
public abstract class StructureSpec<E extends Structure> extends EntitySpec<Structure> {
    public abstract Consumer<BoundQ> getDefaultBounds();

    /**
     * What conditions must be necessary to build this structure,
     * partially for the benefit of the client (so that the UI can
     * warn the player without contacting the server).
     *
     * @return
     */
    public abstract BuildReq getBuildReq();

    @Override
    public void writeSpec(BCFWriter.Map bcf) throws IOException {
        super.writeSpec(bcf);
        bcf.writeName("bounds");
        ToBCFBoundQ q = new ToBCFBoundQ(bcf);
        getDefaultBounds().accept(q);
        q.finish();
        bcf.writeName("type");
        bcf.write("struct");
    }
}
