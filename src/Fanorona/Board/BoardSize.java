package Fanorona.Board;

import java.awt.*;

public enum BoardSize {
    small (new Point(3,3)),
    medium (new Point(5,5)),
    large (new Point(9,5));

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
}
