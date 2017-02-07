package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/26/2016.
 */
public class O04ReturnPing extends OPacketBCF {
    public long pingReceiveTime;

    public O04ReturnPing(long pingReceiveTime) {
        super(0x02);
    }

    @Override
    public void writeData(BCFWriter bcf) throws IOException {
        BCFWriter.Map map = bcf.startMap();
        map.put("queue_time", pingReceiveTime - System.nanoTime());
        map.end();
    }
}
