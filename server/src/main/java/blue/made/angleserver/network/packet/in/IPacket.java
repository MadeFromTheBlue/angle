package blue.made.angleserver.network.packet.in;

import blue.made.bcf.BCFReader;
import blue.made.angleserver.network.Client;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public abstract class IPacket {
	public static interface Loader {
		IPacket create(ByteBuf data, Client from) throws IOException;
	}

	public static interface BCFLoader extends Loader {
		default IPacket create(ByteBuf data, Client from) throws IOException {
			return create(new BCFReader(data), from);
		}

		IPacket create(BCFReader data, Client from) throws IOException;
	}

	public final Client sender;

	public IPacket(Client sender) {
		this.sender = sender;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.getClass().getSimpleName());
		b.append(" from ");
		b.append(sender.getAddress());
		return b.toString();
	}

	public abstract void onProcessed();
}
