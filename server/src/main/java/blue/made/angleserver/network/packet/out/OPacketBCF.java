package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCF;
import blue.made.bcf.BCFReader;
import blue.made.bcf.BCFWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Sam Sartor on 5/26/2016.
 */
public abstract class OPacketBCF extends OPacket {
    protected OPacketBCF(int type) {
        super(type);
    }

    public boolean writeAsJson = false;

    @Override
    public void write(ByteBuf out) {
        if (writeAsJson) {
            writeJSON(out);
        } else {
            try {
                writeData(new BCFWriter(out));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeJSON(ByteBuf out) {
        try {
            // TODO This is really wasteful, make a BCFWriterToJson class
            ByteBuf bcf = Unpooled.buffer();
            write(bcf);
            out.writeBytes(Unpooled.copiedBuffer(BCF.read(new BCFReader(bcf)).toJson().toString(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void writeData(BCFWriter bcf) throws IOException;
}
