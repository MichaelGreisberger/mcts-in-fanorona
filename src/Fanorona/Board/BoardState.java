package Fanorona.Board;

import java.awt.*;
import java.util.Base64;

public class BoardState implements Cloneable {
    private final int X_OFFSET = 3; //offset of positions (1 position = 2bits) previous to board-state-data (same for all sizes)
    private BoardSize size;
    private char[] cState;

    public BoardSize getSize() {
        return size;
    }


    public BoardState(BoardSize size, char[] state) {
        this.size = size;
        this.cState = state;
    }

    public BoardState(BoardSize size) {
        this.size = size;
        this.cState = getInitialState(size).toCharArray();
    }

    /**
     * Returns the value of the board state at the specified position
     *
     * @param x x coordinate of the position for which to return the current board state
     * @param y x coordinate of the  position for which to return the current board state
     * @return Returns the value of the board state at the specified position
     */
    int getPosition(int x, int y) {
        double temp = (double) (x + X_OFFSET + y * size.x()) / 4;
        int position = (int) temp;
        int shift = (int) ((0.75 - (temp - (int) temp)) * 8);
        return (cState[position] >> shift) & 3;
    }

    /**
     * @return the initial state of the current board
     */
    String getInitialState(BoardSize size) {
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

    /**
     * determines the current player (first 2 bit of state)
     *
     * @return 1 for player one (white) 2 for player 2 (black)
     */
    int getCurrentPlayer() {
        return cState[0] >> 6;
    }

    /**
     * Sets the current player in this boards state to the given player
     *
     * @param player current player for this board state.
     */
    void setCurrentPlayer(int player) {
        int clearMask = ~(3 << 6); //ToDo: replace with actual value
        cState[0] = (char) (cState[0] & clearMask | (player << 6));
    }

    /**
     * Changes the board state at the position specified by position to the value specified by newVal
     *
     * @param position position for which to change the value
     * @param newVal   the new value of the position
     */
    void setPosition(Point position, int newVal) {
        setPosition(position.x, position.y, newVal);
    }

    /**
     * Changes the board state at the position specified by position to the value specified by newVal
     *
     * @param x      x coordinate of the position for which to change the value
     * @param y      y coordinate of the position for which to change the value
     * @param newVal the new value of the position
     */
    private void setPosition(int x, int y, int newVal) {
        double temp = (double) (x + X_OFFSET + y * size.x()) / 4;
        int position = (int) temp;
        int shift = (int) ((0.75 - (temp - (int) temp)) * 8);
        int clearMask = ~(3 << shift);
        cState[position] = (char) (cState[position] & clearMask | (newVal << shift));
    }

    /**
     * Returns the value of the board state at the specified position
     *
     * @param position position for which to return the current board state
     * @return Returns the value of the board state at the specified position
     */
    public int getPosition(Point position) {
        return getPosition(position.x, position.y);
    }

    /**
     * Returns the current State encoded in a Base64 String
     *
     * @return the current State encoded in a Base64 String
     */
    public String getStateB64() {
        return Base64.getEncoder().encodeToString(new String(cState).getBytes());
    }

    /**
     * @return a human readable string representation of the state of the current board.
     */
    public String toPrintString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(getCurrentPlayer() == 1 ? "White's turn\n" : "Black's turn\n");
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

    /**
     * @return the column headers of the current board.
     */
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

    /**
     * @return returns the initial state of a small board
     */
    private String getInitialStateSmall() {
        return "B¤\u0095";
        /*
        01000010
        10100100
        10010101
         */
    }

    /**
     * @return returns the initial state of a medium board
     */
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

    /**
     * @return returns the initial state of a large board
     */
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        BoardState state = (BoardState) super.clone();
        state.cState = cState.clone();
        return state;
    }

    public BoardState getCopy() {
        try {
            return (BoardState) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
