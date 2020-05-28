package Fanorona.Move;

/**
 * This class represents the types of moves in Fanrorona.
 */
public enum MoveType {
    APPROACH('A'),
    WITHDRAW('W'),
    PAIKA('P');

    private final char type;

    MoveType(char type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "" + type;
    }
}
