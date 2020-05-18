package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomMctsPlayer implements Player {
    private final int MILLIS_TO_RUN;
    private final String NAME;
    private final Random RAND = new Random();
    private MctsStateStorage storage;
    private Board board;
    private MoveList possibleMoves;
    private boolean verbose = true;

    //TODO: Delete this!
    private int wins = 0;
    public void incWin() {
        wins++;
    }
    public int getWins() {
        return wins;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void reset() {
        storage.reset();
    }


    public RandomMctsPlayer(Board board, int millisToRun, String name, GameStateStatistic statistic) {
        this.storage = new MctsStateStorage(statistic);
        this.board = board;
        this.MILLIS_TO_RUN = millisToRun;
        this.NAME = name;
    }

    public RandomMctsPlayer(Board board, int millisToRun, String name) {
        this.storage = new MctsStateStorage(new RandomPlayerGameStateStatistic());
        this.board = board;
        this.MILLIS_TO_RUN = millisToRun;
        this.NAME = name;
    }

    @Override
    public Move getNextMove() {
        long millisStop = System.currentTimeMillis() + MILLIS_TO_RUN;
        possibleMoves = board.getPossibleMoves();
        int counter = 0;
        int stateCountbefore = storage.getStateCount();
        while (System.currentTimeMillis() < millisStop) {
            simulateGame(possibleMoves, board.getCurrentPlayer());
            counter++;
        }
        if (verbose) {
            System.out.println("Simulated " + counter + " games. Thats " + counter / MILLIS_TO_RUN * 1000 + " simulations per second. The Simulation is overdue for " + (System.currentTimeMillis() - millisStop) + " millis.");
            System.out.println("We discovered " + (storage.getStateCount() - stateCountbefore) + " new States. This means we now store " + storage.getStateCount() + " states!");
            for (Move move : possibleMoves) {
                System.out.println(move.toString() + " " + getStatistics(move));
            }
        }
        return selectBestMove();
    }

    private Move selectBestMove() {
        double maxVal = -1;
        Move curBestMove = null;
        double curVal;
        for (Move move : possibleMoves) {
            Board board = this.board.applyMove(move);
            curVal = storage.getDecisionValue(board.getStateB64());
            if (curVal > maxVal) {
                maxVal = curVal;
                curBestMove = move;
            }
        }
        return curBestMove;
    }

    private void simulateGame(MoveList moves, int player) {
        Board board = this.board.getCopy();
        Set<String> states = new HashSet<>();
        boolean store = true;

        while (moves.size() > 0) {
            board = board.applyMove(getRandomMove(moves));
            if (store) {
                states.add(board.getStateB64());
                store = false;
            } else {
                store = true;
            }
            moves = board.getPossibleMoves();
        }
        int winner = board.getCurrentPlayer() ^ 3;
        if (player == winner) {
            storage.addWin(states);
        } else {
            storage.addLose(states);
        }
    }

    private Move getRandomMove(MoveList moves) {
        return moves.get(getBoundRand(moves.size()));
    }

    private int getBoundRand(int bound) {
        return (int) (RAND.nextDouble() * bound);
    }

    public GameStateStatistic getStatistics(Move move) {
        Board board = this.board.applyMove(move);
        return storage.getStatistics(board.getStateB64());
    }

}
