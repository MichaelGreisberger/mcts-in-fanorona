package Fanorona.mcts;

import Fanorona.Board.Board;
import Fanorona.Move.MoveList;

import java.util.HashMap;
import java.util.Map;

public class MctsStateStorage {
    private Map<String, GameStateStatistic> statisticStorage = new HashMap<>();
    private GameStateStatistic proto;

    public MctsStateStorage(GameStateStatistic prototype) {
        proto = prototype;
    }

    public void reset() {
        this.statisticStorage = new HashMap<>();
    }

    public void addWin(String state) {
        addIfAbsent(state);
        statisticStorage.get(state).incWon();
    }

    public void addWin(Iterable<String> wins) {
        wins.forEach(this::addWin);
    }

    public void addLose(Iterable<String> loses) {
        loses.forEach(this::addLose);
    }

    public void addLose(String state) {
        addIfAbsent(state);
        statisticStorage.get(state).incLost();
    }

    public double getDecisionValue(String state) {
        if (!statisticStorage.containsKey(state)) {
            statisticStorage.put(state, proto.getInstance());
        }
        return statisticStorage.get(state).getDecisionValue();
    }

    private void addIfAbsent(String state) {
        if (!statisticStorage.containsKey(state)) {
            statisticStorage.put(state, proto.getInstance());
        }
    }

    public int getStateCount() {
        return statisticStorage.size();
    }

    public GameStateStatistic getStatistics(String state) {
        return statisticStorage.get(state);
    }
}
