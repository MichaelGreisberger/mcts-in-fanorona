package Fanorona;

import java.util.Iterator;

public class MoveList implements Iterable<Move> {
    private MoveNode first;
    private MoveNode last;
    private int count = 0;

    public MoveNode getFirst() {
        return first;
    }

    public MoveNode getLast() {
        return last;
    }

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

    public Move get(int index) {
        if (index > count - 1) throw new IndexOutOfBoundsException();
        MoveNode curr = first;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.move;
    }

    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public Iterator iterator() {
        return new MoveListIterator(this);
    }
}

class MoveListIterator implements Iterator<Move> {
    private MoveList moves;
    private MoveNode curr;

    public MoveListIterator(MoveList moves) {
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
