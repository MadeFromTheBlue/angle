package blue.made.angleserver.commands;

import blue.made.angleserver.Game;
import blue.made.angleserver.network.Client;
import blue.made.angleshared.exceptions.AngleException;
import blue.made.angleshared.resolver.Provides;
import blue.made.bcf.BCFItem;

/**
 * Created by sam on 2/8/17.
 */
@Provides("reloadcfg")
public class ReloadConfig implements Command {
	@Override
	public void execute(Client sender, BCFItem args) throws AngleException {
		Game.INSTANCE.loadConfigs();
	}
}
