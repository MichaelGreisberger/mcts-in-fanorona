package Fanorona.Move;

import java.awt.*;

/**
 * This class represents a move in Fanorona. It is suited to represent all possible types of moves. It provides method's
 * to evaluate whether the move complies to the rules of the game and to conveniently access its properties.
 */
public class Move {
    /**
     * nodes visited by this move (including the starting node)
     */
    private final Point[] nodes;
    /**
     * type(s) of this move (Approach, Withdrawal, paika)
     * This array is always one element shorter than nodes
     */
    private final MoveType[] types;

    /**
     * Initializes this move from a move string of the form "f2e2A"
     *
     * @param moveString string representation of the move
     */
    public Move(String moveString) {
        char[] chars = moveString.toCharArray();
        int nodeCount = (moveString.length() - 2) / 3 + 1;
        nodes = new Point[nodeCount];
        types = new MoveType[nodeCount - 1];
        nodes[0] = new Point(chars[0] - 'a', chars[1] - 48);
        for (int i = 4; i < chars.length; i += 3) {
            nodes[(i - 2) / 3 + 1] = new Point(chars[i - 2] - 'a', chars[i - 1] - 48);
            switch (chars[i]) {
                case 'A':
                    types[(i - 2) / 3] = MoveType.APPROACH;
                    break;
                case 'W':
                    types[(i - 2) / 3] = MoveType.WITHDRAW;
                    break;
                case 'P':
                    types[(i - 2) / 2 - 1] = MoveType.PAIKA;
                    break;
                default:
                    types[(i - 2) / 2 - 1] = MoveType.PAIKA;
            }
        }
    }

    /**
     * Initializes the move as a simple move
     *
     * @param from position from which to move a piece
     * @param to   position to which to move a piece
     * @param type type of the move
     */
    public Move(Point from, Point to, MoveType type) {
        this.nodes = new Point[]{from, to};
        this.types = new MoveType[]{type};
    }

    /**
     * Initializes new extended capture move.
     *
     * @param nodeSeries Series of nodes visited. The first Point must always be the from-node.
     * @param types      Type of move.
     */
    public Move(Point[] nodeSeries, MoveType[] types) {
        this.nodes = nodeSeries;
        this.types = types;
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
     *
     * @param nextPosition the next position to move to
     * @param type         the type of the move
     * @return the extended move
     */
    public Move extendCapture(Point nextPosition, MoveType type) {
        Point[] newNodes = new Point[nodes.length + 1];
        MoveType[] newTypes = new MoveType[types.length + 1];

        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        System.arraycopy(types, 0, newTypes, 0, types.length);

        newNodes[nodes.length] = nextPosition;
        newTypes[types.length] = type;

        return new Move(newNodes, newTypes);
    }

    /**
     * Evaluates whether this move applies to all the rules for a move
     *
     * @return true if this move applies to the rules, false otherwise
     */
    public boolean appliesToRules() {
        return neverVisitedNodeTwice() && alwaysChangeDirection() && noFalsePaikaMoves();
    }

    /**
     * Evaluates whether the move contains illegal paika moves. Paika moves are illegal if there is a capture move within
     * this move. This is only relevant for extended capture moves.
     *
     * @return true if there are no illegal paika moves involved in this move, false otherwise
     */
    private boolean noFalsePaikaMoves() {
        if (types.length == 1) {
            return true;
        } else {
            for (MoveType type : types) {
                if (type.equals(MoveType.PAIKA)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Evaluates whether this move visits a node twice.
     *
     * @return true if this move never visits a node twice, false otherwise
     */
    boolean neverVisitedNodeTwice() {
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
     *
     * @return true if this move always changes direction, false otherwise
     */
    boolean alwaysChangeDirection() {
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

    @Override
    public String toString() {
        String s = (char) ('a' + nodes[0].x) + "" + nodes[0].y;
        for (int i = 1; i < nodes.length; i++) {
            s += "" + (char) ('a' + nodes[i].x) + "" + nodes[i].y + types[i - 1];
        }
        return s;
    }

    public String toSchaddMoveString() {
        String s = (char) ('a' + nodes[0].x) + "" + (5 - nodes[0].y);
        for (int i = 1; i < nodes.length; i++) {
            s += "-" + (char) ('a' + nodes[i].x) + "" + (5 - nodes[i].y) + types[i - 1];
        }
        return s.replace("P", "");
    }

    public static Move fromSchaddMoveString(String moveString) {
        char[] chars = moveString.toCharArray();
        int nodeCount = (moveString.length() - 2) / 3 + 1;
        Point[] nodes = new Point[nodeCount];
        MoveType[] types = new MoveType[nodeCount - 1];
        nodes[0] = new Point(chars[0] - 'a', (5 - (chars[1] - 48)));
        for (int i = 4; i < chars.length; i += 3) {
            nodes[(i - 2) / 3 + 1] = new Point(chars[i - 2] - 'a', (5 - (chars[i - 1] - 48)));
            switch (chars[i]) {
                case 'A':
                    types[(i - 2) / 3] = MoveType.APPROACH;
                    break;
                case 'W':
                    types[(i - 2) / 3] = MoveType.WITHDRAW;
                    break;
                case 'P':
                    types[(i - 2) / 2 - 1] = MoveType.PAIKA;
                    break;
                default:
                    types[(i - 2) / 2 - 1] = MoveType.PAIKA;
            }
        }
        return new Move(nodes, types);
    }

}
