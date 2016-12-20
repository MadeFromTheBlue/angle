package blue.made.angleserver.action.actions;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.action.Action;
import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.util.Util;
import blue.made.bcf.BCFMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sumner on 12/8/16.
 */
public class SpawnEntity extends Action {
    @Override
    public void run(Player player, BCFMap data) {
        String id = data.get("type").asString();

        // TODO: better error handling
        if (id == null) return;

        // Spawn the actual entity with the configuration JSON
        InvokeWrapper creator = Game.resolver.creator(id, long.class, JsonObject.class);
        creator.invoke(Util.generateUUID(), findConfigJson(id));
    }

    private JsonObject findConfigJson(String id) {
        // Find the Configuration JSON
        InputStream inputStream;
        try {
            inputStream = Util.newFileStream(String.format("configs/%s.json", id));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            // There is no config for this object, just return null
            return null;
        }

        // Read and parse the JSON
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);

        return new JsonParser().parse(reader).getAsJsonObject();
    }
}
