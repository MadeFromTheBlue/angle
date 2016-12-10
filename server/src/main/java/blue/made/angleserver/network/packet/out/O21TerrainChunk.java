package blue.made.angleserver.network.packet.out;

import blue.made.angleserver.world.Chunk;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFType;
import blue.made.bcf.BCFWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class O21TerrainChunk extends OPacketBCF {
    public final World world;
    public final Chunk chunk;
    public final int x;
    public final int y;

    public O21TerrainChunk(World world, int x, int y) {
        super(0x21);
        this.world = world;
        this.x = x;
        this.y = y;
        chunk = world.getChunkAt(x, y);
    }

    @Override
    public void writeData(BCFWriter bcf) throws IOException {
        BCFWriter.Map map = bcf.startMap();

        map.writeName("x");
        map.write(x);
        map.writeName("y");
        map.write(y);

        int x0 = x << World.CHUNK_WIDTH_BITS;
        int x1 = x0 + World.CHUNK_WIDTH;
        if (x1 > world.xwidth) {
            x1 = world.xwidth;
        }
        int y0 = y << World.CHUNK_WIDTH_BITS;
        int y1 = y0 + World.CHUNK_WIDTH;
        if (y1 > world.ywidth) {
            y1 = world.ywidth;
        }

        map.writeName("height_map");
        BCFWriter.Array hm = map.startArray(BCFType.FLOAT, (x1 - x0) * (y1 - y0));
        for (int j = y0; j < y1; j++) {
            for (int i = x0; i < x1; i++) {
                hm.write(world.heights[i + j * world.xwidth]);
            }
        }

        ByteBuf tagbytes = Unpooled.buffer();
        for (int j = y0; j < y1; j++) {
            for (int i = x0; i < x1; i++) {
                short[] tags = world.tileTags[i + j * world.xwidth];
                tagbytes.writeByte(tags.length);
                for (short t : tags) {
                    tagbytes.writeShort(t);
                }
            }
        }
        map.writeName("tile_tags");
        map.write(tagbytes);

        map.end();
    }
}
