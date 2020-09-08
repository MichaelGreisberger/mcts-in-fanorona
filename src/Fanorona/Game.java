package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Move.Move;
import Fanorona.Player.PlayerWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * This is the class that is used to run the game. It initializes the players and "moderates" the game.
 */
public class Game {
    /**
     * First player (always starts the game and plays the white pieces)
     */
    PlayerWrapper player1;

    /**
     * Second player (always plays the black pieces)
     */
    PlayerWrapper player2;

    Board board;

    /**
     * If this is true, additional output is printed to System.out
     */
    private boolean verbose = true;

    /**
     * Stores the last N states of this game (N = 20). Is used for draw-detection
     */
    private Queue<String> recentStates = new LinkedList<>();

    /**
     * Stores the number of revisits of a certain boardstates. Is used for draw-detection
     */
    private Map<String, Integer> revisitCounter = new HashMap<>();

    private GameStatistics statistics = new GameStatistics();

    private boolean writeOutput;

    public Game(BoardSize size, List<String> args, boolean writeOutput) {
        this(new Board(size), args, writeOutput);
    }

    public Game(BoardSize size, boolean verbose, List<String> args, boolean writeOutput) {
        this(size, args, writeOutput);
        this.verbose = verbose;
    }

    public Game(Board board, List<String> args, boolean writeOutput) {
        this.board = board;
        this.statistics.setArguments(args);
        this.writeOutput = writeOutput;
    }

    /**
     * Manages the game. Asks for the next move of the current player, prints messages and keeps track of the status
     * of the game.
     */
    void startGame(int rounds) throws IOException {
        statistics.setP1(player1);
        statistics.setP2(player2);
        PlayerWrapper curr = board.getCurrentPlayer() == 1 ? player1 : player2;
        Move move;
        int i = 0;
        while (i < rounds) {
            printRoundStartMsg(curr.name, curr.color);
            move = curr.getNextMove(board);
            printChosenMoveMSg(curr.name, move.toString());
            board = board.applyMove(move);
            if (hasSomeoneWon()) {
                println(board.toPrintString());
                println(curr.name + " won the game!");
                statistics.setResult(curr.name + " won the game!");
                break;
            }
            if (i >= 99) {
                println(board.toPrintString());
                println("Its a draw!");
                statistics.setResult("Its a draw!");
                break;
            }
            if (curr == player2) {
                curr = player1;
            } else {
                curr = player2;
            }
            i++;
        }
        if (writeOutput) {
            println(statistics.writeStatisticsToTextFile());
            statistics.writeStatisticsToCsvFile();
        } else {
            println(statistics.getPrintableString());
        }
    }

    public void startGame() {
        try {
            startGame(101);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a message that signals the start of a new round to the players.
     *
     * @param name  name of the current player
     * @param color color of the current player
     */
    private void printRoundStartMsg(String name, String color) {
        if (verbose) {
            println("\n" + "<><><><><><><><><><><><><><><><   " + name + "'s Turn (" + color +
                    ")  ><><><><><><><><><><><><><><><><><><>");
            println(board.toPrintString() + "\n");
        }
    }

    /**
     * Prints a message containing the move that the current player chose
     *
     * @param name name of the current player
     * @param move chosen move of the current player
     */
    private void printChosenMoveMSg(String name, String move) {
        if (verbose) {
            println(name + " choose move " + move + " and applies it to board " + board.getStateB64() + "\n");
        }
    }

    /**
     * Determines whether any player won the game
     *
     * @return true if any player won the game, false otherwise.
     */
    private boolean hasSomeoneWon() {
        return board.getPossibleMoves().size() == 0;
    }

    /**
     * checks whether the current game is a draw. This is the case, if a certain state is revisited 3 times in the last
     * 20 rounds.
     *
     * @return true if this game is a draw, false otherwise.
     */
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

    /**
     * exchange the players positions (switch the players)
     */
    public void switchSeats() {
        player1.player = player1.player.reset();
        player2.player = player2.player.reset();
        board.reset();
        statistics.reset();

        PlayerWrapper temp = player1;
        player1 = new PlayerWrapper(player2.player, player2.name, "white");
        player2 = new PlayerWrapper(temp.player, temp.name, "black");
    }

    private void println(String msg) {
        System.out.println(msg);
    }

    public void shutDown() {
        player1.player.shutDown();
        player2.player.shutDown();
    }
}
