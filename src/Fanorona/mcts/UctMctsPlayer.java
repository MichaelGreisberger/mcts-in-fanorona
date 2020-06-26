package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a more advanced implementation of an mcts agent for Fanorona. During the simulation the agent uses the UCT heuristics
 * to select moves. It uses a formula to decide which move to select next. The formula has an exploration and an exploitation
 * term. These terms ensure the balance between searching for unknown strategies and improving the knowledge of already known ones.
 * After a specified time, the simulation is stopped and the move with the highest reward
 * is selected (defined by the statistics used)
 *
 * @see <a href="https://en.wikipedia.org/wiki/Monte_Carlo_tree_search">www.wikipedia.org/wiki/Monte_Carlo_tree_search</a>
 */
public class UctMctsPlayer extends MctsMachinePlayer {
    /**
     * This variable represents the exploration term Cp from the paper "A Survey of Monte Carlo Tree Search Methods"
     *
     * @see <a href="http://repository.essex.ac.uk/4117/1/MCTS-Survey.pdf">repository.essex.ac.uk/4117/1/MCTS-Survey.pdf</a>
     */
    private final double EXPLORATION_FACTOR;//= 1 / Math.sqrt(2); //Cp

    public UctMctsPlayer(int millisToRun, boolean verbose, double explorationFactor) {
        super(millisToRun, verbose, new MctsStateStorage(new GameStateStatisticImpl()));
        EXPLORATION_FACTOR = explorationFactor;
    }
    public UctMctsPlayer(int millisToRun, boolean verbose) {
        this(millisToRun, verbose, 1 / Math.sqrt(2));
    }

    @Override
    public Move getNextMove(Board board) {
        long millisStop = System.currentTimeMillis() + MILLIS_TO_RUN;
        MoveList possibleMoves = board.getPossibleMoves();
        int counter = 0;
        int stateCountPrev = storage.getStateCount();
        while (System.currentTimeMillis() < millisStop) {
            simulateGame(board, board.getCurrentPlayer());
            counter++;
        }
        printVerbosityMsg(board, possibleMoves, counter, stateCountPrev);
        return selectBestMove(board, possibleMoves);
    }

    /**
     * Simulates a game starting with the position in {@param board}.
     * In every step the next move is the one with the largest UCT-Value
     * The results of the simulation are stored for all visited states with respect to the player that choose the move
     * that led to this state. As soon as a state is visited twice, the simulation is stopped and the game is considered
     * a draw.
     *
     * @param board  The starting-point for the simulation
     * @param player the player at turn
     */
    private void simulateGame(Board board, int player) {
        Set<String> statesPlayer = new HashSet<>();
        Set<String> statesOpponent = new HashSet<>();
        statesPlayer.add(board.getStateB64());
        boolean playerTurn = true;
        MoveList moves = board.getPossibleMoves();

        while (moves.size() > 0) {
            board = board.applyMove(getMaxUtcMove(board));
            if (playerTurn) {
                if (!statesPlayer.add(board.getStateB64())) {
                    break; //draw
                }
                playerTurn = false;
            } else {
                if (!statesOpponent.add(board.getStateB64())) {
                    break; //draw
                }
                playerTurn = true;
            }
            moves = board.getPossibleMoves();
        }
        int winner = board.getCurrentPlayer() ^ 3;
        if (moves.size() > 0) {
            storage.addDraw(statesOpponent);
            storage.addDraw(statesPlayer);
        } else {
            if (player == winner) {
                storage.addWin(statesPlayer);
                storage.addLose(statesOpponent);
            } else {
                storage.addWin(statesOpponent);
                storage.addLose(statesPlayer);
            }
        }
    }

    /**
     * This method selects the move that maximises the UCT value of the resulting board state.
     *
     * @param board the state for which to select a move
     * @return the move that maximises the UCT value of the resulting board state.
     */
    private Move getMaxUtcMove(Board board) {
        GameStateStatistic parentStats = storage.getStatistics(board.getStateB64());
        MoveList moves = board.getPossibleMoves();
        double maxUctValue = -1;
        double curUctValue;
        Move maxUctMove = null;

        for (Move move : moves) {
            GameStateStatistic childStats = getStatistics(board, move);
            curUctValue = caltUct(childStats.getWonGames(), childStats.getTotalNumberOfPlays(), parentStats.getTotalNumberOfPlays());
            if (curUctValue > maxUctValue) {
                maxUctMove = move;
                maxUctValue = curUctValue;
            }
        }
        return maxUctMove;
    }

    /**
     * Calculates the UCT value from the supplied parameters
     *
     * @param reward             the reward value for the resulting board state (after applying the move)
     * @param visitedCountChild  the number of times the resulting board state was visited by the simulation
     * @param visitedCountParent the number of times the current board state was visited by the simulation
     * @return the UCT value for the supplied parameters
     */
    private double caltUct(double reward, int visitedCountChild, int visitedCountParent) {
        if (visitedCountChild == 0) {
            return Double.MAX_VALUE; //child never visited -> should be next
        } else if (visitedCountParent == 0) {
            return Double.MIN_VALUE; //child visited but parent not -> state visited from some other state -> another child should be visited first;
        } else {
            return reward / visitedCountChild + EXPLORATION_FACTOR * Math.sqrt(2 * Math.log(visitedCountParent) / visitedCountChild);
        }
    }

    /**
     * @param board board state to which {@param move} is applied
     * @param move  move to applie to {@param board}
     * @return the GameStateStatistic associated to the board state that results after applying {@param move} to {@param board}
     */
    protected GameStateStatistic getStatistics(Board board, Move move) {
        board = board.applyMove(move);
        return storage.getStatistics(board.getStateB64());
    }
}