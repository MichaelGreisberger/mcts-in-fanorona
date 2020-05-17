package Fanorona.Move;

public enum MoveType {
    approach('A'),
    withdraw('W'),
    paika('P');

    private final char type;

    MoveType(char type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "" + type;
    }
}
