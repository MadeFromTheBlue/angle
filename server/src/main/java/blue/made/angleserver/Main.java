package blue.made.angleserver;

import blue.made.angleserver.network.Network;

/**
 * Created by Sam Sartor on 5/16/2016.
 */
public class Main {
	public static void main(String[] args) {
		Game.INSTANCE.start();
		Network net = new Network();
		net.start();

	}
}
