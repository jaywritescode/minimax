package info.jayharris.minimax;

import info.jayharris.minimax.transposition.Transpositions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DecisionTree2Test {

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

        DecisionTree2<TestState, TestAction> tree = new DecisionTree2<TestState, TestAction>(
                new Node2<>(A, null, 0, () -> 0.0),
                new TestTranspositions(),
                new TestCutoffTest()
        );

        assertThat(tree.perform()).extracting("successor").first().isSameAs(B);
    }

    class TestTranspositions implements Transpositions<TestState, TestAction> {

        Map<TestState, Double> map;

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

    class TestCutoffTest implements CutoffTest<TestState, TestAction> {

        @Override
        public boolean apply(Node2<TestState, TestAction> node) {
            return node.terminalTest();
        }
    }
}