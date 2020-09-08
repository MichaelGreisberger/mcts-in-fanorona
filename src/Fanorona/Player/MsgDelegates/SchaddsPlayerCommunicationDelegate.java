package Fanorona.Player.MsgDelegates;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.BufferedReader;
import java.io.IOException;

public class SchaddsPlayerCommunicationDelegate implements StreamPlayerCommunicationDelegate {

//    private final Process process;
//
//    public SchaddsPlayerCommunicationDelegate(Process process) {
//        this.process = process;
//    }

    @Override
    public String getNextMoveMsg(Board board) {
        Move lastMove = board.getLastMove();
        if (lastMove == null) {
            return "Start";
        } else {
            return board.getLastMove().toSchaddMoveString();
        }
    }

    @Override
    public Move parseMoveMsg(BufferedReader in) throws IOException {
        while (true) {
            String msg = in.readLine().trim();
//            System.out.println(msg);
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
