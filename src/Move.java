import util.MoveType;

import java.awt.*;

public class Move {
    private final Point[] nodes;
    private final MoveType[] types;
//    public static final String MOVES_TO_SHORT_ERROR_MESSAGE = "To short move length. Move must at least have length of 1.";

    public Move(int fromX, int fromY, int toX, int toY, MoveType type) {
        this(new Point(fromX, fromY), new Point(toX, toY), type);
    }

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

    public Move(int x, int y, Point to, MoveType type) {
        this(new Point(x, y), to, type);
    }

    public Point[] getNodes() {
        return nodes;
    }

    public MoveType[] getTypes() {
        return types;
    }

    public Point getLastToPositon() {
        return nodes[nodes.length - 1];
    }

    public Point getLastFromPositon() {
        return nodes[nodes.length - 2];
    }

    public MoveType getLastMoveType() {
        return types[types.length - 1];
    }

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


    public boolean appliesToRules() {
        return neverVisitedNodeTwice() && alwaysChangeDirection() && noFalsePaikaMoves();
    }

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

    @Override
    public String toString() {
        String s = (char) ('a' + nodes[0].x) + "" + nodes[0].y;
        for (int i = 1; i < nodes.length; i++) {
            s += "" + (char) ('a' + nodes[i].x) + "" + nodes[i].y + types[i - 1];
        }
        return s;
    }
}
