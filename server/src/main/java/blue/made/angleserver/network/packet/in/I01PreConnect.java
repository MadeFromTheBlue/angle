package blue.made.angleserver.network.packet.in;

import blue.made.bcf.BCFReader;
import blue.made.angleserver.network.Client;

import java.io.IOException;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class I01PreConnect extends IPacket {
	public static class Loader implements IPacket.BCFLoader {
		@Override
		public IPacket create(BCFReader data, Client from) throws IOException {
			I01PreConnect pre = new I01PreConnect(from);
			data.next();
			BCFReader.Map map = data.startMap();
			while (map.next()) {
				switch (map.currentName()) {
					case "name":
						pre.name = map.readString();
						break;
				}
			}
			return pre;
		}
	}

	public String name;

	public I01PreConnect(Client sender) {
		super(sender);
	}

	public void onProcessed() {
		System.out.printf("Client \"%s\" (%s) intends to join.%n", name, sender.getAddress());
		sender.onActivate(this);
	}
}
