package Fanorona.mcts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameStateStatistic_Test {

    @Test
    void DesicionValue_success() {
        GameStateStatistic statistic = new GameStateStatisticImpl();
        statistic.incWon();
        statistic.incLost();
        Assertions.assertEquals(0.5D, statistic.getReward());
    }
}
