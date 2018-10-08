package info.jayharris.minimax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static info.jayharris.minimax.assertions.ProjectAssertions.assertThat;

class NodeTest {

    TestState initial, a, b, c, d;

    @BeforeEach
    void setUp() {
        a = TestState.terminalState("A", 1);
        b = TestState.terminalState("B", 2);
        c = TestState.terminalState("C", 3);
        d = TestState.terminalState("D", 4);

        initial = TestState.nonTerminalState("init", Arrays.asList(
                new TestAction(a),
                new TestAction(b),
                new TestAction(c),
                new TestAction(d)
        ));
    }

    @Test
    void successors() {
        Node node = NodeFactory.rootNode(initial);

        List<Node> successors = node.successors();

        assertThat(successors).extracting("state").containsExactlyInAnyOrder(a, b, c, d);
        assertThat(successors).allMatch(succ -> succ.getDepth() == 1);
    }
}