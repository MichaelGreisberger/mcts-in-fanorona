package Fanorona.mcts;

/**
 * This interface provides the methods needed for a Fanorona player to keep track of the game theoretic value of a specific board state.
 * All values of this statistic are associated with a certain state of a Fanrorona game.
 */
public interface GameStateStatistic {

    /**
     * updates the statistics according to the result
     */
    void update(double result);

    /**
     * returns the number of played games
     */
    int getCount();

    /**
     * returns the expected reward for this state
     */
    double getReward();

    /**
     * returns a new instance of this type of GameStateStatistic
     */
    GameStateStatistic getInstance();
}
