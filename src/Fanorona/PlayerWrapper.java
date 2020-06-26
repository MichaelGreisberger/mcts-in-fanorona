package Fanorona;

/**
 * A class Game uses to add metadata to players (colors, names, won games)
 */
public class PlayerWrapper {
    Player player;
    String name;
    String color;
    int wonGames = 0;

    public PlayerWrapper(Player player, String name, String color) {
        this.player = player;
        this.name = name;
        this.color = color;
    }

    void incWon() {
        wonGames++;
    }

}
