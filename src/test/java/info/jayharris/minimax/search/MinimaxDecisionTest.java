package info.jayharris.minimax.search;

import info.jayharris.minimax.TestAction;
import info.jayharris.minimax.TestState;
import info.jayharris.minimax.search.cutoff.CutoffTest;
import info.jayharris.minimax.search.cutoff.FalseCutoffTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
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

        B = TestState.nonTerminalState("B", b1, b2, b3);
        C = TestState.nonTerminalState("C", c1, c2, c3);
        D = TestState.nonTerminalState("D", d1, d2, d3);

        A = TestState.nonTerminalState("A", Arrays.asList(
                optimal = new TestAction(B),
                new TestAction(C),
                new TestAction(D)
        ));

        MinimaxDecision<TestState, TestAction> decision = new TestMinimaxDecision(node -> 0.0);
        assertThat(decision.perform(A)).isSameAs(optimal);
    }

    @Test
    @DisplayName("it stops expanding nodes and returns the heuristic value when the cutoff test obtains")
    void testCutoffTest() {
        TestState A, B1, B2, C, D;
        TestAction optimal, unexamined;

        D = TestState.nonTerminalState("D", Arrays.asList(unexamined = mock(TestAction.class)));
        C = TestState.nonTerminalState("C", D);
        B1 = TestState.nonTerminalState("B1", C);
        B2 = TestState.terminalState("B2", 2.0);
        A = TestState.nonTerminalState("A", Arrays.asList(
                optimal = new TestAction(B1),
                new TestAction(B2)));

        CutoffTest<TestState, TestAction> cutoffTest = new CutoffTest<TestState, TestAction>() {
            @Override
            public boolean cutoffSearch(Node<TestState, TestAction> node) {
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

        MinimaxDecision<TestState, TestAction> decision = new TestMinimaxDecision(heuristic, cutoffTest);

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

    @Nested
    class _TranspositionTable {
        /*
        Let's say we're playing chess and the search algorithm has determined
        that the estimated utility value of 1. e4 e5 2. Nc3 ... is 0.6. Then we
        shouldn't need to calculate the utility value of 1. Nc3 e5 2. e4 ...
        because it's the same board position and should have the same utility.
         */

        @Test
        @DisplayName("it puts an estimated heuristic value into the transposition table")
        void storesValue() throws Exception {
            TestState e4, e4e5, e4e5Nc3, e4e5d3;

            e4e5d3 = TestState.terminalState("1. e4 e5 2. d3 ...", 0.7);
            e4e5Nc3 = TestState.terminalState("1. e4 e5 2. Nc3 ...", 0.9);
            e4e5 = TestState.nonTerminalState("1. e4 e5", e4e5Nc3, e4e5d3);
            e4 = TestState.nonTerminalState("1. e4 ...", e4e5);

            MapBackedTranspositionTable<TestState, TestAction> transpositionTable =
                    MapBackedTranspositionTable.create();
            MinimaxDecision<TestState, TestAction> decision = new TestMinimaxDecision(
                    state -> 0.0, FalseCutoffTest.getInstance(), transpositionTable);
            decision.perform(e4);

            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(transpositionTable).matches(testMapping(e4e5d3, 0.7));
            softly.assertThat(transpositionTable).matches(testMapping(e4e5Nc3, 0.9));
            softly.assertThat(transpositionTable).matches(testMapping(e4e5, 0.7));
        }

        @Test
        @DisplayName("it doesn't expand nodes whose utility values have already been calculated")
        void stopExpansion() {
            TestState empty, e4, e4e5, e4e5Nc3, e4e5Nc3d6, Nc3, Nc3e5, Nc3e5e4, dummy;

            e4e5Nc3d6 = TestState.terminalState("1. e4 e5 2. Nc3 d6", 0.6);
            // this node should not be evaluated because its parent is in the transposition table
            dummy = Mockito.mock(TestState.class);
            // the following two nodes are #equals
            e4e5Nc3 = TestState.nonTerminalState("1. e4 e5 2. Nc3 ... or 1. Nc3 e5 2. e4 ...", e4e5Nc3d6);
            Nc3e5e4 = TestState.nonTerminalState("1. e4 e5 2. Nc3 ... or 1. Nc3 e5 2. e4 ...", dummy);
            e4e5 = TestState.nonTerminalState("1. e4 e5", e4e5Nc3);
            Nc3e5 = TestState.nonTerminalState("1. Nc3 e5", Nc3e5e4);
            e4 = TestState.nonTerminalState("1. e4 ...", e4e5);
            Nc3 = TestState.nonTerminalState("1. Nc3 ...", Nc3e5);
            // e4 needs to be evaluated first so its estimated heuristic value will be in the
            // transposition table when it comes time to evaluate dummy
            empty = TestState.nonTerminalState("no moves", e4, Nc3);

            MapBackedTranspositionTable<TestState, TestAction> transpositionTable =
                    MapBackedTranspositionTable.create();
            MinimaxDecision<TestState, TestAction> decision = new TestMinimaxDecision(
                    state -> 0.0, FalseCutoffTest.getInstance(), transpositionTable);
            decision.perform(empty);

            verifyZeroInteractions(dummy);
        }

        private Predicate<MapBackedTranspositionTable<TestState, TestAction>> testMapping(TestState state, double val) {
            return table -> table.getUtilityValue(state).getAsDouble() == val;
        }
    }

    class TestMinimaxDecision extends MinimaxDecision<TestState, TestAction> {
        public TestMinimaxDecision(ToDoubleFunction<TestState> heuristicFn) {
            super(heuristicFn);
        }

        public TestMinimaxDecision(ToDoubleFunction<TestState> heuristicFn,
                                   CutoffTest<TestState, TestAction> cutoffTest) {
            super(heuristicFn, cutoffTest);
        }

        public TestMinimaxDecision(ToDoubleFunction<TestState> heuristicFn,
                                   CutoffTest<TestState, TestAction> cutoffTest,
                                   TranspositionTable<TestState, TestAction> transpositionTable) {
            super(heuristicFn, cutoffTest, transpositionTable);
        }

        @Override
        public double utility(TestState state) throws UnknownUtilityException {
            return state.getUtility();
        }
    }
}