package blue.made.angleshared.util;

import blue.made.angleshared.asset.AssetSource;
import blue.made.angleshared.asset.CachingPermanentAssetSource;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

/**
 * Created by sumner on 11/30/16.
 */
public class Util {
    public static AssetSource<JsonElement> configs;
    static {
        JsonParser parse = new JsonParser();
        CachingPermanentAssetSource<JsonElement> source = new CachingPermanentAssetSource<JsonElement>() {
            protected JsonElement load(String group, String id) throws Exception {
                String path = String.format("configs/%s.json", id.replace('.', '/'));
                return parse.parse(new InputStreamReader(Util.newFileStream(path)));
            }
        };
        source.setReady();
        configs = source;
    }


    public static final float FLOAT_TOLERANCE = 0.00001f;
    public static final float floatPI = (float) Math.PI;

    public static InputStream newFileStream(String path) throws FileNotFoundException {
        ClassLoader cl = Util.class.getClassLoader();
        InputStream stream = cl.getResourceAsStream(path);


        if (stream == null) {
            try {
                stream = new FileInputStream(new File(new File("data/"), path));
            } catch (FileNotFoundException ex) {
                throw new FileNotFoundException(String.format("Could not find file %s in jar or data/", path));
            }
        }

        return stream;
    }

    public static JsonElement newJsonElement(String file) throws FileNotFoundException {
        InputStream s = newFileStream(file);
        return new JsonParser().parse(new InputStreamReader(s));
    }

    public static float boundAngle(float theta) {
        theta /= Math.PI;
        theta = (theta + 1) / 2;
        if (theta > 1 || theta < 0) theta -= (int) (theta);
        theta = theta * 2 - 1;
        return theta * (float) Math.PI;
    }

    public static boolean angleInRange(float theta1, float theta2, float tolerance) {
        theta1 = boundAngle(theta1);
        theta2 = boundAngle(theta2);
        float dtheta = Math.abs(theta1 - theta2);

        if (dtheta > Math.PI) {
            if (2 * Math.PI - dtheta <= tolerance)
                return true;
        } else {
            if (dtheta <= tolerance)
                return true;
        }

        return false;
    }

    // TODO: FIX THIS CRAP
    private static long bs = 39274917;

    public static long generateUUID() {
        return System.currentTimeMillis() ^ bs++; // TODO: FIX THIS
    }
}
