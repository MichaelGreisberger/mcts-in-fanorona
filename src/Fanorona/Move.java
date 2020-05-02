package Fanorona;

import Fanorona.util.MoveType;

import java.awt.*;
import java.util.Enumeration;

public class Move {
    private final Point[] nodes;
    private final MoveType[] types;

    /**
     * Initializes the move as a simple move
     * @param fromX x coordinate of the position from which to move a piece
     * @param fromY y coordinate of the position from which to move a piece
     * @param toX x coordinate of the position to which to move a piece
     * @param toY y coordinate of the position to which to move a piece
     * @param type type of the move
     */
    public Move(int fromX, int fromY, int toX, int toY, MoveType type) {
        this(new Point(fromX, fromY), new Point(toX, toY), type);
    }

    /**
     * Initializes this move from a move string of the form "f2e2A"
     * @param moveString string representation of the move
     */
    public Move(String moveString) {
        char[] chars = moveString.toCharArray();
        int nodeCount = (moveString.length() - 2) / 3 + 1;
        nodes = new Point[nodeCount];
        types = new MoveType[nodeCount-1];
        nodes[0] = new Point(chars[0] - 'a', chars[1] - 48);
        for (int i = 4; i < chars.length; i += 3) {
            nodes[(i - 2) / 3 + 1] = new Point(chars[i - 2] - 'a', chars[i - 1] - 48);
            switch (chars[i]) {
                case 'A':
                    types[(i - 2) / 3] = MoveType.approach;
                    break;
                case 'W':
                    types[(i - 2) / 3] = MoveType.withdraw;
                    break;
                case 'P':
                    types[(i - 2) / 2 - 1] = MoveType.paika;
                    break;
            }
        }
    }

    /**
     * Initializes the move as a simple move
     * @param from position from which to move a piece
     * @param to position to which to move a piece
     * @param type type of the move
     */
    public Move(Point from, Point to, MoveType type) {
        this.nodes = new Point[]{from, to};
        this.types = new MoveType[]{type};
    }

    /**
     * Initializes new extended capture move.
     * @param nodeSeries Series of nodes visited. The first Point must always be the from-node.
     * @param types Type of move.
     */
    public Move(Point[] nodeSeries, MoveType[] types) {
        this.nodes = nodeSeries;
        this.types = types;
    }

    /**
     * Initializes the move as a simple move
     * @param x x coordinate of the position from which to move a piece
     * @param y y coordinate of the position from which to move a piece
     * @param to position to which to move a piece
     * @param type type of the move
     */
    public Move(int x, int y, Point to, MoveType type) {
        this(new Point(x, y), to, type);
    }

    /**
     * @return all nodes visited by this move
     */
    public Point[] getNodes() {
        return nodes;
    }

    /**
     * @return all move types of this move
     */
    public MoveType[] getTypes() {
        return types;
    }

    /**
     * @return the last position visited by this move
     */
    public Point getLastToPosition() {
        return nodes[nodes.length - 1];
    }

    /**
     * @return the second last position visited by this move which is the position from witch the last move originates
     */
    public Point getLastFromPosition() {
        return nodes[nodes.length - 2];
    }

    /**
     * @return returns the type of the last move
     */
    public MoveType getLastMoveType() {
        return types[types.length - 1];
    }

    /**
     * Extends this move by a move specified by its next position and the type of the move
     * @param nextPosition the next position to move to
     * @param type the type of the move
     * @return the extended move
     */
    public Move extendCapture(Point nextPosition, MoveType type) {
        Point[] newNodes = new Point[nodes.length + 1];
        MoveType[] newTypes = new MoveType[types.length + 1];

        for (int i = 0; i < nodes.length; i++) {
            newNodes[i] = nodes[i];
        }
        for (int i = 0; i < types.length; i++) {
            newTypes[i] = types[i];
        }

        newNodes[nodes.length] = nextPosition;
        newTypes[types.length] = type;

        return new Move(newNodes, newTypes);
    }

    /**
     * Evaluates whether this move applies to all the rules for a move
     * @return true if this move applies to the rules, false otherwise
     */
    public boolean appliesToRules() {
        return neverVisitedNodeTwice() && alwaysChangeDirection() && noFalsePaikaMoves();
    }

    /**
     * Evaluates whether the move contains illegal paika moves. Paika moves are illegal if there is a capture move within
     * this move. This is only relevant for extended capture moves.
     * @return true if there are no illegal paika moves involved in this move, false otherwise
     */
    public boolean noFalsePaikaMoves() {
        if (types.length == 1) {
            return true;
        } else {
            for (MoveType type : types) {
                if (type.equals(MoveType.paika)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Evaluates whether this move visits a node twice.
     * @return true if this move never visits a node twice, false otherwise
     */
    public boolean neverVisitedNodeTwice() {
        for (int i = 0; i < nodes.length - 1; i++) {
            for (int h = i + 1; h < nodes.length; h++) {
                if (nodes[i].equals(nodes[h])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Evaluates whether this move always changes direction.
     * @return true if this move always changes direction, false otherwise
     */
    public boolean alwaysChangeDirection() {
        if (nodes.length == 2) {
            return true; //no extended capturing move -> no direction changes
        } else {
            int prevMoveOffsetX = nodes[1].x - nodes[0].x;
            int prevMoveOffsetY = nodes[1].y - nodes[0].y;
            for (int i = 1; i < nodes.length - 1; i++) {
                int currMoveOffsetX = nodes[i + 1].x - nodes[i].x;
                int currMoveOffsetY = nodes[i + 1].y - nodes[i].y;

                if (prevMoveOffsetX == currMoveOffsetX && prevMoveOffsetY == currMoveOffsetY) {
                    return false;
                }

                prevMoveOffsetX = currMoveOffsetX;
                prevMoveOffsetY = currMoveOffsetY;
            }
            return true;
        }
    }

    /**
     * Returns a string containing all moves in their textual representation separated by commas
     * @param moves A iterable collection of moves to parse
     * @return a string with all moves in their textual representation
     */
    public static String movesToString(Iterable<Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (Move mo : moves) {
            sb.append(mo.toString()).append(", ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String s = (char) ('a' + nodes[0].x) + "" + nodes[0].y;
        for (int i = 1; i < nodes.length; i++) {
            s += "" + (char) ('a' + nodes[i].x) + "" + nodes[i].y + types[i - 1];
        }
        return s;
    }

}
