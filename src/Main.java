import Fanorona.Board;
import Fanorona.Move;
import Fanorona.MoveList;
import Fanorona.util.BoardSize;

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
        MoveList moves = board.getPossibleMoves();
//        List<Move> moves = board.getPossibleMoves();
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
                    if (input.equals("s")) {
                        System.out.println(board.getStateB64());
                    } else if (movesDict.containsKey(input)) {
                        board.applyMove(movesDict.get(input));
                        movesDict = getMovesDict();
                        printGameInfoAndState();
                    } else {
                        System.out.println("unknown move: " + input);
                    }
                }
            } else if (input.equals("r")) {
                counter = 0;
                int whiteWins = 0, blackWins = 0;
                long millis = System.currentTimeMillis();
                while (System.currentTimeMillis() < millis + 60000) {
                    int randMove = (int) (rand.nextDouble() * moves.size());
//                    printGameInfoAndState();
                    try {
                        board.applyMove(moves.get(randMove));
                    } catch (IndexOutOfBoundsException e) {
                        if ((board.getCurrentPlayer() ^ 3) == 1) {
                            whiteWins++;
                        } else {
                            blackWins++;
                        }
                        counter++;
//                        System.out.println("player " + (board.getCurrentPlayer() ^ 3) + " won! this was the " + ++counter + " game after " + (System.currentTimeMillis() - millis) / 1000 + " seconds.");
//                        printGameInfoAndState();
                        board = new Board(BoardSize.large);
                    }
                    moves = board.getPossibleMoves();
                }
                System.out.println("White wins " + whiteWins + " times, Black wins " + blackWins + " times. This makes a total of " + counter + " games.\n" +
                        "White won " + ((double)whiteWins / counter * 100) + "% of al games.");
            }
//            printGameInfoAndState();


        } catch (IOException e) {
            e.printStackTrace();
        }


//        Fanorona.Move move = new Fanorona.Move(1, 1, 2, 2, MoveType.approach);
//        System.out.println(move);
//
//        Board board = new Board(BoardSize.small);
//        System.out.printf(board.toPrintString());
//        board = new Board(BoardSize.medium);
//        System.out.printf(board.toPrintString());
//        board = new Board(BoardSize.large);
//        System.out.printf(board.toPrintString());
//
//        for (Fanorona.Move mo :
//                board.getPossibleMoves()) {
//            System.out.println(mo.toString());
//        }
//
//        board = new Board(BoardSize.large, BoardStateExamples.PAPER_EXAMPLE_STATE);
//////        System.out.println(board.toPrintString());
//////        for (Fanorona.Move mo :
//////                board.getPossibleMoves()) {
//////            System.out.println(mo.toString());
//////        }
////
////        board.setPosition(8,4,1);
//        System.out.println(board.toPrintString());
//        List<Fanorona.Move> movesDict = board.getPossibleMoves();
//        for (Fanorona.Move mo : movesDict) {
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
        MoveList possibleMoves = board.getPossibleMoves();
//        List<Move> possibleMoves = board.getPossibleMoves();
        for (Move move : possibleMoves) {
            moves.put(move.toString(), move);
        }
        return moves;
    }
}
