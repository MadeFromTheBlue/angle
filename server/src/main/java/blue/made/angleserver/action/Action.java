package blue.made.angleserver.action;

import blue.made.angleserver.Player;
import blue.made.bcf.BCFMap;
import blue.made.bcf.BCFWriter;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/14/2016.
 */
public abstract class Action {
    String registeredId;

    public void writeSpec(BCFWriter.Map to) throws IOException {
        to.put("id", registeredId);
        to.put("uitype", this.getUIType());
    }

    public String getRegisteredId() {
        return registeredId;
    }

    public String getUIType() {
        return "plain";
    }

    public abstract void run(Player player, BCFMap data);
}
