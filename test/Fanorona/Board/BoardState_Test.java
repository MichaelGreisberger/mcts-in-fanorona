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

}
