package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Main;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player.MsgDelegates.HumanPlayerCommunicationDelegate;
import Fanorona.Player.MsgDelegates.StreamPlayerCommunicationDelegate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This implementation of {@code Player} uses streams for communication. Those streams could connect to a console or
 * another program.
 */
public class StreamPlayer implements Player {
    private final StreamPlayerCommunicationDelegate msgDelegate;
    private BufferedReader in;
    private PrintWriter out;
    private Process process;
    private String cmd;
    private Socket socket;

    private StreamPlayer(InputStream in, OutputStream out, StreamPlayerCommunicationDelegate msgDelegate) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(new OutputStreamWriter(out), true);
        this.msgDelegate = msgDelegate;
    }

    private StreamPlayer(InputStream in, OutputStream out, StreamPlayerCommunicationDelegate msgDelegate, Process process, String cmd) {
        this(in, out, msgDelegate);
        this.process = process;
        this.cmd = cmd;
    }

    private StreamPlayer(InputStream in, OutputStream out, StreamPlayerCommunicationDelegate msgDelegate, Socket socket) {
        this(in, out, msgDelegate);
        this.socket = socket;
    }

    public StreamPlayer(InputStream in, OutputStream out) {
        this(in, out, new HumanPlayerCommunicationDelegate());
    }

    public static Player initWithSchaddExecutable(int millisToRun, String schaddFilePath, StreamPlayerCommunicationDelegate delegate, int schaddPlayerType, boolean verbose) throws IOException {
        String cmd = "java -jar " + schaddFilePath + " " + schaddPlayerType + " " + (verbose ? 1 : 0) + " " + millisToRun;
        System.out.println("command for schadd player: " + cmd);
        Process process = execCmd(cmd);
        if (process.isAlive()) {
            return new StreamPlayer(process.getInputStream(), process.getOutputStream(), delegate, process, cmd);
        } else {
            Scanner scanner = new Scanner(process.getInputStream());
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            throw new IOException("Couldn't start process! Exit Code: " + process.exitValue() + ", info: " + process.info().toString() + "\noutput: " + sb.toString());
        }
    }

    public static Player initWithSchaddExecutable(String cmd, StreamPlayerCommunicationDelegate delegate) throws IOException {
        Process process = execCmd(cmd);
        return new StreamPlayer(process.getInputStream(), process.getOutputStream(), delegate, process, cmd);
    }

    private static Process execCmd(String cmd) throws IOException {
        return Runtime.getRuntime().exec(cmd, null, new File(Main.tmpdir));
    }

    @Override
    public Move getNextMove(Board board) throws IOException {
        while (true) {
            out.println(msgDelegate.getNextMoveMsg(board));
            Move selectedMove = msgDelegate.parseMoveMsg(in);
            MoveList possibleMoves = board.getPossibleMoves();
            for (Move move : possibleMoves) {
                if (selectedMove.toString().equals(move.toString())) {
                    return move;
                }
            }
            out.println(msgDelegate.getIllegalMoveMsg(board));
        }
    }

    @Override
    public Player reset() {
        if (process != null) {
            process.destroy();
            try {
                process.waitFor();
                return initWithSchaddExecutable(cmd, msgDelegate);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Error reseting Player");
    }

    @Override
    public int getNumberOfStoredStates() {
        return 0;
    }

    @Override
    public int getNumberOfSimulatedGames() {
        return 0;
    }

    @Override
    public void shutDown() {
        if (process != null) {
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
