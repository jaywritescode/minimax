package info.jayharris.minimax.search.cutoff;

import info.jayharris.minimax.TestAction;
import info.jayharris.minimax.TestState;
import info.jayharris.minimax.search.Node;
import info.jayharris.minimax.search.TestNodeBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DepthCutoffTestTest {

    @Test
    @DisplayName("it cuts off the search")
    void cutoffSearchTrue() throws Exception {
        int maxDepth = 3;

        Node<TestState, TestAction> node = TestNodeBuilder.create().setDepth(maxDepth + 1).build();

        assertThat(new DepthCutoffTest(maxDepth).cutoffSearch(node)).isTrue();
    }

    @Test
    @DisplayName("it does not cut off the search")
    void cutoffSearchFalse() throws Exception {
        int maxDepth = 3;

        Node<TestState, TestAction> node = TestNodeBuilder.create().setDepth(maxDepth).build();

        assertThat(new DepthCutoffTest(maxDepth).cutoffSearch(node)).isFalse();
    }
}