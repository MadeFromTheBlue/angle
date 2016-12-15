/*import blue.made.angleserver.tmp.config.Level;
import blue.made.angleserver.tmp.config.Square;
import blue.made.angleserver.tmp.logic.Game;
import blue.made.angleserver.tmp.logic.minions.Minion;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ConfigLoadTests extends TestBase {

    static Game game;

    @Before
    public void before() throws FileNotFoundException {
        // Reinitialize the game for every test
        game = Game.newGame("test-config.json");
    }

    // Test board configuration load: Ensure that the board configuration was loaded properly
    @Test
    public void testBoardConfigLoad() {
        Board board = game.getBoard();

        // Ensure that the width and height of the board are correct
        assertEquals(48, board.getHeight());
        assertEquals(64, board.getWidth());

        // Test a few of the squares and ensure that they are of the proper type
        assertEquals(Square.SquareType.GROUND, board.getSquare(0, 0).getSquareType());
        assertEquals(Square.SquareType.GROUND, board.getSquare(11, 0).getSquareType());
        assertEquals(Square.SquareType.GROUND, board.getSquare(14, 13).getSquareType());
        assertEquals(Square.SquareType.GROUND, board.getSquare(36, 15).getSquareType());
        assertEquals(Square.SquareType.GROUND, board.getSquare(50, 24).getSquareType());

        assertEquals(Square.SquareType.TRENCH, board.getSquare(13, 2).getSquareType());
        assertEquals(Square.SquareType.TRENCH, board.getSquare(15, 23).getSquareType());
        assertEquals(Square.SquareType.TRENCH, board.getSquare(44, 23).getSquareType());
        assertEquals(Square.SquareType.TRENCH, board.getSquare(48, 38).getSquareType());
        assertEquals(Square.SquareType.TRENCH, board.getSquare(31, 38).getSquareType());

        // Test some squares and ensure that they have the correct cliff sides
        assertContains(board.getSquare(24, 4).getCliffSides(), Square.CliffSide.RIGHT);
        assertContains(board.getSquare(27, 4).getCliffSides(), Square.CliffSide.LEFT);
        assertContains(board.getSquare(33, 40).getCliffSides(), Square.CliffSide.TOP);
        assertContains(board.getSquare(40, 38).getCliffSides(), Square.CliffSide.BOTTOM);

        Square.CliffSide[] cliffSides4616 = board.getSquare(46, 16).getCliffSides();
        assertContains(cliffSides4616, Square.CliffSide.RIGHT);
        assertContains(cliffSides4616, Square.CliffSide.LEFT);
        assertTrue(cliffSides4616.length == 2);

        Square.CliffSide[] cliffSides2413 = board.getSquare(24, 13).getCliffSides();
        assertContains(cliffSides2413, Square.CliffSide.RIGHT);
        assertContains(cliffSides2413, Square.CliffSide.BOTTOM);
        assertTrue(cliffSides4616.length == 2);
    }

    // Test level configuration load: Ensure that the level configuration was loaded correctly
    @Test
    public void testLevelConfigLoad() {
        List<Level> levels = game.getLevels();

        // Ensure that the correct number of levels were loaded
        assertEquals(2, levels.size());

        // Test the first level and ensuring that the configuration was loaded correctly
        Level firstLevel = levels.get(0);
        assertEquals(Minion.Type.GROUND, firstLevel.getWave(0).minionType);
        assertEquals(15, firstLevel.getWave(0).count);
        assertEquals(20.0, firstLevel.getWave(0).start, 0.00001);
        assertEquals(25.0, firstLevel.getWave(0).end, 0.00001);
    }

}
*/