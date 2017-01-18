package blue.made.angleserver.world;

import blue.made.angleserver.entity.minions.Minion;
import blue.made.angleserver.entity.towers.Tower;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sam Sartor on 6/13/2016.
 */
public class Chunk {
    public List<Minion> minions = new LinkedList<>();
    public List<Tower> towers = new LinkedList<>();
}
