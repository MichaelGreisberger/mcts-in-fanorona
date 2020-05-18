package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Move.Move;
import Fanorona.mcts.RandomMctsPlayer;
import Fanorona.mcts.RandomPlayerGameStateStatistic;
import Fanorona.mcts.RandomPlayerGameStateStatistic2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Game {

    private InputStream in;
    private OutputStream out;
    private Player white;
    private Player black;
    private Board board;
    private boolean verbose = true;
    private Queue<String> recentStates = new LinkedList<>();
    private Map<String, Integer> revisitCounter = new HashMap<>();


    public Game(InputStream in, OutputStream out, BoardSize size) {
        this.in = in;
        this.out = out;
        board = new Board(size);
    }

    public Game(InputStream in, OutputStream out, Board board) {
        this.in = in;
        this.out = out;
        this.board = board;
    }

    public void initializePlayersFromConsole() throws IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.out), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(this.in));
        out.println("Do you want to play against AI? (y/n)");
        if (in.readLine().toLowerCase().startsWith("y")) {
            out.println("Would you like to be the starting player? (y/n)");
            if (in.readLine().toLowerCase().startsWith("y")) {
                white = new ConsolePlayer(board, this.in, this.out);
                black = new RandomMctsPlayer(board, 15000, "BlackAI");
            } else {
                white = new RandomMctsPlayer(board, 15000, "WhiteAI");
                black = new ConsolePlayer(board, this.in, this.out);
            }
        } else {
            out.println("Do you want to play against another human player? (y/n)");
            if (in.readLine().toLowerCase().startsWith("y")) {
                white = new ConsolePlayer(board, this.in, this.out);
                black = new ConsolePlayer(board, this.in, this.out);
            } else {
                out.println("Do you want to watch two AI players play against each other?! (y/n)");
                if (in.readLine().toLowerCase().startsWith("y")) {
                    white = new RandomMctsPlayer(board, 1500, "RPGSS", new RandomPlayerGameStateStatistic());
                    black = new RandomMctsPlayer(board, 1500, "RPGSS2", new RandomPlayerGameStateStatistic2());
                } else {
                    out.println("What the fuck do you want than?!?");
                    out.println("AHH never mind.. I'm out!");
                    throw new RuntimeException("Game doesn't want to play with you any longer.");
                }
            }
        }
    }

    public void runGame() {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.out), true);
        Player curr = board.getCurrentPlayer() == 1 ? white : black;
        Move move;
        while (true) {
            if (verbose) {
                out.println();
                out.println("<><><><><><><><><><><><><><><><   " + curr.getName() + "'s Turn   ><><><><><><><><><><><><><><><><><><>");
                out.print(board.toPrintString());
                out.flush();
            }
            move = curr.getNextMove();
            if (verbose) {
                out.println(curr.getName() + " choose move " + move + " and applies it to board " + board.getStateB64());
            }
            board = board.applyMove(move);
            white.setBoard(board);
            black.setBoard(board);
            if (hasSomeoneWon()) {
                out.println(board.toPrintString());
                out.println(curr.getName() + " won the game!");
                curr.incWin();
                printPlayerWinStats();
                return;
            }
            if (isDraw()) {
                out.println(board.toPrintString());
                System.out.println("Its a draw!");
                printPlayerWinStats();
                return;
            }
            if (curr == black) {
                curr = white;
            } else {
                curr = black;
            }
        }
    }

    private void printPlayerWinStats() {
        System.out.println(black.getName() + " (black) won " + black.getWins() + " times and " + white.getName() + " (white) won " +
                white.getWins() + " times. This means that " + white.getName() + " won " +
                (((double) white.getWins()) / (white.getWins() + black.getWins()) * 100) + "% of all games!") ;
    }

    private boolean hasSomeoneWon() {
        return board.getPossibleMoves().size() == 0;
    }

    public void reset() {
        board = new Board(board.getState().getSize());
        white.setBoard(board);
        white.reset();
        black.setBoard(board);
        black.reset();
        switchTables();
    }

    private boolean isDraw() {
        if (recentStates.contains(board.getStateB64())) {
            if (!revisitCounter.containsKey(board.getStateB64())) {
                revisitCounter.put(board.getStateB64(), 1);
            }
            if (revisitCounter.get(board.getStateB64()) >= 3) {
                return true;
            } else {
                revisitCounter.put(board.getStateB64(), (revisitCounter.get(board.getStateB64()) + 1));
            }
        }
        if (recentStates.size() > 20) {
            revisitCounter.put(recentStates.poll(), 0);
        }
        recentStates.offer(board.getStateB64());
        return false;
    }

    public void switchTables() {
        Player temp = white;
        white = black;
        black = temp;
    }
}
