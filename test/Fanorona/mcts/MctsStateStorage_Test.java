package Fanorona.mcts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class MctsStateStorage_Test {

    @Test
    void TestAddWin() {
        MctsStateStorage store = new MctsStateStorage(new GameStateStatisticImpl());
        GameStateStatisticImpl expected = new GameStateStatisticImpl();
        expected.incWon();
        store.addWin("123");

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
    }

    @Test
    void TestAddLose() {
        MctsStateStorage store = new MctsStateStorage(new GameStateStatisticImpl());
        GameStateStatisticImpl expected = new GameStateStatisticImpl();
        expected.incLost();
        store.addLose("123");

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
    }

    @Test
    void TestAddAllWin() {
        MctsStateStorage store = new MctsStateStorage(new GameStateStatisticImpl());
        GameStateStatisticImpl expected = new GameStateStatisticImpl();

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
    void TestAddAllLose() {
        MctsStateStorage store = new MctsStateStorage(new GameStateStatisticImpl());
        GameStateStatisticImpl expected = new GameStateStatisticImpl();

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
