package Fanorona.Board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardState_Test {


    @Test
    void SetCurrentPlayer_success() {
        BoardState state = new BoardState(BoardSize.LARGE);
        state.setCurrentPlayer(2);

        Assertions.assertEquals("woLCqsKqwqrCqsKmScKVVVVVVQ==", state.getStateB64());
    }

    @Test
    void CalcPiecesLargeInitial_success() {
        BoardState state = new BoardState(BoardSize.LARGE);
        int expected = 22;
        Assertions.assertEquals(expected, state.countPieces(1));
        Assertions.assertEquals(expected, state.countPieces(2));
    }

    @Test
    void CalcPiecesPaperstateExample_success() {
        BoardState state = new BoardState(BoardSize.LARGE, BoardStateExamples.PAPER_EXAMPLE_STATE.toCharArray());
        int expectedWhite = 3;
        int expectedBlack = 7;
        Assertions.assertEquals(expectedWhite, state.countPieces(1));
        Assertions.assertEquals(expectedBlack, state.countPieces(2));
    }
}
