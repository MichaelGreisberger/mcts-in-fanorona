package Fanorona.Player.MsgDelegates;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Provides an interface to communicate with the players provided by Maarten Schadd from Maastricht University.
 * This class is useless unless you have a copy of the binaries provided by Maarten Schadd.
 */
public class SchaddsPlayerCommunicationDelegate implements StreamPlayerCommunicationDelegate {

    @Override
    public String getNextMoveMsg(Board board) {
        Move lastMove = board.getLastMove();
        if (lastMove == null) {
            return "Start";
        } else {
            return lastMove.toSchaddMoveString();
        }
    }

    @Override
    public Move parseMoveMsg(BufferedReader in) throws IOException {
        while (true) {
            String msg = in.readLine().trim();
            if (isMoveString(msg)) {
                msg = msg.replace("-", "");
                if (msg.length() == 4) {
                    return Move.fromSchaddMoveString(msg + "P");
                } else {
                    return Move.fromSchaddMoveString(msg);
                }
            }
        }
    }

    @Override
    public String getIllegalMoveMsg(Board board) {
        return "";
    }

    private boolean isMoveString(String msg) {
        String moveRegex = "\\S\\d(-\\S\\d[WA]?)+";
        return msg.matches(moveRegex);
    }
}
