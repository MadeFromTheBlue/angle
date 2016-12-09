package blue.made.angleclient.action;

import blue.made.bcf.BCFWriter;
import blue.made.angleclient.Game;
import blue.made.angleclient.entity.Entity;
import blue.made.angleclient.ui.UI;
import blue.made.angleclient.ui.UIState;

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
					map.writeName("clickx");
					map.write(ui.gu.getMouseX());
					map.writeName("clicky");
					map.write(ui.gu.getMouseY());
					return 0x60;
				});
				ui.pop();
			}
		});
	}
}
