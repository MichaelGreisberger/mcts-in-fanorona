package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomMctsPlayer implements Player {
    private final MctsStateStorage STORE;
    private final int MILLIS_TO_RUN;
    private final String NAME;
    private final Random RAND = new Random();
    private Board board;
    private int player;
    private MoveList possibleMoves;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }


    public RandomMctsPlayer(Board board, int player, int millisToRun, String name, GameStateStatistic statistic) {
        this.STORE = new MctsStateStorage(statistic);
        this.board = board;
        this.player = player;
        this.MILLIS_TO_RUN = millisToRun;
        this.NAME = name;
    }

    public RandomMctsPlayer(Board board, int player, int millisToRun, String name) {
        this.STORE = new MctsStateStorage(new RandomPlayerGameStateStatistic());
        this.board = board;
        this.player = player;
        this.MILLIS_TO_RUN = millisToRun;
        this.NAME = name;
    }

    @Override
    public Move getNextMove() {
        long millisStop = System.currentTimeMillis() + MILLIS_TO_RUN;
        possibleMoves = STORE.getMoves(board);
        int counter = 0;
        int stateCountbefore = STORE.getStateCount();
        while (System.currentTimeMillis() < millisStop) {
            simulateGame(possibleMoves);
            counter++;
        }
        System.out.println("Simulated " + counter + " games. Thats " + counter / MILLIS_TO_RUN * 1000 + " simulations per second. The Simulation is overdue for " + (System.currentTimeMillis() - millisStop) + " millis.");
        System.out.println("We discovered " + (STORE.getStateCount() - stateCountbefore) + " new States. This means we now store " + STORE.getStateCount() + " states!");
        return selectBestMove();
    }

    private Move selectBestMove() {
        double maxVal = -1;
        Move curBestMove = null;
        double curVal;
        for (Move move : possibleMoves) {
            Board board = this.board.getCopy();
            board.applyMove(move);
            curVal = STORE.getDecisionValue(board.getStateB64());
            if (curVal > maxVal) {
                maxVal = curVal;
                curBestMove = move;
            }
        }
        return curBestMove;
    }

    private void simulateGame(MoveList moves) {
        Board board = this.board.getCopy();
        Set<String> states = new HashSet<>();
        while (moves.size() > 0) {
            states.add(board.getStateB64());
            board.applyMove(getRandomMove(moves));
            moves = STORE.getMoves(board);
        }
        int winner = board.getCurrentPlayer() ^ 3;
        if (player == winner) {
            STORE.addWin(states);
        } else {
            STORE.addLose(states);
        }
    }

    private Move getRandomMove(MoveList moves) {
        return moves.get(getBoundRand(moves.size()));
    }

    private int getBoundRand(int bound) {
        return (int) (RAND.nextDouble() * bound);
    }

    public GameStateStatistic getStatistics(Move move) {
        Board board = this.board.getCopy();
        board.applyMove(move);
        return STORE.getStatistics(board.getStateB64());
    }

}
