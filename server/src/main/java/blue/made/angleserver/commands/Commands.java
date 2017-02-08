package blue.made.angleserver.commands;

import blue.made.angleserver.network.Client;
import blue.made.angleshared.exceptions.AngleException;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.resolver.Resolver;
import blue.made.bcf.BCFItem;

import java.util.HashMap;

/**
 * Created by sam on 2/8/17.
 */
public class Commands {
	public Resolver resolver = new Resolver();
	private HashMap<String, Command> creators = new HashMap<>();

	public Command get(String name) {
		return creators.computeIfAbsent(name, n -> {
			InvokeWrapper c = resolver.creator(n, String.class);
			if (c == null) {
				c = resolver.creator(n);
				if (c == null) return null;
				return (Command) c.invoke();
			}
			return (Command) c.invoke(n);
		});
	}

	public void run(String name, Client sender, BCFItem args) throws AngleException {
		Command c = get(name);
		if (c == null) throw AngleException.create("UnknownCommand", "com", name);
		c.execute(sender, args);
	}
}
