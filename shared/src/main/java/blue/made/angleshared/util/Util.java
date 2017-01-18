package blue.made.angleshared.util;

import blue.made.angleshared.asset.AssetSource;
import blue.made.angleshared.asset.UncachedAssetSource;
import blue.made.angleshared.asset.WaitingAssetSource;
import blue.made.bcf.BCF;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;

/**
 * Created by sumner on 11/30/16.
 */
public class Util {
    public static AssetSource<JsonElement> jsonConfigs;

    static {
        JsonParser parse = new JsonParser();
        jsonConfigs = new UncachedAssetSource<JsonElement>() {
            protected JsonElement load(String group, String id) throws Exception {
                String path = String.format("configs/%s.", id.replace('.', '/'));
                try {
                    return parse.parse(new InputStreamReader(Util.newFileStream(path + "json")));
                } catch (FileNotFoundException e) {
                    BCFItem bcf;
                    try {
                        bcf = BCF.read(new BCFReader(Util.newFileStream(path + "bcf")));
                    } catch (FileNotFoundException e2) {
                        throw e;
                    }
                    return BCF.toJson(bcf);
                }
            }
        };
        ((WaitingAssetSource) jsonConfigs).setReady();
    }

    public static AssetSource<BCFItem> bcfConfigs;

    static {
        JsonParser parse = new JsonParser();
        bcfConfigs = new UncachedAssetSource<BCFItem>() {
            protected BCFItem load(String group, String id) throws Exception {
                String path = String.format("configs/%s.", id.replace('.', '/'));
                try {
                    return BCF.read(new BCFReader(Util.newFileStream(path + "bcf")));
                } catch (FileNotFoundException e) {
                    JsonElement json;
                    try {
                        json = parse.parse(new InputStreamReader(Util.newFileStream(path + "json")));
                    } catch (FileNotFoundException e2) {
                        throw e;
                    }
                    return BCF.fromJson(json);
                }
            }
        };
        ((WaitingAssetSource) bcfConfigs).setReady();
    }


    public static final float FLOAT_TOLERANCE = 0.00001f;
    public static final float floatPI = (float) Math.PI;

    /**
     * Creates a new file stream of a given file name.
     *
     * @param path the path to the file
     * @return an InputStream of the file
     * @throws FileNotFoundException if the file does not exist
     */
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

    /**
     * Normalizes an angle to between [-pi, pi] radians.
     *
     * @param theta the angle to bound in radians
     * @return the bounded angle in radians
     */
    public static float boundAngle(float theta) {
        theta /= Math.PI;
        theta = (theta + 1) / 2;
        if (theta > 1 || theta < 0) theta -= (int) (theta);
        theta = theta * 2 - 1;
        return theta * (float) Math.PI;
    }

    /**
     * Determines if {@code theta1} is within {@code tolerance} of {@code theta2}.
     *
     * @param theta1    the first angle
     * @param theta2    the second angle
     * @param tolerance the maximum difference between {@code theta1} and {@code theta2}
     * @return true if the difference is less than or equal to the tolerance, otherwise false
     */
    public static boolean angleInRange(float theta1, float theta2, float tolerance) {
        theta1 = boundAngle(theta1);
        theta2 = boundAngle(theta2);
        float dtheta = Math.abs(theta1 - theta2);

        if (dtheta > Math.PI) {
            if (2 * Math.PI - dtheta <= tolerance) return true;
        } else {
            if (dtheta <= tolerance) return true;
        }

        return false;
    }

    private static long seed = System.currentTimeMillis();

    // TODO: Change this one day?
    public static long generateUUID() {
        return seed++;
    }
}
