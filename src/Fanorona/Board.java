package Fanorona;
import Fanorona.util.BoardSize;
import Fanorona.util.MoveType;

import java.awt.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int X_OFFSET = 3; //offset of positions (1 position = 2bits) previous to board-state-data (same for all sizes)
    private BoardSize size;
    private char[] cState;

    public Board(BoardSize size, String state) {
        this(size, state.toCharArray());
    }

    public Board(BoardSize size, char[] state) {
        this.size = size;
        this.cState = state;
    }

    public Board(BoardSize size) {
        this.size = size;
        cState = getInitialState().toCharArray();
    }

    public String getInitialState() {
        switch (size) {
            case small:
                return getInitialStateSmall();
            case medium:
                return getInitialStateMedium();
            case large:
                return getInitialStateLarge();
            default:
                throw new IllegalArgumentException("Illegal boardsize: " + size);
        }
    }

    public MoveList getExtendedCaptureMoves(Move prevMove, int player, int opponent) {
        Board newBoard = new Board(size, cState.clone());
        MoveList newMoves = new MoveList();
//        LinkedList<Move> newMoves = new LinkedList<>();
        Point currentPosition = prevMove.getLastToPositon();
        newBoard.move(prevMove.getLastFromPositon(), currentPosition, prevMove.getLastMoveType(), player, opponent);

        Move newMove;

        for (Point emptyNeighbour : newBoard.getEmptyNeigbours(currentPosition.x, currentPosition.y)) {
            if (isOpponentInDirection(currentPosition.x, currentPosition.y, emptyNeighbour, opponent)) {
                newMove = prevMove.extendCapture(emptyNeighbour, MoveType.approach);
                if (newMove.appliesToRules()) newMoves.append(newMove);
            }
            if (isOpponentAgainstDirection(currentPosition.x, currentPosition.y, emptyNeighbour, opponent)) {
                newMove = prevMove.extendCapture(emptyNeighbour, MoveType.withdraw);
                if (newMove.appliesToRules()) newMoves.append(newMove);
            }
        }

        if (!newMoves.isEmpty()) {
            MoveList newNewMoves = new MoveList();
//            LinkedList<Move> newNewMoves = new LinkedList<>();
            for (Move move : newMoves) {
                newNewMoves.append(newBoard.getExtendedCaptureMoves(move, player, opponent));
            }
            newMoves.append(newNewMoves);
        }
        return newMoves;
    }

    public MoveList getPossibleMoves() {
        int currPlayer = getCurrentPlayer();
        int opponent = currPlayer ^ 3;
//        List<Fanorona.Move> paikaMoves = new LinkedList<>();
//        List<Fanorona.Move> captureMoves = new LinkedList<>();
        MoveList paikaMoves = new MoveList();
        MoveList captureMoves = new MoveList();
        for (int y = 0; y < size.y(); y++) {
            for (int x = 0; x < size.x(); x++) {
                if (getPosition(x, y) == currPlayer) {
                    List<Point> neighbours = getEmptyNeigbours(x, y);
                    for (Point point : neighbours) {
                        if (isOpponentInDirection(x, y, point, opponent)) {
                            Move move = new Move(x, y, point, MoveType.approach);
                            captureMoves.append(move);
                            captureMoves.append(getExtendedCaptureMoves(move, currPlayer, opponent));
                        }
                        if (isOpponentAgainstDirection(x, y, point, opponent)) {
                            Move move = new Move(x, y, point, MoveType.withdraw);
                            captureMoves.append(move);
                            captureMoves.append(getExtendedCaptureMoves(move, currPlayer, opponent));
                        }
                        if (captureMoves.isEmpty()) {
                            paikaMoves.append(new Move(x, y, point.x, point.y, MoveType.paika));
                        }
                    }
                }
            }
        }
        if (captureMoves.isEmpty()) {
            return paikaMoves;
        } else {
            return captureMoves;
        }
    }

    public void applyMove(Move move) {
        MoveType[] types = move.getTypes();
        Point[] nodes = move.getNodes();
        int player = getCurrentPlayer();
        int opponent = player ^ 3;

        for (int i = 1; i < nodes.length; i++) {
            move(nodes[i - 1], nodes[i], types[i - 1], player, opponent);
//            System.out.println(toPrintString());
        }

        setCurrentPlayer(opponent);

    }

    public void move(Point from, Point to, MoveType type, int player, int opponent) {
        int dirX;
        int dirY;
        switch (type) {
            case approach:
                dirX = to.x - from.x;
                dirY = to.y - from.y;
                capture(to.x + dirX, to.y + dirY, dirX, dirY, opponent);
                setPosition(from.x, from.y, 0);
                setPosition(to.x, to.y, player);
                break;
            case withdraw:
                dirX = from.x - to.x;
                dirY = from.y - to.y;
                capture(to.x + dirX, to.y + dirY, dirX, dirY, opponent);
                setPosition(from.x, from.y, 0);
                setPosition(to.x, to.y, player);
                break;
            case paika:
                setPosition(from.x, from.y, 0);
                setPosition(to.x, to.y, player);
        }
    }

    public void capture(int x, int y, int dirX, int dirY, int opponent) {
        setPosition(x, y, 0);
        int nextX = x + dirX;
        int nextY = y + dirY;
        if (isInBoardSpace(nextX, nextY) && getPosition(nextX, nextY) == opponent) {
            capture(nextX, nextY, dirX, dirY, opponent);
        }
    }

    public void setCurrentPlayer(int player) {
        int clearMask = ~(3 << 6);
        cState[0] = (char) (cState[0] & clearMask | (player << 6));
    }

    private boolean isOpponentInDirection(int fromX, int fromY, Point direction, int opponent) {
        int targetX = fromX + (direction.x - fromX) * 2;
        int targetY = fromY + (direction.y - fromY) * 2;
        if (isInBoardSpace(targetX, targetY)) {
            return getPosition(targetX, targetY) == opponent;
        } else {
            return false;
        }
    }

    private boolean isOpponentAgainstDirection(int fromX, int fromY, Point direction, int opponent) {
        int targetX = fromX - (direction.x - fromX);
        int targetY = fromY - (direction.y - fromY);
        if (isInBoardSpace(targetX, targetY)) {
            return getPosition(targetX, targetY) == opponent;
        } else {
            return false;
        }
    }

    public List<Point> getEmptyNeigbours(int x, int y) {
        List<Point> neigbours = new LinkedList<>();
        for (Point point : getSurroundingNodes(x, y)) {
            if (getPosition(point.x, point.y) == 0) {
                neigbours.add(point);
            }
        }
        return neigbours;
    }

    public List<Point> getSurroundingNodes(int x, int y) {
        if (isStrongNode(x, y)) {
            return getAightNeigbours(x, y);
        } else {
            return getFourNeigbours(x, y);
        }
    }

    private List<Point> getAightNeigbours(int x, int y) {
        List<Point> neighbours = new LinkedList<>();
        if (isInBoardSpace(x, y - 1)) neighbours.add(new Point(x, y - 1));
        if (isInBoardSpace(x + 1, y - 1)) neighbours.add(new Point(x + 1, y - 1));
        if (isInBoardSpace(x + 1, y)) neighbours.add(new Point(x + 1, y));
        if (isInBoardSpace(x + 1, y + 1)) neighbours.add(new Point(x + 1, y + 1));
        if (isInBoardSpace(x, y + 1)) neighbours.add(new Point(x, y + 1));
        if (isInBoardSpace(x - 1, y + 1)) neighbours.add(new Point(x - 1, y + 1));
        if (isInBoardSpace(x - 1, y)) neighbours.add(new Point(x - 1, y));
        if (isInBoardSpace(x - 1, y - 1)) neighbours.add(new Point(x - 1, y - 1));
        return neighbours;
    }

    private List<Point> getFourNeigbours(int x, int y) {
        List<Point> neighbours = new LinkedList<>();
        if (isInBoardSpace(x, y - 1)) neighbours.add(new Point(x, y - 1));
        if (isInBoardSpace(x + 1, y)) neighbours.add(new Point(x + 1, y));
        if (isInBoardSpace(x, y + 1)) neighbours.add(new Point(x, y + 1));
        if (isInBoardSpace(x - 1, y)) neighbours.add(new Point(x - 1, y));
        return neighbours;
    }

    private boolean isStrongNode(int x, int y) {
        return (x + y * size.x()) % 2 == 0;
    }

    private boolean isInBoardSpace(int x, int y) {
        return x >= 0 && x < size.x() && y >= 0 && y < size.y();
    }

    public void setPosition(Point position, int newVal) {
        setPosition(position.x, position.y, newVal);
    }

    public void setPosition(int x, int y, int newVal) {
        double temp = (double) (x + X_OFFSET + y * size.x()) / 4;
        int position = (int) temp;
        int shift = (int) ((0.75 - (temp - (int) temp)) * 8);
        int clearMask = ~(3 << shift);
        cState[position] = (char) (cState[position] & clearMask | (newVal << shift));
    }

    public int getPosition(Point position) {
        return getPosition(position.x, position.y);
    }

    public int getPosition(int x, int y) {

        double temp = (double) (x + X_OFFSET + y * size.x()) / 4;
        int position = (int) temp;
        int shift = (int) ((0.75 - (temp - (int) temp)) * 8);
        int ret = (cState[position] >> shift) & 3;
//        System.out.println(String.format("x: %d, y: %d, pos: %d, shift: %d, ret: %d, temp: %2f", x, y, position, shift, ret, temp));
        return ret;
    }

    public String getStateB64() {
        return Base64.getEncoder().encodeToString(new String(cState).getBytes());
    }

    public static Board fromB64(String s64, BoardSize size) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bState = decoder.decode(s64);
        return new Board(size, new String(bState));
    }

    /**
     * determines the current player (first 2 bit of state)
     *
     * @return 1 for player one (white) 2 for player 2 (black)
     */
    public int getCurrentPlayer() {
        return cState[0] >> 6;
    }

    public String toPrintString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCurrentPlayer() == 1 ? "White's turn\n" : "Black's turn\n");
        sb.append(getPrintHeader());
        int xlength = size.x() * 2 - 1; //x nodes, x-1 spaces in between
        int ylength = size.y() * 2 - 1;
        for (int y = 0; y < ylength; y++) {
            sb.append(y % 2 == 0 ? y / 2 + " " : "  ");
            for (int x = 0; x < xlength; x++) {
                if (x % 2 == 0) {
                    if (y % 2 == 0) {
                        int pos = getPosition(x / 2, y / 2);
//                        sb.append(pos == 0 ? " " : pos == 1 ? "\u25CF" : "\u25CB");
                        sb.append(pos == 0 ? " " : pos == 1 ? "W" : "B");
                    } else {
                        sb.append("|");
                    }
                } else {
                    if (y % 2 == 0) {
                        sb.append(" - ");
                    } else {
                        sb.append((x + y) % 4 == 0 ? " / " : " \\ ");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getPrintHeader() {
        switch (size) {
            case large:
                return "  a   b   c   d   e   f   g   h   i\n";
            case medium:
                return "  a   b   c   d   e\n";
            case small:
                return "  a   b   c\n";
            default:
                return "Unknown Boardsize";
        }

    }

    private String getInitialStateSmall() {
        return "B¤\u0095";
        /*
        01000010
        10100100
        10010101
         */
    }

    private String getInitialStateMedium() {
        return "Bªª¤\u0095UU";
        /*
        01000010
        10101010
        10101010
        10100100
        10010101
        01010101
        01010101
         */
    }

    private String getInitialStateLarge() {
        return "Bªªªª¦I\u0095UUUU";
        /*
        01000010
        10101010
        10101010
        10101010
        10101010
        10100110
        01001001
        10010101
        01010101
        01010101
        01010101
        01010101
         */
    }

}
