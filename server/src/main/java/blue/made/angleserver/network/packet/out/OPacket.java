package blue.made.angleserver.network.packet.out;

import io.netty.buffer.ByteBuf;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public abstract class OPacket {
	public final byte type;

	protected OPacket(int type) {
		this.type = (byte) type;
	}

	public abstract void write(ByteBuf out);
}
