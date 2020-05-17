package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.mcts.RandomMctsPlayer;
import Fanorona.mcts.RandomPlayerGameStateStatistic;
import Fanorona.mcts.RandomPlayerGameStateStatistic2;
import Fanorona.Board.BoardSize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Game {

    private InputStream in;
    private OutputStream out;
    private Player white;
    private Player black;
    private Board board;

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
                black = new RandomMctsPlayer(board, 2, 15000, "BlackAI");
            } else {
                white = new RandomMctsPlayer(board, 1, 15000, "WhiteAI");
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
                    white = new RandomMctsPlayer(board, 1, 500, "RPGSS", new RandomPlayerGameStateStatistic());
                    black = new RandomMctsPlayer(board, 2, 500, "RPGSS2", new RandomPlayerGameStateStatistic2());
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
            out.println();
            out.println("<><><><><><><><><><><><><><><><   " + curr.getName() + "'s Turn   ><><><><><><><><><><><><><><><><><><>");
            out.print(board.toPrintString());
            out.flush();
            move = curr.getNextMove();
            System.out.println(curr.getName() + " choose move " + move + " and applies it to board " + board.getStateB64());
            board.applyMove(move);
            if (hasSomeoneWon()) {
                out.println(board.toPrintString());
                out.println(curr.getName() + " won the game!");
                return;
            }
            if (curr == black) {
                curr = white;
            } else {
                curr = black;
            }
        }
    }

    private boolean hasSomeoneWon() {
        return board.getPossibleMoves().size() == 0;
    }

    public void reset() {
        board = new Board(board.getState().getSize());
        white.setBoard(board);
        black.setBoard(board);
        switchTables();
    }

    public void switchTables() {
        Player temp = white;
        white = black;
        black = temp;
    }
}
