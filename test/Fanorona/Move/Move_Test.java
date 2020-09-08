package Fanorona.Move;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class Move_Test {

    @Test
    void testMoveStringApproach_success() {
        Move move = new Move(new Point(1, 1), new Point(2, 2), MoveType.APPROACH);
        assertEquals("b1c2A", move.toString());
    }

    @Test
    void testMoveStringWithdrawl_success() {
        Move move = new Move(new Point(1, 1), new Point(2, 2), MoveType.WITHDRAW);
        assertEquals("b1c2W", move.toString());
    }

    @Test
    void testMoveStringPaika_success() {
        Move move = new Move(new Point(1, 1), new Point(2, 2), MoveType.PAIKA);
        assertEquals("b1c2P", move.toString());
    }

    @Test
    void testMoveStringExtended_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(2, 3)}, new MoveType[]{MoveType.APPROACH, MoveType.WITHDRAW});
        assertEquals("b1c2Ac3W", move.toString());
    }

    @Test
    void testNeverVisitTwiceTrue_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(2, 3)}, new MoveType[]{MoveType.APPROACH, MoveType.WITHDRAW});
        assertTrue(move.neverVisitedNodeTwice());
    }

    @Test
    void testNeverVisitTwiceFalse_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(1, 1)}, new MoveType[]{MoveType.APPROACH, MoveType.WITHDRAW});
        assertFalse(move.neverVisitedNodeTwice());
    }
    @Test
    void testAlwaysChangeDirectionTrue_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(2, 3)}, new MoveType[]{MoveType.APPROACH, MoveType.WITHDRAW});
        assertTrue(move.alwaysChangeDirection());
    }
    @Test
    void testAlwaysChangeDirectionFalse_success() {
        Move move = new Move(new Point[]{new Point(1, 1), new Point(2, 2), new Point(3, 3)}, new MoveType[]{MoveType.APPROACH, MoveType.WITHDRAW});
        assertFalse(move.alwaysChangeDirection());
    }

    @Test
    void newMoveWithinvalidMoveString_noFailure() {
        Move move = new Move("fdjkalö");
        System.out.println(move.toString());
    }
//    @Test
//    public void testCreateMove_failureToShort() {
//        Fanorona.Move.Move move = new Fanorona.Move.Move(new Point[] {new Point(0,0)}, new MoveType[] {});
//        Exception expectedException = assertThrows(Exception.class, () -> move.isValidMove());
//
//        String expectedMessage = Fanorona.Move.Move.MOVES_TO_SHORT_ERROR_MESSAGE;
//        String actualMessage = expectedException.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
}
