package blue.made.angleserver.network.packet.out;

import blue.made.bcf.BCFType;
import blue.made.bcf.BCFWriter;
import blue.made.angleserver.Game;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.TileTag;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class O20TerrainMeta extends OPacketBCF {
	public final World world;
	public boolean minimal;

	public O20TerrainMeta(World world, boolean minimal) {
		super(0x20);
		this.world = world;
		this.minimal = minimal;
	}

	@Override
	public void writeData(BCFWriter bcf) throws IOException {
		BCFWriter.Map map = bcf.startMap();

		map.writeName("xwidth");
		map.write(world.xwidth);
		map.writeName("ywidth");
		map.write(world.ywidth);
		map.writeName("chunk_size");
		map.write(World.CHUNK_WIDTH);

		if (!minimal) {
			map.writeName("tile_tag_reg");
			BCFWriter.Map tags = map.startMap();
			for (TileTag t : Game.INSTANCE.tags.getTags()) {
				if (t != null) {
					tags.writeName(t.getName());
					BCFWriter.Map tag = tags.startMap();
					tag.writeName("id");
					tag.write(t.getId());
					tag.writeName("parents");
					BCFWriter.Array pars = tag.startArray(BCFType.STRING, t.parents.size());
					for (TileTag p : t.parents) pars.write(p.getName());
					tag.end();
				}
			}
			tags.end();
		}

		map.end();
	}
}
