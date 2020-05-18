package Fanorona.Board;

import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.mcts.RandomMctsPlayer;
import Fanorona.mcts.RandomPlayerGameStateStatistic;
import Fanorona.mcts.RandomPlayerGameStateStatistic2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Board_Test {

    private final String BOARD_LARGE_PRINT_STRING = "  a   b   c   d   e   f   g   h   i\n" +
            "0 B - B - B - B - B - B - B - B - B\n" +
            "  | \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "1 B - B - B - B - B - B - B - B - B\n" +
            "  | / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "2 B - W - B - W -   - B - W - B - W\n" +
            "  | \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "3 W - W - W - W - W - W - W - W - W\n" +
            "  | / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "4 W - W - W - W - W - W - W - W - W\n";
    private final String BOARD_MEDIUM_PRINT_STRING = "  a   b   c   d   e\n" +
            "0 B - B - B - B - B\n" +
            "  | \\ | / | \\ | / |\n" +
            "1 B - B - B - B - B\n" +
            "  | / | \\ | / | \\ |\n" +
            "2 B - W -   - B - W\n" +
            "  | \\ | / | \\ | / |\n" +
            "3 W - W - W - W - W\n" +
            "  | / | \\ | / | \\ |\n" +
            "4 W - W - W - W - W\n";
    private final String BOARD_SMALL_PRINT_STRING = "  a   b   c\n" +
            "0 B - B - B\n" +
            "  | \\ | / |\n" +
            "1 W -   - B\n" +
            "  | / | \\ |\n" +
            "2 W - W - W\n";

    private final String BOARD_EXAMPLE_STATE_1_PRINTSTRING = "  a   b   c   d   e   f   g   h   i\n" +
            "0   - B -   - B -   - B -   - B -  \n" +
            "  | \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "1 B -   -   -   -   - B -   -   - B\n" +
            "  | / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "2 B -   - W -   -   -   - W -   - W\n" +
            "  | \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "3 W - W -   - B -   -   -   -   - W\n" +
            "  | / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "4 W - W -   -   -   -   -   - W -  \n";
    private final String BOARD_EXAMPLE_STATE_2_PRINTSTRING = "  a   b   c   d   e   f   g   h   i\n" +
            "0 B - B - B - B - B - B -   - B - B\n" +
            "  | \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "1 B - B - B - B - B -   - B - B - B\n" +
            "  | / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "2 B - W - B - W - W - B - W - B - W\n" +
            "  | \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "3 W - W - W -   - W - W - W - W - W\n" +
            "  | / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "4 W - W - W - W - W - W - W - W - W\n";

    @Test
    void boardPositionSmallBlack_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(2, board.getState().getPosition(0, 0));
    }

    @Test
    void boardPositionSmallWhite_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(1, board.getState().getPosition(2, 2));
    }

    @Test
    void boardPositionSmallNone_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(0, board.getState().getPosition(1, 1));
    }

    @Test
    void boardPositionMediumBlack_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(2, board.getState().getPosition(0, 2));
    }

    @Test
    void boardPositionMediumWhite_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(1, board.getState().getPosition(1, 2));
    }

    @Test
    void boardPositionMediumNone_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(0, board.getState().getPosition(2, 2));
    }

    @Test
    void boardPositionLargeBlack_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(2, board.getState().getPosition(0, 2));
    }

    @Test
    void boardPositionLargeWhite_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(1, board.getState().getPosition(1, 2));
    }

    @Test
    void boardPositionLargeNone_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(0, board.getState().getPosition(4, 2));
    }

    @Test
    void boardPrintStringLarge_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(BOARD_LARGE_PRINT_STRING, board.toPrintString());
    }

    @Test
    void boardPrintStringMedium_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(BOARD_MEDIUM_PRINT_STRING, board.toPrintString());
    }

    @Test
    void boardPrintStringSmall_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(BOARD_SMALL_PRINT_STRING, board.toPrintString());
    }

    @Test
    void boardPrintStringExampleState1_success() {
        Board board = new Board(BoardSize.large, BoardStateExamples.EXAMPLE_STATE_1);
        assertEquals(BOARD_EXAMPLE_STATE_1_PRINTSTRING, board.toPrintString());
    }


    @Test
    void boardPrintStringExampleState2_success() {
        Board board = new Board(BoardSize.large, BoardStateExamples.EXAMPLE_STATE_2);
        assertEquals(BOARD_EXAMPLE_STATE_2_PRINTSTRING, board.toPrintString());
    }


    @Test
    void boardPossibleMovesExampleState1_success() {
        Board board = new Board(BoardSize.large, BoardStateExamples.EXAMPLE_STATE_1);
        System.out.println(board.toPrintString());
        MoveList actualList = board.getPossibleMoves();
        String expected = "f1e0W, f1e0Wd1A, i1i0W, i1i0Wh1A, a2b2A, a2b2Ab1W, d3c3A, d3c3Ac4W, d3c3Ac4Wd4W, d3e4W, d3e4Wf3A, ";
        assertEquals(11, actualList.size());
        for (Move move : actualList) {
            assertTrue(expected.contains(move.toString()));
        }
    }

    @Test
    void boardPossibleMovesExampleStateS64_1_success() {
        Board board = Board.fromB64(BoardStateExamples.EXMPLE_STATE_S64_1, BoardSize.large);
        board.applyMove(new Move("c3c2Ab2A"));
        System.out.println(board.toPrintString());
        System.out.println(board.getStateB64());
        System.out.println(board.getPossibleMoves());
    }

    @Test
    void TEST() {
        Board board = Board.fromB64("QgQAABAEARAAAAEV", BoardSize.large);
//        board.applyMove(new Move("c3c2Ab2A"));
        RandomMctsPlayer player = new RandomMctsPlayer(board,  5000, "TestAI", new RandomPlayerGameStateStatistic());
        System.out.println(player.getNextMove());
        MoveList possibleMoves = board.getPossibleMoves();
        for (Move move : possibleMoves) {
            System.out.println(move + " " + player.getStatistics(move));
        }
        System.out.println(board.toPrintString());
        System.out.println(board.getStateB64());
        System.out.println(board.getPossibleMoves());
    }

    /*
    White's turn
  a   b   c   d   e   f   g   h   i
0   -   -   -   -   -   -   -   -
  | \ | / | \ | / | \ | / | \ | / |
1   -   - B - B -   - W - B - W - B
  | / | \ | / | \ | / | \ | / | \ |
2   -   -   -   -   -   -   -   -
  | \ | / | \ | / | \ | / | \ | / |
3   - W -   - B - B -   - B -   -
  | / | \ | / | \ | / | \ | / | \ |
4   -   -   -   -   -   -   -   -

     */
    @Test
    public void boardPossibleMovesPaperExampleState_success() {
        Board board = new Board(BoardSize.large, BoardStateExamples.PAPER_EXAMPLE_STATE);
        MoveList actualList = board.getPossibleMoves();
        String expected = "f1e2A, f1e2Ae1W, f1e2Af3W, f1e1A, f1e1Ae2A, f1e1W, f1e1We2A, f1e1We2Af3W, f1e1We2Af3We3A, f1e1We2Af3We3W, b3c2A, b3c2Ac3W, b3c2Ab1W, b3c2Ab1Wa1W, b3c3A, b3c3Ac2A, ";
        assertEquals(16, actualList.size());
        for (Move move : actualList) {
            assertTrue(expected.contains(move.toString()));
        }
    }

    @Test
    public void boardApplyMoveApproach_success() {
        Board board = Board.fromB64(BoardStateExamples.APPROACH_BEFORE_B64, BoardSize.large);
        System.out.println(board.toPrintString());
        Move move = new Move(BoardStateExamples.APPROACH_ACTION);
        System.out.println(move);
        board = board.applyMove(move);
        System.out.println(board.toPrintString());
        assertEquals(BoardStateExamples.APPROACH_AFTER_B64, board.getStateB64());
    }

    @Test
    public void boardApplyMoveWithdrawal_success() {
        Board board = Board.fromB64(BoardStateExamples.WITHDRAW_BEFORE_B64, BoardSize.large);
        System.out.println(board.toPrintString());
        Move move = new Move(BoardStateExamples.WITHDRAW_ACTION);
        System.out.println(move);
        board = board.applyMove(move);
        System.out.println(board.toPrintString());
        assertEquals(BoardStateExamples.WITHDRAW_AFTER_B64, board.getStateB64());
    }

    @Test
    public void boardApplyMoveExtendedCapture_success() {
        Board board = Board.fromB64(BoardStateExamples.EXTENDED_BEFORE_B64, BoardSize.large);
        System.out.println(board.toPrintString());
        Move move = new Move(BoardStateExamples.EXTENDED_ACTION);
        System.out.println(move);
        board = board.applyMove(move);
        System.out.println(board.toPrintString());
        assertEquals(BoardStateExamples.EXTENDED_AFTER_B64, board.getStateB64());
    }

    @Test
    public void boardApplyMovePaika_success() {
        Board board = Board.fromB64(BoardStateExamples.PAIKA_BEFORE_B64, BoardSize.large);
        System.out.println(board.toPrintString());
        Move move = new Move(BoardStateExamples.PAIKA_ACTION);
        System.out.println(move);
        board = board.applyMove(move);
        System.out.println(board.toPrintString());
        assertEquals(BoardStateExamples.PAIKA_AFTER_B64, board.getStateB64());
    }

    @Test
    public void boardFromB64_success() {
        Board board = new Board(BoardSize.large, BoardStateExamples.PAPER_EXAMPLE_STATE);
        String expected = board.toPrintString();
        String s64 = board.getStateB64();
        System.out.println(s64);
        board = Board.fromB64(s64, BoardSize.large);
        String actual = board.toPrintString();
        assertEquals(expected, actual);
    }

}
