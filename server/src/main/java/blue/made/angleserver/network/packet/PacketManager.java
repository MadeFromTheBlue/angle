package blue.made.angleserver.network.packet;

import blue.made.angleserver.network.Client;
import blue.made.angleserver.network.NetServerHandler;
import blue.made.angleserver.network.packet.in.IPacket;
import blue.made.angleserver.network.packet.out.OPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public class PacketManager {
	private IPacket.Loader[] loaders = new IPacket.Loader[256];

	private class Encoder extends MessageToByteEncoder<OPacket> {
		@Override
		protected void encode(ChannelHandlerContext ctx, OPacket msg, ByteBuf out) throws Exception {
			out.writeInt(-1);
			int start = out.writerIndex();
			PacketManager.this.write(msg, out);
			out.setInt(start - 4, out.writerIndex() - start);
		}
	}

	private class Decoder extends ByteToMessageDecoder {
		public int length = -1;

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
			Client c = ctx.channel().attr(NetServerHandler.clientAttribute).get();
			while (true) {
				if (length == -1) {
					if (in.readableBytes() < 4) return;
					length = in.readInt();
				}
				if (in.readableBytes() < length) return;
				try {
					ByteBuf slice = in.readSlice(length);
					IPacket p = PacketManager.this.read(slice, c);
					out.add(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
				length = -1;
			}
		}
	}

	public MessageToByteEncoder newEncoder() {
		return new Encoder();
	}

	public void write(OPacket packet, ByteBuf out) {
		try {
			out.writeByte(packet.type);
			packet.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ByteToMessageDecoder newDecoder() {
		return new Decoder();
	}

	public IPacket read(ByteBuf data, Client from) throws IOException {
		int type = data.readByte() & 0xFF;
		IPacket.Loader loader = loaders[type];
		if (loader == null) {
			throw new IOException(String.format("Packet type %d is not recognised", type));
		}
		return loader.create(data, from);
	}

	public void register(int id, IPacket.Loader creator) {
		loaders[id] = creator;
	}
}
