package blue.made.angleserver.network.packet.in;

import blue.made.angleserver.Game;
import blue.made.angleserver.entity.structure.StructureEntity;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.network.packet.out.O20TerrainMeta;
import blue.made.angleserver.network.packet.out.O21TerrainChunk;
import blue.made.angleserver.world.World;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFList;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFReader;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class I02Request extends IPacket {
    public static class Loader implements IPacket.BCFLoader {
        @Override
        public IPacket create(BCFReader data, Client from) throws IOException {
            data.next();
            return new I02Request(from, data.read().asMap());
        }
    }

    public static interface Answer {
        public void answer(Client from, BCFMap meta);
    }

    public BCFMap data;

    public I02Request(Client sender, BCFMap data) {
        super(sender);
        this.data = data;
    }

    public void onProcessed() {
        String type = data.get("what").asString();
        Answer a = answers.get(type);
        if (a != null) a.answer(sender, data);
        else System.out.printf("Unknown request of type \"%s\"%n", type);
    }

    public static HashMap<String, Answer> answers = new HashMap<>();

    static {
        answers.put("terrain_chunk", (from, meta) -> {
            World world = Game.INSTANCE.world;
            BCFItem posi = meta.get("pos");
            if (posi != null && posi.isCollection()) {
                BCFList pos = posi.asCollection().convertToList();
                for (int i = 0; i < pos.size(); i += 2) {
                    sendChunk(from, world, pos.get(i).asNumeric().intValue(), pos.get(i + 1).asNumeric().intValue());
                }
            }
            BCFItem rangei = meta.get("range");
            if (rangei != null && rangei.isMap()) {
                BCFMap map = rangei.asMap();
                int x0 = map.get("x0").asNumeric().intValue();
                if (x0 < 0) x0 = 0;
                int x1 = map.get("x1").asNumeric().intValue();
                if (x1 > world.xchunks) x1 = world.xchunks;
                int y0 = map.get("y0").asNumeric().intValue();
                if (y0 < 0) y0 = 0;
                int y1 = map.get("y1").asNumeric().intValue();
                if (y1 > world.ychunks) y1 = world.ychunks;
                for (int x = x0; x < x1; x++) {
                    for (int y = y0; y < y1; y++) {
                        sendChunk(from, world, x, y);
                    }
                }
            }
            if (meta.containsKey("x") && meta.containsKey("y")) {
                int x = meta.get("x").asNumeric().intValue();
                int y = meta.get("y").asNumeric().intValue();
                sendChunk(from, world, x, y);
            }
        });
        answers.put("terrain_meta", (from, meta) -> {
            from.send(new O20TerrainMeta(Game.INSTANCE.world, !meta.containsKey("full")));
        });
    }

    private static void sendChunk(Client from, World w, int x, int y) {
        from.send(new O21TerrainChunk(w, x, y));
        for (StructureEntity s : w.getChunk(x, y).structures) {
            //from.send(new O40SpawnEntity(s)); // TODO Signal not build, just show
        }
    }
}
