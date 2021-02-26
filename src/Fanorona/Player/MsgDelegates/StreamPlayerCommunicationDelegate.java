package Fanorona.Player.MsgDelegates;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This interface provides methods to construct and parse messages sent between a StreamPlayer and its counterpart
 */
public interface StreamPlayerCommunicationDelegate {

    /**
     * Creates a message asking the opponent for his next move
     *
     * @param board used to retrieve information necessary to ask for the next move
     * @return A message to ask the opponent for his next move
     */
    String getNextMoveMsg(Board board);

    /**
     * Parses the move communicated through the provided stream
     *
     * @param in a stream of data containing the next move of an opponent
     * @return The move of the opponent
     * @throws IOException if something goes wrong while reading the stream
     */
    Move parseMoveMsg(BufferedReader in) throws IOException;

    /**
     * Message that indicates that an opponents move was not legit
     *
     * @param board The board to which a move should be applied to
     * @return A message indicating that a move was not legit
     */
    String getIllegalMoveMsg(Board board);
}
