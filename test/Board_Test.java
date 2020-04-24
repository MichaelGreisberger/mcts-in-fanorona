import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import util.BoardSize;

public class Board_Test {

    private final String BOARD_LARGE_PRINT_STRING = "White's turn\n" +
            "2 - 2 - 2 - 2 - 2 - 2 - 2 - 2 - 2\n" +
            "| \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "2 - 2 - 2 - 2 - 2 - 2 - 2 - 2 - 2\n" +
            "| / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "2 - 1 - 2 - 1 - 0 - 2 - 1 - 2 - 1\n" +
            "| \\ | / | \\ | / | \\ | / | \\ | / |\n" +
            "1 - 1 - 1 - 1 - 1 - 1 - 1 - 1 - 1\n" +
            "| / | \\ | / | \\ | / | \\ | / | \\ |\n" +
            "1 - 1 - 1 - 1 - 1 - 1 - 1 - 1 - 1\n";
    private final String BOARD_MEDIUM_PRINT_STRING = "White's turn\n" +
            "2 - 2 - 2 - 2 - 2\n" +
            "| \\ | / | \\ | / |\n" +
            "2 - 2 - 2 - 2 - 2\n" +
            "| / | \\ | / | \\ |\n" +
            "2 - 1 - 0 - 2 - 1\n" +
            "| \\ | / | \\ | / |\n" +
            "1 - 1 - 1 - 1 - 1\n" +
            "| / | \\ | / | \\ |\n" +
            "1 - 1 - 1 - 1 - 1\n";
    private final String BOARD_SMALL_PRINT_STRING = "White's turn\n" +
            "2 - 2 - 2\n" +
            "| \\ | / |\n" +
            "1 - 0 - 2\n" +
            "| / | \\ |\n" +
            "1 - 1 - 1\n";
    @Test
    public void boardPositionSmallBlack_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(2, board.getPosition(0,0));
    }
    @Test
    public void boardPositionSmallWhite_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(1, board.getPosition(2,2));
    }
    @Test
    public void boardPositionSmallNone_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(0, board.getPosition(1,1));
    }
    @Test
    public void boardPositionMediumBlack_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(2, board.getPosition(0,2));
    }
    @Test
    public void boardPositionMediumWhite_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(1, board.getPosition(1,2));
    }
    @Test
    public void boardPositionMediumNone_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(0, board.getPosition(2,2));
    }
    @Test
    public void boardPositionLargeBlack_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(2, board.getPosition(0,2));
    }
    @Test
    public void boardPositionLargeWhite_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(1, board.getPosition(1,2));
    }
    @Test
    public void boardPositionLargeNone_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(0, board.getPosition(4,2));
    }

    @Test
    public void boardPrintStringLarge_success() {
        Board board = new Board(BoardSize.large);
        assertEquals(BOARD_LARGE_PRINT_STRING, board.toPrintString());
    }
    @Test
    public void boardPrintStringMedium_success() {
        Board board = new Board(BoardSize.medium);
        assertEquals(BOARD_MEDIUM_PRINT_STRING, board.toPrintString());
    }
    @Test
    public void boardPrintStringSmall_success() {
        Board board = new Board(BoardSize.small);
        assertEquals(BOARD_SMALL_PRINT_STRING, board.toPrintString());
    }
}
