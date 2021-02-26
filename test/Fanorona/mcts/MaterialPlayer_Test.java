package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Board.BoardStateExamples;
import Fanorona.Move.Move;
import Fanorona.Player.Player;
import Fanorona.Player.SteeredMaterialPlayer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MaterialPlayer_Test {
    private final int MILLIS_TO_RUN = 500;

    @Test
    void MaterialPlayer_BigFailure() {
        Player player = new SteeredMaterialPlayer(MILLIS_TO_RUN, true, 100, 8, "uct");
        try {
            Board board = Board.fromB64(BoardStateExamples.MATERIAL_PLAYER_FAIL_1, BoardSize.LARGE);
            Move move = player.getNextMove(board);
            System.out.println(board.toPrintString());
            System.out.println(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
