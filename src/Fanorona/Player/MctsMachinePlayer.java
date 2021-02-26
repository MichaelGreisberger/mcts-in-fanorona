package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.mcts.GameStateStatistic;
import Fanorona.mcts.MctsStateStorage;
import Fanorona.mcts.SimpleGameStateStatistic;

import java.util.Set;

/**
 * The base class for all artificial players
 */
public abstract class MctsMachinePlayer implements Player {
    /**
     * how long may the simulation run before the next move must be selected.
     */
    final int MILLIS_TO_RUN;

    /**
     * if true additional information is printed to System.out
     */
    private boolean verbose;

    /**
     * Storage of GameStateStatistics to select best moves
     */
    MctsStateStorage storage;

    MctsMachinePlayer(int millisToRun, boolean verbose, MctsStateStorage storage) {
        this.verbose = verbose;
        MILLIS_TO_RUN = millisToRun;
        this.storage = storage;
    }

    MctsMachinePlayer(int millisToRun, boolean verbose) {
        this(millisToRun, verbose, new MctsStateStorage(new SimpleGameStateStatistic()));
    }

    /**
     * @inheritDoc
     */
    @Override
    public abstract Move getNextMove(Board board);

    Move selectBestMove(Board board, MoveList possibleMoves) {
        double maxVal = -Double.MAX_VALUE;
        Move bestMove = null;
        double curVal;
        for (Move move : possibleMoves) {
            curVal = getStatistics(board, move).getReward();
            if (curVal > maxVal) {
                maxVal = curVal;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Prints information to the console. This information contains the number of simulations, the number of states and the
     * possible moves as well as statistics regarding those moves
     *
     * @param board          current board
     * @param possibleMoves  current possible moves
     * @param counter        number of simulated games
     * @param stateCountPrev number of previously stored states
     */
    void printVerbosityMsg(Board board, MoveList possibleMoves, int counter, int stateCountPrev) {
        if (verbose) {
            System.out.println("Simulated " + counter + " games. Thats " + (double)counter / MILLIS_TO_RUN * 1000 + " simulations per second."); //The Simulation is overdue for " + (System.currentTimeMillis() - millisStop) + " millis."
            System.out.println("We discovered " + (storage.getStateCount() - stateCountPrev) + " new States. This means we now store " + storage.getStateCount() + " states!");
            for (Move move : possibleMoves) {
                System.out.println(move.toString() + " " + getStatistics(board, move));
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Player reset() {
        storage.reset();
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void shutDown() {
        //nothing to do here
    }

    protected abstract GameStateStatistic getStatistics(Board board, Move move);

    /**
     * @inheritDoc
     */
    @Override
    public int getNumberOfStoredStates() {
        return storage.getStateCount();
    }

    double getResult(int player, Board board) {
        double result;
        if (board.getBlackPieces() > 0 && board.getWhitePieces() > 0) {
            result = 0.5;
        } else if (player == (board.getCurrentPlayer() ^ 3)) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * back propagates the result to all states of player and opponent
     *
     * @param statesPlayer   states of this player that where touched during a game
     * @param statesOpponent states of the opponent that where touched during a game
     * @param result         result of the game
     */
    void backpropagate(Set<String> statesPlayer, Set<String> statesOpponent, double result) {
        for (String stateB64 : statesPlayer) {
            storage.addState(stateB64).update(result);
        }
        for (String stateB64 : statesOpponent) {
            storage.addState(stateB64).update(1 - result);
        }
    }
}
