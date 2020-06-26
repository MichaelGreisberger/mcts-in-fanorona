package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This implementation of {@code Player} uses streams for communication. Those streams could connect to a console or
 * another program.
 */
public class StreamPlayer implements Player {
    private Scanner in;
    private PrintWriter out;
    private boolean socketPlayer = false;

    public StreamPlayer(InputStream in, OutputStream out, boolean socketPlayer) {
        this.in = new Scanner(new InputStreamReader(in));
        this.out = new PrintWriter(new OutputStreamWriter(out), true);
        this.socketPlayer = socketPlayer;
    }
    public StreamPlayer(InputStream in, OutputStream out) {
        this(in, out, false);
    }
    public static Player initFromSocket(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        return new StreamPlayer(socket.getInputStream(), socket.getOutputStream(), true);
    }

    @Override
    public Move getNextMove(Board board) {
        while (true) {
            if (socketPlayer) {
                out.println(board.toPrintString());
            }
            out.println("Your next possible moves are:");
            MoveList possibleMoves = board.getPossibleMoves();
            out.println(possibleMoves);
            out.println("Select your move.");
            String selectedMove = in.nextLine().trim();
            selectedMove = selectedMove.replace(",", "");
            for (Move move : possibleMoves) {
                if (selectedMove.equals(move.toString())) {
                    return move;
                }
            }
            out.println("This was an illegal Move! :O Please select a legal move!\n");
        }
    }

}
