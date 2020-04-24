import org.junit.jupiter.api.Test;
import util.MoveType;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Move_Test {

    @Test
    public void testMoveStringApproach_success() {
        Move move = new Move(1, 1, 2, 2, MoveType.approach);
        assertEquals("b1c2A", move.toString());
    }

    @Test
    public void testMoveStringWithdrawl_success() {
        Move move = new Move(1, 1, 2, 2, MoveType.withdraw);
        assertEquals("b1c2W", move.toString());
    }

    @Test
    public void testMoveStringPaika_success() {
        Move move = new Move(1, 1, 2, 2, MoveType.paika);
        assertEquals("b1c2P", move.toString());
    }

    @Test
    public void testMoveStringExtended_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(2, 3)}, new MoveType[]{MoveType.approach, MoveType.withdraw});
        assertEquals("b1c2Ac3W", move.toString());
    }

    @Test
    public void testNeverVisitTwiceTrue_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(2, 3)}, new MoveType[]{MoveType.approach, MoveType.withdraw});
        assertTrue(move.neverVisitedNodeTwice());
    }

    @Test
    public void testNeverVisitTwiceFalse_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(1, 1)}, new MoveType[]{MoveType.approach, MoveType.withdraw});
        assertFalse(move.neverVisitedNodeTwice());
    }
    @Test
    public void testAlwaysChangeDirectionTrue_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(2, 3)}, new MoveType[]{MoveType.approach, MoveType.withdraw});
        assertTrue(move.alwaysChangeDirection());
    }
    @Test
    public void testAlwaysChangeDirectionFalse_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(3, 3)}, new MoveType[]{MoveType.approach, MoveType.withdraw});
        assertFalse(move.alwaysChangeDirection());
    }
//    @Test
//    public void testCreateMove_failureToShort() {
//        Move move = new Move(new Point[] {new Point(0,0)}, new MoveType[] {});
//        Exception expectedException = assertThrows(Exception.class, () -> move.isValidMove());
//
//        String expectedMessage = Move.MOVES_TO_SHORT_ERROR_MESSAGE;
//        String actualMessage = expectedException.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
}
