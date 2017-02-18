package blue.made.angleserver;

import blue.made.angleserver.action.Action;
import blue.made.angleserver.commands.Command;
import blue.made.angleserver.commands.Commands;
import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.network.Client;
import blue.made.angleserver.network.packet.out.O02Configs;
import blue.made.angleserver.network.packet.out.OPacket;
import blue.made.angleserver.world.Tile;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.TagRegistry;
import blue.made.angleserver.world.tags.Tags;
import blue.made.angleshared.ConfigMerge;
import blue.made.angleshared.exceptions.AngleException;
import blue.made.angleshared.resolver.Resolver;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFList;
import blue.made.bcf.BCFMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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
    public static Commands commands = new Commands();

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
        commands.resolver.addPackage("blue.made.angleserver.commands", Command.class::isAssignableFrom);

        loadConfigs();

        initWorld();
    }

    private void initWorld() {
        world = new World(tags);

        BCFMap boardConfig = configMerger.getCombined().get("board").asMap();

        int width = boardConfig.get("width").asNumeric().intValue();
        int height = boardConfig.get("height").asNumeric().intValue();

        world.buildInitial(width, height);

        // Deserialize the squareTypes
        BCFList squareTypes = boardConfig.get("squareTypes").asList();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = world.getTile(i, j);

                if (squareTypes.get(j).asString().charAt(i) == 'T')
                    tile.addTag(Tags.path);
                else
                    tile.addTag(Tags.ground);
            }
        }
    }

    public void loadConfigs() {
        try {
            JsonArray configFiles = Util.getJsonFromFile("config_registry.json").getAsJsonArray();

            for (JsonElement e : configFiles) {
                configMerger.merge(Util.bcfConfigs.get("main", e.getAsString()).pull().asMap());
                sendToClients(new O02Configs(configMerger.getCombined()));
            }
        } catch (FileNotFoundException e) {
            throw AngleException.create("InvalidConfigurationException", "message", e.getMessage());
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
