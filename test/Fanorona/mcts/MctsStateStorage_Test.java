package Fanorona.mcts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MctsStateStorage_Test {

    @Test
    void TestAddWin() {
        MctsStateStorage store = new MctsStateStorage(new UctStateStatistics());
        UctStateStatistics expected = new UctStateStatistics();
        expected.update(1);
        store.addState("123").update(1);


        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
    }

    @Test
    void TestAddLose() {
        MctsStateStorage store = new MctsStateStorage(new UctStateStatistics());
        UctStateStatistics expected = new UctStateStatistics();
        expected.update(-1);
        store.addState("123").update(-1);

        Assertions.assertEquals(expected.toString(), store.getStatistics("123").toString());
    }
}
