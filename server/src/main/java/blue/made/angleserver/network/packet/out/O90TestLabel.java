package blue.made.angleserver.network.packet.out;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * Created by Sam Sartor on 6/10/2016.
 */
public class O90TestLabel extends OPacket {
    public String data;

    public O90TestLabel() {
        super(0x90);
    }

    public O90TestLabel(String info) {
        this();
        data = info;
    }

    public O90TestLabel(String format, Object... args) {
        this();
        data = String.format(format, args);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeBytes(data.getBytes(StandardCharsets.UTF_8));
    }
}
