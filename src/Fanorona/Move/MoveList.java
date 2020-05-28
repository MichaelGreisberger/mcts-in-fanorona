package Fanorona.Move;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * This class provides a simple data structure for handling a list of moves.
 */
public class MoveList implements Iterable<Move> {
    private MoveNode first;
    private MoveNode last;
    private int count = 0;

    /**
     * @return the first move of this collection of moves
     */
    MoveNode getFirst() {
        return first;
    }

    /**
     * @return the last move of this collection of moves
     */
    MoveNode getLast() {
        return last;
    }

    /**
     * Adds another move to the tail of this collection
     *
     * @param move move to add
     */
    public void append(Move move) {
        if (first == null) {
            first = new MoveNode(move);
            last = first;
        } else if (first == last) {
            last = new MoveNode(move);
            last.prev = first;
            first.next = last;
        } else {
            MoveNode prevLast = last;
            last = new MoveNode(move);
            prevLast.next = last;
            last.prev = prevLast;
        }
        count++;
    }

    public int size() {
        return count;
    }

    /**
     * Adds a collection of moves to the tail of this collection
     *
     * @param moves moves to add
     */
    public void append(MoveList moves) {
        if (isEmpty()) {
            first = moves.first;
            last = moves.last;
            count += moves.count;
        } else if (!moves.isEmpty()) {
            last.next = moves.first;
            moves.first.prev = last;
            last = moves.last;
            count += moves.count;
        }
    }

    /**
     * @param index position of the move in the collection
     * @return the move at the given index
     */
    public Move get(int index) {
        if (index > count - 1) throw new IndexOutOfBoundsException();
        MoveNode curr = first;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.move;
    }

    /**
     * @return true if this collection does not contain elements
     */
    public boolean isEmpty() {
        return first == null;
    }

    @NotNull
    @Override
    public Iterator<Move> iterator() {
        return new MoveListIterator(this);
    }

    @Override
    public String toString() {
        String s = "";
        for (Move move : this) {
            s += move + ", ";
        }
        return s;
    }
}

class MoveListIterator implements Iterator<Move> {
    private MoveList moves;
    private MoveNode curr;

    MoveListIterator(MoveList moves) {
        this.moves = moves;
    }

    @Override
    public boolean hasNext() {
        if (curr == null) {
            return true;
        } else {
            return curr.next != null;
        }
    }

    @Override
    public Move next() {
        if (curr == null) {
            curr = moves.getFirst();
        } else {
            curr = curr.next;
        }
        return curr.move;
    }
}

class MoveNode {
    MoveNode next;
    MoveNode prev;
    Move move;

    MoveNode(Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return move.toString();
    }
}
