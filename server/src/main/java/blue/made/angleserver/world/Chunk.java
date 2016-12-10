package blue.made.angleserver.world;

import blue.made.angleserver.entity.structure.StructureEntity;
import blue.made.angleserver.util.bounds.PointBoundQ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam Sartor on 6/13/2016.
 */
public class Chunk {
    public List<StructureEntity> structures = new ArrayList<>();
    public boolean invalidStructures = false;

    public StructureEntity getStructure(int x, int y) {
        PointBoundQ check = new PointBoundQ(x, y);
        for (StructureEntity struct : structures) {
            struct.getBounds().accept(check);
            check.reset();
        }
        return null;
    }
}
