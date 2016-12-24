package blue.made.angleclient.network.packet;

import blue.made.angleclient.Game;
import blue.made.angleclient.action.Action;
import blue.made.angleclient.action.ActionRegistry;
import blue.made.angleclient.entity.Entity;
import blue.made.angleclient.network.packet.in.ServerInfo;
import blue.made.angleclient.world.Chunk;
import blue.made.angleclient.world.Tags;
import blue.made.angleclient.world.World;
import blue.made.bcf.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class PacketManager {
    public static final IPacket.Loader[] ipackets = new IPacket.Loader[256];
    public static final Charset charset = StandardCharsets.UTF_8;

    public static MessageToByteEncoder<OPacket> makeEncoder() {
        return new MessageToByteEncoder<OPacket>() {
            ByteBuf data = Unpooled.buffer();

            @Override
            protected void encode(ChannelHandlerContext ctx, OPacket msg, ByteBuf out) throws Exception {
                try {
                    data.clear();
                    int id = msg.save(data);
                    if (id < 0 || id > 255)
                        throw new IllegalArgumentException("Packet id " + id + " is not a byte");
                    out.writeInt(data.readableBytes() + 1);
                    out.writeByte(id);
                    out.writeBytes(data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
        };
    }

    public static ByteToMessageDecoder makeDecoder() {
        return new ReplayingDecoder() {
            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                int len = in.readInt();
                int type = in.readByte() & 0xFF;
                IPacket.Loader loader = ipackets[type];
                //System.out.printf("Packet: %d (%db)%n", type, len);
                ByteBuf pd = in.readSlice(len - 1);
                if (loader != null) out.add(loader.load(pd));
                else System.out.printf("No loader for packet type 0x%02X%n", type);
            }
        };
    }

    static {
        ipackets[0x01] = (ByteBuf data) -> {
            BCFReader reader = new BCFReader(data);
            reader.next();
            BCFMap map = reader.read().asMap();

            String name = null;
            if (map.containsKey("name")) name = map.get("name").toString();
            String desc = null;
            if (map.containsKey("desc")) desc = map.get("desc").toString();

            BufferedImage ico = null;
            if (map.containsKey("ico")) {
                int w = map.get("ico_w").asNumeric().intValue();
                int h = map.get("ico_h").asNumeric().intValue();
                ByteBuf icodat = map.get("ico").asRaw();
                ico = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        ico.setRGB(x, y, data.readInt());
                    }
                }
            }

            BCFList version = map.get("version").asCollection().convertToList();

            return new ServerInfo(name, desc, ico, version.get(0).asNumeric().shortValue(), version.get(1).asNumeric().shortValue(), version.get(2).asNumeric().shortValue());
        };
        ipackets[0x12] = (ByteBuf data) -> {
            BCFReader reader = new BCFReader(data);
            BCFMap map = BCF.read(reader).asMap();
            String id = map.get("id").asString();
            Action a = ActionRegistry.create(id, map.get("uitype").asString("plain"));

            return (IPacket) () -> {
                ActionRegistry.registry.put(id, a);
            };
        };
        ipackets[0x20] = (ByteBuf data) -> {
            BCFReader reader = new BCFReader(data);
            reader.next();
            BCFMap map = reader.read().asMap();
            int xwidth = map.get("xwidth").asNumeric().intValue();
            int ywidth = map.get("ywidth").asNumeric().intValue();
            int chunkSize = map.get("chunk_size").asNumeric().intValue();

            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            System.out.println(gson.toJson(map.toJson()));

            BCFMap tags = map.get("tile_tag_reg").asMap();

            return (IPacket) () -> {
                Game game = Game.INSTANCE;
                game.world = new World(xwidth, ywidth, chunkSize);
                if (tags != null) game.world.tags = new Tags(tags);
                System.out.printf("New %dx%d world%n", xwidth, ywidth);
                game.onWorldLoad.accept(game);
            };
        };
        ipackets[0x21] = (ByteBuf data) -> {
            Game game = Game.INSTANCE;
            int size = game.world.chunkSize;
            BCFReader reader = new BCFReader(data);
            reader.next();
            BCFMap map = reader.read().asMap();
            int x = map.get("x").asNumeric().intValue();
            int y = map.get("y").asNumeric().intValue();

            return (IPacket) () -> {
                Chunk c = new Chunk(x, y);
                c.height = new float[size * size];
                Iterator<BCFItem> hm = map.get("height_map").asArray().iterator();
                int s = game.world.chunkSize;
                s *= s;
                int i = 0;
                while (i < s) {
                    c.height[i++] = hm.next().asNumeric().floatValue();
                }
                ByteBuf tagdat = map.get("tile_tags").asRaw();
                i = 0;
                c.tags = new short[s][];
                while (i < s) {
                    short[] tags = new short[tagdat.readByte() & 0xFF];
                    for (int j = 0; j < tags.length; j++) tags[j] = tagdat.readShort();
                    c.tags[i++] = tags;
                }
                game.world.chunks[x][y] = c;
            };
        };
        ipackets[0x40] = (ByteBuf data) -> {
            Game game = Game.INSTANCE;
            BCFMap map = BCF.read(new BCFReader(data)).asMap();

            return (IPacket) () -> {
                // TODO: Actually spawn the entity
            };
        };
        ipackets[0x41] = (ByteBuf data) -> {
            Game game = Game.INSTANCE;
            BCFMap map = BCF.read(new BCFReader(data)).asMap();
            long uuid = map.get("id").asNumeric().longValue();

            return (IPacket) () -> {
                Entity a = null;
                /* TODO: Make this work
                a = game.world.units.get(uuid);
				if (a == null) a = game.world.structure.get(uuid);
				*/

                // a = game.world.structures.get(uuid);

                if (a != null) a.dataRec(map);
            };
        };
    }
}
