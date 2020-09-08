package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.mcts.GameStateStatistic;
import Fanorona.mcts.GameStateStatisticImpl;
import Fanorona.mcts.MctsStateStorage;

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

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose) {
        this(millisToRun, verbose, new Random().nextLong(), 10);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, int uctDepth) {
        this(millisToRun, verbose, new Random().nextLong(), uctDepth);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, long seed, int uctDepth) {
        super(millisToRun, verbose);
        this.RAND = new Random(seed);
        this.UCT_CRITERIA_DEPTH = uctDepth;
        if (verbose) {
            System.out.println("The seed for this RandomMctsPlayer is: " + seed);
        }
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor) {
        this(millisToRun, verbose, explorationFactor, new Random().nextLong());
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, long seed) {
        this(millisToRun, verbose, explorationFactor, seed, 10);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, int depth) {
        this(millisToRun, verbose, explorationFactor, new Random().nextLong(), depth);
    }

    public SteeredRandomMctsPlayer(int millisToRun, boolean verbose, double explorationFactor, long seed, int uctDepth) {
        super(millisToRun, verbose, explorationFactor, "uct");
        this.RAND = new Random(seed);
        this.UCT_CRITERIA_DEPTH = uctDepth;
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
//        MoveList moves = getPossibleMoves(board);
        MoveList moves = board.getPossibleMoves();
        int depth = 0;

        while (moves.size() > 0) {
            if (depth > UCT_CRITERIA_DEPTH) {
                board = board.applyMove(getRandomMove(moves));
            } else {
                board = board.applyMove(getMaxUtcMove(board, moves));
            }
            playerTurn = updateStates(board.getStateB64(), statesPlayer, statesOpponent, playerTurn);
//            moves = getPossibleMoves(board);
            moves = board.getPossibleMoves();
            depth++;
            if (depth >= 100) {
                break; //draw
            }
        }
        int winner = board.getCurrentPlayer() ^ 3;
        backpropagate(statesPlayer, statesOpponent, player, winner, moves.size() > 0);
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
        storage = new MctsStateStorage(new GameStateStatisticImpl());
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