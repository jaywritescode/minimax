package info.jayharris.minimax;

import info.jayharris.minimax.transposition.Transpositions;

public class DecisionTree<S extends State<S, A>, A extends Action<S, A>> {

    final Node<S, A> root;
    final Transpositions<S, A> transpositions;
    final CutoffTest<S, A> cutoffTest;

    public DecisionTree(Node<S, A> root, Transpositions<S, A> transpositions, CutoffTest<S, A> cutoffTest) {
        this.root = root;
        this.transpositions = transpositions;
        this.cutoffTest = cutoffTest;
    }

    public A perform() {
        return root.successors().stream()
                .peek(this::minValue)
                .max(Node.comparator)
                .map(Node::getAction)
                .orElseThrow(RuntimeException::new);
    }

    private void maxValue(Node<S, A> node) {
        S state = node.getState();

        if (transpositions.get(state).isPresent()) {
            node.setValue(transpositions.get(state).getAsDouble());
            return;
        }

        if (node.terminalTest() || cutoffTest.apply(node)) {
            node.calculateHeuristicValue();
            transpositions.put(state, node.getValue());
            return;
        }

        node.successors().stream()
                .peek(this::minValue)
                .max(Node.comparator)
                .ifPresent(optimal -> {
                    node.setValue(optimal.getValue());
                    transpositions.put(state, node.getValue());
                });
    }
    
    private void minValue(Node<S, A> node) {
        S state = node.getState();

        if (transpositions.get(state).isPresent()) {
            node.setValue(transpositions.get(state).getAsDouble());
            return;
        }

        if (node.terminalTest() || cutoffTest.apply(node)) {
            node.calculateHeuristicValue();
            transpositions.put(state, node.getValue());
            return;
        }

        node.successors().stream()
                .peek(this::maxValue)
                .min(Node.comparator)
                .ifPresent(optimal -> {
                    node.setValue(optimal.getValue());
                    transpositions.put(state, node.getValue());
                });
    }
}
