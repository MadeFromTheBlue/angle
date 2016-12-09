package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCFWriter;
import blue.made.angleserver.entity.EntitySpec;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/7/2016.
 */
public class O10EntitySpec extends OPacketBCF {
	public EntitySpec spec;

	public O10EntitySpec(EntitySpec spec) {
		super(0x10);
		this.spec = spec;
	}

	@Override
	public void writeData(BCFWriter bcf) throws IOException {
		BCFWriter.Map map = bcf.startMap();
		spec.writeSpec(map);
		map.end();
	}
}
