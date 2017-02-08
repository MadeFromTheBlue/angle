package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sumner Evans on 2017/02/07.
 */
public class O02Configs extends OPacketBCF {
    BCFMap config;

    public O02Configs(BCFMap config) {
        super(0x02);
        this.config = config;
    }

    @Override
    public void writeData(BCFWriter bcf) throws IOException {
        bcf.write(config);
    }
}
