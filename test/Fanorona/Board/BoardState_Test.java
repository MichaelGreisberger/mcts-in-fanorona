package Fanorona.Board;

import Fanorona.Move.Move;
import Fanorona.Player.RandomMctsPlayer;
import Fanorona.Player.SteeredMctsPlayer;
import Fanorona.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BoardState_Test {


    @Test
    void SetCurrentPlayer_success() {
        BoardState state = new BoardState(BoardSize.LARGE);
        state.setCurrentPlayer(2);

        Assertions.assertEquals("woLCqsKqwqrCqsKmScKVVVVVVQ==", state.getStateB64());
    }

    @Test
    void test() {
        RandomMctsPlayer random = new RandomMctsPlayer(1000, true);
        SteeredMctsPlayer uct = new SteeredMctsPlayer(1000, true);
        try {
            System.out.println(Util.getRandomLegalState(10, BoardSize.LARGE));
            System.out.println(Util.getRandomLegalState(10, BoardSize.LARGE));
            for (int i = 5; i < 8; i++) {
                String state = Util.getRandomLegalState(i, BoardSize.LARGE);
                Board board = Board.fromB64(state, BoardSize.LARGE);
                System.out.println("---------------------------------------   " + i + "   ---------------------------------------------------");
                System.out.println(board.toPrintString());
                System.out.println("\n\nrandom player");
                Move randMove = random.getNextMove(board);

                System.out.println("\n\nuct player");
                Move uctMove = uct.getNextMove(board);
                System.out.println("!!!!     uct: " + uctMove + " rand; " + randMove + "    !!!!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
