package blue.made.angleserver.network.packet.in;

import blue.made.angleserver.network.Client;
import blue.made.angleserver.network.packet.out.O04ReturnPing;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/26/2016.
 */
public class I04Ping extends IPacket {
    public static class Loader implements IPacket.Loader {
        @Override
        public IPacket create(ByteBuf data, Client from) throws IOException {
            return new I04Ping(from);
        }
    }

    public long time;

    public I04Ping(Client sender) {
        super(sender);
        time = System.nanoTime();
    }

    public void onProcessed() {
        sender.send(new O04ReturnPing(time));
    }
}
