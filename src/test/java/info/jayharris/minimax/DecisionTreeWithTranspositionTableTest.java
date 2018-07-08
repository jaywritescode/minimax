package info.jayharris.minimax;

import info.jayharris.minimax.transposition.BaseTranspositionTable;
import info.jayharris.minimax.transposition.HashMapTranspositionTable;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.OptionalLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DecisionTreeWithTranspositionTableTest {

    @Nested
    class Perform {

        @Test
        @DisplayName("it returns an optimal move")
        void returnOptimalMove() {
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

            DecisionTreeWithTranspositionTable<TestState, TestAction> tree = new DecisionTreeWithTranspositionTable(A);

            assertThat(tree.perform().successor).isSameAs(B);
        }

        @Test
        @DisplayName("it persists a state's utility")
        void persistUtility() {
            TestState A, B, b1, b2;

            b1 = TestState.terminalState("b1", 6);
            b2 = TestState.terminalState("b2", 9);

            B = TestState.nonTerminalState("B", Arrays.asList(
                    new TestAction(b1),
                    new TestAction(b2)
            ));

            A = TestState.nonTerminalState("A", Arrays.asList(
                    new TestAction(B)
            ));

            BaseTranspositionTable<TestState, TestAction> transpositionTable = new HashMapTranspositionTable<>();

            DecisionTreeWithTranspositionTable<TestState, TestAction> decisionTree = new DecisionTreeWithTranspositionTable<>(A, transpositionTable);

            decisionTree.perform();

            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(transpositionTable.get(B)).hasValue(6);
            softly.assertThat(transpositionTable.get(b1)).hasValue(6);
            softly.assertThat(transpositionTable.get(b2)).hasValue(9);
            softly.assertThat(transpositionTable.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("it retrieves the cached utility")
        void useCachedUtility() {
            TestState root, A, B, x1, x2, x3, x4;

            x1 = TestState.terminalState("x1", 8);
            x2 = TestState.terminalState("x2", 5);
            x3 = TestState.terminalState("x3", 12);
            x4 = TestState.terminalState("x4", 11);

            A = TestState.nonTerminalState("A", Arrays.asList(
                    new TestAction(x1),
                    new TestAction(x2),
                    new TestAction(x3),
                    new TestAction(x4)));
            B = TestState.terminalState("B", 4);

            root = TestState.nonTerminalState("root", Arrays.asList(
                    new TestAction(A),
                    new TestAction(B)));

            BaseTranspositionTable<TestState, TestAction> transpositionTable = mock(BaseTranspositionTable.class);

            when(transpositionTable.contains(A)).thenReturn(true);
            when(transpositionTable.get(A)).thenReturn(OptionalLong.of(5));

            DecisionTreeWithTranspositionTable<TestState, TestAction> decisionTree =
                    new DecisionTreeWithTranspositionTable<>(root, transpositionTable);

            assertThat(decisionTree.perform().successor).isEqualTo(A);

            verify(transpositionTable).get(A);
            verify(transpositionTable, never()).contains(x1);
            verify(transpositionTable, never()).contains(x2);
            verify(transpositionTable, never()).contains(x3);
            verify(transpositionTable, never()).contains(x4);
        }
    }
}
