package Fanorona.mcts;

public class RandomPlayerGameStateStatistic implements GameStateStatistic {
    private int wonGames = 0;
    private int lostGames = 0;

    @Override
    public void incWon() {
        wonGames++;
    }

    @Override
    public void incLost() {
        lostGames++;
    }

    @Override
    public double getDecisionValue() {
        if (wonGames == 0) {
            return 0;
        } else if (lostGames == 0) {
            return wonGames;
        } else {
            return (double) wonGames / lostGames;
        }
    }

    @Override
    public GameStateStatistic getInstance() {
        return new RandomPlayerGameStateStatistic();
    }

    @Override
    public String toString() {
        return "Won: " + wonGames + ", lost: " + lostGames + ", decisionValue: " + getDecisionValue();
    }

}
