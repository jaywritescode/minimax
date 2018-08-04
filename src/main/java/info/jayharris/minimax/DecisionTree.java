package info.jayharris.minimax;

import info.jayharris.minimax.transposition.BaseTranspositionTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalLong;

public class DecisionTree<S extends State<S, A>, A extends Action<S, A>> {

    Logger logger = LoggerFactory.getLogger(DecisionTree.class);

    final Node<S, A> root;
    final BaseTranspositionTable<S, A> transpositionTable;

    public DecisionTree(S root) {
        this(root, new BaseTranspositionTable<S, A>() {
            @Override
            public boolean contains(S state) {
                return false;
            }

            @Override
            public OptionalLong get(S state) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void put(S state, long utility) { }

            @Override
            public long size() {
                return 0;
            }
        });
    }

    public DecisionTree(S root, BaseTranspositionTable<S, A> transpositionTable) {
        this.root = NodeFactory.rootNode(root);
        this.transpositionTable = transpositionTable;
    }

    public A perform() {
        return root.successors().stream()
                .peek(this::minValue)
                .max(Node.comparator)
                .map(Node::getAction)
                .orElseThrow(RuntimeException::new);
    }

    public BaseTranspositionTable<S, A> getTranspositionTable() {
        return transpositionTable;
    }

    private void maxValue(Node<S, A> node) {
        if (transpositionTable.contains(node.getState())) {
            node.setUtility(transpositionTable.get(node.getState()).getAsLong());
            return;
        }

        if (node.terminalTest()) {
            node.setUtility(node.getState().utility());
            transpositionTable.put(node.getState(), node.getUtility());
            return;
        }

        node.successors().stream()
                .peek(this::minValue)
                .max(Node.comparator)
                .ifPresent(optimal -> {
                    node.setUtility(optimal.getUtility());
                    transpositionTable.put(node.getState(), node.getUtility());
                });
    }

    private void minValue(Node<S, A> node) {
        if (transpositionTable.contains(node.getState())) {
            node.setUtility(transpositionTable.get(node.getState()).getAsLong());
            return;
        }

        if (node.terminalTest()) {
            node.setUtility(node.getState().utility());
            transpositionTable.put(node.getState(), node.getUtility());
            return;
        }

        node.successors().stream()
                .peek(this::maxValue)
                .min(Node.comparator)
                .ifPresent(optimal -> {
                    node.setUtility(optimal.getUtility());
                    transpositionTable.put(node.getState(), node.getUtility());
                });
    }
}
