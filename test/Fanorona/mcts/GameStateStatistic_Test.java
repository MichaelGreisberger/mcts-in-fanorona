package Fanorona.mcts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameStateStatistic_Test {

    @Test
    void DecisionValueUltStateStatistic_success() {
        GameStateStatistic statistic = new UctStateStatistics();
        statistic.update(1);
        statistic.update(-1);
        Assertions.assertEquals(0.5D, statistic.getReward());
    }

    @Test
    void DecisionValueSimpleGameStateStatistic_success() {
        GameStateStatistic statistic = new SimpleGameStateStatistic();
        statistic.update(1);
        statistic.update(-1);
        Assertions.assertEquals(0.0D, statistic.getReward());
    }
}
