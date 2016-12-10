package blue.made.angleserver.world;

import blue.made.angleserver.entity.structure.Structure;
import blue.made.angleserver.util.bounds.PointBoundQ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam Sartor on 6/13/2016.
 */
public class Chunk {
    public List<Structure> structures = new ArrayList<>();
    public boolean invalidStructures = false;

    public Structure getStructure(int x, int y) {
        PointBoundQ check = new PointBoundQ(x, y);
        for (Structure struct : structures) {
            struct.getBounds().accept(check);
            check.reset();
        }
        return null;
    }
}
