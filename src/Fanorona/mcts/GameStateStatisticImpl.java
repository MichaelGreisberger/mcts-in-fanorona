package Fanorona.mcts;

/**
 * A simple implementation of GameStateStatistic
 * The reward value is calculated as the percentage of won games with respect to won, lost and draw games.
 */
public class GameStateStatisticImpl implements GameStateStatistic {
    private int wonGames = 0;
    private int lostGames = 0;
    private int drawGames = 0;

    @Override
    public void incWon() {
        wonGames++;
    }

    @Override
    public void incLost() {
        lostGames++;
    }

    @Override
    public void incDraw() {
        drawGames++;
    }

    @Override
    public double getReward() {
        if (wonGames + drawGames + lostGames == 0) {
            return 0;
        } else {
            return (double) wonGames / (wonGames + lostGames + drawGames);
        }
    }

    @Override
    public int getTotalNumberOfPlays() {
        return wonGames + lostGames + drawGames;
    }

    @Override
    public GameStateStatistic getInstance() {
        return new GameStateStatisticImpl();
    }

    @Override
    public String toString() {
        return "Won: " + wonGames + ", lost: " + lostGames + ", draw: " + drawGames + ", decisionValue: " + getReward();
    }

}
