package Fanorona.Board;

import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Move.MoveType;

import java.awt.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Board {
//    private static Map<String, MoveList> store = new HashMap<>();
    private BoardState state;
    private String s64;

    public BoardState getState() {
        return state;
    }

    public Board(BoardSize size, String state) {
        this(size, state.toCharArray());
    }

    public Board(BoardSize size, char[] state) {
        this(new BoardState(size, state));
    }

    public Board(BoardSize size) {
        this(new BoardState(size));
    }

    public Board(BoardState state) {
        this.state = state;
    }


    /**
     * Searches for all possible extended capture moves from the previous move
     *
     * @param prevMove the move to extend
     * @param player   the current player
     * @param opponent the current opponent
     * @return all possible extended capture moves to the previous move
     */
    public MoveList getExtendedCaptureMoves(Move prevMove, int player, int opponent) {
        Board newBoard = new Board(state.getCopy());
        MoveList newMoves = new MoveList();
        Point currentPosition = prevMove.getLastToPosition();
        newBoard.move(prevMove.getLastFromPosition(), currentPosition, prevMove.getLastMoveType(), player, opponent);

        Move newMove;

        for (Point emptyNeighbour : newBoard.getEmptyNeighbours(currentPosition.x, currentPosition.y)) {
            if (isOpponentInDirection(currentPosition, emptyNeighbour, opponent)) {
                newMove = prevMove.extendCapture(emptyNeighbour, MoveType.approach);
                if (newMove.appliesToRules()) newMoves.append(newMove);
            }
            if (isOpponentAgainstDirection(currentPosition, emptyNeighbour, opponent)) {
                newMove = prevMove.extendCapture(emptyNeighbour, MoveType.withdraw);
                if (newMove.appliesToRules()) newMoves.append(newMove);
            }
        }

        if (!newMoves.isEmpty()) {
            MoveList newNewMoves = new MoveList();
            for (Move move : newMoves) {
                newNewMoves.append(newBoard.getExtendedCaptureMoves(move, player, opponent));
            }
            newMoves.append(newNewMoves);
        }
        return newMoves;
    }

    /**
     * Searches for all possible moves for the current board state. If there are no capture moves possible then
     * all possible paika moves are returned. If there are capture moves possible then this method searches for possible
     * extended capture moves. It returns all variations of capture and extended capture moves. For example if there is
     * a capture move like f1f2A and an extended capture move f1f2Ae2W then both moves are returned.
     *
     * @return All possible moves for the current state of the Board according to the rules.
     */
    public MoveList getPossibleMoves() {
//        String s64 = getStateB64();
//        if (!store.containsKey(s64)) {
//            store.put(getStateB64(),calculatePossibleMoves());
//        }
//        return store.get(s64);
        return calculatePossibleMoves();
    }

    private MoveList calculatePossibleMoves() {
        int currPlayer = state.getCurrentPlayer();
        int opponent = currPlayer ^ 3;
        MoveList paikaMoves = new MoveList();
        MoveList captureMoves = new MoveList();
        for (int y = 0; y < state.getSize().y(); y++) {
            for (int x = 0; x < state.getSize().x(); x++) {
                Point position = new Point(x,y);
                if (state.getPosition(position) == currPlayer) {
                    List<Point> neighbours = getEmptyNeighbours(x, y);
                    for (Point neighbour : neighbours) {
                        if (isOpponentInDirection(position, neighbour, opponent)) {
                            Move move = new Move(position, neighbour, MoveType.approach);
                            captureMoves.append(move);
                            captureMoves.append(getExtendedCaptureMoves(move, currPlayer, opponent));
                        }
                        if (isOpponentAgainstDirection(position, neighbour, opponent)) {
                            Move move = new Move(position, neighbour, MoveType.withdraw);
                            captureMoves.append(move);
                            captureMoves.append(getExtendedCaptureMoves(move, currPlayer, opponent));
                        }
                        if (captureMoves.isEmpty()) {
                            paikaMoves.append(new Move(position, neighbour, MoveType.paika));
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

    /**
     * Executes the given move. Moves the piece and captures all enemy pieces.
     * This method can execute all types of moves (approach, withdrawal, paika, extended capturing)
     *
     * @param move the move to execute
     */
    public void applyMove(Move move) {
        s64 = null;
        MoveType[] types = move.getTypes();
        Point[] nodes = move.getNodes();
        int player = state.getCurrentPlayer();
        int opponent = player ^ 3;

        for (int i = 1; i < nodes.length; i++) {
            move(nodes[i - 1], nodes[i], types[i - 1], player, opponent);
        }
        state.setCurrentPlayer(opponent);
    }

    /**
     * Executes a single move (for example one part of an extended capture move)
     *
     * @param from     starting position of this move
     * @param to       ending position of this move
     * @param type     type of this move (approach, withdrawal, paika)
     * @param player   the current player
     * @param opponent the opponent player
     */
    private void move(Point from, Point to, MoveType type, int player, int opponent) {
        int dirX;
        int dirY;
        switch (type) {
            case approach:
                dirX = to.x - from.x;
                dirY = to.y - from.y;
                capture(to.x + dirX, to.y + dirY, dirX, dirY, opponent);
                break;
            case withdraw:
                dirX = from.x - to.x;
                dirY = from.y - to.y;
                capture(to.x + dirX, to.y + dirY, dirX, dirY, opponent);
                break;
        }

        state.setPosition(from, 0);
        state.setPosition(to, player);
    }


    /**
     * captures the position specified by the parameters x and y. Then evaluates the next position in
     * the direction specified with the parameters dirX and dirY and captures the position if it was inhabited by an
     * opponent piece.
     *
     * @param x        x coordinate of the position that should be captured
     * @param y        y coordinate of the position that should be captured
     * @param dirX     x direction to look for more opponent pieces to capture
     * @param dirY     y direction to look for more opponent pieces to capture
     * @param opponent pieces to capture
     */
    public void capture(int x, int y, int dirX, int dirY, int opponent) {
        capture(new Point(x,y), dirX, dirY, opponent);
    }

    /**
     * captures the position specified by the parameters x and y. Then evaluates the next position in
     * the direction specified with the parameters dirX and dirY and captures the position if it was inhabited by an
     * opponent piece.
     *
     * @param node     position to capture (the position of this node will change!)
     * @param dirX     x direction to look for more opponent pieces to capture
     * @param dirY     y direction to look for more opponent pieces to capture
     * @param opponent pieces to capture
     */
    private void capture(Point node, int dirX, int dirY, int opponent) {
        state.setPosition(node, 0);
        node.translate(dirX, dirY);
        if (isInBoardSpace(node) && state.getPosition(node) == opponent) {
            capture(node, dirX, dirY, opponent);
        }
    }


    /**
     * Evaluated whether there is an opponent piece in the direction specified by direction.
     *
     * @param from     position from with the current player moves the piece
     * @param to       point to which the current player moves the piece
     * @param opponent opponent player
     * @return true if there is an opponent piece in direction of the move
     */
    private boolean isOpponentInDirection(Point from, Point to, int opponent) {
        int dirX = (to.x - from.x) * 2;
        int dirY = (to.y - from.y) * 2;
        return isOpponent(from.x + dirX, from.y + dirY, opponent);
    }

    /**
     * Evaluated whether there is an opponent piece against the direction specified by direction.
     *
     * @param from     position from with the current player moves the piece
     * @param to       point to which the current player moves the piece
     * @param opponent opponent player
     * @return true if there is an opponent piece against the direction of the move
     */
    private boolean isOpponentAgainstDirection(Point from, Point to, int opponent) {
        int dirX = to.x - from.x;
        int dirY = to.y - from.y;
        return isOpponent(from.x - dirX, from.y - dirY, opponent);
    }

    /**
     * Evaluated whether there is an opponent piece at the specified position.
     *
     * @param x     x coordinate of position to check
     * @param y     y coordinate of position to check
     * @param opponent opponent player
     * @return true if there is an opponent piece at the position {@param node}
     */
    private boolean isOpponent(int x, int y, int opponent) {
        if (isInBoardSpace(x, y)) {
            return state.getPosition(x, y) == opponent;
        } else {
            return false;
        }
    }

    /**
     * Returns a list of points containing all empty positions in the neighbourhood of the position specified by the
     * parameters x and y.
     *
     * @param x x coordinate of the position for which to search empty neighbours
     * @param y y coordinate of the position for which to search empty neighbours
     * @return all empty neighbours of the specified position
     */
    public List<Point> getEmptyNeighbours(int x, int y) {
        List<Point> neighbours = new LinkedList<>();
        for (Point point : getSurroundingNodes(x, y)) {
            if (state.getPosition(point.x, point.y) == 0) {
                neighbours.add(point);
            }
        }
        return neighbours;
    }

    /**
     * Returns a list of points containing all positions in the neighbourhood of the position specified by the
     * parameters x and y.
     *
     * @param x x coordinate of the position for which to search all neighbours
     * @param y y coordinate of the position for which to search all neighbours
     * @return all neighbours of the specified position
     */
    public List<Point> getSurroundingNodes(int x, int y) {
        if (isStrongNode(x, y)) {
            return getEightNeighbours(x, y);
        } else {
            return getFourNeighbours(x, y);
        }
    }

    /**
     * Returns a list containing all eight neighbours of the specified position. This method does not check whether the
     * specified position has eight neighbours or not. It does check whether a possible neighbour is in the boundaries
     * of the board or not.
     *
     * @param x x coordinate of the position for which to return the neighbours
     * @param y y coordinate of the position for which to return the neighbours
     * @return All eight neighbours of the specified position.
     */
    private List<Point> getEightNeighbours(int x, int y) {
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

    /**
     * Returns a list containing all four neighbours of the specified position. This method does not check whether the
     * specified position has more than four neighbours. It does check whether a possible neighbour is in the boundaries
     * of the board or not.
     *
     * @param x x coordinate of the position for which to return the neighbours
     * @param y y coordinate of the position for which to return the neighbours
     * @return All eight neighbours of the specified position.
     */
    private List<Point> getFourNeighbours(int x, int y) {
        List<Point> neighbours = new LinkedList<>();
        if (isInBoardSpace(x, y - 1)) neighbours.add(new Point(x, y - 1));
        if (isInBoardSpace(x + 1, y)) neighbours.add(new Point(x + 1, y));
        if (isInBoardSpace(x, y + 1)) neighbours.add(new Point(x, y + 1));
        if (isInBoardSpace(x - 1, y)) neighbours.add(new Point(x - 1, y));
        return neighbours;
    }

    /**
     * Evaluates whether the specified position is a strong node (with eight neighbours) or not (with four neighbours)
     *
     * @param x x coordinate of the position in question
     * @param y y coordinate of the position in question
     * @return true if this position has eight neighbours and false otherwise
     */
    private boolean isStrongNode(int x, int y) {
        return (x + y * state.getSize().x()) % 2 == 0;
    }

    /**
     * Evaluates whether the specified position is in the boundaries of the board.
     *
     * @param node position in question
     * @return true if this position is in the boundaries of the board and false otherwise
     */
    private boolean isInBoardSpace(Point node) {
        return isInBoardSpace(node.x, node.y);
    }

    /**
     * Evaluates whether the specified position is in the boundaries of the board.
     *
     * @param x x coordinate of the position in question
     * @param y y coordinate of the position in question
     * @return true if this position is in the boundaries of the board and false otherwise
     */
    private boolean isInBoardSpace(int x, int y) {
        return x >= 0 && x < state.getSize().x() && y >= 0 && y < state.getSize().y();
    }

    /**
     * Returns the board initialized with the state specified by the Base64 encoded string s64
     *
     * @param s64  A string representation of the desired board state in Base64 encoding
     * @param size The size of the desired board
     * @returnthe board initialized with the state specified by the Base64 encoded string s64
     */
    public static Board fromB64(String s64, BoardSize size) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bState = decoder.decode(s64);
        return new Board(size, new String(bState).toCharArray());
    }


    public String getStateB64() {
        if (s64 == null) {
            s64 = state.getStateB64();
        }
        return s64;
    }

    public int getCurrentPlayer() {
        return state.getCurrentPlayer();
    }

    public String toPrintString() {
        return state.toPrintString();
    }

    public Board getCopy() {
        return new Board(state.getCopy());
    }
}