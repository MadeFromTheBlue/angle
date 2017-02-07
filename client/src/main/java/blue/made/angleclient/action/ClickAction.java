package blue.made.angleclient.action;

import blue.made.angleclient.Game;
import blue.made.angleclient.entity.Entity;
import blue.made.angleclient.ui.UI;
import blue.made.angleclient.ui.UIState;
import blue.made.bcf.BCFWriter;

/**
 * Created by Sam Sartor on 6/22/2016.
 */
public class ClickAction extends Action {
    public ClickAction(String id) {
        super(id);
    }

    @Override
    public void onRun(Entity target, UI ui) {
        ui.push(new UIState(ui) {
            @Override
            public void onLClick() {
                Game.INSTANCE.net.send(data -> {
                    BCFWriter.Map map = encodeBase(ClickAction.this, target, data);
                    map.put("clickx", ui.gu.getMouseX());
                    map.put("clicky", ui.gu.getMouseY());
                    return 0x60;
                });
                ui.pop();
            }
        });
    }
}
