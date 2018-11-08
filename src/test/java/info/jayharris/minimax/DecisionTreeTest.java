package info.jayharris.minimax;

import info.jayharris.minimax.transposition.Transpositions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class DecisionTreeTest {

    @Test
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

        DecisionTree<TestState, TestAction> tree = new DecisionTree<>(
                Node.root(A),
                new TestTranspositions(),
                new TestHeuristicEvaluationFunction(),
                new TestCutoffTest()
        );

        assertThat(tree.perform()).extracting("successor").first().isSameAs(B);
    }

    @Test
    @DisplayName("it only calculates each node's heuristic value once")
    void testCalculateHeuristicValueOnlyOnce() {
        TestState A, B, C, s1, s2, s3, s4;

        s1 = TestState.terminalState("s1", 4.0);
        s2 = TestState.terminalState("s2", 5.0);
        s3 = TestState.terminalState("s3", 6.0);
        s4 = TestState.terminalState("s4", 7.0);

        B = TestState.nonTerminalState("B", Arrays.asList(
                new TestAction(s1),
                new TestAction(s2),
                new TestAction(s3)
        ));
        C = TestState.nonTerminalState("C", Arrays.asList(
                new TestAction(s1),
                new TestAction(s2),
                new TestAction(s3),
                new TestAction(s4)
        ));

        A = TestState.nonTerminalState("A", Arrays.asList(
                new TestAction(B),
                new TestAction(C)
        ));

        DecisionTree<TestState, TestAction> tree = new DecisionTree<>(
                Node.root(A),
                new TestTranspositions(),
                new TestHeuristicEvaluationFunction(),
                new TestCutoffTest()
        );

        tree.perform();

        assertThat(s1).extracting("evalCount").first().isEqualTo(1);
        assertThat(s2).extracting("evalCount").first().isEqualTo(1);
        assertThat(s3).extracting("evalCount").first().isEqualTo(1);
    }

    @Test
    @DisplayName("it stops generating successors when the cutoff test obtains")
    void testStopAtCutoffTest() {
        TestState A, B, C, D, s1;

        s1 = TestState.terminalState("s1", 6);

        D = TestState.nonTerminalState("D", Collections.singletonList(new TestAction(s1)));
        C = TestState.nonTerminalState("C", Collections.singletonList(new TestAction(D)));
        B = TestState.nonTerminalState("B", Collections.singletonList(new TestAction(C)));
        A = TestState.nonTerminalState("A", Collections.singletonList(new TestAction(B)));

        DecisionTree<TestState, TestAction> tree = new DecisionTree<>(
                Node.root(A),
                new TestTranspositions(),
                new TestHeuristicEvaluationFunction(),
                new TestCutoffTest()
        );

        tree.perform();

        assertThat(s1).extracting("evalCount").first().isEqualTo(0);
    }

    class TestTranspositions implements Transpositions<TestState> {

        final Map<TestState, Double> map;

        TestTranspositions() {
            map = new HashMap<>();
        }

        @Override
        public OptionalDouble get(TestState equivalence) {
            if (map.containsKey(equivalence)) {
                return OptionalDouble.of(map.get(equivalence));
            }

            return OptionalDouble.empty();
        }

        @Override
        public void put(TestState equivalences, double utility) {
            map.put(equivalences, utility);
        }
    }

    class TestCutoffTest implements CutoffTest<TestState> {

        @Override
        public boolean apply(Node<TestState, ?> node) {
            return node.getDepth() >= 2;
        }
    }

    class TestHeuristicEvaluationFunction implements HeuristicEvaluationFunction<TestState> {

        @Override
        public double apply(TestState state) {
            return state.eval();
        }
    }
}