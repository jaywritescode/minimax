package info.jayharris.minimax;

import info.jayharris.minimax.transposition.BaseTranspositionTable;
import info.jayharris.minimax.transposition.HashMapTranspositionTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static info.jayharris.minimax.assertions.ProjectAssertions.assertThat;

class DecisionTreeTest {

    @Test
    @DisplayName("it returns the optimal action")
    void perform() {
        TestState A, B, C, D, b1, b2, b3, c1, c2, c3, d1, d2, d3;

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
                new TestAction(B),
                new TestAction(C),
                new TestAction(D)
        ));

        DecisionTree<TestState, TestAction> tree = new DecisionTree<>(A);

        assertThat(tree.perform().successor).isSameAs(B);
    }

    @Test
    @DisplayName("it persists the calculated utility values in the transposition table")
    void testPersistUtilityValues() {
        TestState A, B, C, b1, b2, b3;

        b1 = TestState.terminalState("b1", 3);
        b2 = TestState.terminalState("b2", 12);
        b3 = TestState.terminalState("b3", 8);

        B = TestState.nonTerminalState("B", Arrays.asList(
                new TestAction(b1),
                new TestAction(b2),
                new TestAction(b3)
        ));
        C = TestState.terminalState("C", 0);

        A = TestState.nonTerminalState("A", Arrays.asList(
                new TestAction(B),
                new TestAction(C)
        ));

        BaseTranspositionTable<TestState, TestAction> transpositionTable = new HashMapTranspositionTable<>();

        DecisionTree<TestState, TestAction> decisionTree = new DecisionTree<>(A, transpositionTable);

        decisionTree.perform();

        assertThat(transpositionTable).hasValue(b1, 3);
        assertThat(transpositionTable).hasValue(b2, 12);
        assertThat(transpositionTable).hasValue(b3, 8);
        assertThat(transpositionTable).hasValue(B, 3);
        assertThat(transpositionTable).hasValue(C, 0);
    }

}