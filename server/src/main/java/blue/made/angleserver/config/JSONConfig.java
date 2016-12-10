package blue.made.angleserver.config;

import blue.made.angleserver.world.Tile;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by sam on 12/8/16.
 */
public class JSONConfig {
    private static Gson gson = new Gson();

    public static void loadWorld(World world, JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject().get("board").getAsJsonObject();

        // Deserialize the primitives
        int width = jsonObject.get("width").getAsInt();
        int height = jsonObject.get("height").getAsInt();

        world.buildInitial(width, height);

        // Deserialize the squareTypes
        Type stringArrayType = new TypeToken<String[]>() {
        }.getType();

        String[] squareTypes = gson.fromJson(jsonObject.get("squareTypes").getAsJsonArray(), stringArrayType);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = world.getTile(i, j);

                if (squareTypes[j].charAt(i) == 'T') tile.addTag(Tags.path);
                else tile.addTag(Tags.ground);
            }
        }
    }
}