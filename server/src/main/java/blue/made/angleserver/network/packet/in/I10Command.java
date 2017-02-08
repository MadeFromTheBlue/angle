package blue.made.angleserver.network.packet.in;

import blue.made.angleserver.Game;
import blue.made.angleserver.commands.Commands;
import blue.made.angleserver.network.Client;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFNull;
import blue.made.bcf.BCFReader;

import java.io.IOException;

/**
 * Created by sam on 2/8/17.
 */
public class I10Command extends IPacket {
	public static class Loader implements IPacket.BCFLoader {
		@Override
		public IPacket create(BCFReader data, Client from) throws IOException {
			I10Command command = new I10Command(from);
			data.next();
			BCFMap map = data.read().asMap();
			command.name = map.get("name").asString();
			command.args = map.get("args");
			if (command.args == null) command.args = BCFNull.INSTANCE;
			return command;
		}
	}

	public String name;
	public BCFItem args;

	public I10Command(Client sender) {
		super(sender);
	}

	public void onProcessed() {
		try {
			Game.commands.run(name, sender, args);
		} catch (Exception e) {
			System.err.println("Exception caught while running command \"" + name + "\":");
			e.printStackTrace();
		}
	}
}