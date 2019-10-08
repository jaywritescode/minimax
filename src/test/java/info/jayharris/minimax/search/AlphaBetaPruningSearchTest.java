package info.jayharris.minimax.search;

import info.jayharris.minimax.TestAction;
import info.jayharris.minimax.TestState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class AlphaBetaPruningSearchTest {

    @Test
    void perform() {
        TestState A, B, C, D, b1, b2, b3, c1, c2, c3, d1, d2, d3;
        TestAction optimal;

        b1 = TestState.terminalState("b1", 3);
        b2 = TestState.terminalState("b2", 12);
        b3 = TestState.terminalState("b3", 8);

        c1 = TestState.terminalState("c1", 2);
        c2 = TestState.terminalState("c2", 4);
        c3 = TestState.terminalState("c3", 6);

        d1 = TestState.terminalState("d1", 14);
        d2 = TestState.terminalState("d2", 5);
        d3 = TestState.terminalState("d3", 2);

        B = TestState.nonTerminalState("B", Arrays.asList(
                new TestAction(b1),
                new TestAction(b2),
                new TestAction(b3)
        ));
        C = TestState.nonTerminalState("C", Arrays.asList(
                new TestAction(c1),
                new TestAction(c2),
                new TestAction(c3)
        ));
        D = TestState.nonTerminalState("D", Arrays.asList(
                new TestAction(d1),
                new TestAction(d2),
                new TestAction(d3)
        ));

        A = TestState.nonTerminalState("A", Arrays.asList(
                optimal = new TestAction(B),
                new TestAction(C),
                new TestAction(D)
        ));

        AlphaBetaPruningSearch<TestState, TestAction> decision = new AlphaBetaPruningSearch<TestState, TestAction>() {
            @Override
            public double utility(TestState state) {
                return state.getUtility();
            }
        };

        assertThat(decision.perform(A)).isSameAs(optimal);
        // assert that nodes that don't need to be expanded are not expanded
        assertThat(c2.getUtilityCalculated()).isZero();
        assertThat(c3.getUtilityCalculated()).isZero();
    }
}