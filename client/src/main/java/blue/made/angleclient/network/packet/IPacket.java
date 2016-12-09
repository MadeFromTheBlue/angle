package blue.made.angleclient.network.packet;

import blue.made.bcf.BCFReader;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public interface IPacket {
	interface Loader {
		IPacket load(ByteBuf data) throws IOException;
	}

	void onProcess();
}
