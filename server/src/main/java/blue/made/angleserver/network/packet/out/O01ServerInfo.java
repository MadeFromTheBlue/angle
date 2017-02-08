package blue.made.angleserver.network.packet.out;

import blue.made.angleshared.AngleInfo;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/26/2016.
 */
public class O01ServerInfo extends OPacketBCF {
    public O01ServerInfo() {
        super(0x01);
    }

    public String name = "TestServer";
    public String desc = "Default Stuff";

    @Override
    public void writeData(BCFWriter bcf) throws IOException {
        BCFWriter.Map map = bcf.startMap();
        map.writeName("name");
        map.write(name);
        map.writeName("desc");
        map.write(desc);
        /*
        data.writeName("ico_w");
		data.write(0);
		data.writeName("ico_h");
		data.write(0);
		data.writeName("ico");
		data.write(Unpooled.buffer());
		*/
        map.writeName("version");
        map.write(BCF.store(AngleInfo.VERSION_MAJOR, AngleInfo.VERSION_MINOR, AngleInfo.VERSION_PATCH));
        map.end();
    }
}
