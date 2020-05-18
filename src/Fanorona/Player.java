package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

public interface Player {
    Move getNextMove();
    String getName();
    void setBoard(Board board);
    void reset();

    //TODO: Delete this!
    void incWin();
    int getWins();
}
