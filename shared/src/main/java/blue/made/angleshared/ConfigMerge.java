package blue.made.angleshared;

import blue.made.angleshared.resolver.InvokeWrapper;
import blue.made.angleshared.resolver.Resolver;
import blue.made.bcf.BCFItem;
import blue.made.bcf.BCFList;
import blue.made.bcf.BCFMap;

import java.util.HashMap;

/**
 * Created by Sumner Evans on 2017/02/07.
 */
public class ConfigMerge {
    public class TowerType {
        public BCFMap config;
        public InvokeWrapper constructor;

        private TowerType(BCFMap config, InvokeWrapper constructor) {
            this.config = config;
            this.constructor = constructor;
        }

        public Object create(long uuid) {
            return constructor.invoke(uuid, config);
        }
    }

    public HashMap<String, TowerType> towerTypes = new HashMap<>();
    public BCFMap board = new BCFMap();

    private Resolver resolver;

    public ConfigMerge(Resolver resolver) {
        this.resolver = resolver;
    }

    public void merge(BCFMap config) {
        BCFItem boardConfig = config.get("board");
        if (boardConfig != null) board.putAll(boardConfig.asMap());

        BCFItem towersConfig = config.get("towers");
        if (towersConfig != null && towersConfig.isCollection()) {
            towersConfig.asCollection().forEach(c -> mergeTower(c.asMap()));
        }
    }

    private void mergeTower(BCFMap config) {
        // Get the name and provider from the config
        String name = config.get("name").asString();
        String providedBy = config.get("provided_by").asString();
        InvokeWrapper invoker = resolver.creator(providedBy, long.class, BCFMap.class);

        towerTypes.put(name, new TowerType(config, invoker));
    }

    public BCFMap getCombined() {
        BCFMap map = new BCFMap();

        BCFList towers = new BCFList();
        towerTypes.values().forEach(t -> towers.add(t.config));
        map.put("towers", towers);

        map.put("board", board);

        return map;
    }
}
