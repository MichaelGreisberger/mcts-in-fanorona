package Fanorona;

import Fanorona.Move;
import Fanorona.MoveList;
import org.junit.jupiter.api.Test;
import Fanorona.util.MoveType;

import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class MoveList_Test {

    private final Move SIMPLE_MOVE = new Move(new Point(0,0), new Point(1,1), MoveType.approach);
    private final Move SIMPLE_MOVE2 = new Move(new Point(1,1), new Point(2,2), MoveType.withdraw);
    private final Move SIMPLE_MOVE3 = new Move(new Point(2,2), new Point(3,3), MoveType.paika);
    private final Move SIMPLE_MOVE4 = new Move(new Point(3,3), new Point(4,4), MoveType.approach);
    private final Move SIMPLE_MOVE5 = new Move(new Point(4,4), new Point(5,5), MoveType.withdraw);
    private final Move SIMPLE_MOVE6 = new Move(new Point(5,5), new Point(6,6), MoveType.paika);
    private final String[] EXPECTED_STRINGS = new String[] {"a0b1A", "b1c2W", "c2d3P", "d3e4A", "e4f5W", "f5g6P"};
    @Test
    public void appendSingleTest_succes() {
        MoveList moveList = new MoveList();
        moveList.append(SIMPLE_MOVE);

        assertEquals(SIMPLE_MOVE, moveList.getFirst().move);
        assertEquals(SIMPLE_MOVE, moveList.getLast().move);
    }

    @Test
    public void appendMultipleTest_succes() {
        MoveList moveList = new MoveList();
        moveList.append(SIMPLE_MOVE);
        moveList.append(SIMPLE_MOVE2);
        moveList.append(SIMPLE_MOVE3);

        assertEquals(SIMPLE_MOVE, moveList.getFirst().move);
        assertEquals(SIMPLE_MOVE2, moveList.getFirst().next.move);
        assertEquals(SIMPLE_MOVE2, moveList.getLast().prev.move);
        assertEquals(SIMPLE_MOVE3, moveList.getLast().move);
    }

    @Test
    public void appendListTest_succes() {
        MoveList moveList = new MoveList();
        MoveList moveList2 = new MoveList();

        moveList.append(SIMPLE_MOVE);
        moveList.append(SIMPLE_MOVE2);
        moveList.append(SIMPLE_MOVE3);

        moveList2.append(SIMPLE_MOVE4);
        moveList2.append(SIMPLE_MOVE5);
        moveList2.append(SIMPLE_MOVE6);

        moveList.append(moveList2);

        assertEquals(SIMPLE_MOVE, moveList.getFirst().move);
        assertEquals(SIMPLE_MOVE2, moveList.getFirst().next.move);
        assertEquals(SIMPLE_MOVE5, moveList.getLast().prev.move);
        assertEquals(SIMPLE_MOVE6, moveList.getLast().move);
    }

    @Test
    public void appendIteratorTest_succes() {
        MoveList moveList = new MoveList();

        moveList.append(SIMPLE_MOVE);
        moveList.append(SIMPLE_MOVE2);
        moveList.append(SIMPLE_MOVE3);
        moveList.append(SIMPLE_MOVE4);
        moveList.append(SIMPLE_MOVE5);
        moveList.append(SIMPLE_MOVE6);

        int i = 0;
        for (Move move : moveList) {
            assertEquals(EXPECTED_STRINGS[i++], move.toString());
        }
    }

}
