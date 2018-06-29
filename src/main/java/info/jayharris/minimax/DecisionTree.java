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
}
