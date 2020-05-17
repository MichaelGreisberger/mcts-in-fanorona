package Fanorona.mcts;

public class RandomPlayerGameStateStatistic2 implements GameStateStatistic{
    private int wonGames = 0;
    private int lostGames = 0;

    public void incWon() {
        wonGames++;
    }

    public void incLost() {
        lostGames++;
    }

    public double getDecisionValue() {
        if (wonGames + lostGames == 0) {
            return 0;
        } else {
            return (double) wonGames / (wonGames + lostGames);
        }
    }

    @Override
    public GameStateStatistic getInstance() {
        return new RandomPlayerGameStateStatistic2();
    }

    @Override
    public String toString() {
        return "Won: " + wonGames + ", lost: " + lostGames + ", decisionValue: " + getDecisionValue();
    }
}
