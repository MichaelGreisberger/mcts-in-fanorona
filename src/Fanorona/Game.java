package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Move.Move;
import Fanorona.mcts.UctMctsPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * This is the class that is used to run the game. It initializes the players and "moderates" the game.
 */
public class Game {
    /**
     * First player (always starts the game and plays the white pieces)
     */
    private PlayerWrapper player1;

    /**
     * Second player (always plays the black pieces)
     */
    private PlayerWrapper player2;

    private Board board;

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

    /**
     * Used to read data from the console
     */
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Main entry point for the application. Initializes the game from the Console.
     */
    public static void main(String[] args) {
        Game game = new Game(BoardSize.LARGE);
        try {
            game.initializeFromStreams();
            game.runGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game(BoardSize size) {
        this.board = new Board(size);
    }

    public Game(BoardSize size, boolean verbose) {
        this(size);
        this.verbose = verbose;
    }

    /**
     * Initializes the game with the provided streams in Question/Answer kind of way so only use this for streams that
     * are somehow connected to either a human being or HAL 9000.
     */
    public void initializeFromStreams() throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        if (answerWasYes("Do you want to play against AI?")) {
            if (answerWasYes("Would you like to be the starting player?")) {
                System.out.println("Whats your Name?");
                player1 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "white");
                player2 = new PlayerWrapper(new UctMctsPlayer(1000, verbose), "AI", "black");
            } else {
                System.out.println("Whats your Name?");
                player1 = new PlayerWrapper(new UctMctsPlayer(1000, verbose), "AI", "white");
                player2 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "black");
            }
        } else {
            if (answerWasYes("Do you want to play against another human player?")) {
                System.out.println("Whats the name of player1?");
                player1 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "white");
                System.out.println("Whats the name of player2?");
                player2 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "black");
            } else {
                if (answerWasYes("Do you want to watch two AI players play against each other?!")) {
                    if (!answerWasYes("Do you want to see moves and player outputs?")) {
                        verbose = false;
                    }
                    player1 = new PlayerWrapper(new UctMctsPlayer(1500, verbose), "UCT 1", "white");
                    player2 = new PlayerWrapper(new UctMctsPlayer(1500, verbose), "UCT 2", "black");
                } else {
                    System.out.println("What the fuck do you want than?!?");
                    System.out.println("AHH never mind.. I'm out!");
                    throw new RuntimeException("Game doesn't want to play with you any longer.");
                }
            }
        }
    }

    /**
     * Writes the {@param question} to the Console and reads the answer. If the answer started with "y" then true is returned.
     * @param question The question to answer
     * @return the answer to the question
     */
    private boolean answerWasYes(String question) throws IOException {
        System.out.println(question + " (y/n)");
        return in.readLine().toLowerCase().startsWith("y");
    }

    /**
     * Manages the game. Asks for the next move of the current player, prints messages and keeps track of the status
     * of the game.
     */
    public void runGame() {
        PlayerWrapper curr = board.getCurrentPlayer() == 1 ? player1 : player2;
        Move move;
        while (true) {
            printRoundStartMsg(curr.name, curr.color);
            move = curr.player.getNextMove(board);
            printChosenMoveMSg(curr.name, move.toString());
            board = board.applyMove(move);
            if (hasSomeoneWon()) {
                System.out.println(board.toPrintString());
                System.out.println(curr.name + " won the game!");
                curr.incWon();
//                printWinnerMsg(out);
                return;
            }
            if (isDraw()) {
                System.out.println(board.toPrintString());
                System.out.println("Its a draw!");
//                printWinnerMsg(out);
                return;
            }
            if (curr == player2) {
                curr = player1;
            } else {
                curr = player2;
            }
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
            System.out.println("\n" + "<><><><><><><><><><><><><><><><   " + name + "'s Turn (" + color +
                    ")  ><><><><><><><><><><><><><><><><><><>" + "\n" + board.toPrintString() + "\n");
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
            System.out.println(name + " choose move " + move + " and applies it to board " + board.getStateB64() + "\n");
        }
    }

    /**
     * Prints a message summarizing the game statistics of the two players. This message only makes sense if this game
     * is a multi-round game.
     */
    private void printWinnerMsg() {
        System.out.println(player2.name + " (" + player2.color + ") won " + player2.wonGames + " times and " +
                player1.name + " (" + player1.color + ") won " + player1.wonGames + " times. This means that " +
                player1.name + " won " + (((double) player1.wonGames) / (player1.wonGames + player2.wonGames) * 100) +
                "% of all games!");
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
    public void switchTables() {
        PlayerWrapper temp = player1;
        player1 = player2;
        player1.color = "white";
        player2 = temp;
        player2.color = "black";
    }

    /**
     * A class Game uses to add metadata to players (colors, names, won games)
     */
    private class PlayerWrapper {
        Player player;
        String name;
        String color;
        int wonGames = 0;

        PlayerWrapper(Player player, String name, String color) {
            this.player = player;
            this.name = name;
            this.color = color;
        }

        void incWon() {
            wonGames++;
        }
    }
}
