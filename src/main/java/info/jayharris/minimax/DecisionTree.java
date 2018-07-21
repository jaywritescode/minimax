package info.jayharris.minimax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import info.jayharris.minimax.transposition.BaseTranspositionTable;

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
        return maxValue(root).get().getAction();
    }

    private Optional<Node<S, A>> maxValue(Node<S, A> node) {
        if (transpositionTable.contains(node.getState())) {
            node.setUtility(transpositionTable.get(node.getState()).getAsLong());
            return node;
        }

        if (node.terminalTest()) {
            logger.debug("Node {} is a terminal node. Setting its value, returning empty.");

        return node.successors().stream()
                .peek(n -> n.setUtility(minValue(n).getUtility()))
                .max(Node.comparator)
                .orElseThrow(RuntimeException::new);
    }

        logger.debug("Node {} is a non-terminal node. Iterating through successors, returning the successor with the greatest utility.", node.getState());

        Optional<Node<S, A>> optimal = node.successors().stream()
                .peek(this::minValue)
                .max(Node.comparator);
        optimal.ifPresent(o -> node.setUtility(o.getUtility()));

        return optimal;
    }

    private Optional<Node<S, A>> minValue(Node<S, A> node) {
        if (transpositionTable.contains(node.getState())) {
            node.setUtility(transpositionTable.get(node.getState()).getAsLong());
            return node;
        }

        if (node.terminalTest()) {
            logger.debug("Node {} is a terminal node. Setting its value, returning empty.", node.getState());

            node.setUtility(node.getState().utility());
            return Optional.empty();
        }

        logger.debug("Node {} is a non-terminal node. Iterating through successors, returning the successor with the smallest utility.", node.getState());

        Optional<Node<S, A>> optimal = node.successors().stream()
                .peek(this::maxValue)
                .min(Node.comparator);
        optimal.ifPresent(o -> node.setUtility(o.getUtility()));

        return optimal;
    }
}
