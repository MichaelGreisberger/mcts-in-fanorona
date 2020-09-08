package Fanorona.Board;

import java.awt.*;

/**
 * This class represents the size of a Fanrorona board. It contains a Textual description as well as the concrete sizes
 */
public enum BoardSize {
    SMALL(new Point(3, 3)),
    MEDIUM(new Point(5, 5)),
    LARGE(new Point(9, 5));

    private Point point;

    BoardSize(Point point) {
        this.point = point;
    }

    public int y() {
        return point.y;
    }

    public int x() {
        return point.x;
    }

    public int maxPiecesPP() {
        return point.x * point.y / 2;
    }
}
