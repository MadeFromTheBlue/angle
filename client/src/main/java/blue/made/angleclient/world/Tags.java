package blue.made.angleclient.world;

import blue.made.bcf.BCFCollection;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sam Sartor on 5/31/2016.
 */
public class Tags {
    public class Tag {
        public Color color;
        public String name;
        public List<Tag> parents = new ArrayList<>();

        public boolean is(Tag t) {
            if (t == this) return true;
            for (Tag p : parents) {
                if (p.is(t)) return true;
            }
            return false;
        }
    }

    public Tag[] tags = new Tag[0x10000];
    public HashMap<String, Tag> nameMap = new HashMap<>();

    public Tags(BCFMap serverdat) {
        for (Map.Entry<String, BCFItem> e : serverdat.entrySet()) {
            BCFMap m = e.getValue().asMap();
            int id = m.get("id").asNumeric().shortValue() & 0xFFFF;
            Tag t = new Tag();
            t.name = e.getKey();
            tags[id] = t;
            nameMap.put(t.name, t);
        }
        for (Map.Entry<String, BCFItem> e : serverdat.entrySet()) {
            Tag t = nameMap.get(e.getKey());
            BCFCollection ps = e.getValue().asMap().get("parents").asCollection();
            for (BCFItem p : ps.convertToList()) {
                t.parents.add(nameMap.get(p.asString()));
            }
        }
        InputStream colorin = this.getClass().getClassLoader().getResourceAsStream("tilecolors.json");
        System.out.println(colorin);
        JsonObject colors = new JsonParser().parse(new InputStreamReader(colorin)).getAsJsonObject();
        for (Tag t : nameMap.values()) {
            for (Map.Entry<String, JsonElement> color : colors.entrySet()) {
                if (t.is(nameMap.get(color.getKey()))) {
                    t.color = new Color(Integer.parseInt(color.getValue().getAsString(), 16), false);
                    break;
                }
            }
        }
    }
}
