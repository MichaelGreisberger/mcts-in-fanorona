package Fanorona.mcts;

/**
 * A simple implementation of GameStateStatistic
 * The reward value is calculated as the percentage of won games with respect to won, lost and draw games.
 */
public class UctStateStatistics implements GameStateStatistic {
    private int wonGames = 0;
    private int lostGames = 0;
    private int drawGames = 0;

    @Override
    public double getReward() {
        if (wonGames + drawGames + lostGames == 0) {
            return 0;
        } else {
            return ((double) wonGames) / (wonGames + lostGames + drawGames);
        }
    }

    @Override
    public void update(double result) {
        if (result == 0) {
            drawGames++;
        } else if (result == 1) {
            wonGames++;
        } else {
            lostGames++;
        }
    }

    @Override
    public int getCount() {
        return wonGames + lostGames + drawGames;
    }

    @Override
    public GameStateStatistic getInstance() {
        return new UctStateStatistics();
    }

    @Override
    public String toString() {
        return "Won: " + wonGames + ", lost: " + lostGames + ", draw: " + drawGames + ", count: " + getCount() + ", decisionValue: " + getReward();
    }

}
