package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player;

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
public class UctMctsPlayer implements Player {
    private static final double EXPLORATION_FACTOR = 1 / Math.sqrt(2); //Cp
    private final MctsStateStorage storage;
    private final int MILLIS_TO_RUN;
    private boolean verbose;

    public UctMctsPlayer(int millisToRun, boolean verbose) {
        this.storage = new MctsStateStorage(new GameStateStatisticImpl());
        this.MILLIS_TO_RUN = millisToRun;
        this.verbose = verbose;
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
        if (verbose) {
            System.out.println("Simulated " + counter + " games. Thats " + counter / MILLIS_TO_RUN * 1000 + " simulations per second. The Simulation is overdue for " + (System.currentTimeMillis() - millisStop) + " millis.");
            System.out.println("We discovered " + (storage.getStateCount() - stateCountPrev) + " new States. This means we now store " + storage.getStateCount() + " states!");
            for (Move move : possibleMoves) {
                System.out.println(move.toString() + " " + getStatistics(board, move));
            }
        }
        return selectBestMove(board, possibleMoves);
    }

    private Move selectBestMove(Board board, MoveList possibleMoves) {
        double maxReward = -1;
        Move bestMove = null;
        double curVal;
        for (Move move : possibleMoves) {
            curVal = getStatistics(board, move).getReward();
            if (curVal > maxReward) {
                maxReward = curVal;
                bestMove = move;
            }
        }
        return bestMove;
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
    private GameStateStatistic getStatistics(Board board, Move move) {
        board = board.applyMove(move);
        return storage.getStatistics(board.getStateB64());
    }

}
