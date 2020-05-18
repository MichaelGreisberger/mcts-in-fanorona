package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConsolePlayer implements Player {

    private Board board;
    private String name;

    private Scanner in;
    private PrintWriter out;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void reset() {
        initNameFromConsole();
    }

    //TODO: Delete this!
    public void incWin(){

    }

    public int getWins() {
        return 0;
    }

    public ConsolePlayer(Board board, InputStream in, OutputStream out) {
        this.in = new Scanner(new InputStreamReader(in));
        this.out = new PrintWriter(new OutputStreamWriter(out), true);
        this.board = board;
        initNameFromConsole();
    }

    private void initNameFromConsole() {
        this.out.println("Whats your Name?");
        this.name = this.in.nextLine().trim();
    }
    @Override
    public Move getNextMove() {
        while (true) {
            out.println("Your next possible moves are:");
            MoveList possibleMoves = board.getPossibleMoves();
            out.println(possibleMoves);
            out.println("Select your move.");
            String selectedMove = in.nextLine();
            for (Move move : possibleMoves) {
                if (selectedMove.equals(move.toString())) {
                    return move;
                }
            }
            out.println("This was an illegal Move! :O Please select a legal move!\n");
        }
    }
}
