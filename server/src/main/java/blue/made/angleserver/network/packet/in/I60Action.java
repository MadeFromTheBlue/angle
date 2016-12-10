package blue.made.angleserver.network.packet.in;

import blue.made.angleserver.action.Action;
import blue.made.angleserver.action.ActionRegistry;
import blue.made.angleserver.network.Client;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFReader;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/21/2016.
 */
public class I60Action extends IPacket {
    public static class Loader implements IPacket.BCFLoader {
        @Override
        public IPacket create(BCFReader data, Client from) throws IOException {
            return new I60Action(from, BCF.read(data).asMap());
        }
    }

    public BCFMap map;

    public I60Action(Client sender, BCFMap map) {
        super(sender);
        this.map = map;
    }

    @Override
    public void onProcessed() {
        Action act = ActionRegistry.actions.get(map.get("action").asString());
        act.run(sender.player, map);
    }
}
