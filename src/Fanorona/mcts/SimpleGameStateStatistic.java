package Fanorona.mcts;

/**
 * A simple implementation of the game state. This implementation only stores the number of times a state was visited and
 * the sum of all rewards achieved during those visits.
 */
public class SimpleGameStateStatistic implements GameStateStatistic {

    private int count = 0;
    private double results = 0;

    @Override
    public void update(double result) {
        results += result;
        count++;
    }

    @Override
    public int getCount() {
        return count;
    }

    /**
     * @return the average reward of all games that touched this state.
     */
    @Override
    public double getReward() {
        if (count == 0) {
            return 0;
        } else {
            return results / count;
        }
    }

    @Override
    public GameStateStatistic getInstance() {
        return new SimpleGameStateStatistic();
    }

    @Override
    public String toString() {
        return "sum of the results: " + results + ", count: " + count + ", reward: " + getReward();
    }
}
