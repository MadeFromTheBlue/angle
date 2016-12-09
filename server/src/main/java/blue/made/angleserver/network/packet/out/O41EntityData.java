package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;
import blue.made.angleserver.entity.Entity;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/5/2016.
 */
public class O41EntityData extends OPacketBCF {
	public BCFMap data = new BCFMap();
	public final long uuid;

	public O41EntityData(long uuid) {
		super(0x41);
		this.uuid = uuid;
	}

	public O41EntityData(Entity entity) {
		this(entity.uuid);
	}

	public void add(String name, BCFItem data) {
		this.data.put(name, data);
	}

	@Override
	public void writeData(BCFWriter bcf) throws IOException {
		BCFWriter.Map map = bcf.startMap();
		map.writeName("id");
		map.write(uuid);
		data.append(map);
		map.end();
	}
}
