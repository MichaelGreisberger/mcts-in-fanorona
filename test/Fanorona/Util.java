package Fanorona;

import Fanorona.Board.BoardSize;
import Fanorona.Player.PlayerWrapper;
import Fanorona.Player.RandomMctsPlayer;

import java.io.IOException;
import java.util.LinkedList;

public class Util {

    public static String getRandomLegalState(int rounds, BoardSize size) throws IOException {
        Game game = new Game(size, new LinkedList<>(), false, false);
        game.player1 = new PlayerWrapper(new  RandomMctsPlayer(0, false), "r1", "white");
        game.player2 = new PlayerWrapper(new  RandomMctsPlayer(0, false), "r2", "black");

        game.startGame(rounds);

        return game.board.getStateB64();
    }
}
