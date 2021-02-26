package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.mcts.GameStateStatistic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * This is a simple implementation of a mcts agent for Fanorona. During the simulation the agent does not use heuristics
 * to select moves. Instead, the next move is randomly selected during the simulation. After a specified time, the simulation
 * is stopped and the move with the highest reward is selected (defined by the statistics used)
 */
public class RandomMctsPlayer extends MctsMachinePlayer {
    /**
     * Random-generator for move-selection during simulation
     */
    private final Random RAND;
    private int latestSimulationCount;

    public RandomMctsPlayer(int millisToRun, boolean verbose) {
        this(millisToRun, verbose, new Random().nextLong());
    }

    public RandomMctsPlayer(int millisToRun, boolean verbose, long seed) {
        super(millisToRun, verbose);
        this.RAND = new Random(seed);
        if (verbose){
            System.out.println("The seed for this RandomMctsPlayer is: " + seed);
        }
    }

    @Override
    public Move getNextMove(Board board) {
        long millisStop = System.currentTimeMillis() + MILLIS_TO_RUN;
        MoveList possibleMoves = board.getPossibleMoves();
        latestSimulationCount = 0;
        int stateCountBefore = storage.getStateCount();
        while (System.currentTimeMillis() < millisStop) {
            simulateGame(board, possibleMoves);
            latestSimulationCount++;
        }
        printVerbosityMsg(board, possibleMoves, latestSimulationCount, stateCountBefore);
        return selectBestMove(board, possibleMoves);
    }

    @Override
    public int getNumberOfSimulatedGames() {
        return latestSimulationCount;
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
        int depth = 0;

        while (moves.size() > 0) {
            board = board.applyMove(getRandomMove(moves));
            if (store) {
                states.add(board.getStateB64());
                store = false;
            } else {
                store = true;
            }
            depth++;
            if (depth >= 100) {
                break; //draw
            }
            moves = board.getPossibleMoves();
        }
        backpropagate(states, new HashSet<>(), getResult(player, board));
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

    @Override
    Move selectBestMove(Board board, MoveList possibleMoves) {
        List<Move> bestMoves = new ArrayList<>(20);
        double maxVal = -1;
        double curVal;
        for (Move move : possibleMoves) {
            curVal = getStatistics(board, move).getReward();
            if (curVal >= maxVal) {
                if (curVal > maxVal) {
                    maxVal = curVal;
                    bestMoves = new ArrayList<>(20);
                }
                bestMoves.add(move);
            }
        }
        return bestMoves.get(RAND.nextInt(bestMoves.size()));
    }

    /**
     * Retrieves the statistics for the board resulting after {@param move} is applied to {@param board}
     *
     * @param board board to which {@param move} is applied
     * @param move  move to apply to {@param board}
     * @return the game statistics for the board resulting after {@param move} is applied to {@param board}
     */
    @Override
    protected GameStateStatistic getStatistics(Board board, Move move) {
        board = board.applyMove(move);
        return storage.getStatistics(board.getStateB64());
    }

}
