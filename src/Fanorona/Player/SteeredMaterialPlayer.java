package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.MoveList;
import Fanorona.mcts.MaterialStateStatistic;
import Fanorona.mcts.MctsStateStorage;

import java.util.HashSet;
import java.util.Set;

public class SteeredMaterialPlayer extends SteeredMctsPlayer {
    /**
     * This constant defines after which depth the default-policy is used.
     */
    public final int MATERIAL_DEPTH;

    public SteeredMaterialPlayer(int millisToRun) {
        super(millisToRun, true);
        MATERIAL_DEPTH = 10;
    }

    public SteeredMaterialPlayer(int millisToRun, boolean verbose, double explorationFactor, String type) {
        this(millisToRun, verbose, explorationFactor, 10, type);
    }

    public SteeredMaterialPlayer(int millisToRun, boolean verbose, double explorationFactor, int materialDepth, String type) {
        super(millisToRun, verbose, explorationFactor, new MctsStateStorage(new MaterialStateStatistic()), type);
        this.MATERIAL_DEPTH = materialDepth;
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
        boolean playerTurn = true;
        int depth = 0;

        statesPlayer.add(board.getStateB64());
        MoveList moves = board.getPossibleMoves();

        while (moves.size() > 0 && depth < MATERIAL_DEPTH * 2) {
            board = board.applyMove(getMaxUtcMove(board, moves));
            playerTurn = updateStates(board.getStateB64(), statesPlayer, statesOpponent, playerTurn);
            moves = board.getPossibleMoves();
            depth++;
        }
        backpropagate(statesPlayer, statesOpponent, material(board));
    }

    private int material(Board board) {
        if (board.getCurrentPlayer() == 1) {
            return board.getBlackPieces() - board.getWhitePieces();
        } else {
            return board.getWhitePieces() - board.getBlackPieces();
        }
    }

    protected void backpropagate(Set<String> statesPlayer, Set<String> statesOpponent, int material) {
        for (String stateB64 : statesPlayer) {
            ((MaterialStateStatistic) storage.addState(stateB64)).update(material);
        }
        for (String stateB64 : statesOpponent) {
            ((MaterialStateStatistic) storage.addState(stateB64)).update(-material);
        }
    }
}
