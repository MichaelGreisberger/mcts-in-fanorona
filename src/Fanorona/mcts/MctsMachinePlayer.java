package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player;

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

    @Override
    public abstract Move getNextMove(Board board);

    Move selectBestMove(Board board, MoveList possibleMoves) {
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

    void printVerbosityMsg(Board board, MoveList possibleMoves, int counter, int stateCountPrev) {
        if (verbose) {
            System.out.println("Simulated " + counter + " games. Thats " + (double)counter / MILLIS_TO_RUN * 1000 + " simulations per second."); //The Simulation is overdue for " + (System.currentTimeMillis() - millisStop) + " millis."
            System.out.println("We discovered " + (storage.getStateCount() - stateCountPrev) + " new States. This means we now store " + storage.getStateCount() + " states!");
            for (Move move : possibleMoves) {
                System.out.println(move.toString() + " " + getStatistics(board, move));
            }
        }
    }

    protected abstract GameStateStatistic getStatistics(Board board, Move move);
}
