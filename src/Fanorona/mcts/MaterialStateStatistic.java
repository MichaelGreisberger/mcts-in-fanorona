package Fanorona.mcts;

public class MaterialStateStatistic implements GameStateStatistic {

    private int count = 0;
    private int materialSum = 0;

    public void update(int material) {
        materialSum += material;
        count++;
    }

    @Override
    public void incWon() {
        count++;
    }

    @Override
    public void incLost() {
        count++;
    }

    @Override
    public void incDraw() {
        count++;
    }

    @Override
    public int getTotalNumberOfPlays() {
        return count;
    }

    @Override
    public double getReward() {
        if (count == 0) {
            return 0;
        } else {
            return (double) materialSum / count;
        }
    }

    @Override
    public GameStateStatistic getInstance() {
        return new MaterialStateStatistic();
    }

    @Override
    public String toString() {
        return "Material: " + materialSum + ", count: " + count + ", reward: " + getReward();
    }
}
