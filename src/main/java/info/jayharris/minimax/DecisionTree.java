package info.jayharris.minimax;

import info.jayharris.minimax.transposition.Transpositions;

public class DecisionTree<S extends State<S, A>, A extends Action<S, A>> {

    private final Node<S, A> root;
    private final Transpositions<S, A> transpositions;
    private final HeuristicEvaluationFunction<S> heuristicFunction;
    private final CutoffTest<S> cutoffTest;

    public DecisionTree(
            Node<S, A> root,
            Transpositions<S, A> transpositions,
            HeuristicEvaluationFunction<S> heuristicFunction,
            CutoffTest<S> cutoffTest) {
        this.root = root;
        this.transpositions = transpositions;
        this.heuristicFunction = heuristicFunction;
        this.cutoffTest = cutoffTest;
    }

    public A perform() {
        return root.successors()
                .peek(this::minValue)
                .max(Node.comparator)
                .map(Node::getAction)
                .orElseThrow(RuntimeException::new);
    }

    private void maxValue(Node<S, A> node) {
        S state = node.getState();

        if (transpositions.get(state).isPresent()) {
            node.setHeuristicValue(transpositions.get(state).getAsDouble());
            return;
        }

        if (node.terminalTest() || cutoffTest.apply(node)) {
            node.calculateHeuristicValue(heuristicFunction);
            transpositions.put(state, node.getHeuristicValue());
            return;
        }

        node.successors()
                .peek(this::minValue)
                .max(Node.comparator)
                .ifPresent(optimal -> {
                    node.setHeuristicValue(optimal.getHeuristicValue());
                    transpositions.put(state, node.getHeuristicValue());
                });
    }
    
    private void minValue(Node<S, A> node) {
        S state = node.getState();

        if (transpositions.get(state).isPresent()) {
            node.setHeuristicValue(transpositions.get(state).getAsDouble());
            return;
        }

        if (node.terminalTest() || cutoffTest.apply(node)) {
            node.calculateHeuristicValue(heuristicFunction);
            transpositions.put(state, node.getHeuristicValue());
            return;
        }

        node.successors()
                .peek(this::maxValue)
                .min(Node.comparator)
                .ifPresent(optimal -> {
                    node.setHeuristicValue(optimal.getHeuristicValue());
                    transpositions.put(state, node.getHeuristicValue());
                });
    }
}
