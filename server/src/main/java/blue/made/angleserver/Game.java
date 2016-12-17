package blue.made.angleserver;

import blue.made.angleserver.action.Actions;
import blue.made.angleserver.config.JSONConfig;
import blue.made.angleserver.entity.Entities;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.TagRegistry;
import blue.made.angleshared.util.Util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sam Sartor on 5/9/2016.
 */
public class Game {
    public static enum State {
        RUNNING;
    }

    public static final short VERSION_MAJOR = -2;
    public static final short VERSION_MINOR = 0;
    public static final short VERSION_PATCH = 0;

    public static Game INSTANCE = new Game();

    private State state = State.RUNNING;
    public World world;
    public TagRegistry tags = new TagRegistry();

    /**
     * Master list of active clients.
     */
    public ArrayList<Client> active = new ArrayList<>();

    public void start() {
        Actions.init();
        Entities.init();
        world = new World(tags);

        try {
            JSONConfig.loadWorld(world, Util.getJsonFromFile("default-config.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public void run() {

    }

    private static Random uuidr = new Random();

    public static synchronized long newUUID() {
        return System.currentTimeMillis() ^ ((long) uuidr.nextInt() << 24);
    }
}
