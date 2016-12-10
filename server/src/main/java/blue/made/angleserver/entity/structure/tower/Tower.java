package blue.made.angleserver.entity.structure.tower;

import blue.made.angleserver.entity.Entity;
import blue.made.angleserver.entity.structure.Structure;
import blue.made.angleserver.entity.structure.StructureSpec;
import blue.made.angleserver.entity.structure.req.BuildReq;
import blue.made.angleserver.entity.structure.req.BuildReqAnd;
import blue.made.angleserver.entity.structure.req.BuildReqOverlap;
import blue.made.angleserver.entity.structure.req.BuildReqTilesAre;
import blue.made.angleserver.util.bounds.BoundQ;
import blue.made.angleserver.util.bounds.GridBoundQ;
import blue.made.angleserver.world.Chunk;
import blue.made.angleserver.world.World;
import blue.made.angleserver.world.tags.Tags;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Created by sam on 12/8/16.
 */
public abstract class Tower<E extends Tower.TowerEntity> extends StructureSpec<E> {
    public static abstract class TowerEntity extends Structure {
        public Chunk[] toAttack = new Chunk[0];

        public TowerEntity(long uuid) {
            super(uuid);
        }

        @Override
        public void tick(World world) {
            for (Chunk c : toAttack) {
                // TODO call attack on entities
            }
        }

        /**
         * Call this function to specify the bounds of the tower's attack range. This is just an
         * optimization, further checks will be needed in {@link #attack(Entity)}.
         * <br>
         * MAKE SURE TO CALL THIS AT LEAST ONCE, PROBABLY IN {@link #postSpawn()}
         */
        public void buildTargets(World world, Consumer<BoundQ> range) {
            HashSet<Chunk> locs = new HashSet<>();
            BoundQ q = new GridBoundQ(World.CHUNK_WIDTH, l -> {
                locs.add(world.getChunk(l.x, l.y));
                return false;
            });
            toAttack = new Chunk[locs.size()];
            locs.toArray(toAttack);
        }

        /**
         * Checks if an entity is is range and attacks it
         *
         * @return true if the entity can be (and was) attacked
         */
        public abstract boolean attack(Entity e);
    }

    @Override
    public BuildReq getBuildReq() {
        // Tower must not overlap other structures and must be on ground
        return new BuildReqAnd(new BuildReqOverlap(), new BuildReqTilesAre(Tags.ground));
    }
}
