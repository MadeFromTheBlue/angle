package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sumner Evans on 2017/01/17.
 */
public class O02FSSync extends OPacketBCF   {

    protected O02FSSync() {
        super(0x02);
    }

    @Override
    public void writeData(BCFWriter bcf) throws IOException {
        BCFWriter.Map map = bcf.startMap();

        map.end();
    }
}
