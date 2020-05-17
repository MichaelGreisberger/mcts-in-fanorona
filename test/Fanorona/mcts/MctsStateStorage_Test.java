package Fanorona.mcts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class MctsStateStorage_Test {

    @Test
    public void TestAddWin() {
        MctsStateStorage store = new MctsStateStorage(new RandomPlayerGameStateStatistic());
        RandomPlayerGameStateStatistic expected = new RandomPlayerGameStateStatistic();
        expected.incWon();
        store.addWin("123");

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
    }

    @Test
    public void TestAddLose() {
        MctsStateStorage store = new MctsStateStorage(new RandomPlayerGameStateStatistic());
        RandomPlayerGameStateStatistic expected = new RandomPlayerGameStateStatistic();
        expected.incLost();
        store.addLose("123");

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
    }

    @Test
    public void TestAddAllWin() {
        MctsStateStorage store = new MctsStateStorage(new RandomPlayerGameStateStatistic());
        RandomPlayerGameStateStatistic expected = new RandomPlayerGameStateStatistic();

        expected.incWon();

        List<String> states = new LinkedList<>();
        states.add("123");
        states.add("456");
        states.add("789");
        store.addWin(states);

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
        Assertions.assertEquals(expected.toString(), store.getStatistics("456").toString());
        Assertions.assertEquals(expected.toString(), store.getStatistics("789").toString());
    }

    @Test
    public void TestAddAllLose() {
        MctsStateStorage store = new MctsStateStorage(new RandomPlayerGameStateStatistic());
        RandomPlayerGameStateStatistic expected = new RandomPlayerGameStateStatistic();

        expected.incLost();

        List<String> states = new LinkedList<>();
        states.add("123");
        states.add("456");
        states.add("789");
        store.addLose(states);

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
        Assertions.assertEquals(expected.toString(), store.getStatistics("456").toString());
        Assertions.assertEquals(expected.toString(), store.getStatistics("789").toString());
    }
}