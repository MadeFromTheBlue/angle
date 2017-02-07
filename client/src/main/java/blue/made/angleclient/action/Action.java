package blue.made.angleclient.action;

import blue.made.angleclient.Game;
import blue.made.angleclient.entity.Entity;
import blue.made.angleclient.ui.UI;
import blue.made.bcf.BCFWriter;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by Sam Sartor on 6/18/2016.
 */
public class Action {
    public String id;

    public Action(String id) {
        this.id = id;
    }

    public void onRun(Entity target, UI ui) {
        Game.INSTANCE.net.send(data -> {
            encodeBase(this, target, data);
            return 0x60;
        });
    }

    public static BCFWriter.Map encodeBase(Action a, Entity t, ByteBuf data) throws IOException {
        BCFWriter.Map map = new BCFWriter(data).startMap();
        map.put("action", a.id);
        map.put("targets", t.uuid);
        return map;
    }
}
