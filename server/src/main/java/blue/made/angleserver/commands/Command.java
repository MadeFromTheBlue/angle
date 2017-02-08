package blue.made.angleserver.commands;

import blue.made.angleshared.exceptions.AngleException;
import blue.made.bcf.BCFItem;
import blue.made.angleserver.network.Client;

import java.util.HashMap;

/**
 * Created by sam on 2/8/17.
 */
public interface Command {
	public void execute(Client sender, BCFItem args) throws AngleException;
}
