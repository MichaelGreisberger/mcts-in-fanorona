package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.MoveList;

import java.util.HashMap;
import java.util.Map;

public class MctsStateStorage {
    private Map<String, GameStateStatistic> statisticStore = new HashMap<>();
    private Map<String, MoveList> moveStore = new HashMap<>();
    private GameStateStatistic proto;

    public MctsStateStorage(GameStateStatistic prototype) {
        proto = prototype;
    }

    public void addWin(String state) {
        addIfAbsent(state);
        statisticStore.get(state).incWon();
    }

    public void addWin(Iterable<String> wins) {
        wins.forEach(this::addWin);
    }

    public void addLose(Iterable<String> loses) {
        loses.forEach(this::addLose);
    }

    public void addLose(String state) {
        addIfAbsent(state);
        statisticStore.get(state).incLost();
    }

    public double getDecisionValue(String state) {
        if (!statisticStore.containsKey(state)) {
            statisticStore.put(state, proto.getInstance());
        }
        return statisticStore.get(state).getDecisionValue();
    }

    private void addIfAbsent(String state) {
        if (!statisticStore.containsKey(state)) {
            statisticStore.put(state, proto.getInstance());
        }
    }

    public MoveList getMoves(Board board) {
        String s64 = board.getStateB64();
        if (!moveStore.containsKey(s64)) {
            moveStore.put(s64, board.getPossibleMoves());
        }
        return moveStore.get(s64);
    }

    public int getStateCount() {
        return statisticStore.size();
    }

    public GameStateStatistic getStatistics(String state) {
        return statisticStore.get(state);
    }
}
