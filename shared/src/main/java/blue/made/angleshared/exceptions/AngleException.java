package blue.made.angleshared.exceptions;

import blue.made.angleshared.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sumner Evans on 2016/12/20.
 */
public class AngleException extends RuntimeException {

    public class ErrorConfig {
        public int id;
        public String message;
    }

    private static Map<String, ErrorConfig> errorConfigs;

    static {
        try {
            JsonElement json = Util.getJsonFromFile("error_messages.json");

            Type errorConfigListType = new TypeToken<HashMap<String, ErrorConfig>>() {}.getType();
            errorConfigs = new Gson().fromJson(json.getAsJsonArray(), errorConfigListType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public AngleException(String s) {
        super(s);
    }

    /**
     * <p>Throws an AngleException of the given type with the arguments provided.</p>
     * <p><strong>Example Usage:</strong></p>
     * <pre>
     * {@code
     * throw new AngleException("InvalidConfigurationException", new HashMap<String, String> () {{
     *    put("message", "UpgradeEntity requires upgrade_to to be specified");
     * }});
     * }
     * </pre>
     *
     * @param s
     * @param args
     */
    public AngleException(String s, Map<String, String> args) {
        super(constructMessage(errorConfigs.get(s), args));
    }

    private static String constructMessage(ErrorConfig errorConfig, Map<String, String> args) {
        String message = errorConfig.message;
        for (Map.Entry<String, String> e : args.entrySet()) {
            message = message.replace(String.format("{%s}", e.getKey()), e.getValue());
        }
        return message;
    }
}
