package Fanorona.Player.MsgDelegates;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.BufferedReader;
import java.io.IOException;

public class HumanPlayerCommunicationDelegate implements StreamPlayerCommunicationDelegate {
    private StringBuilder sb = new StringBuilder();

    @Override
    public String getNextMoveMsg(Board board) {
        sb.setLength(0);
        sb.append("Your next possible moves are:\n");
        sb.append(board.getPossibleMoves()).append("\n");
        sb.append("Select your move.\n");
        return sb.toString();
    }

    @Override
    public Move parseMoveMsg(BufferedReader in) throws IOException {
        return new Move(in.readLine().trim().replace(",", ""));
    }

    @Override
    public String getIllegalMoveMsg(Board board) {
        return "This was an illegal Move! :O Please select a legal move!\n";
    }
}
