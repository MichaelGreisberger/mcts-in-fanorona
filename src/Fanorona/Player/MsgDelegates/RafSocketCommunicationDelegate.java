package Fanorona.Player.MsgDelegates;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Move.Move;
import Fanorona.Move.MoveType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * This delegate is used to communicate with Fanorona_raf, an external Fanorona player programmed in Python
 *
 * @link https://github.com/AndryRajoelimanana/Fanorona_raf
 */
public class RafSocketCommunicationDelegate implements StreamPlayerCommunicationDelegate {
    private final int DEPTH;
    private final BoardSize SIZE;

    public RafSocketCommunicationDelegate(int depth, BoardSize size) {
        this.DEPTH = depth;
        SIZE = size;
    }

    @Override
    public String getNextMoveMsg(Board board) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("boardstate", getBoardStateArray(board));
            obj.put("was_capture", !board.getCapturedLastRound().isEmpty());
            obj.put("depth", DEPTH);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while parsing board for communication with raf");
        }
        return obj.toString();
    }

    @Override
    public Move parseMoveMsg(BufferedReader in) throws IOException {
        try {
            JSONObject obj = new JSONObject(in.readLine());
            JSONArray moves = (JSONArray) obj.get("move_log");
            JSONObject moveDict = (JSONObject) obj.get("movedict");
            int from = moves.getInt(0);
            int to = moves.getInt(1);
            Move move = new Move(parseNode(from), parseNode(to), getMoveType(from, to, moveDict));
            for (int i = 2; i < moves.length(); i++) {
                from = to;
                to = moves.getInt(i);
                move = move.extendCapture(parseNode(to), getMoveType(from, to, moveDict));
            }
            return move;
        } catch (JSONException e) {
            e.printStackTrace();
            return new Move(new Point(0, 0), new Point(0, 0), MoveType.PAIKA);
        }
    }

    private Point parseNode(int node) {
        int x = SIZE.x() - node % 10;
        int y = SIZE.y() - node / 10 - 1;
        return new Point(x, y);
    }


    private MoveType getMoveType(int from, int to, JSONObject moveDict) throws JSONException {
        JSONArray dictEntry = (JSONArray) moveDict.get(String.valueOf(to));
        if (dictEntry.toString().equals("[]")) {
            return MoveType.PAIKA;
        } else {
            int approachExample = to + to - from;
            for (int i = 0; i < dictEntry.length(); i++) {
                if (dictEntry.get(i).equals(approachExample)) {
                    return MoveType.APPROACH;
                }
            }
            return MoveType.WITHDRAW;
        }
    }

    @Override
    public String getIllegalMoveMsg(Board board) {
        return getNextMoveMsg(board);
    }

    private JSONArray getBoardStateArray(Board board) {
        JSONArray boardState = new JSONArray();
        for (int row = board.getState().getSize().y() - 1; row >= 0; row--) {
            boardState.put("None");
            for (int column = board.getState().getSize().x() - 1; column >= 0; column--) {
                Point position = new Point(column, row);
                boardState.put(getBoardStateValueForPos(board, position));
            }
        }
        return boardState;
    }

    private String getBoardStateValueForPos(Board board, Point pos) {
        int posValue = board.getState().getPosition(pos);
        switch (posValue) {
            case 0:
                return "none";
            case 1:
                return "one";
            case 2:
                return "two";
            default:
                throw new RuntimeException("Invalid board state!");
        }
    }
}
