package blue.made.angleserver;

import blue.made.angleserver.action.Action;
import blue.made.angleserver.config.JSONConfig;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.network.packet.out.OPacket;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.TagRegistry;
import blue.made.angleshared.ConfigMerge;
import blue.made.angleshared.resolver.Resolver;
import blue.made.angleshared.util.Util;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Created by Sam Sartor on 5/9/2016.
 */
public class Game {
    public static enum State {
        RUNNING;
    }

    public static Game INSTANCE = new Game();
    public static Resolver entityResolver = new Resolver();
    public static Resolver actionResolver = new Resolver();
    public static ConfigMerge configMerger = new ConfigMerge(entityResolver);

    private boolean gameOver;
    private Instant now;
    private State state = State.RUNNING;
    public World world;
    public TagRegistry tags = new TagRegistry();

    /**
     * Master list of active clients.
     */
    public ArrayList<Client> active = new ArrayList<>();

    public void start() {
        actionResolver.addPackage("blue.made.angleserver.action.actions", Action.class::isAssignableFrom);
        entityResolver.addPackage("blue.made.angleserver.entity", Entity.class::isAssignableFrom);

        configMerger.merge(Util.bcfConfigs.get("main", "master_config").pull().asMap());

        world = new World(tags);

        try {
            JSONConfig.loadWorld(world, Util.getJsonFromFile("default-config.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public void flushClients() {
        active.forEach(Client::send);
    }

    public void sendToClients(OPacket p) {
        active.forEach(c -> c.send(p));
    }

    public void queueToClients(OPacket p) {
        active.forEach(c -> c.queuePacket(p));
    }

    public void run() {
        while (!gameOver) {
            // TODO: timing
            // TODO: Do stuff

            if (world != null) world.tick();
            flushClients();

            this.now = Instant.now();
        }
    }

    public Instant getNow() {
        return now;
    }

    // Testing only
    public static void _reset() {
        INSTANCE = new Game();
    }
}
