package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.mcts.GameStateStatistic;
import Fanorona.mcts.MctsStateStorage;
import Fanorona.mcts.SimpleGameStateStatistic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This is another mcts agent for Fanorona using the UCT-Criteria as tree policy and random selection as default policy.
 * For more information on UTC look at SteeredMctsPlayer.
 */
public class SteeredRandomMctsPlayer extends SteeredMctsPlayer {

    /**
     * Random-generator for move-selection during simulation
     */
    private final Random RAND;

    private Map<String, MoveList> moveDict = new HashMap<>();
    /**
     * This constant defines after which depth the default-policy is used.
     */
    public final int UCT_CRITERIA_DEPTH;

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, String type) {
        this(millisToRun, verbose, new Random().nextLong(), 10, type);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, int uctDepth, String type) {
        this(millisToRun, verbose, new Random().nextLong(), uctDepth, type);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, long seed, int uctDepth, String type) {
        super(millisToRun, verbose, type);
        this.RAND = new Random(seed);
        this.UCT_CRITERIA_DEPTH = uctDepth;
        if (verbose) {
            System.out.println("The seed for this RandomMctsPlayer is: " + seed);
        }
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, String type) {
        this(millisToRun, verbose, explorationFactor, new Random().nextLong(), type);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, long seed, String type) {
        this(millisToRun, verbose, explorationFactor, seed, 10, type);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, int depth, String type) {
        this(millisToRun, verbose, explorationFactor, new Random().nextLong(), depth, type);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, long seed, int depth, String type) {
        super(millisToRun, verbose, explorationFactor, type);
        this.RAND = new Random(seed);
        this.UCT_CRITERIA_DEPTH = depth;
        if (verbose) {
            System.out.println("The seed for this RandomMctsPlayer is: " + seed);
        }
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
    @Override
    protected void simulateGame(Board board, int player) {
        Set<String> statesPlayer = new HashSet<>();
        Set<String> statesOpponent = new HashSet<>();
        statesPlayer.add(board.getStateB64());
        boolean playerTurn = true;
        MoveList moves = board.getPossibleMoves();
        int depth = 0;

        while (moves.size() > 0) {
            if (depth < UCT_CRITERIA_DEPTH * 2) {
                board = board.applyMove(getMaxUtcMove(board, moves));
            } else {
                board = board.applyMove(getRandomMove(moves));
            }
            playerTurn = updateStates(board.getStateB64(), statesPlayer, statesOpponent, playerTurn);
            moves = board.getPossibleMoves();
            depth++;
            if (depth >= 100) {
                break; //draw
            }
        }
        backpropagate(statesPlayer, statesOpponent, getResult(player, board));
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

    private MoveList getPossibleMoves(Board board) {
        String key = board.getStateB64();
        if (!moveDict.containsKey(key)) {
            moveDict.put(key, board.getPossibleMoves());
        }
        return moveDict.get(key);
    }

    @Override
    public Player reset() {
        storage = new MctsStateStorage(new SimpleGameStateStatistic());
        moveDict = new HashMap<>();
        return this;
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