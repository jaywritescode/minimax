package info.jayharris.minimax;

import info.jayharris.minimax.transposition.Transpositions;

public class DecisionTree2<S extends State<S, A>, A extends Action<S, A>> {

    final Node2<S, A> root;
    final Transpositions<S, A> transpositions;
    final CutoffTest<S, A> cutoffTest;

    public DecisionTree2(Node2<S, A> root, Transpositions<S, A> transpositions, CutoffTest<S, A> cutoffTest) {
        this.root = root;
        this.transpositions = transpositions;
        this.cutoffTest = cutoffTest;
    }

    public A perform() {
        return root.successors().stream()
                .peek(this::minValue)
                .max(Node2.comparator)
                .map(Node2::getAction)
                .orElseThrow(RuntimeException::new);
    }

    private void maxValue(Node2<S, A> node) {
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
                .max(Node2.comparator)
                .ifPresent(optimal -> {
                    node.setValue(optimal.getValue());
                    transpositions.put(state, node.getValue());
                });
    }
    
    private void minValue(Node2<S, A> node) {
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
                .min(Node2.comparator)
                .ifPresent(optimal -> {
                    node.setValue(optimal.getValue());
                    transpositions.put(state, node.getValue());
                });
    }
}
