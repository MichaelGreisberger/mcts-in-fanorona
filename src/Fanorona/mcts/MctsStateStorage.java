package Fanorona.mcts;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store statistics for certain board states so that computer players can calculate, store and retrieve
 * the game theoretic value of a board state. This helps to choose the best action in a certain state.
 */
public class MctsStateStorage {
    /**
     * Stores statistics for simulated games. The key of this collection is the Base64-encoded state-string of the board
     * for which the statistic is stored.
     */
    private Map<String, GameStateStatistic> statisticStorage = new HashMap<>();
    /**
     * Prototype-Pattern
     * This variable is used to initialize new statistics. This way all different kinds of Statistics can be uses. They
     * only have to implement GameStateStatistic
     */
    private GameStateStatistic proto;

    public MctsStateStorage(GameStateStatistic prototype) {
        proto = prototype;
    }

    public GameStateStatistic addState(String state) {
        addIfAbsent(state);
        return statisticStorage.get(state);
    }

    /**
     * @return the number of states stored in this storage
     */
    public int getStateCount() {
        return statisticStorage.size();
    }

    /**
     * retrieves the statistics for this state. If no such statistics existed prior to the call, they are created.
     *
     * @param state The state for which to retrieve the statistics
     * @return the statistics for the board specified by {@param state}
     */
    public GameStateStatistic getStatistics(String state) {
//        addIfAbsent(state);
        if (statisticStorage.containsKey(state)) {
            return statisticStorage.get(state);
        } else {
            return proto.getInstance();
        }
    }

    /**
     * Adds a new instance of the same type as {@code proto} to the storage if none existed.
     */
    private void addIfAbsent(String state) {
        if (!statisticStorage.containsKey(state)) {
            statisticStorage.put(state, proto.getInstance());
        }
    }

    public void reset() {
        statisticStorage.clear();
    }

}
