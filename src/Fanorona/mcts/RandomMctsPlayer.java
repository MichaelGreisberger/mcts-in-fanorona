package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;
import Fanorona.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomMctsPlayer implements Player {
    private final MctsGameStore gameStore;
    private final int millisToRun;
    private final String name;
    private Board board;
    private int player;
    private MoveList possibleMoves;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    Random rand = new Random();

    public RandomMctsPlayer(Board board, int player, int millisToRun, String name, GameStateStatistic statistic) {
        this.gameStore = new MctsGameStore(statistic);
        this.board = board;
        this.player = player;
        this.millisToRun = millisToRun;
        this.name = name;
    }

    public RandomMctsPlayer(Board board, int player, int millisToRun, String name) {
        this.gameStore = new MctsGameStore(new RandomPlayerGameStateStatistic());
        this.board = board;
        this.player = player;
        this.millisToRun = millisToRun;
        this.name = name;
    }

    @Override
    public Move getNextMove() {
        long millisStop = System.currentTimeMillis() + millisToRun;
        possibleMoves = gameStore.getMoves(board);
        while (System.currentTimeMillis() < millisStop) {
            simulateGame(possibleMoves);
        }
        return chooseNextMove();
    }

    private Move chooseNextMove() {
        double maxVal = -1;
        Move curBestMove = null;
        double curVal;
        for (Move move : possibleMoves) {
            Board board = this.board.getCopy();
            board.applyMove(move);
            curVal = gameStore.getDecisionValue(board.getStateB64());
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
            moves = gameStore.getMoves(board);
        }
        int winner = board.getCurrentPlayer() ^ 3;
        if (player == winner) {
            saveWin(states);
        } else {
            saveLose(states);
        }
    }

    private void saveLose(Iterable<String> states) {
        for (String s : states) {
            gameStore.addLose(s);
        }
    }

    private void saveWin(Iterable<String> states) {
        for (String s : states) {
            gameStore.addWin(s);
        }
    }

    private Move getRandomMove(MoveList moves) {
        return moves.get(getBoundRand(moves.size()));
    }

    private int getBoundRand(int bound) {
        return (int) (rand.nextDouble() * bound);
    }

    public GameStateStatistic getStatistics(Move move) {
        Board board = this.board.getCopy();
        board.applyMove(move);
        return gameStore.getStatistics(board.getStateB64());
    }

}
