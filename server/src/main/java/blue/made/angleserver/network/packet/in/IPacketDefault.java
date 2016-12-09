package blue.made.angleserver.network.packet.in;

import blue.made.angleserver.network.Client;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Created by Sam Sartor on 5/26/2016.
 */
public abstract class IPacketDefault extends IPacket {
	public static class Loader implements IPacket.Loader {
		public Constructor<? extends IPacketDefault> constructor;

		public Loader(Class<? extends IPacketDefault> claz) throws NoSuchMethodException {
			constructor = claz.getConstructor(Client.class);
		}

		@Override
		public IPacket create(ByteBuf data, Client from) throws IOException {
			IPacketDefault pack = null;
			try {
				pack = constructor.newInstance(from);
			} catch (Exception e) {
				throw new IOException("Could not construct " + constructor.getDeclaringClass().getCanonicalName());
			}
			pack.load(data);
			return pack;
		}
	}

	public IPacketDefault(Client sender) {
		super(sender);
	}

	public abstract void load(ByteBuf data);
}
