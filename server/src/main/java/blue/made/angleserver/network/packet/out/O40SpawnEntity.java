package blue.made.angleserver.network.packet.out;

import blue.made.angleserver.entity.Entity;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/2/2016.
 */
public class O40SpawnEntity extends OPacketBCF {
    public Entity entity;

    public O40SpawnEntity(Entity entity) {
        super(0x40);
        this.entity = entity;
    }

    @Override
    public void writeData(BCFWriter bcf) throws IOException {
        BCFWriter.Map map = bcf.startMap();
        entity.writeInitialData(map);
        map.end();
    }
}
