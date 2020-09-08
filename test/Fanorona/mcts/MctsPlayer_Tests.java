package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Board.BoardStateExamples;
import Fanorona.Player.Player;
import Fanorona.Player.RandomMctsPlayer;
import Fanorona.Player.SteeredMctsPlayer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class MctsPlayer_Tests {
    private final int MILLIS_TO_RUN = 500;

    @Test
    void RandomMctsPlayer_getNextMove_noFailure() {
        Player player = new RandomMctsPlayer(MILLIS_TO_RUN,  true);
        try {
            player.getNextMove(Board.fromB64(BoardStateExamples.EXMPLE_STATE_S64_1, BoardSize.LARGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void UctMctsPlayer_getNextMove_noFailure() {
        Player player = new SteeredMctsPlayer(MILLIS_TO_RUN, true);
        try {
            player.getNextMove(Board.fromB64(BoardStateExamples.EXMPLE_STATE_S64_1, BoardSize.LARGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
