import Fanorona.Board.Board;
import Fanorona.Game;
import Fanorona.Move.Move;
import Fanorona.Board.BoardSize;
import Fanorona.Move.MoveList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    private static Board board = new Board(BoardSize.large);
    private static Map<String, Move> movesDict;
    private static int counter;

    public static void main(String[] args) {

        movesDict = getMovesDict();
        MoveList moves = board.getPossibleMoves();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        System.out.println("Welcome to Fanorona CLI!");
        System.out.println();
        System.out.println("if you want to play type 'p', if you want tro launch the random game simulator press 'r'...");
        try {
            input = reader.readLine();
            if (input.equals("p")) {
//                Game game = new Game(System.in, System.out, new Board(BoardState.fromB64("woAAQgAAAAAAAgAAAg==", BoardSize.large)));
                Game game = new Game(System.in, System.out, BoardSize.large);
                game.initializePlayersFromConsole();
                game.runGame();
                System.out.println("Wanna try again? (y/n)");
                while (true) {//(reader.readLine().startsWith("y")) {
                    game.reset();
                    game.runGame();
//                    System.out.println("Wanna try again? (y/n)");
                }
            } else if (input.equals("r")) {
                System.out.println("Starting simulation: this simulation runs for one minute and prints details after it finishes.");
                counter = 0;
                Random rand = new Random();
                int whiteWins = 0, blackWins = 0;
                long millis = System.currentTimeMillis();
                while (System.currentTimeMillis() < millis + 60000) {
                    int randMove = (int) (rand.nextDouble() * moves.size());
                    try {
                        board = board.applyMove(moves.get(randMove));
                    } catch (IndexOutOfBoundsException e) {
                        if ((board.getCurrentPlayer() ^ 3) == 1) {
                            whiteWins++;
                        } else {
                            blackWins++;
                        }
                        counter++;
                        board = new Board(BoardSize.large);
                    }
                    moves = board.getPossibleMoves();
                }
                System.out.println("White wins " + whiteWins + " times, Black wins " + blackWins + " times. This makes a total of " + counter + " games.\n" +
                        "White won " + ((double)whiteWins / counter * 100) + "% of all games.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        for (Move move : possibleMoves) {
            moves.put(move.toString(), move);
        }
        return moves;
    }
}
