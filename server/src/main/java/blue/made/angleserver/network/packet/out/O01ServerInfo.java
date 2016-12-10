package blue.made.angleserver.network.packet.out;

import blue.made.angleserver.Game;
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
		map.writeName("ico_w");
		map.write(0);
		map.writeName("ico_h");
		map.write(0);
		map.writeName("ico");
		map.write(Unpooled.buffer());
		*/
        map.writeName("version");
        map.write(BCF.store(Game.VERSION_MAJOR, Game.VERSION_MINOR, Game.VERSION_PATCH));
        map.end();
    }
}
