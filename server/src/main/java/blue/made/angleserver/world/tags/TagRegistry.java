package blue.made.angleserver.world.tags;

import blue.made.bcf.BCFReader;
import blue.made.bcf.BCFWriter;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.map.hash.TObjectShortHashMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public class TagRegistry {
    public static final TileTag UNREGISTERED = new TileTag();

    private TileTag[] tags = new TileTag[(2 << Short.SIZE) - 1];
    private TObjectShortHashMap<String> ids = new TObjectShortHashMap<>();
    private short nextEmpty = 1;

    public void load(BCFReader reader) throws IOException {
        BCFReader.Map map = reader.startMap();
        while (map.next()) {
            short id = map.readShort();
            ids.put(map.currentName(), id);
            tags[id & 0xFFFF] = UNREGISTERED;
        }
    }

    public void save(BCFWriter writer) throws IOException {
        BCFWriter.Map map = writer.startMap();
        TObjectShortIterator<String> it = ids.iterator();
        while (it.hasNext()) {
            it.advance();
            map.writeName(it.key());
            map.write(it.value());
        }
        map.end();
    }

    public void register(String name, TileTag tag) {
        if (ids.contains(name)) tag.id = ids.get(name);
        else {
            while (tags[nextEmpty & 0xFFFF] != null && nextEmpty != 0) nextEmpty++;
            if (nextEmpty == 0) throw new IllegalStateException("Too many tags registered (max 65,535)!");
            tag.id = nextEmpty++;
            ids.put(name, tag.id);
        }
        tag.name = name;
        tags[tag.id & 0xFFFF] = tag;
    }

    public Set<String> getNames() {
        return ids.keySet();
    }

    public TileTag fromId(short id) {
        return tags[id & 0xFFFF];
    }

    public TileTag fromName(String name) {
        return tags[ids.get(name) & 0xFFFF];
    }

    public List<TileTag> getTags() {
        return Collections.unmodifiableList(Arrays.asList(tags));
    }
}
