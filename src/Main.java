import util.BoardSize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    private static Board board = new Board(BoardSize.large);
    private static Map<String, Move> movesDict;
    private static int counter;
    public static void main(String[] args) {

        movesDict = getMovesDict();
        List<Move> moves = board.getPossibleMoves();
        Random rand = new Random();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        System.out.println("Welcome to Fanorona CLI!");
        System.out.println();
        System.out.println("if you want to play type 'p', if you want tro launch the random game simulator press 'r'...");
        try {
            input = reader.readLine();
            if (input.equals("p")) {
                printGameInfoAndState();
                while (true) {
                    input = reader.readLine();
                    if (movesDict.containsKey(input)) {
                        board.applyMove(movesDict.get(input));
                        movesDict = getMovesDict();
                        printGameInfoAndState();
                    } else {
                        System.out.println("unknown move: " + input);
                    }
                }
            } else if (input.equals("r")) {
                counter = 0;
                long millis = System.currentTimeMillis();
                while (true) {
                    int randMove = (int) (rand.nextDouble() * moves.size());
//                    printGameInfoAndState();
                    try {
                        board.applyMove(moves.get(randMove));
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("player " + (board.getCurrentPlayer() ^ 3) + " won! this was the " + ++counter + " game after " + (System.currentTimeMillis() - millis) / 1000 + " seconds.");
                        board = new Board(BoardSize.large);
                    }
                    moves = board.getPossibleMoves();
                }
            }
            printGameInfoAndState();


        } catch (IOException e) {
            e.printStackTrace();
        }


//        Move move = new Move(1, 1, 2, 2, MoveType.approach);
//        System.out.println(move);
//
//        Board board = new Board(BoardSize.small);
//        System.out.printf(board.toPrintString());
//        board = new Board(BoardSize.medium);
//        System.out.printf(board.toPrintString());
//        board = new Board(BoardSize.large);
//        System.out.printf(board.toPrintString());
//
//        for (Move mo :
//                board.getPossibleMoves()) {
//            System.out.println(mo.toString());
//        }
//
//        board = new Board(BoardSize.large, BoardStateExamples.PAPER_EXAMPLE_STATE);
//////        System.out.println(board.toPrintString());
//////        for (Move mo :
//////                board.getPossibleMoves()) {
//////            System.out.println(mo.toString());
//////        }
////
////        board.setPosition(8,4,1);
//        System.out.println(board.toPrintString());
//        List<Move> movesDict = board.getPossibleMoves();
//        for (Move mo : movesDict) {
//            System.out.println(mo.toString());
//        }
//        board.applyMove(movesDict.get(0));
//        System.out.println(board.toPrintString());

    }

    public static void printGameInfoAndState() {
        System.out.println();
        System.out.println(board.toPrintString());
        printMoves();
        System.out.println("Type a move string to apply move");
    }

    public static void printMoves() {
        System.out.print("Your possible movesDict are: ");
        for (Move mo : movesDict.values()) {
            System.out.print(mo.toString() + ", ");
        }
        System.out.println();
    }

    private static Map<String, Move> getMovesDict() {
        Map<String, Move> moves = new HashMap<>();
        for (Move move : board.getPossibleMoves()) {
            moves.put(move.toString(), move);
        }
        return moves;
    }
}
