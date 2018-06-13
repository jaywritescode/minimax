package info.jayharris.minimax;

import java.util.HashMap;
import java.util.Map;

public class DecisionTree<S extends State<S, A>, A extends Action<S, A>> {

    final Node<S, A> root;

    final Map<S, Long> transpositions;

    public DecisionTree(S root) {
        this.root = NodeFactory.rootNode(root);
        this.transpositions = new HashMap<>();
    }

    public A perform() {
        return maxValue(root).getAction();
    }

    private Node<S, A> maxValue(Node<S, A> node) {
        if (node.terminalTest()) {
            node.setUtility();
            return node;
        }

        return node.successors().stream()
                .peek(n -> n.setUtility(minValue(n).getUtility()))
                .max(Node.comparator)
                .orElseThrow(RuntimeException::new);
    }

    private Node<S, A> minValue(Node<S, A> node) {
        if (node.terminalTest()) {
            node.setUtility();
            return node;
        }

        return node.successors().stream()
                .peek(n -> n.setUtility(maxValue(n).getUtility()))
                .min(Node.comparator)
                .orElseThrow(RuntimeException::new);
    }

    private void addToTranspositionTable(S state, long value) {
        this.transpositions.put(state, value);
    }
}
