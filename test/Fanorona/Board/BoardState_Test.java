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

//    @Test
//    void twoFiveDrawExample() {
//        Board board = new Board(BoardSize.LARGE, BoardStateExamples.TwoFiveDrawExample_char);
//        Player p1 = new SteeredMaterialPlayer(5000, true, "puct");
//        Player p2 = new SteeredMaterialPlayer(5000, true, "puct");
//        Game game = new Game(board, p1, p2);
//        game.startGame();
//    }


//    @Test
//    void test() {
//        RandomMctsPlayer random = new RandomMctsPlayer(1000, true);
//        SteeredMctsPlayer uct = new SteeredMctsPlayer(1000, true);
//        try {
//            System.out.println(Util.getRandomLegalState(10, BoardSize.LARGE));
//            System.out.println(Util.getRandomLegalState(10, BoardSize.LARGE));
//            for (int i = 5; i < 8; i++) {
//                String state = Util.getRandomLegalState(i, BoardSize.LARGE);
//                Board board = Board.fromB64(state, BoardSize.LARGE);
//                System.out.println("---------------------------------------   " + i + "   ---------------------------------------------------");
//                System.out.println(board.toPrintString());
//                System.out.println("\n\nrandom player");
//                Move randMove = random.getNextMove(board);
//
//                System.out.println("\n\nuct player");
//                Move uctMove = uct.getNextMove(board);
//                System.out.println("!!!!     uct: " + uctMove + " rand; " + randMove + "    !!!!");
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }



}
