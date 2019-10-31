package info.jayharris.minimax.search;

import info.jayharris.minimax.Node;
import info.jayharris.minimax.TestAction;
import info.jayharris.minimax.TestState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MinimaxDecisionTest {

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

        MinimaxDecision<TestState, TestAction> decision = new MinimaxDecision<TestState, TestAction>(node -> 0.0) {
            @Override
            public double utility(TestState state) {
                return state.getUtility();
            }
        };

        assertThat(decision.perform(A)).isSameAs(optimal);
    }

    @Test
    @DisplayName("it stops expanding nodes and returns the heuristic value when the cutoff test obtains")
    void testCutoffTest() {
        TestState A, B1, B2, C, D;
        TestAction optimal, unexamined;

        D = TestState.nonTerminalState("D", Arrays.asList(unexamined = mock(TestAction.class)));
        C = TestState.nonTerminalState("C", Arrays.asList(new TestAction(D)));
        B1 = TestState.nonTerminalState("B1", Arrays.asList(new TestAction(C)));
        B2 = TestState.terminalState("B2", 2.0);
        A = TestState.nonTerminalState("A", Arrays.asList(
                optimal = new TestAction(B1),
                new TestAction(B2)));

        CutoffTest<TestState, TestAction> cutoffTest = new CutoffTest<TestState, TestAction>() {
            @Override
            public boolean test(Node<TestState, TestAction> node) {
                return node.getDepth() >= 2;
            }
        };
        ToDoubleFunction<TestState> heuristic = new ToDoubleFunction<TestState>() {
            List<Double> values = Arrays.asList(3.0, 4.0);
            Iterator<Double> iter = values.iterator();

            @Override
            public double applyAsDouble(TestState value) {
                return iter.next();
            }
        };

        MinimaxDecision<TestState, TestAction> decision = new MinimaxDecision<TestState, TestAction>(heuristic,
                                                                                                     cutoffTest) {
            @Override
            public double utility(TestState state) {
                return state.getUtility();
            }
        };

        // We expect the minimax algorithm to expand A into B1 and B2.
        // B1 has one successor: C.
        // C has one successor, but the cutoff test obtains at C and it should resolve to its heuristic value.
        //      It chooses the first value from `values` defined in the anonymous ToDoubleFunction, so the
        //      value of C is 3.0.
        // C propagates its value to B1. The value of B1 is 3.0.
        // B2 is a terminal state and its utility is defined to be 2.0. We should not apply `heuristic` to B2,
        //      which would result in a value of 4.0 (from `values` in the ToDoubleFuncion), and in that case,
        //      the algorithm would choose B2 over B1.
        // Because the value of B1 is greater than that of B2, the algorithm chooses B1.
        assertThat(decision.perform(A)).isSameAs(optimal);
        // We should not expand node D because the terminal test obtained at its parent node.
        verify(unexamined, never()).perform(any());
    }

    @Test
    @DisplayName("it doesn't expand nodes whose utility values have already been calculated")
    void testTranspositionTable() {
        TestState A, b1, b2, c1, c2, D;

        D = TestState.terminalState("D", 5.0);
        c1 = TestState.nonTerminalState("c1", Arrays.asList(new TestAction(D)));
        c2 = TestState.nonTerminalState("c2", Arrays.asList(new TestAction(D)));
        b1 = TestState.nonTerminalState("b1", Arrays.asList(new TestAction(c1)));
        b2 = TestState.nonTerminalState("b2", Arrays.asList(new TestAction(c2)));
        A = TestState.nonTerminalState("A", Arrays.asList(new TestAction(b1), new TestAction(b2)));

        MinimaxDecision<TestState, TestAction> decision = new MinimaxDecision<TestState, TestAction>(
                node -> 0.0, FalseCutoffTest.getInstance(), MapBackedTranspositionTable.create()) {
            @Override
            public double utility(TestState state) {
                return state.getUtility();
            }
        };

        decision.perform(A);
        assertThat(D.countActionsCalls).isEqualTo(1);
    }
}