package Fanorona.Board;

import java.awt.*;
import java.util.Base64;

/**
 * This class represents the actual state of a Fanorona board and provides method's to retrieve or change it.
 */
public class BoardState implements Cloneable {
    private final int X_OFFSET = 3; //offset of positions (1 position = 2bits) previous to board-state-data (same for all sizes)
    private BoardSize size;

    /**
     * cState is the actual storage for the board state. A character can store up to 4 nodes of the board.
     * The first character in the array (index 0) represents the current player in the two most significant bits
     * and the first field of the board in the two least significant bits. the 4 bits in the middle are (currently)
     * unused. All other bits are used to represent the nodes of the board.
     * This is true for all supported board sizes as it is true that (x * y * 2) % 8 == 2 where x * y represents the
     * number of nodes for a specific board size. With * 2 we obtain the bits needed to represent the whole board.
     * % 8 determines the number of bits in the first partly filled byte (char).
     */
    private char[] cState;

    public BoardSize getSize() {
        return size;
    }

    BoardState(BoardSize size, char[] state) {
        this.size = size;
        this.cState = state;
    }

    BoardState(BoardSize size) {
        this.size = size;
        this.cState = getInitialState(size).toCharArray();
    }

    /**
     * @return the initial state of the current board
     */
    private String getInitialState(BoardSize size) {
        switch (size) {
            case SMALL:
                return getInitialStateSmall();
            case MEDIUM:
                return getInitialStateMedium();
            case LARGE:
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
        int clearMask = -193; //0011111 ~(3 << 6)
        //first deletes the current player (CPXXXXXX & 00111111) and shifts the new player's value to the correct position
        //afterwards inserts the the new player (00XXXXXX | NP000000)
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
        //position of coordinates in array (integer part is index, floating point part represents the bits in char)
        float position = (float) (x + X_OFFSET + y * size.x()) / 4;
        int index = (int) position;
        //number of shift's required to clear the relevant part of the char determined by index and to shift the new value
        //to the same position as the old value.
        //example: relevant char looks like 01001001 where every two bits represent a node. We want to change the second two
        //bit from the right, 10, to 01. The floating point part of position is 0.5. Therefor (0.75 - 0.5) * 8 results in the
        //two required shifts. clearMask therefor is 11110011 and newVal is 00000100
        int shift = (int) (6 - (position - (int) position) * 8);
        int clearMask = ~(3 << shift);
        cState[index] = (char) (cState[index] & clearMask | (newVal << shift));
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
     * Returns the value of the board state at the specified position
     *
     * @param x x coordinate of the position for which to return the current board state
     * @param y x coordinate of the  position for which to return the current board state
     * @return Returns the value of the board state at the specified position
     */
    int getPosition(int x, int y) {
        //position of coordinates in array (integer part is index, floating point part represents the bits in char)
        float position = (float) (x + X_OFFSET + y * size.x()) / 4;
        int index = (int) position;
        //number of shift's required to shift the relevant part of the char determined by index to the least significant two bits.
        int shift = (int) (6 - (position - (int) position) * 8);
        return (cState[index] >> shift) & 3;
    }

    /**
     * Returns the current State encoded in a Base64 String
     *
     * @return the current State encoded in a Base64 String
     */
    String getStateB64() {
        return Base64.getEncoder().encodeToString(new String(cState).getBytes());
    }

    /**
     * @return a human readable string representation of the state of the current board.
     */
    String toPrintString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPrintHeader());
        int xlength = size.x() * 2 - 1; //x nodes, x-1 spaces in between
        int ylength = size.y() * 2 - 1;
        for (int y = 0; y < ylength; y++) {
            sb.append(y % 2 == 0 ? y / 2 + " " : "  ");
            for (int x = 0; x < xlength; x++) {
                if (x % 2 == 0) {
                    if (y % 2 == 0) {
                        int pos = getPosition(x / 2, y / 2);
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
            case LARGE:
                return "  a   b   c   d   e   f   g   h   i\n";
            case MEDIUM:
                return "  a   b   c   d   e\n";
            case SMALL:
                return "  a   b   c\n";
            default:
                return "Unknown Boardsize";
        }
    }

    /**
     * @return returns the initial state of a small board
     */
    private String getInitialStateSmall() {
        return "B¨U";
        /*
        01000010
        10101000
        01010101
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

    BoardState getCopy() {
        try {
            return (BoardState) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reset() {
        cState = getInitialState(size).toCharArray();
    }

    public int countPieces(int player) {
        int count = 0;
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                if (getPosition(x, y) == player) {
                    count++;
                }
            }
        }
        return count;
    }
}
