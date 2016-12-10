package blue.made.angleclient.network.packet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public interface OPacket {
    int save(ByteBuf data) throws IOException;
}
