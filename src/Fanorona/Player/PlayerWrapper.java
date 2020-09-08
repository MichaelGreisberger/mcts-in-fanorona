package Fanorona.Player;

import Fanorona.Board.Board;
import Fanorona.Move.Move;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A class Game uses to add metadata to players (colors, names, won games)
 */
public class PlayerWrapper {
    public Player player;
    public List<Move> selectedMoves;
    public List<String> simulatedGames;
    public List<String> storedStates;
    public double timeUsed = 0;
    public String name;
    public String color;

    public PlayerWrapper(Player player, String name, String color) {
        this.player = player;
        this.name = name;
        this.color = color;
        this.selectedMoves = new LinkedList<>();
        this.simulatedGames = new LinkedList<>();
        this.storedStates = new LinkedList<>();
    }

    public Move getNextMove(Board board) throws IOException {
        double start = System.currentTimeMillis();
        Move move = player.getNextMove(board);
        double end = System.currentTimeMillis();
        timeUsed += end - start;
        selectedMoves.add(move);
        simulatedGames.add("" + player.getNumberOfSimulatedGames());
        storedStates.add("" + player.getNumberOfStoredStates());
        return move;
    }

}
