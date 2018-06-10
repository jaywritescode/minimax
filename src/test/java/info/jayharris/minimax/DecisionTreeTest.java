package info.jayharris.minimax;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.OptionalLong;

class DecisionTreeTest {

    static TestState A, B, C, D, b1, b2, b3, c1, c2, c3, d1, d2, d3;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void perform() {
        DecisionTree tree = new DecisionTree(A);

        assertThat(((TestAction) tree.perform()).successor).isSameAs(B);
    }

    static class TestState implements State<TestState, TestAction> {

        final String id;

        Collection<TestAction> actions;
        OptionalLong utility;
        boolean isTerminal;

        private TestState(String id, Collection<TestAction> actions, OptionalLong utility) {
            this.id = id;

            this.actions = actions;
            this.utility = utility;
            this.isTerminal = actions.isEmpty();
        }

        @Override

        public Collection<TestAction> actions() {
            return actions;
        }

        @Override
        public OptionalLong utility() {
            return utility;
        }

        @Override
        public boolean terminalTest() {
            return isTerminal;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TestState{");
            sb.append("id='").append(id).append('\'');
            sb.append('}');
            return sb.toString();
        }

        static TestState terminalState(String id, long utility) {
            return new TestState(id, Collections.emptyList(), OptionalLong.of(utility));
        }

        static TestState nonTerminalState(String id, Collection<TestAction> actions) {
            return new TestState(id, actions, OptionalLong.empty());
        }
    }

    static class TestAction implements Action<TestState, TestAction> {

        TestState successor;

        public TestAction(TestState successor) {
            this.successor = successor;
        }

        @Override
        public TestState apply(TestState initialState) {
            return successor;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TestAction{");
            sb.append("successor=").append(successor);
            sb.append('}');
            return sb.toString();
        }
    }
}