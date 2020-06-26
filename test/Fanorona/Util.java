package Fanorona;

import Fanorona.Board.BoardSize;
import Fanorona.mcts.RandomMctsPlayer;

public class Util {

    public static String getRandomLegalState(int rounds, BoardSize size) {
        Game game = new Game(size, false);
        game.player1 = new PlayerWrapper(new  RandomMctsPlayer(0, false), "r1", "white");
        game.player2 = new PlayerWrapper(new  RandomMctsPlayer(0, false), "r2", "black");

        game.startGame(rounds);

        return game.board.getStateB64();
    }
}
