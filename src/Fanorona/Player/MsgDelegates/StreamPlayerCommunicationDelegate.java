package Fanorona.Player.MsgDelegates;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This interface provides methods to construct and parse messages sent between a StreamPlayer and its counterpart.
 */
public interface StreamPlayerCommunicationDelegate {
    String getNextMoveMsg(Board board);

    Move parseMoveMsg(BufferedReader in) throws IOException;

    String getIllegalMoveMsg(Board board);
}
