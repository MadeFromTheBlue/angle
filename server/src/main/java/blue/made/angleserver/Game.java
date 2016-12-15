package blue.made.angleserver;

import blue.made.angleserver.action.Actions;
import blue.made.angleserver.config.JSONConfig;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.TagRegistry;
import blue.made.angleshared.resolver.Resolver;
import blue.made.angleshared.util.Util;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Sam Sartor on 5/9/2016.
 */
public class Game {
    public static enum State {
        RUNNING;
    }

    public static Game INSTANCE = new Game();
    public static Resolver resolver = new Resolver();

    private State state = State.RUNNING;
    public World world;
    public TagRegistry tags = new TagRegistry();

    /**
     * Master list of active clients.
     */
    public ArrayList<Client> active = new ArrayList<>();

    public void start() {
        Actions.init();
        resolver.addPackage("blue.made.angleserver.entity");

        world = new World(tags);

        try {
            JSONConfig.loadWorld(world, Util.newJsonElement("default-config.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public void run() {

    }
}
