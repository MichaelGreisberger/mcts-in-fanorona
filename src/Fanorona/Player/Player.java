package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.IOException;

/**
 * This interface defines the method's a Fanorona player needs to play the game.
 */
public interface Player {
    /**
     * @return the next move the player wants to make with respect to the {@param board}
     * @param board the board for which a move should be returned
     */
    Move getNextMove(Board board) throws IOException;

    Player reset();

    int getNumberOfStoredStates();

    int getNumberOfSimulatedGames();

    void shutDown();
}
