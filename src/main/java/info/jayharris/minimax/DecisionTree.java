package info.jayharris.minimax;

public class DecisionTree<S extends State<S, A>, A extends Action<S, A>> {

    final Node<S, A> root;

    public DecisionTree(S root) {
        this.root = NodeFactory.rootNode(root);
    }

    public A perform() {
        return maxValue(root).getAction();
    }

    private Node<S, A> maxValue(Node<S, A> node) {
        if (node.terminalTest()) {
            node.setUtility();
            return node;
        }

        long value = Long.MIN_VALUE;
        Node<S, A> bestSuccessor = null;
        for (Node<S, A> successor : node.successors()) {
            long successorValue = minValue(successor).getUtility();
            if (successorValue > value) {
                value = successorValue;
                bestSuccessor = successor;
            }
        }

        return bestSuccessor;
    }

    private Node<S,A> minValue(Node<S, A> node) {
        if (node.terminalTest()) {
            node.setUtility();
            return node;
        }

        long value = Long.MAX_VALUE;
        Node<S, A> bestSuccessor = null;
        for (Node<S, A> successor : node.successors()) {
            long successorValue = maxValue(successor).getUtility();
            if (successorValue < value) {
                value = successorValue;
                bestSuccessor = successor;
            }
        }

        return bestSuccessor;
    }
}
