package Fanorona.mcts;

/**
 * This interface provides the methods needed for a Fanorona player to keep track of the game theoretic value of a specific board state.
 * All values of this statistic are associated with a certain state of a Fanrorona game.
 */
public interface GameStateStatistic {
    /**
     * Increments the number of games won
     */
    void incWon();

    /**
     * Increments the number of games lost
     */
    void incLost();

    /**
     * Increments the number of games that resulted in a draw
     */
    void incDraw();

    /**
     * returns the number of won games
     */
    int getWonGames();

    /**
     * returns the number of lost games
     */
    int getLostGames();

    /**
     * returns the number of games that resulted in a draw
     */
    int getDrawGames();

    /**
     * returns the number of played games
     */
    int getTotalNumberOfPlays();

    /**
     * returns the expected reward for this state
     */
    double getReward();

    /**
     * returns a new instance of this type of GameStateStatistic
     */
    GameStateStatistic getInstance();
}
