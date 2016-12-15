package blue.made.angleclient;

import blue.made.angleclient.network.Network;
import blue.made.angleclient.ui.UI;
import blue.made.angleclient.world.World;
import blue.made.angleshared.resolver.Resolver;

import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class Game {
    public static Game INSTANCE = new Game();
    public static Resolver resolver = new Resolver();

    public World world;
    public Network net;

    public Consumer<Game> onWorldLoad;
    public UI ui;

    public void onWorldLoad(Consumer<Game> adapter) {
        onWorldLoad = adapter;
    }
}
