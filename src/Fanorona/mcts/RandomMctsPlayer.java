package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This is a simple implementation of a mcts agent for Fanorona. During the simulation the agent does not use heuristics
 * to select moves. Instead, the next move is randomly selected during the simulation. After a specified time, the simulation
 * is stopped and the move with the highest reward is selected (defined by the statistics used)
 */
public class RandomMctsPlayer implements Player {
    /**
     * how long may the simulation run before the next move must be selected.
     */
    private final int MILLIS_TO_RUN;
    /**
     * Random-generator for move-selection during simulation
     */
    private final Random RAND;
    /**
     * Storage of GameStateStatistics to select best moves
     */
    private MctsStateStorage storage;
    /**
     * if true additional information is printed to System.out
     */
    private boolean verbose;

    public RandomMctsPlayer(int millisToRun, boolean verbose) {
        this(millisToRun, verbose, new Random().nextLong());
    }

    public RandomMctsPlayer(int millisToRun, boolean verbose, long seed) {
        this.storage = new MctsStateStorage(new GameStateStatisticImpl());
        this.MILLIS_TO_RUN = millisToRun;
        this.verbose = verbose;
        this.RAND = new Random(seed);
        if (verbose){
            System.out.println("The seed for this RandomMctsPlayer is: " + seed);
        }
    }

    @Override
    public Move getNextMove(Board board) {
        long millisStop = System.currentTimeMillis() + MILLIS_TO_RUN;
        MoveList possibleMoves = board.getPossibleMoves();
        int counter = 0;
        int stateCountbefore = storage.getStateCount();
        while (System.currentTimeMillis() < millisStop) {
            simulateGame(board, possibleMoves);
            counter++;
        }
        if (verbose) {
            System.out.println("Simulated " + counter + " games. Thats " + counter / MILLIS_TO_RUN * 1000 + " simulations per second. The Simulation is overdue for " + (System.currentTimeMillis() - millisStop) + " millis.");
            System.out.println("We discovered " + (storage.getStateCount() - stateCountbefore) + " new States. This means we now store " + storage.getStateCount() + " states!");
            for (Move move : possibleMoves) {
                System.out.println(move.toString() + " " + getStatistics(board, move));
            }
        }
        return selectBestMove(board, possibleMoves);
    }

    private Move selectBestMove(Board board, MoveList possibleMoves) {
        double maxVal = -1;
        Move curBestMove = null;
        double curVal;
        for (Move move : possibleMoves) {
            curVal = getStatistics(board, move).getReward();
            if (curVal > maxVal) {
                maxVal = curVal;
                curBestMove = move;
            }
        }
        return curBestMove;
    }

    /**
     * Simulates a random game starting with the position in {@code board}.
     * The results of the simulation are stored for all visited states where the acting player is this agent.
     */
    private void simulateGame(Board board, MoveList possibleMoves) {
        boolean store = true;
        int player = board.getCurrentPlayer();
        MoveList moves = possibleMoves;
        Set<String> states = new HashSet<>();

        while (moves.size() > 0) {
            board = board.applyMove(getRandomMove(moves));
            if (store) {
                if (!states.add(board.getStateB64())) {
                    break; //draw
                }
                store = false;
            } else {
                store = true;
            }
            moves = board.getPossibleMoves();
        }

        if (moves.size() > 0) {
            storage.addDraw(states);
        } else {
            int winner = board.getCurrentPlayer() ^ 3;
            if (player == winner) {
                storage.addWin(states);
            } else {
                storage.addLose(states);
            }
        }
    }

    /**
     * @param moves moves from which to choose one at random
     * @return a move selected from {@param moves} at random
     */
    private Move getRandomMove(MoveList moves) {
        return moves.get(getBoundRand(moves.size()));
    }

    /**
     * @param bound upper bound of the random value
     * @return a random value between 0 (inclusive) and bound (exclusive)
     */
    private int getBoundRand(int bound) {
        return (int) ((RAND.nextDouble() - Double.MIN_VALUE) * bound);
    }

    /**
     * Retrieves the statistics for the board resulting after {@param move} is applied to {@param board}
     *
     * @param board board to which {@param move} is applied
     * @param move  move to apply to {@param board}
     * @return the game statistics for the board resulting after {@param move} is applied to {@param board}
     */
    private GameStateStatistic getStatistics(Board board, Move move) {
        board = board.applyMove(move);
        return storage.getStatistics(board.getStateB64());
    }

}
