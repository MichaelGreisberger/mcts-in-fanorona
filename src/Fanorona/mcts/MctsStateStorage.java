package Fanorona.mcts;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store statistics for certain board states so that computer players can calculate, store and retrieve
 * the game theoretic value of a board state. This helps to choose the best action in a certain state.
 */
class MctsStateStorage {
    private Map<String, GameStateStatistic> statisticStorage = new HashMap<>();
    private GameStateStatistic proto;

    MctsStateStorage(GameStateStatistic prototype) {
        proto = prototype;
    }

    /**
     * Resets this storage
     */
    void reset() {
        this.statisticStorage = new HashMap<>();
    }

    /**
     * Increases the number of won games for all states in {@param states}
     *
     * @param states states for which to increment the win-counter
     */
    void addWin(Iterable<String> states) {
        states.forEach(this::addWin);
    }

    /**
     * Increases the number of lost games for all states in {@param states}
     *
     * @param states states for which to increment the lost-counter
     */
    void addLose(Iterable<String> states) {
        states.forEach(this::addLose);
    }

    /**
     * Increases the number of draw games for all states in {@param states}
     *
     * @param states states for which to increment the draw-counter
     */
    void addDraw(Iterable<String> states) {
        states.forEach(this::addDraw);
    }

    /**
     * Increases the number of won games for {@param state}
     *
     * @param state state for which to increment the win-counter
     */
    void addWin(String state) {
        addIfAbsent(state);
        statisticStorage.get(state).incWon();
    }

    /**
     * Increases the number of lost games for {@param state}
     *
     * @param state state for which to increment the lost-counter
     */
    void addLose(String state) {
        addIfAbsent(state);
        statisticStorage.get(state).incLost();
    }

    /**
     * Increases the number of draw games for {@param state}
     *
     * @param state state for which to increment the draw-counter
     */
    void addDraw(String state) {
        addIfAbsent(state);
        statisticStorage.get(state).incDraw();
    }

    /**
     * @return the number of states stored in this storage
     */
    int getStateCount() {
        return statisticStorage.size();
    }

    /**
     * retrieves the statistics for this state. If no such statistics existed prior to the call, they are created.
     *
     * @param state The state for which to retrieve the statistics
     * @return the statistics for the board specified by {@param state}
     */
    GameStateStatistic getStatistics(String state) {
        addIfAbsent(state);
        return statisticStorage.get(state);
    }

    /**
     * Adds a new instance of the same type as {@code proto} to the storage if none existed.
     */
    private void addIfAbsent(String state) {
        if (!statisticStorage.containsKey(state)) {
            statisticStorage.put(state, proto.getInstance());
        }
    }

}
