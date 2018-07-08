package info.jayharris.minimax;

import info.jayharris.minimax.transposition.BaseTranspositionTable;
import info.jayharris.minimax.transposition.HashMapTranspositionTable;

import java.util.function.Consumer;

public class DecisionTreeWithTranspositionTable<S extends State<S, A>, A extends Action<S, A>> {

    final Node<S, A> root;
    final BaseTranspositionTable<S, A> transpositions;

    public DecisionTreeWithTranspositionTable(S root) {
        this(root, new HashMapTranspositionTable<>());
    }

    public DecisionTreeWithTranspositionTable(S root, BaseTranspositionTable<S, A> transpositions) {
        this.root = NodeFactory.rootNode(root);
        this.transpositions = transpositions;
    }

    public A perform() {
        return maxValue(root).getAction();
    }

    private Node<S, A> maxValue(Node<S, A> node) {
        if (transpositions.contains(node.getState())) {
            node.setUtility(transpositions.get(node.getState()).getAsLong());
            return node;
        }

        if (node.terminalTest()) {
            node.setUtility();
            return node;
        }

        return node.successors().stream()
            .peek(n -> {
                long utility = minValue(n).getUtility();
                n.setUtility(utility);
                transpositions.put(n.getState(), utility);
            })
            .max(Node.comparator)
            .orElseThrow(RuntimeException::new);
    }

    private Node<S, A> minValue(Node<S, A> node) {
        if (transpositions.contains(node.getState())) {
            node.setUtility(transpositions.get(node.getState()).getAsLong());
            return node;
        }

        if (node.terminalTest()) {
            node.setUtility();
            return node;
        }

        return node.successors().stream()
            .peek(n -> {
                long utility = maxValue(n).getUtility();
                n.setUtility(utility);
                transpositions.put(n.getState(), utility);
            })
            .min(Node.comparator)
            .orElseThrow(RuntimeException::new);
    }

    private void setUtilityAndPersist(Node<S, A> node, long utility) {
        node.setUtility(utility);
        transpositions.put(node.getState(), utility);
    }

    private Consumer<Node<S, A>> doSetUtilityAndPersist(long utility) {
        return node -> setUtilityAndPersist(node, utility);
    }
}
