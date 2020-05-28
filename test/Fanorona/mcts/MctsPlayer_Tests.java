package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Board.BoardStateExamples;
import Fanorona.Player;
import org.junit.jupiter.api.Test;

class MctsPlayer_Tests {
    private final int MILLIS_TO_RUN = 500;

    @Test
    void RandomMctsPlayer_getNextMove_noFailure() {
        Player player = new RandomMctsPlayer(MILLIS_TO_RUN,  true);
        player.getNextMove(Board.fromB64(BoardStateExamples.EXMPLE_STATE_S64_1, BoardSize.LARGE));
    }

    @Test
    void UctMctsPlayer_getNextMove_noFailure() {
        Player player = new UctMctsPlayer(MILLIS_TO_RUN,  true);
        player.getNextMove(Board.fromB64(BoardStateExamples.EXMPLE_STATE_S64_1, BoardSize.LARGE));
    }

}
