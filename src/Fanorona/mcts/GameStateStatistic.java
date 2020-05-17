package Fanorona.mcts;

public interface GameStateStatistic {
    void incWon();
    void incLost();
    double getDecisionValue();
    GameStateStatistic getInstance();
}
