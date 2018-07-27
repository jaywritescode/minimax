package info.jayharris.minimax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DecisionTree<S extends State<S, A>, A extends Action<S, A>> {

    Logger logger = LoggerFactory.getLogger(DecisionTree.class);

    final Node<S, A> root;

    public DecisionTree(S root) {
        this.root = NodeFactory.rootNode(root);
    }

    public A perform() {
        return maxValue(root).get().getAction();
    }

    private Optional<Node<S, A>> maxValue(Node<S, A> node) {
        if (node.terminalTest()) {
            logger.debug("Node {} is a terminal node. Setting its value, returning empty.");

            node.setUtility();
            return Optional.empty();
        }

        logger.debug("Node {} is a non-terminal node. Iterating through successors, returning the successor with the greatest utility.", node.getState());

        Optional<Node<S, A>> optimal = node.successors().stream()
                .peek(this::minValue)
                .max(Node.comparator);
        optimal.ifPresent(o -> node.setUtility(o.getUtility()));

        return optimal;
    }

    private Optional<Node<S, A>> minValue(Node<S, A> node) {
        if (node.terminalTest()) {
            logger.debug("Node {} is a terminal node. Setting its value, returning empty.", node.getState());

            node.setUtility();
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
